package draw_it.data.user;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class AuthUser extends User {

    public static final String ROLE_AUTH = "ROLE_AUTH";

    public AuthUser() {
        role = ROLE_AUTH;
    }

    @Id
    @NotEmpty(message = "Login is required.")
    @Column(unique = true, nullable = false)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @NotEmpty(message = "Password is required.")
    @Column(nullable = false)
    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @NotNull(message = "Profile is required.")
    @OneToOne(optional = false)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthUser that = (AuthUser) o;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;

        return true;
    }
}
