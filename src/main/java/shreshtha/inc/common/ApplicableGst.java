package shreshtha.inc.common;

import java.math.BigDecimal;

public record ApplicableGst (BigDecimal amountInfo,
                             GstCategory category,
                             Double percentageApplicable) {}
