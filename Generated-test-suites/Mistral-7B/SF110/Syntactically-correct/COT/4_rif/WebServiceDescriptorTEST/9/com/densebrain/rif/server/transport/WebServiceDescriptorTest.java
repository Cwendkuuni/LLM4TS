package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebServiceDescriptorTest {

    // Test constructor
    @Test
    public void testWebServiceDescriptorConstruction() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    // Test getter and setter for serviceClazz
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());

        Class newServiceClazz = Mockito.mock(Class.class);
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    // Test getter and setter for targetNamespace
    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "typesNamespace");
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());

        String newTargetNamespace = "newTargetNamespace";
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    // Test getter and setter for typesNamespace
    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "typesNamespace");
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());

        String newTypesNamespace = "newTypesNamespace";
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    // Test equals method
    @Test
    public void testEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace2", "typesNamespace2");

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));

        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));

        webServiceDescriptor3.setServiceClazz(serviceClazz2);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace2");

        assertNotSame(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());

        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());

        Mockito.when(serviceClazz.hashCode()).thenReturn(0);
        assertEquals(0, webServiceDescriptor1.hashCode());
    }

    // Edge cases and exception handling tests

    // Test null serviceClazz
    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorNullServiceClazz() {
        new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
    }

    // Test null targetNamespace
    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorNullTargetNamespace() {
        new WebServiceDescriptor(Mockito.mock(Class.class), null, "typesNamespace");
    }

    // Test null typesNamespace
    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorNullTypesNamespace() {
        new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", null);
    }

    // Test empty targetNamespace
    @Test
    public void testWebServiceDescriptorEmptyTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "", "typesNamespace");
        assertEquals("", webServiceDescriptor.getTargetNamespace());
    }

    // Test empty typesNamespace
    @Test
    public void testWebServiceDescriptorEmptyTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "");
        assertEquals("", webServiceDescriptor.getTypesNamespace());
    }

    // Test equal objects with same values
    @Test
    public void testWebServiceDescriptorEqualObjects() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    // Test unequal objects with different serviceClazz
    @Test
    public void testWebServiceDescriptorUnequalObjectsDifferentServiceClazz() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace1", "typesNamespace1");

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertNotSame(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    // Test unequal objects with different targetNamespace
    @Test
    public void testWebServiceDescriptorUnequalObjectsDifferentTargetNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace1");

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertNotSame(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    // Test unequal objects with different typesNamespace
    @Test
    public void testWebServiceDescriptorUnequalObjectsDifferentTypesNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace2");

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertNotSame(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }
}