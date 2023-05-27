package shreshtha.inc.authentication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppRoleRepository extends JpaRepository<AppRole, Integer> {
    Optional<AppRole> findByRoleName(AppRole.ERole roleName);
    List<AppRole> findAllByRoleNameIn(Iterable<AppRole.ERole> roleNames);
}
