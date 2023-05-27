package shreshtha.inc.common;

import java.math.BigDecimal;

public record Particular (BigDecimal amountInfo, String descriptionOfGoodsOrService, String hsnSacCode, Integer quantityOrSize, BigDecimal unitAmountInfo, String unitAmountSummary, Integer units, BigDecimal gstAmount
) {}