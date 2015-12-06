package draw_it.controller;

import draw_it.data.user.users_registration.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

/**
 * Created by Артем on 19.05.2015.
 */
@Controller
@RequestMapping("/picture")
public class DisplayAvatar {

    @Autowired
    private UserProfileService userProfileService;

    @RequestMapping("/avatar")
    public void showImage(@RequestParam("id") Integer id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        response.setContentType("image/jpg");

//        Path path = Paths.get("E:\\picture\\3.jpg");
//        byte[] data = Files.readAllBytes(path);
//        response.getOutputStream().write(data);

        byte[] avatar = userProfileService.getAvatar(id);

        if (avatar == null || avatar.length==0) {
            InputStream is = getClass().getResourceAsStream("/img/ava_default.jpg");
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[64*1024];
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();

            avatar = buffer.toByteArray();
        }

        response.getOutputStream().write(avatar);

        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    @InitBinder
    protected void initBinder(ServletRequestDataBinder binder)
            throws ServletException {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
    }
}
