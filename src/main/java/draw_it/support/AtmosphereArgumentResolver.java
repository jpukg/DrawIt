package draw_it.support;

import draw_it.utils.AtmosphereUtils;
import org.atmosphere.cpr.AtmosphereResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Gunnar Hillert
 * @since 1.0
 */
public class AtmosphereArgumentResolver implements
        HandlerMethodArgumentResolver {

    @Autowired
    private AtmosphereUtils atmosphereUtils;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return AtmosphereResource.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        return atmosphereUtils.getAtmosphereResource(webRequest
                .getNativeRequest(HttpServletRequest.class));
    }
}
