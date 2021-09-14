package org.example.http.framework.resolver.argument;

import org.example.http.framework.Request;
import org.example.http.framework.annotation.RequestParseBody;
import org.example.http.framework.exception.UnsupportedParameterException;

import java.io.OutputStream;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RequestParseBodyHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final Class<RequestParseBody> annotation = RequestParseBody.class;

    @Override
    public boolean supportsParameter(Parameter parameter) {
        return parameter.getType().isAssignableFrom(Map.class) && parameter.isAnnotationPresent(annotation);
    }

    @Override
    public Object resolveArgument(Parameter parameter, Request request, OutputStream response) {
        if (!supportsParameter(parameter))
            throw new UnsupportedParameterException(parameter.getType().getName());

        final var parsedBody = parseBody(request.getBody());
        if(parsedBody == null){
            throw new UnsupportedParameterException(parameter.getType().getName());
        }
        return parsedBody;
    }
    public static Map<String, List<String>> parseBody(byte[] body) {
        String bodyDecipher = new String(body, StandardCharsets.UTF_8);
        var bodyList = new HashMap<String, List<String>>();
        String[] bodyLines = bodyDecipher.split("\n");
        for (String Line : bodyLines) {
            String[] bodyParts = Line.split("&");
            for (String part : bodyParts) {
                String[] bodyKeyAndValue = part.split("=");
                String key = bodyKeyAndValue[0];
                List<String> value = new ArrayList<String>(Arrays.asList(bodyKeyAndValue[1]));
                bodyList.put(key, value);
            }
        }
        return bodyList;
    }

}

