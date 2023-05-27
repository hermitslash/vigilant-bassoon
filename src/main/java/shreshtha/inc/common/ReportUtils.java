package shreshtha.inc.common;

import io.vertx.core.json.JsonObject;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.query.JsonQueryExecuterFactory;
import shreshtha.inc.config.ReportConfigSvc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
public class ReportUtils {
    public final static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("ddMMyyyy");

    public static JsonObject generatePdfReport(ReportConfigSvc reportConfigSvc, ReportInfo reportInfo,
                                               JasperReportTitle jasperReportTitle, String customerName) throws Exception {
        var ldt = LocalDateTime.now();
        var reportConfigInfo = reportConfigSvc.readReportConfigFromSource().stream()
                .filter(y -> jasperReportTitle.getTitle().equals(y.title())
                        && jasperReportTitle.getCustomerId().equals(y.customerName()))
                .findFirst().orElseThrow();
        Map<String, Object> params = new HashMap<>();
        var jasperReportIS = reportConfigSvc.getFileFromResource(reportConfigInfo.templatePath());
        var outputFileName = reportConfigInfo.outputFileName().concat("-"+customerName.toUpperCase()+"-")
                .concat(ldt.format(DATE_TIME_FORMATTER)).concat(".pdf");
        var userHome = System.getProperty("user.home");
        var downloadsFolder = userHome.concat(File.separator).concat("Downloads")
                .concat(File.separator).concat(jasperReportTitle.getCustomerId())
                .concat(File.separator).concat("reports");
        Files.createDirectories(Paths.get(downloadsFolder));
        var fullOutputFileName = downloadsFolder.concat(File.separator).concat(outputFileName);
        params.put(JsonQueryExecuterFactory.JSON_DATE_PATTERN, "dd/MM/yyyy");
        params.put(JsonQueryExecuterFactory.JSON_NUMBER_PATTERN, "#,##0.##");
        params.put(JsonQueryExecuterFactory.JSON_LOCALE, Locale.ENGLISH);
        params.put(JRParameter.REPORT_LOCALE, Locale.US);
        JasperReport pdfReport = JasperCompileManager.compileReport(jasperReportIS);
        String reportJsonObj = JsonObject.mapFrom(reportInfo).encode();
        JRDataSource jsonDataSource = new JsonDataSource(
                new ByteArrayInputStream(reportJsonObj.getBytes(StandardCharsets.UTF_8)));
        JasperPrint pdfPrinter = JasperFillManager.fillReport(pdfReport, params, jsonDataSource);
        JasperExportManager.exportReportToPdfFile(pdfPrinter, fullOutputFileName);
        return new JsonObject().put("filePath", fullOutputFileName).put("fileContent",
                Files.readAllBytes(Paths.get(fullOutputFileName)));
    }
}

