# Rest Web Service Util

This library gives you two basic features when you are working with RestService and jax-rs.

There are the result of working in angular projects where i use a lot of web services rest.


## ObjectMapperProvider

### Requires:
- Jackson as jax-rs provider (the library must be in the server)

### Features:
- DataFormat using full utc format

Works with OffsetDateTime


- Accept simple value as array

Example: 

Without the provider:

```json
{
    "foo": {"msg": "Hello World" }
}
```

With the provider
```json
{
    "foo": [{"msg": "Hello World" }]
}
```

- Disable the use of getters and setter as mapper by default

## ValidationExceptionMapper

### Requires:
- Nothing

### Features:
- Default mapper for the EjbException in jax-rs applications. It takes the message in the exception as key and search it in resource bundle file. (By default: messages.properties)

- Use the HttpServletRequest to get the locale of the request and send the correct message.

- In the case that the cause is a ConstraintViolationException. Iterate all the messages and return an array of errors.
