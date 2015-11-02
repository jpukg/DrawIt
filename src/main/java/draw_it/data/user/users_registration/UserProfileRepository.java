package draw_it.data.user.users_registration;

import draw_it.data.user.UserProfile;
import org.springframework.data.repository.CrudRepository;

//@Service
public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {

    UserProfile save(UserProfile userProfile);

    UserProfile getById(long id);
}

