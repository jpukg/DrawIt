package draw_it.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@ControllerAdvice
@EnableWebMvc
public class ExceptionHandling {
    private static Logger log = LogManager.getLogger(Exception.class.getName());

    @ExceptionHandler(Exception.class)
    public ModelAndView handleIOException(Exception exception) {
        log.warn("BROKEN.", exception);

        ModelAndView andView = new ModelAndView();
        andView.addObject("message", exception.getMessage());
        andView.setViewName("exception");
        return andView;
    }
}
