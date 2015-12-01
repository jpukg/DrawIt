package draw_it.utils;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CountryContainer {
    private static List<String> countries;

    public static List<String> getCountries() {
        if (countries == null) {
            String[] locales = Locale.getISOCountries();
            Locale.setDefault(new Locale("eu"));
            
	    countries = new ArrayList<>();
            
	    for (String s : locales) {
                Locale obj = new Locale("", s);
                countries.add(obj.getDisplayCountry());
            }
        }
        return countries;
    }
}
