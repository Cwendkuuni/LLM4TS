
package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "http://namespace.com", "http://types.com");
        assertEquals("http://namespace.com", webServiceDescriptor.getTargetNamespace());
        assertEquals("http://types.com", webServiceDescriptor.getTypesNamespace());
        assertEquals(Object.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "http://namespace.com", "http://types.com");
        assertEquals(Object.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(String.class, "http://namespace.com", "http://types.com");
        webServiceDescriptor.setServiceClazz(Object.class);
        assertEquals(Object.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "http://namespace.com", "http://types.com");
        assertEquals("http://namespace.com", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "http://namespace.com", "http://types.com");
        webServiceDescriptor.setTargetNamespace("newNamespace");
        assertEquals("newNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "http://namespace.com", "http://types.com");
        assertEquals("http://types.com", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "http://namespace.com", "http://types.com");
        webServiceDescriptor.setTypesNamespace("newTypesNamespace");
        assertEquals("newTypesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullServiceClazz() {
        new WebServiceDescriptor(null, "http://namespace.com", "http://types.com");
    }

    @Test
    public void testSetServiceClazzNull() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "http://namespace.com", "http://types.com");
        webServiceDescriptor.setServiceClazz(null);
        assertNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazzDifferentType() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "http://namespace.com", "http://types.com");
        webServiceDescriptor.setServiceClazz(String.class);
        assertNotEquals(String.class, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetTargetNamespaceEmpty() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "http://namespace.com", "http://types.com");
        webServiceDescriptor.setTargetNamespace("");
        assertEquals("", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTypesNamespaceEmpty() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "http://namespace.com", "http://types.com");
        webServiceDescriptor.setTypesNamespace("");
        assertEquals("", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Object.class, "http://namespace1.com", "http://types1.com");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Object.class, "http://namespace2.com", "http://types2.com");

        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(Object.class, "http://namespace1.com", "http://types1.com");
        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());

        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(String.class, "http://namespace1.com", "http://types1.com");
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor4.hashCode());

        WebServiceDescriptor webServiceDescriptor5 = new WebServiceDescriptor(Object.class, "http://namespace1.com", "http://types2.com");
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor5.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Object.class, "http://namespace1.com", "http://types1.com");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Object.class, "http://namespace2.com", "http://types2.com");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(Object.class, "http://namespace1.com", "http://types1.com");

        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(Object.class, "http://namespace1.com", "http://types2.com");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));
    }

}
