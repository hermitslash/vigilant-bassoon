package shreshtha.inc.authentication;

import shreshtha.inc.common.AppUser;
import shreshtha.inc.common.AppUserDto;

import java.util.Optional;

interface AuthServiceGateway {

    JpaUser registerCustomerAccount(AppUser appUser);
    JpaUser approveCustomerAccount(Long accountId);

    Optional<JpaUser> loggedInUser();
}