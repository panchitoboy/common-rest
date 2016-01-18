package com.github.panchitoboy.common.example;

import com.github.panchitoboy.common.rest.EJBExceptionMapper;
import com.github.panchitoboy.common.rest.ObjectMapperProvider;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("resources")
public class JAXRSConfigurationExample extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> set = new HashSet<Class<?>>(2);
        set.add(ObjectMapperProvider.class);
        set.add(ResourceExample.class);
        set.add(EJBExceptionMapper.class);
        return set;
    }

}
