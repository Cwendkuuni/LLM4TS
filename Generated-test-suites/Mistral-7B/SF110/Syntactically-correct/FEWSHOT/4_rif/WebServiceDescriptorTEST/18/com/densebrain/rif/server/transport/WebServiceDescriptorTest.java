package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstruction() {
        Class mockServiceClass = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(mockServiceClass, targetNamespace, typesNamespace);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertEquals(mockServiceClass, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetGetServiceClazz() {
        Class mockServiceClass = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        webServiceDescriptor.setServiceClazz(mockServiceClass);
        assertEquals(mockServiceClass, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        webServiceDescriptor.setTargetNamespace("targetNamespace");
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        webServiceDescriptor.setTypesNamespace("typesNamespace");
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class mockServiceClass = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(mockServiceClass, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClass, "targetNamespace2", "typesNamespace2");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(mockServiceClass, "targetNamespace1", "typesNamespace1");
        int result1 = webServiceDescriptor1.hashCode();
        int result2 = webServiceDescriptor2.hashCode();
        int result3 = webServiceDescriptor3.hashCode();
        assertNotSame(result1, result2);
        assertEquals(result1, result3);
    }

    @Test
    public void testEquals() {
        Class mockServiceClass = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(mockServiceClass, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClass, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(mockServiceClass, "targetNamespace2", "typesNamespace2");
        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(null, null, null);
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor4));
    }
}