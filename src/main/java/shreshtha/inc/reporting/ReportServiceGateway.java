package shreshtha.inc.reporting;

import shreshtha.inc.common.ReportInfo;
import shreshtha.inc.common.ReportInfoData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

interface ReportServiceGateway {
    JpaReportInfoData initiateReportData(ReportInfoData rd) throws Exception;
    Optional<JpaReportInfoData> findReportInfoDataByInvoiceNo(String invoiceNo);
    Stream<JpaReportInfoData> findAllReportInfoData(String customerId);
    Stream<JpaReportInfoData> findAllByCurrentDate(String currentDate, String customerId);
    void updateReportDataDownloads(ReportInfoData reportInfoData);
}