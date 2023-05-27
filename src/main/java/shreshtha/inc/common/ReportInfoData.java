package shreshtha.inc.common;

import java.time.OffsetDateTime;

public record ReportInfoData (
    String invoiceNo,
    ReportData reportData,
    OffsetDateTime dateCreated,
    OffsetDateTime lastUpdated
) {}