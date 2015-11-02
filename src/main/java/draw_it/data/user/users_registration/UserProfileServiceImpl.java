package draw_it.data.user.users_registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import draw_it.data.user.UserProfile;

import javax.sql.DataSource;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

//    @Autowired
//    private AuthUserRepository authUserRepository;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    public boolean containsEmail(String email) {
        String sqlEmail = "select count(*) from user_profile where email=?";
        jdbcTemplate = new JdbcTemplate(dataSource);
        Integer amountEmail = jdbcTemplate.queryForObject(sqlEmail, new Object[]{email}, Integer.class);
        return !(amountEmail == 0);
    }

    public boolean containsLogin(String login) {
        String sqlLogin = "select count(*) from auth_user where login=?";
        jdbcTemplate = new JdbcTemplate(dataSource);
        Integer amountLogin = jdbcTemplate.queryForObject(sqlLogin, new Object[]{login}, Integer.class);
        return !(amountLogin == 0);
    }

    @Override
    public void saveUser(String login, String password, long userProfileId) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "insert into auth_user values (?,?,?)";
        jdbcTemplate.update(sql, login, password, userProfileId);
    }


    public byte[] getAvatar(Integer id) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT avatar FROM user_profile WHERE id=?";
        return jdbcTemplate.query(sql, new Object[]{id}, (resultSet, i) -> {
            return resultSet.getBytes("avatar");
        }).get(0);
    }

    @Transactional
    public void edit(UserProfile userProfile, String login, String password, boolean enableImage) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = null;
        if (!enableImage) {
            sql = "UPDATE user_profile SET name=?, surname=?,email=?, country=? WHERE id=?;";
            jdbcTemplate.update(sql, userProfile.getName(), userProfile.getSurname(), userProfile.getEmail(),
                    userProfile.getCountry(), userProfile.getId());

        } else {
            sql = "UPDATE user_profile SET name=?, surname=?,email=?, country=?, avatar=? WHERE id=?;";
            jdbcTemplate.update(sql, userProfile.getName(), userProfile.getSurname(), userProfile.getEmail(),
                    userProfile.getCountry(), userProfile.getAvatar(), userProfile.getId());
        }
        String sqlAuth ="UPDATE auth_user SET  login=?, password=? WHERE profile_id=?";
        jdbcTemplate.update(sqlAuth, login, password, userProfile.getId());
    }


    public String getLogin(Integer id) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT login FROM auth_user where profile_id=?";
        return jdbcTemplate.query(sql, new Object[]{id}, (resultSet, i) -> {
            return resultSet.getString("login");
        }).get(0);
    }

    public String getPassword(Integer id) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "SELECT password FROM auth_user where profile_id=?";
        return jdbcTemplate.query(sql, new Object[]{id}, (resultSet, i) -> {
            return resultSet.getString("password");
        }).get(0);
    }
}
