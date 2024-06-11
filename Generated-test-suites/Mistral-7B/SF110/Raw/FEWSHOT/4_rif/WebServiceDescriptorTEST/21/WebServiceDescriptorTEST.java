package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertSame(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetSetServiceClazz() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);

        webServiceDescriptor.setServiceClazz(serviceClazz);
        assertSame(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);

        webServiceDescriptor.setTargetNamespace("http://example.com/target");
        assertEquals("http://example.com/target", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);

        webServiceDescriptor.setTypesNamespace("http://example.com/types");
        assertEquals("http://example.com/types", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "target1", "types1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "target1", "types1");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, "target2", "types2");

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor1.hashCode());
        assertEquals(webServiceDescriptor1.hashCode(), new WebServiceDescriptor(serviceClazz1, "target1", "types1").hashCode());
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "target1", "types1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz1, "target1", "types1");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz2, "target1", "types1");
        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(serviceClazz1, "target2", "types2");

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor4));
        assertFalse(webServiceDescriptor1.equals(null));
        assertFalse(webServiceDescriptor1.equals(""));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Object obj = new Object();
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "target", "types");

        assertFalse(webServiceDescriptor.equals(obj));
    }

    @Test
    public void testEqualsWithNullTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), null, "types");

        assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testEqualsWithNullTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "target", null);

        assertFalse(webServiceDescriptor.equals(null));
    }
}