package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstruction() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetAndGetServiceClazz() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);

        webServiceDescriptor.setServiceClazz(serviceClazz);
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetAndGetTargetNamespace() {
        String targetNamespace = "targetNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);

        webServiceDescriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetAndGetTypesNamespace() {
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);

        webServiceDescriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));

        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace1");

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));

        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(null, "targetNamespace1", "typesNamespace1");

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor4));

        WebServiceDescriptor webServiceDescriptor5 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", null);

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor5));
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());

        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace1");

        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());

        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(null, "targetNamespace1", "typesNamespace1");

        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor4.hashCode());

        WebServiceDescriptor webServiceDescriptor5 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", null);

        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor5.hashCode());
    }
}