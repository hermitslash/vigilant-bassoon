package shreshtha.inc.reporting;

import io.vertx.core.AbstractVerticle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Service;
import shreshtha.inc.common.*;
import shreshtha.inc.config.ReportConfigSvc;

import java.io.File;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
class ReportHandler extends AbstractVerticle implements ReportingService {

	private final ApplicationProperties applicationProperties;
    private final ReportServiceGateway reportServiceGateway;
    private final ReportConfigSvc reportConfigSvc;
    private final AsyncTaskExecutor asyncTaskExecutor;

    @Override
    public ReportInfoData initiateReportData(ReportInfoData rd) throws Exception {
        return asyncTaskExecutor.submitCompletable(() -> {
            ReportData initialReportData = reportConfigSvc.readInitialDataFromSource()
                    .initiateReportData(rd.reportData().getCompanyInfos().get(0),
                            rd.reportData().getParticulars(),
                            rd.reportData().getPoNo(),rd.reportData().getPosCity(),
                            rd.reportData().getPosState(), rd.reportData().getCustomerId(), rd.reportData().getDateOfSupply())
                    .updateApplicableGst().updateAmountsInReportData()
                    .completeReportDataUpdates();
            ReportInfoData rd1 = new ReportInfoData(CommonUtils.generateInvoiceNo(applicationProperties.reports().invLength()), initialReportData, OffsetDateTime.now(Clock.systemUTC()), OffsetDateTime.now(Clock.systemUTC()));
            return Optional.of(reportServiceGateway.initiateReportData(rd1)).map(y -> new ReportInfoData(y.getInvoiceNo(), y.getReportData(), y.getDateCreated(), y.getLastUpdated()))
                    .orElseThrow();
        }).join();
    }

    @Override
    public File downloadReport(ReportInfoData reportInfoData) throws Exception {
        return asyncTaskExecutor.submitCompletable(() -> {
            var reportInfo = CommonUtils.mergeCompanyInfos(reportInfoData);
            var foundTitle = JasperReportTitle.findByTitle(reportInfoData.reportData().getReportTitle().name());
            var customerName = reportInfoData.reportData().getCompanyInfos().stream().filter(ci -> ci.category()
                    .equals(CompanyCategory.BILL_TO)).map(CompanyInfo::companyName).findFirst().orElseThrow(IllegalArgumentException::new);
            var downloadedFile = ReportUtils.generatePdfReport(reportConfigSvc, reportInfo, foundTitle, customerName);
            log.info("FilePath: {}", downloadedFile.getString("filePath"));
            return reportConfigSvc.writeContentToFile(downloadedFile.getBinary("fileContent"), downloadedFile.getString("filePath"));
        }).join();
    }

    @Override
    public BigDecimal endOfDayTransactionsAmount(String currentDate, String customerId) {
        return asyncTaskExecutor.submitCompletable(() -> {
            var transactionsForCurrDate = reportServiceGateway.findAllByCurrentDate(currentDate, customerId);
            return transactionsForCurrDate.map(JpaReportInfoData::getReportData).map(ReportData::getAmountChargeable).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        }).join();
    }

    @Override
    public ReportInfo findReportInfoByInvoiceNo(String invoiceNo) {
        return asyncTaskExecutor.submitCompletable(() -> CommonUtils.mergeCompanyInfos(reportServiceGateway.findReportInfoDataByInvoiceNo(invoiceNo).map(jrid -> new ReportInfoData(jrid.getInvoiceNo(), jrid.getReportData(), jrid.getDateCreated(), jrid.getLastUpdated())).orElseThrow())).join();
    }

    @Override
    public ReportInfoData findReportInfoDataByInvoiceNo(String invoiceNo) {
        return asyncTaskExecutor.submitCompletable(() -> reportServiceGateway.findReportInfoDataByInvoiceNo(invoiceNo).map(jrid -> new ReportInfoData(jrid.getInvoiceNo(), jrid.getReportData(), jrid.getDateCreated(), jrid.getLastUpdated())).orElseThrow()).join();
    }

    @Override
    public List<ReportInfo> findAllReportInfos(String customerId) {
        return asyncTaskExecutor.submitCompletable(() -> {
            Function<JpaReportInfoData, ReportInfo> mergeInfo = jrid -> CommonUtils.mergeCompanyInfos(new ReportInfoData(jrid.getInvoiceNo(), jrid.getReportData(), jrid.getDateCreated(), jrid.getLastUpdated()));
            return reportServiceGateway.findAllReportInfoData(customerId)
                    .map(mergeInfo).toList();
        }).join();
    }
    
    @Override
    public void updateReportDataDownloads(ReportInfoData reportInfoData) {
        asyncTaskExecutor.submitCompletable(() -> {
            reportServiceGateway.updateReportDataDownloads(reportInfoData);
        }).join();
    }

}