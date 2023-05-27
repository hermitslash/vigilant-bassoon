package shreshtha.inc.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum JasperReportTitle {
    UKSMG_TAX_INVOICE("Tax Invoice", "TAX_INVOICE", "uksmg");

    private final String label;
    private final String title;
    private final String customerId;

    public static JasperReportTitle findByTitle(String title) {
        return Arrays.stream(JasperReportTitle.values()).filter(y -> y.getTitle().equals(title)).findFirst()
                .orElseThrow(NoJasperReportTitleFoundException::new);
    }

    public static List<JasperReportTitle> findByCustomerId(String customerId) {
        return Arrays.stream(JasperReportTitle.values()).filter(y -> y.getCustomerId().equals(customerId)).toList();
    }
}
