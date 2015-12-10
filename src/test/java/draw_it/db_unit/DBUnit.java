package draw_it.db_unit;

import draw_it.data.user.users_registration.UserProfileRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/tests/test-beans.xml"})
public class DBUnit {

    private final static String SQL_SELECT_STATEMENT = "SELECT * FROM USER_PROFILE";

    @Autowired(required = true)
    private DataBaseFiller dbFiller;

    @Autowired(required = true)
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Before
    public void setUp() throws Exception {
        dbFiller.fill();
    }

    @Test(expected = java.lang.Throwable.class)
    public void test1() {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL_SELECT_STATEMENT);
        Assert.assertEquals(rows.get(0).get("name"), userProfileRepository.getById(1).getName());
        Assert.assertEquals(rows.get(0).get("email"), userProfileRepository.getById(1).getEmail());
        Assert.assertEquals(rows.get(0).get("country"), userProfileRepository.getById(1).getCountry());
        Assert.assertEquals(rows.get(0).get("point_amount"), userProfileRepository.getById(1).getPointAmount());
    }
}