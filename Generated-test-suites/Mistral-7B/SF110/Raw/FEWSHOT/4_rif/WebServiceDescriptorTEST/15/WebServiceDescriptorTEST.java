package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class WebServiceDescriptorTest {
    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/myservice";
        String typesNamespace = "http://example.com/myservice/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = MyService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);
        assertSame(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz1 = MyService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        webServiceDescriptor.setServiceClazz(serviceClazz1);
        assertSame(serviceClazz1, webServiceDescriptor.getServiceClazz());

        Class serviceClazz2 = AnotherService.class;
        webServiceDescriptor.setServiceClazz(serviceClazz2);
        assertSame(serviceClazz2, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "http://example.com/myservice";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, targetNamespace, null);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String targetNamespace1 = "http://example.com/myservice";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        webServiceDescriptor.setTargetNamespace(targetNamespace1);
        assertEquals(targetNamespace1, webServiceDescriptor.getTargetNamespace());

        String targetNamespace2 = "http://another.com/myservice";
        webServiceDescriptor.setTargetNamespace(targetNamespace2);
        assertEquals(targetNamespace2, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "http://example.com/myservice/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, typesNamespace);
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String typesNamespace1 = "http://example.com/myservice/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        webServiceDescriptor.setTypesNamespace(typesNamespace1);
        assertEquals(typesNamespace1, webServiceDescriptor.getTypesNamespace());

        String typesNamespace2 = "http://another.com/myservice/types";
        webServiceDescriptor.setTypesNamespace(typesNamespace2);
        assertEquals(typesNamespace2, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/myservice";
        String typesNamespace = "http://example.com/myservice/types";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());

        Class serviceClazz2 = AnotherService.class;
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());

        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(serviceClazz, null, typesNamespace);
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor4.hashCode());

        WebServiceDescriptor webServiceDescriptor5 = new WebServiceDescriptor(serviceClazz, targetNamespace, null);
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor5.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/myservice";
        String typesNamespace = "http://example.com/myservice/types";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));

        Class serviceClazz2 = AnotherService.class;
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));

        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(serviceClazz, null, typesNamespace);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor4));

        WebServiceDescriptor webServiceDescriptor5 = new WebServiceDescriptor(serviceClazz, targetNamespace, null);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor5));
    }

    @Test
    public void testEqualsNull() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testEqualsSameInstance() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
    }

    private static class MyService {}
    private static class AnotherService {}
}