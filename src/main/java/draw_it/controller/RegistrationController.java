package draw_it.controller;


import draw_it.data.user.users_registration.UserProfileRepository;
import draw_it.data.user.users_registration.UserProfileService;
import draw_it.utils.CountryContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import draw_it.data.user.UserProfile;
@Controller
public class RegistrationController {

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileService userProfileService;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationForm(Model model) {
        model.addAttribute("userProfile", new UserProfile());
        model.addAttribute("countries", CountryContainer.getCountries());

        return "registration";
    }


    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registrationConfirm(Model model, @ModelAttribute("userProfile") UserProfile userProfile,
                                      @RequestParam String login, @RequestParam String country, @RequestParam String password, @RequestParam String passwordRepeat,
                                      @RequestParam CommonsMultipartFile avatar) throws IOException {
        model.addAttribute("countries", CountryContainer.getCountries());

        StringBuilder errors = new StringBuilder();
        
	boolean isSave = true;

        if (userProfile.getEmail().isEmpty() || login.isEmpty()) {
            errors.append("Email and login cannot be empty.<br/>");
        } else {
            model.addAttribute("login", login);
	//            Check password
            Matcher matcher;
        
	    matcher = patternPassword.matcher(password);
        
	    if (!password.equals(passwordRepeat)) {
                errors.append("The password repeating is bad.<br/>");
                isSave = false;
            } else {
                if (!matcher.find()) {
                    errors.append("Your password is weak.<br/>");
                    isSave = false;
                }
            }
//              Check email
        
	    matcher = patternEmail.matcher(userProfile.getEmail());
        
	    if (!matcher.find()) {
                errors.append("Email is not valid.<br/>");
                isSave = false;
            }

            if (userProfileService.containsEmail(userProfile.getEmail())) {
                errors.append("This email is already registered.<br/>");
                isSave = false;
            }

            if (userProfileService.containsLogin(login)) {
                errors.append("This login is already registered.");
                isSave = false;
            }

            if (isSave) {
//                Потому, что страны и очков нету в model Attribute
                userProfile.setCountry(country);
                userProfile.setGameAmount(0L);
                userProfile.setPointAmount(0L);

                if (avatar.getBytes().length != 0) {
                    userProfile.setAvatar(avatar.getBytes());
                }

                userProfileRepository.save(userProfile);
                userProfileService.saveUser(login, password, userProfile.getId());


                return "redirect:/login?registered";
            }
        }
        model.addAttribute("errors", errors.toString());
        return "registration";
    }

    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder)
            throws ServletException {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    private static final String DEFAULT_AVATAR = "resources\\ava.jpg";

    private Pattern patternPassword;

    private Pattern patternEmail;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final String PASSWORD_PATTERN =
            "[\\w\\S]{4,20}";

    public RegistrationController() {
        patternEmail = Pattern.compile(EMAIL_PATTERN);
        patternPassword = Pattern.compile(PASSWORD_PATTERN);
    }
}
