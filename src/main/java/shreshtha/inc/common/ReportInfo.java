package shreshtha.inc.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
public class ReportInfo {
    private String invoiceNo;
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
    private String dateOfSupply = LocalDate.now().toString();
    private BigDecimal totalTaxableValue = BigDecimal.ZERO;
    private IsPaid isPaid = IsPaid.NO;
    private String poNo;
    private Boolean isPrintAllowed = false;
    private String currentDate = LocalDate.now().toString();
    private String addressLine;

    private String companyName;

    private String emailAddress;

    private String gstNo;

    private String panNo;

    private String phoneNumber;

    private String pinCode;

    private String billingAddressLine;

    private String billingCompanyName;

    private String billingEmailAddress;

    private String billingGstNo;

    private String billingPanNo;

    private String billingPhoneNumber;

    private String billingPinCode;
    private List<ApplicableGst> applicableGsts = new ArrayList<>();
    private List<Particular> particulars = new ArrayList<>();
    private ReportTitle reportTitle = ReportTitle.TAX_INVOICE;
    private String errorMessage;
    private Integer errorCode;
}
