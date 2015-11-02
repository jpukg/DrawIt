package draw_it.data.words;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


@Service
public class WordServiceImpl implements WordService {

    @Autowired
    private javax.sql.DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Override
    public synchronized String pickOneWord() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        String sqlCount = "select count(*) from word";
        String takeWord = "select word from word where id=?";
        Integer count = jdbcTemplate.queryForObject(sqlCount, Integer.class);
        Random random = new Random();

        String word = jdbcTemplate.queryForObject(takeWord, new Object[]{random.nextInt(count)+1}, String.class);
        return word;
    }
}
