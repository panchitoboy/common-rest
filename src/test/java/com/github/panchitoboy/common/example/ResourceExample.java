package com.github.panchitoboy.common.example;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Set;
import javax.ejb.Stateless;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("test")
@Stateless
public class ResourceExample {

    public static final String MESSAGE = "test";

    @GET
    @Path("date")
    @Produces(MediaType.APPLICATION_JSON)
    public OffsetDateTime getDate() {
        OffsetDateTime time = OffsetDateTime.now().withHour(21).withMinute(0).withSecond(0).withNano(0);
        return time;
    }

    @GET
    @Path("array")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList getArray() {
        ArrayList<String> array = new ArrayList<>();
        array.add(MESSAGE);
        return array;
    }

    @GET
    @Path("exception")
    @Produces(MediaType.APPLICATION_JSON)
    public EntityExample getEntity() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<EntityExample>> constraintViolations = validator.validate(new EntityExample());
        throw new ConstraintViolationException(constraintViolations);
    }

}
