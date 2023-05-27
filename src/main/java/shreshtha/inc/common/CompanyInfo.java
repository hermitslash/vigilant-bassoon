package shreshtha.inc.common;

public record CompanyInfo (
    String addressLine,
    CompanyCategory category,
    String companyName,
    String emailAddress,
    String gstNo,
    String panNo,
    String phoneNumber,
    String pinCode

) {}
