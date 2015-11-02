package draw_it.data.user;

import java.util.Random;

public class FreeUser extends User {
    public final static String FREE_USER_LOGIN = "anonymous";
    public final static String FREE_USER_PASSWORD = "password";
    public final static String FREE_USER_ROLE = "ROLE_FREE";

    public FreeUser() {
        login = FREE_USER_LOGIN + new Random().nextInt(1000000000);
        password = FREE_USER_PASSWORD;
        role = FREE_USER_ROLE;
    }
}
