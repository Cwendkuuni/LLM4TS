// Your complete JUnit 4 test file for the given WebServiceDescriptor class goes here

package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

@RunWith(Parameterized.class)
public class WebServiceDescriptorTest {
    private WebServiceDescriptor webServiceDescriptor1;
    private WebServiceDescriptor webServiceDescriptor2;
    private Class<SomeService> serviceClazz;

    public WebServiceDescriptorTest(Class<SomeService> serviceClazz) {
        this.serviceClazz = serviceClazz;
        this.webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        this.webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace2");
    }

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
            new Object[]{SomeService.class},
            new Object[]{SomeOtherService.class}
        });
    }

    @Test
    public void testWebServiceDescriptor() {
        // Test constructor
        Class<?> serviceClazz = SomeService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        assertNotNull(webServiceDescriptor);
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());

        // Test setters
        String newTargetNamespace = "newTargetNamespace";
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());

        String newTypesNamespace = "newTypesNamespace";
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());

        // Test hashCode and equals
        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor1.hashCode());
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertFalse(webServiceDescriptor1.equals(new Object()));
        assertFalse(webServiceDescriptor2.equals(new Object()));

        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(webServiceDescriptor1.getServiceClazz(), webServiceDescriptor1.getTargetNamespace(), webServiceDescriptor1.getTypesNamespace());
        assertTrue(webServiceDescriptor1.equals(descriptor3));
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
    }

    @Test(expected = NullPointerException.class)
    public void testNullGettersAndSetters() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(SomeService.class, "targetNamespace", "typesNamespace");
        webServiceDescriptor.setServiceClazz(null);
        webServiceDescriptor.setTargetNamespace(null);
        webServiceDescriptor.setTypesNamespace(null);
    }
}