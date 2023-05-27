package shreshtha.inc.common;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {ApplicationProperties.class})
class ReportSvcConfig {
}
