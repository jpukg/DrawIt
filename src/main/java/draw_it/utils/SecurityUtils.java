package draw_it.utils;

import draw_it.data.user.User;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {
    public static User getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }
}
