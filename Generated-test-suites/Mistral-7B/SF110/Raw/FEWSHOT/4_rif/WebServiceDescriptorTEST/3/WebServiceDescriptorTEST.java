package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "target-namespace";
        String typesNamespace = "types-namespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertNotNull(webServiceDescriptor);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGettersAndSetters() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "target-namespace";
        String typesNamespace = "types-namespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        webServiceDescriptor.setServiceClazz(null);
        assertNull(webServiceDescriptor.getServiceClazz());

        webServiceDescriptor.setServiceClazz(serviceClazz);
        assertNotNull(webServiceDescriptor.getServiceClazz());

        webServiceDescriptor.setTargetNamespace("new-target-namespace");
        assertEquals("new-target-namespace", webServiceDescriptor.getTargetNamespace());

        webServiceDescriptor.setTypesNamespace("new-types-namespace");
        assertEquals("new-types-namespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "target-namespace";
        String typesNamespace1 = "types-namespace1";
        String typesNamespace2 = "types-namespace2";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace2);

        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace1);

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "target-namespace";
        String typesNamespace1 = "types-namespace1";
        String typesNamespace2 = "types-namespace2";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace2);
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace1);

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
}