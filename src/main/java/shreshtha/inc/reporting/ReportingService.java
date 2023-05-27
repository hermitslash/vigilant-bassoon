package shreshtha.inc.reporting;

import shreshtha.inc.common.ReportInfo;
import shreshtha.inc.common.ReportInfoData;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

interface ReportingService {

    ReportInfoData initiateReportData(ReportInfoData rd) throws Exception;

    File downloadReport(ReportInfoData reportInfoData) throws Exception;

    BigDecimal endOfDayTransactionsAmount(String currentDate, String customerId);

    ReportInfo findReportInfoByInvoiceNo(String invoiceNo);
    ReportInfoData findReportInfoDataByInvoiceNo(String invoiceNo);

    List<ReportInfo> findAllReportInfos(String customerId);
    void updateReportDataDownloads(ReportInfoData reportInfoData);

}