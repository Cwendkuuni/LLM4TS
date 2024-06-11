package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        assertNotNull(webServiceDescriptor);
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals("targetNamespace", webServiceServiceDescriptor.getTargetNamespace());
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        webServiceDescriptor.setServiceClazz(serviceClazz);
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(TestService.class, null, "typesNamespace");
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(TestService.class, null, "typesNamespace");
        webServiceDescriptor.setTargetNamespace("targetNamespace");
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(TestService.class, "targetNamespace", null);
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(TestService.class, "targetNamespace", null);
        webServiceDescriptor.setTypesNamespace("typesNamespace");
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullServiceClazz() {
        new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace2");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();
        int hashCode3 = webServiceDescriptor3.hashCode();
        assertNotSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertSame(hashCode1, webServiceDescriptor3.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(TestService2.class, "targetNamespace1", "typesNamespace1");
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
    }

    @Test
    public void testEqualsWithNull() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        assertFalse(webServiceDescriptor.equals(new Object()));
    }

    private static class TestService {
        // empty class
    }

    private static class TestService2 {
        // empty class different from TestService
    }
}