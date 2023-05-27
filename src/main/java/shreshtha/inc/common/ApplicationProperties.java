package shreshtha.inc.common;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.util.List;

@ConfigurationProperties(prefix = "application")
public record ApplicationProperties(String apiBasePath, Resource indexFile, ReportProperties reports,
                                    CorsProperties cors) {
    public record ReportProperties(String configPath, String initialDataPath, Integer invLength) {
    }

    public record CorsProperties(String mappingPathPattern,List<String> allowedHeaders, List<String> allowedOrigins, List<String> allowedMethods,
                                 Long maxAge) {
    }
}
