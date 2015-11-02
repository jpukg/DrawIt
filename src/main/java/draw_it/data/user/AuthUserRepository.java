package draw_it.data.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("authUserRepository")
public interface AuthUserRepository extends CrudRepository<AuthUser, Long> {
    AuthUser findByLogin(String login);
}
