package com.github.panchitoboy.common.rest;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.panchitoboy.common.example.EntityExample;
import com.github.panchitoboy.common.example.JAXRSConfigurationExample;
import com.github.panchitoboy.common.example.ResourceExample;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class ObjectMapperProviderTest {

    @Deployment
    public static Archive<?> deployment() {

        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();

        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addClasses(EJBExceptionMapper.class)
                .addClasses(ObjectMapperProvider.class)
                .addClasses(JavaTimeModule.class)
                .addClasses(EntityExample.class)
                .addClasses(JAXRSConfigurationExample.class, ResourceExample.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(files);

        System.out.println(war.toString(true));

        return war;
    }

    private WebTarget target;

    @ArquillianResource
    private URL base;

    @Before
    public void setUp() throws MalformedURLException {

        Client client = ClientBuilder.newBuilder().register(JacksonFeature.class).build();
        target = client.target(URI.create(new URL(base, "resources/test").toExternalForm()));
    }

    @Test
    public void utcFormatResponse() throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        OffsetDateTime time = OffsetDateTime.now().withHour(21).withMinute(0).withSecond(0).withNano(0);

        Response r1 = target.path("/date").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
        Assert.assertEquals("Response code must be 200", 200, r1.getStatus());
        String value = r1.readEntity(String.class);
        Assert.assertEquals("Response must be the same", "\"" + formatter.format(time) + "\"", value);
    }

    @Test
    public void singleValueAsArray() throws IOException {
        Response r1 = target.path("/array").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
        Assert.assertEquals("Response code must be 200", 200, r1.getStatus());
        String value = r1.readEntity(String.class);
        Assert.assertEquals("Response must be the same", "[\"" + ResourceExample.MESSAGE + "\"]", value);
    }

}
