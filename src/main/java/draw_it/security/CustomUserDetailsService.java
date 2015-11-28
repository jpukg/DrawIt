package draw_it.security;

import draw_it.data.user.AuthUserRepository;
import draw_it.data.user.FreeUser;
import draw_it.data.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    @Qualifier("authUserRepository")
    private AuthUserRepository userRepository;

    public void setUserRepository(AuthUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = null;
        if (username.equals(FreeUser.FREE_USER_LOGIN)) {
            user = new FreeUser();
        } else {
            user = userRepository.findByLogin(username);
        }
        if (user == null) {
            throw new UsernameNotFoundException("Could not find user " + username);
        }

        return new CustomUserDetails(user);
    }

    private final static class CustomUserDetails extends User implements UserDetails {

		private static final long serialVersionUID = 2255333352L;
	
        public CustomUserDetails(User user) {
            super(user);
        }

        public Collection<? extends GrantedAuthority> getAuthorities() {
            return AuthorityUtils.createAuthorityList(getRole());
        }

        public String getUsername() {
            return getLogin();
        }

        public boolean isAccountNonExpired() {
            return true;
        }

        public boolean isAccountNonLocked() {
            return true;
        }

        public boolean isCredentialsNonExpired() {
            return true;
        }

        public boolean isEnabled() {
            return true;
        }

    }
}
