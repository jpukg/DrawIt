package draw_it.controller;

import draw_it.data.user.*;
import draw_it.data.user.users_registration.UserProfileRepository;
import draw_it.data.user.users_registration.UserProfileService;
import draw_it.utils.CountryContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class AuthController {

    @Autowired
    @Qualifier(value = "authenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @RequestMapping(value = {"/login"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            @RequestParam(value = "registered", required = false) String registered,
            ModelMap model) {

        int allGames = 0;
        List<UserProfile> userProfiles = new ArrayList<>();
        for (UserProfile profile : userProfileRepository.findAll()) {
            allGames += profile.getGameAmount();
            userProfiles.add(profile);
        }
        userProfileRepository.findAll();
        model.addAttribute("usersAmount", userProfiles.size());
        model.addAttribute("gamesAmount", allGames / 2);

        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }

        if (logout != null) {
            model.addAttribute("msg", "You have successfully logged out.");
        }

        if (registered != null) {
            model.addAttribute("msg", "You have successfully registered.");
        }

        return "login";
    }

    @RequestMapping(value = "/enter_anonymously", method = RequestMethod.POST)
    public String enterAnonymously() {

        Authentication request = new UsernamePasswordAuthenticationToken(
                FreeUser.FREE_USER_LOGIN, FreeUser.FREE_USER_PASSWORD);
        Authentication result = authenticationManager.authenticate(request);
        SecurityContextHolder.getContext().setAuthentication(result);

        return "redirect:/main";
    }

    @RequestMapping(value = "/login_as_auth", method = RequestMethod.POST)
    public String loginAsAuth(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        SecurityContextHolder.getContext().setAuthentication(null);

        return "redirect:/login";
    }

}
