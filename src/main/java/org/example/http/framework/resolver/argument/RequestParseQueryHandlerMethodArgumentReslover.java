package org.example.http.framework.resolver.argument;

import org.example.http.framework.Request;
import org.example.http.framework.annotation.RequestHeader;
import org.example.http.framework.annotation.RequestParseBody;
import org.example.http.framework.annotation.RequestParseQuery;
import org.example.http.framework.exception.UnsupportedParameterException;

import java.io.OutputStream;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public  class  RequestParseQueryHandlerMethodArgumentReslover implements HandlerMethodArgumentResolver{
    private final Class<RequestParseQuery> annotation = RequestParseQuery.class;

    @Override
    public boolean supportsParameter(Parameter parameter) {
        return parameter.getType().isAssignableFrom(Map.class) && parameter.isAnnotationPresent(annotation);
    }

    @Override
    public Object resolveArgument(Parameter parameter, Request request, OutputStream response) {
        if (!supportsParameter(parameter))
            throw new UnsupportedParameterException(parameter.getType().getName());

        final var parsedQuery = parseQuery(request.getPath());
        if(parsedQuery == null){
            throw new UnsupportedParameterException(parameter.getType().getName());
        }
        return parsedQuery;
    }



    public static Map<String, List<String>> parseQuery(String path) {
        var queryParams = path.split("\\?");
        String query = queryParams[1];
        var queryList = query.split("&");
        var queryFinal = new HashMap<String, List<String>>();
        for (String splittedQuery : queryList) {
            String[] splitted = splittedQuery.split("=");

            String key = splitted[0];
            var values = Arrays.stream(splitted[1].split("\\+")).toList();

            queryFinal.put(key, values);
        }
        return queryFinal;
    }
}
