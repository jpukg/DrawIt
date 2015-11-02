package draw_it.data.message;

import draw_it.data.user.User;

/**
 * Created by Marina on 07.06.2015.
 */
public class Member {
    private String login;

    public Member(User user) {
        this.login = user.getLogin();
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
