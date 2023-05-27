package shreshtha.inc.authentication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface JpaUserRepository extends JpaRepository<JpaUser, Long> {
    Optional<JpaUser> findByUsername(String username);
    Optional<JpaUser> findByUsernameAndEmailAddress(String username, String emailAddress);
    Boolean existsByUsername(String username);

    Boolean existsByUsernameAndEmailAddress(String username, String emailAddress);
}
