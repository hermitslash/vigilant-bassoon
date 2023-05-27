package shreshtha.inc.common;

public record AppUserDto (
     Long id,
     String username,
     String firstName,
     String lastName,
     String emailAddress,
     String phoneNumber,
     String companyName,
     String roles
) {}
