package com.github.panchitoboy.common.rest;

import com.github.panchitoboy.common.example.EntityExample;
import com.github.panchitoboy.common.example.JAXRSConfigurationExample;
import com.github.panchitoboy.common.example.ResourceExample;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
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
public class EJBExceptionMapperTest {

    @Deployment
    public static Archive<?> deployment() {

        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();

        WebArchive war = ShrinkWrap.create(WebArchive.class)
                .addClasses(EJBExceptionMapper.class)
                .addClasses(ObjectMapperProvider.class)
                .addClasses(EntityExample.class)
                .addClasses(JAXRSConfigurationExample.class, ResourceExample.class)
                .addAsResource("messages.properties")
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
    public void validateException() throws IOException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(EJBExceptionMapper.resourceBundleFile);
        String message = resourceBundle.getString(EntityExample.VALIDATION_NAME_NULL);

        Response r1 = target.path("/exception").request(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).get();
        Assert.assertEquals("Response code must be 400", 400, r1.getStatus());
        String value = r1.readEntity(String.class);
        Assert.assertEquals("Response must be the same", "[\"" + message + "\"]", value);
    }

}
