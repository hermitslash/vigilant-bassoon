package shreshtha.inc.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportData {
    private String customerId;
    private String accountNo;
    private String bankName;
    private String branchName;
    private String downloadedAt;
    private String ifscCode;
    private String posCity;
    private String posState;
    private TaxPayableUnderReverseCharge taxPayableUnderReverseCharge = TaxPayableUnderReverseCharge.NO;
    private BigDecimal amountChargeable;
    private String amountChargeableInWords;
    private String dateOfSupply;
    private BigDecimal totalTaxableValue = BigDecimal.ZERO;
    private IsPaid isPaid = IsPaid.NO;
    private String poNo;
    private Boolean isPrintAllowed = false;
    private String currentDate = LocalDate.now().toString();
    private List<ApplicableGst> applicableGsts;
    private List<CompanyInfo> companyInfos;
    private List<Particular> particulars;
    private ReportTitle reportTitle = ReportTitle.TAX_INVOICE;


    public ReportData initiateReportData(CompanyInfo billingCompanyInfo, List<Particular> particulars, String poNo, String posCity, String posState, String customerId, String dateOfSupply) {
        if (Objects.isNull(billingCompanyInfo) || StringUtils.isAnyBlank(billingCompanyInfo.companyName(), poNo, posState, posCity, dateOfSupply)) {
            throw new CompanyInfoNotFoundException("CompanyInfo/City/State/SupplyDate/PONO cannot be null/blank.");
        }
        if (LocalDate.parse(dateOfSupply).isAfter(LocalDate.now())) {
            throw new DomainException("Cannot be a future date.");
        }
        this.poNo = poNo;
        this.posState = posState;
        this.posCity = posCity;
        this.customerId = customerId;
        this.dateOfSupply = dateOfSupply;
        return this.addParticulars(particulars).addBillingCompanyInfo(billingCompanyInfo);
    }

    public ReportData updateAmountsInReportData() {
        List<Particular> ps = getParticulars();
        var calculatedTotalAmount = ps.stream().map(Particular::amountInfo).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        var calculatedTotalGst = ps.stream().map(Particular::gstAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        setTotalTaxableValue(calculatedTotalAmount);
        setAmountChargeable(calculatedTotalAmount.add(calculatedTotalGst));
        setAmountChargeableInWords(CommonUtils.numberToWords(getAmountChargeable().longValue()).concat("rupees only"));
        return this;
    }

    public ReportData updateApplicableGst() {
        List<Particular> ps = getParticulars();
        var calculatedTotalGst = ps.stream().map(Particular::gstAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        List<ApplicableGst> applicableGsts = getApplicableGsts().stream().filter(y -> !y.category().name().equals(GstCategory.IGST.name()))
                .map(a -> new ApplicableGst(calculatedTotalGst.multiply(BigDecimal.valueOf(0.5)), a.category(), a.percentageApplicable())).toList();
        setApplicableGsts(applicableGsts);
        return this;
    }

    public ReportData addParticulars(List<Particular> particulars) {
        getParticulars().addAll(particulars.stream().map(parReq -> {
            if (parReq.unitAmountInfo() == null || parReq.unitAmountInfo().equals(BigDecimal.ZERO)) {
                throw new DomainException("AmountInfo cannot be null or 0");
            } else if (parReq.units() == null || parReq.units() == 0) {
                throw new DomainException("Units cannot be null or 0");
            }
            var convUnits = new BigDecimal(parReq.units());
            var totalAmountInfo = convUnits.multiply(parReq.unitAmountInfo());
            return new Particular(totalAmountInfo, parReq.descriptionOfGoodsOrService(), parReq.hsnSacCode(), parReq.quantityOrSize(), parReq.unitAmountInfo(), parReq.unitAmountSummary(), parReq.units(), totalAmountInfo.multiply(BigDecimal.valueOf(18.0 / 100.0)));
        }).toList()); return this;
    }

    public ReportData addBillingCompanyInfo(CompanyInfo companyInfo) {
        CompanyInfo companyInfo1 = new CompanyInfo(companyInfo.addressLine(), CompanyCategory.BILL_TO, companyInfo.companyName(), companyInfo.emailAddress(), companyInfo.gstNo(), companyInfo.panNo(), companyInfo.phoneNumber(), companyInfo.pinCode());
        getCompanyInfos().add(companyInfo1);
        return this;
    }

    public ReportData completeReportDataUpdates() {
        int particularsCount = getParticulars().size();
        if (particularsCount == 0) {
            throw new DomainException("No Particulars Found");
        }
        if (Objects.equals(getAmountChargeable(), BigDecimal.ZERO) || Objects.isNull(getAmountChargeableInWords()) || getAmountChargeableInWords().isBlank() || getIsPrintAllowed() || Objects.equals(getTotalTaxableValue(), BigDecimal.ZERO)) {
            throw new DomainException("Some fields are empty/already updated!");
        }
        setIsPrintAllowed(true);
        return this;
    }

    public void updateReportDataDownloads() {
        if (getDownloadedAt() == null) setDownloadedAt(LocalDateTime.now().toString());
    }

}
