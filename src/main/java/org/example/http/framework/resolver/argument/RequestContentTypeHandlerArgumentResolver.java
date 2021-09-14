package org.example.http.framework.resolver.argument;

import org.example.http.framework.Request;
import org.example.http.framework.annotation.RequestHeader;
import org.example.http.framework.exception.UnsupportedParameterException;

import java.io.OutputStream;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class RequestContentTypeHandlerArgumentResolver implements HandlerMethodArgumentResolver {
    private final String contentType = "application/x-www-form-urlencoded"; //Content-Type:
    private final Class<RequestHeader> annotation = RequestHeader.class;

    @Override
    public boolean supportsParameter(Parameter parameter) {
        return parameter.getType().isAssignableFrom(String.class) && parameter.isAnnotationPresent(annotation);
    }


    @Override
    public Object resolveArgument(Parameter parameter, Request request, OutputStream response) {
        if (!supportsParameter(parameter)) {
            // this should never happen
            throw new UnsupportedParameterException(parameter.getType().getName());
        }

        final RequestHeader annotation = parameter.getAnnotation(this.annotation);
        if(!annotation.value().contentEquals(contentType)) {
            return new String(request.getBody(), StandardCharsets.UTF_8);
        }
        else {
            throw new UnsupportedParameterException(annotation.value());
        }
    }
}
