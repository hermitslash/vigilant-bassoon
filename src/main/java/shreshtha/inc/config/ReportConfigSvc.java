package shreshtha.inc.config;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import shreshtha.inc.common.ReportConfigData;
import shreshtha.inc.common.ReportConfigInfo;
import shreshtha.inc.common.ReportData;
import shreshtha.inc.common.ApplicationProperties;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReportConfigSvc {
	
	private final ApplicationProperties applicationProperties;
	
    public String readFileFromResource(String filePath) throws IOException {
        if (Objects.isNull(filePath)) {
            throw new RuntimeException("File path is null");
        } else {
            try (InputStream reportConfigIS = getClass().getClassLoader().getResourceAsStream(filePath)) {
                return new BufferedReader(new InputStreamReader(Objects.requireNonNull(reportConfigIS)))
                        .lines().collect(Collectors.joining(""));
            }
        }
    }

    public File writeContentToFile(byte[] fileContent, String strFileName) throws IOException {
        BufferedOutputStream bos = null;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(strFileName);
            bos = new BufferedOutputStream(fos);
            bos.write(fileContent);

        } catch (FileNotFoundException fnfe) {
            System.out.println("Specified file not found" + fnfe);
        } catch (IOException ioe) {
            System.out.println("Error while writing file" + ioe);
        } finally {
            if (bos != null) {
                    bos.flush();
                    bos.close();
            }
        }
        return Paths.get(strFileName).toFile();
    }

    public InputStream getFileFromResource(String filePath) {
        if (Objects.isNull(filePath)) {
            throw new RuntimeException("File path is null");
        } else {
            return getClass().getClassLoader().getResourceAsStream(filePath);
        }
    }

    public List<ReportConfigInfo> readReportConfigFromSource() throws IOException {
        JsonObject reportConfigObj = new JsonObject(readFileFromResource(applicationProperties.reports().configPath()));
        ReportConfigData rcn = Json.decodeValue(reportConfigObj.encode(), ReportConfigData.class);
        return rcn.reports();
    }

    @SneakyThrows
    public Optional<String> findReportTitleByCustomerId(String subscriber) {
        return readReportConfigFromSource().stream().filter(y -> y.customerName().equals(subscriber))
                .map(ReportConfigInfo::reportTitle).findFirst();
    }

    public ReportData readInitialDataFromSource() throws IOException {
        JsonObject initialDataObj = new JsonObject(readFileFromResource(applicationProperties.reports().initialDataPath()));
        return Json.decodeValue(initialDataObj.encode(), ReportData.class);
    }
}