package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebServiceDescriptorTest {

    // Test constructor
    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    // Test getters and setters for targetNamespace
    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", null);
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());

        String newTargetNamespace = "newTargetNamespace";
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    // Test getters and setters for typesNamespace
    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, "typesNamespace");
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());

        String newTypesNamespace = "newTypesNamespace";
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    // Test getters and setters for serviceClazz
    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), null, null);
        assertEquals(webServiceDescriptor.getServiceClazz(), webServiceDescriptor.getServiceClazz());

        Class newServiceClazz = Mockito.mock(Class.class);
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "namespace1", "namespace2");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Mockito.mock(Class.class), "namespace1", "namespace2");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(Mockito.mock(Class.class), "namespace3", "namespace2");

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());
    }

    // Test equals method
    @Test
    public void testEquals() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "namespace1", "namespace2");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Mockito.mock(Class.class), "namespace1", "namespace2");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(Mockito.mock(Class.class), "namespace3", "namespace2");

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
        assertFalse(webServiceDescriptor1.equals(null));
        assertFalse(webServiceDescriptor1.equals("wrongType"));
    }

    // Edge cases and exception handling
    @Test(expected = NullPointerException.class)
    public void testSetServiceClazzNull() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        webServiceDescriptor.setServiceClazz(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetTargetNamespaceNull() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        webServiceDescriptor.setTargetNamespace(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetTypesNamespaceNull() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        webServiceDescriptor.setTypesNamespace(null);
    }

    @Test
    public void testHashCodeSameObjects() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "namespace1", "namespace2");
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;
        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    @Test
    public void testEqualsSameObjects() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "namespace1", "namespace2");
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsDifferentObjects() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "namespace1", "namespace2");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Mockito.mock(Class.class), "namespace3", "namespace2");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsDifferentClasses() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class1.class), "namespace1", "namespace2");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Mockito.mock(Class2.class), "namespace1", "namespace2");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsNullObject() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "namespace1", "namespace2");
        assertFalse(webServiceDescriptor.equals(null));
    }
}