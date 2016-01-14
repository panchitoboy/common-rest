# Rest Web Service Util

This library gives you two importants features when you are working with RestService and jax-rs.

There are the result of working in angular projects where i use a lot of web services rest.

## ObjectMapperProvider

### Requires:
- Jackson as jax-rs provider (the library must be in the server)

### Features:
- DataFormat using full utc format

- Accept simple value as array

Example: 
Without the provider

Array with one element:

{
    "foo": {"msg": "Hello World" }
}

With the provider
{
    "foo": [{"msg": "Hello World" }]
}

- Disable the use of getters and setter as mapper by default

## ValidationExceptionMapper

### Requires:
- Nothing, but user Apache Delta Spike lastest version.

### Features:
- Default mapper for the EjbException in jax-rs applications. It takes the message in the exception as key and search it in resource bundle file. (By default: messages.properties)

- Use Apache DeltaSpike to define a custom location of your messages file. 

- Use the HttpServletRequest to get the locale of the request and send the correct message.

- In the case that the cause is a ConstraintViolationException. Iterate all the messages and return an array of erros.

## Basic Usage

TO DO

## Examples

TO DO
