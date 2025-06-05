package com.moro.tchat.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class EndpointObtainFilter extends OncePerRequestFilter {

    private final RequestMappingHandlerMapping mappings;

    private final HandlerExceptionResolver resolver;

    public EndpointObtainFilter(RequestMappingHandlerMapping mappings, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        this.mappings = mappings;
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String httpMethod = request.getMethod();
        String resourcePath = request.getServletPath();

        if (resourcePath.endsWith(".css") || resourcePath.endsWith(".js") || resourcePath.contains("/ws")) {
            filterChain.doFilter(request, response);
            return;
        }

        Set<String> mappings = this.mappings.getHandlerMethods().keySet().stream()
                .map(mapping -> (mapping.getMethodsCondition() + " " + mapping.getPathPatternsCondition()))
                .collect(Collectors.toSet());

        String currentMapping = "[" + httpMethod + "] [" + resourcePath + "]";

        if (!isMappingsContainingCurrentMapping(mappings, currentMapping)) {
            NoResourceFoundException noResourceFoundException = new NoResourceFoundException(HttpMethod.valueOf(httpMethod), resourcePath);
            resolver.resolveException(request, response, null, noResourceFoundException);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isMappingsContainingCurrentMapping(Set<String> mappings, String currentMapping) {
        return mappings.stream()
                .anyMatch(mapping -> mapping.equals(currentMapping) ||
                        Pattern.matches(mapping
                                .replaceAll("\\{.*?}", ".*")
                                .replace("[", "\\[")
                                .replace("]", "\\]"), currentMapping));
    }

}
