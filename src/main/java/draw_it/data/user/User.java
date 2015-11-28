package draw_it.data.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class User implements Serializable {

    public User() {

    }

    public User(User user) {
        login = user.login;
        password = user.password;
        role = user.role;
    }

    protected String login;

    @JsonIgnore
    protected String password;

    @JsonIgnore
    protected String role;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!login.equals(user.login)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }
}
