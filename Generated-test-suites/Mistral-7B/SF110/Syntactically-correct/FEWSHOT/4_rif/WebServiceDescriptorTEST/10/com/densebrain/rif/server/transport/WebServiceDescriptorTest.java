package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstruction() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertNotNull(webServiceDescriptor);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGettersAndSetters() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);
        webServiceDescriptor.setTargetNamespace("targetNamespace");
        webServiceDescriptor.setTypesNamespace("typesNamespace");
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
        webServiceDescriptor.setServiceClazz(null);
        assertNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace1");
        assertEquals(webServiceDescriptor1, webServiceDescriptor2);
        assertNotEquals(webServiceDescriptor1, webServiceDescriptor3);
        assertNotEquals(webServiceDescriptor1, webServiceDescriptor4);
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace1");
        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();
        int hashCode3 = webServiceDescriptor3.hashCode();
        int hashCode4 = webServiceDescriptor4.hashCode();
        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }
}