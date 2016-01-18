package com.github.panchitoboy.common.rest;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import javax.ejb.EJBException;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class EJBExceptionMapper implements ExceptionMapper<EJBException> {

    @Context
    HttpServletRequest req;

    public static final String resourceBundleFile = "messages";

    @Override
    public Response toResponse(EJBException ex) {
        Exception exception = ex.getCausedByException();
        if (exception instanceof ConstraintViolationException) {
            JsonArrayBuilder array = Json.createArrayBuilder();
            ConstraintViolationException cve = (ConstraintViolationException) exception;
            Set<ConstraintViolation<?>> violations = cve.getConstraintViolations();

            violations.stream().forEach((violation) -> {
                array.add(getMessage(violation.getMessage(), req));
            });

            return Response.status(Response.Status.BAD_REQUEST).entity(array.build()).type(MediaType.APPLICATION_JSON).build();
        }

        JsonArrayBuilder array = Json.createArrayBuilder();
        array.add(getMessage(exception.getMessage(), req));
        return Response.status(Response.Status.BAD_REQUEST).entity(array.build()).type(MediaType.APPLICATION_JSON).build();
    }

    private String getMessage(String key, HttpServletRequest req) {
        Locale currentLocale = req.getLocale().stripExtensions();
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle(resourceBundleFile, currentLocale);
            String message = resourceBundle.getString(key);
            return message;
        } catch (MissingResourceException ex) {
            return key;
        }
    }

}
