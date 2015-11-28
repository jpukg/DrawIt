package draw_it.controller;

import draw_it.data.user.AuthUser;
import draw_it.data.user.AuthUserRepository;
import draw_it.data.user.User;
import draw_it.data.user.UserProfile;
import draw_it.data.user.users_registration.UserProfileRepository;
import draw_it.data.user.users_registration.UserProfileService;
import draw_it.utils.CountryContainer;
import draw_it.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import javax.servlet.ServletException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ProfileController {

    @Autowired
    @Qualifier("authUserRepository")
    private AuthUserRepository authUserRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private UserProfileService userProfileService;

    private Pattern patternPassword;

    private static final String PASSWORD_PATTERN =
            "[\\w\\S]{4,20}";

    private Pattern patternEmail;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public ProfileController() {
        patternEmail = Pattern.compile(EMAIL_PATTERN);
        patternPassword = Pattern.compile(PASSWORD_PATTERN);
    }

    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder)
            throws ServletException {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(value = "/my_profile", method = RequestMethod.GET)
    public String personalInfo(ModelMap model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        return "redirect:/profile/" + currentUser.getLogin();
    }

    @RequestMapping(value = "/profile/{login}", method = RequestMethod.GET)
    public String personalInfoUser(ModelMap model, @PathVariable String login) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();

        AuthUser authUser = authUserRepository.findByLogin(login);
        model.addAttribute("user", authUser.getProfile());

        model.addAttribute("currentUserID", currentUser.getProfile().getId());
        model.addAttribute("login", login);

        return "profile";
    }

    @RequestMapping(value = "/profile/edit", method = RequestMethod.GET)
    public String getEdit(@RequestParam(value = "id", required = true) Integer id,
                          Model model) {
        if (id != SecurityUtils.getCurrentUser().getProfile().getId()){
            return "403";
        }

        model.addAttribute("userProfile", userProfileRepository.getById(id));
        model.addAttribute("login", userProfileService.getLogin(id));

        model.addAttribute("countries", CountryContainer.getCountries());
//        Check access
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        UserProfile userProfile = currentUser.getProfile();
        model.addAttribute("user", userProfileRepository.getById((int) userProfile.getId()));
        model.addAttribute("currentUserID", currentUser.getProfile().getId());
        model.addAttribute("countryCurrent", currentUser.getProfile().getCountry());

        return "edit_profile";
    }


    @RequestMapping(value = "/profile/edit", method = RequestMethod.POST)
    public String commitEdit(@ModelAttribute("userProfile") UserProfile userProfile,
                             @RequestParam(value = "id", required = true) Integer id,
                             @RequestParam CommonsMultipartFile avatar,
                             @RequestParam String login,
                             @RequestParam String country,
                             @RequestParam String password, @RequestParam String passwordRepeat,
                             Model model) {
        if (id != SecurityUtils.getCurrentUser().getProfile().getId()){
            return "403";
        }

            model.addAttribute("countries", CountryContainer.getCountries());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        model.addAttribute("currentUserID", currentUser.getProfile().getId());
        StringBuilder errors = new StringBuilder();
        boolean isSave = true;
        if (userProfile.getEmail().isEmpty() || login.isEmpty()) {
            errors.append("Email and login cannot be empty.<br/>");
        } else {
            model.addAttribute("login", login);

            Matcher matcher;
            matcher = patternPassword.matcher(password);
            if (!password.isEmpty()) {
                if (!password.equals(passwordRepeat)) {
                    errors.append("The password repeating is bad.<br/>");
                    isSave = false;
                } else {
                    if (!matcher.find()) {
                        errors.append("Your password is weak.<br/>");
                        isSave = false;
                    }
                }
            } else {

// TODO fix, because it may cause an ambiguity:
                currentUser = SecurityUtils.getCurrentUser();
                currentUser.setPassword(password);
                password = userProfileService.getPassword(id);
            }

            matcher = patternEmail.matcher(userProfile.getEmail());
            if (!matcher.find()) {
                errors.append("Email is not valid.<br/>");
                isSave = false;
            }

            UserProfile userProfileExisted = userProfileRepository.getById(id);

            if (!userProfileExisted.getEmail().equals(userProfile.getEmail())) {
                if (userProfileService.containsEmail(userProfile.getEmail())) {
                    errors.append("This email is already registered.<br/>");
                    isSave = false;
                }
            }

            if (!userProfileService.getLogin(id).equals(login)) {
                if (userProfileService.containsLogin(login)) {
                    errors.append("This login is already registered.");
                    isSave = false;
                }
            } else {
                isSave = true;
            }


            boolean enableImage = false;
            if (avatar.getBytes().length != 0) {
                userProfile.setAvatar(avatar.getBytes());
                enableImage = true;
            }

            System.out.println("BOOL " + (userProfile.getName().equals(userProfileExisted.getName()) && userProfile.getEmail().equals(userProfileExisted.getEmail()) &&
                    userProfile.getSurname().equals(userProfileExisted.getSurname()) && country.equals(userProfileExisted.getCountry()) &&
                    avatar.getBytes().length == 0 && login.equals(userProfileService.getLogin(id))
            ));

            if (userProfile.getName().equals(userProfileExisted.getName()) && userProfile.getEmail().equals(userProfileExisted.getEmail()) &&
                    userProfile.getSurname().equals(userProfileExisted.getSurname()) && country.equals(userProfileExisted.getCountry()) &&
                    avatar.getBytes().length == 0 && login.equals(userProfileService.getLogin(id))) {
                model.addAttribute("duplicate", "You don't change your profile");
            } else {
                if (isSave) {
                    userProfile.setCountry(country);
                    userProfileService.edit(userProfile, login, password, enableImage);
                    model.addAttribute("success", "Edit is successful.");
                    model.addAttribute("countryCurrent", userProfile.getCountry());
//                Reset new login
                    currentUser = (User) auth.getPrincipal();
                    currentUser.setLogin(login);
                    currentUser.setPassword(password);
                    currentUser.setProfile(userProfile);
                }
            }

            model.addAttribute("userProfile", userProfileRepository.getById(id));
        }
        model.addAttribute("errors", errors.toString());
        return "edit_profile";
    }
}
