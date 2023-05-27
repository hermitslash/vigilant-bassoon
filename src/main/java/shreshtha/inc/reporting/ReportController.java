package shreshtha.inc.reporting;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shreshtha.inc.common.ReportInfo;
import shreshtha.inc.common.ReportInfoData;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
//@RequestMapping(produces = "application/vnd.shreshtha.inc.reporting-v1+json")
@Slf4j
public class ReportController {
    private final ReportHandler reportHandler;

    @PostMapping("/api/reporting-svc/reportInfoData/initiate")
    @PreAuthorize("hasAnyAuthority('SCOPE_INITIATE_REPORT', 'SCOPE_RS_WRITE')")
    public ResponseEntity<ReportInfoData> initiateReportInfoData(@Valid @RequestBody ReportInfoData reportInfoData) throws Exception {
        return ResponseEntity.ofNullable(reportHandler.initiateReportData(reportInfoData));
    }

    @GetMapping("/api/reporting-svc/reportInfo")
    @PreAuthorize("hasAnyAuthority('SCOPE_RS_READ')")
    public ResponseEntity<ReportInfo> reportInfoByInvoiceNo(@RequestParam("invoiceNo") String invoiceNo) throws Exception {
    	if(Objects.isNull(invoiceNo) || invoiceNo.isBlank()) {
    		ReportInfo errorReportInfo = new ReportInfo().withErrorCode(HttpStatus.NO_CONTENT.value()).withErrorMessage("QueryParam InvoiceNo is missing!");
    		return ResponseEntity.ok(errorReportInfo);
    	}
        return ResponseEntity.ofNullable(reportHandler.findReportInfoByInvoiceNo(invoiceNo));
    }

    @GetMapping(value = "/api/reporting-svc/download/reportInfo", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @PreAuthorize("hasAnyAuthority('SCOPE_DOWNLOAD_REPORT')")
    public ResponseEntity<Resource> downloadReport(@RequestParam("invoiceNo") String invoiceNo) throws Exception {
        var foundReportInfoData = reportHandler.findReportInfoDataByInvoiceNo(invoiceNo);
        File downloadedReport = reportHandler.downloadReport(foundReportInfoData);
        ResponseEntity<Resource> reportDownloadResource = Objects.isNull(foundReportInfoData) ? ResponseEntity.noContent().build() : ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+downloadedReport.getName()+ "\"")
                .body(new FileSystemResource(downloadedReport));
        reportHandler.updateReportDataDownloads(foundReportInfoData);
        return reportDownloadResource;
    }

    @GetMapping("/api/reporting-svc/reportInfos")
    @PreAuthorize("hasAnyAuthority('SCOPE_RS_READ')")
    public List<ReportInfo> allReportInfoByCustomer(@RequestHeader("customerId") String customerId) {
        return reportHandler.findAllReportInfos(customerId);
    }

    @GetMapping("/api/reporting-svc/end-of-day-transactions")
    @PreAuthorize("hasAnyAuthority('SCOPE_RS_EOD_READ')")
    public ResponseEntity<BigDecimal> endOfDayTransactions(@RequestHeader("customerId") String customerId, @RequestParam("currentDate") String currentDate) {
        return ResponseEntity.ofNullable(reportHandler.endOfDayTransactionsAmount(currentDate, customerId));
    }

}
