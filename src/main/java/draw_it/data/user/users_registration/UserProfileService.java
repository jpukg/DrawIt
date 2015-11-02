package draw_it.data.user.users_registration;

import draw_it.data.user.UserProfile;

public interface UserProfileService {

    public boolean containsEmail(String email);

    public boolean containsLogin(String login);

    public void saveUser(String login, String password, long userProfileId);

    public byte[] getAvatar(Integer id);

    public String getLogin(Integer id);

    public String getPassword(Integer id);

    public void edit(UserProfile userProfile, String login, String password, boolean enableImage) ;

}
