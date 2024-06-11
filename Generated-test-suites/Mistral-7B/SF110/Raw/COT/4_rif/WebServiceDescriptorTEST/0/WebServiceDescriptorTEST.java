package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebServiceDescriptorTest {

    // Test constructor
    @Test
    public void testWebServiceDescriptorConstructor() {
        Class mockServiceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, targetNamespace, typesNamespace);
        assertEquals(mockServiceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    // Test getter and setter methods for serviceClazz
    @Test
    public void testGetServiceClazz() {
        Class mockServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, null, null);
        assertEquals(mockServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class oldServiceClazz = Mockito.mock(Class.class);
        Class newServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(oldServiceClazz, null, null);
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    // Test getter and setter methods for targetNamespace
    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "targetNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, targetNamespace, null);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String oldTargetNamespace = "oldTargetNamespace";
        String newTargetNamespace = "newTargetNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, oldTargetNamespace, null);
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    // Test getter and setter methods for typesNamespace
    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, typesNamespace);
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String oldTypesNamespace = "oldTypesNamespace";
        String newTypesNamespace = "newTypesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, oldTypesNamespace);
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        Class mockServiceClazz1 = Mockito.mock(Class.class);
        Class mockServiceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(mockServiceClazz1, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz2, null, null);
        assertNotSame(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor1.hashCode());
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(mockServiceClazz1, null, null);
        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());
    }

    // Test equals method
    @Test
    public void testEquals() {
        Class mockServiceClazz1 = Mockito.mock(Class.class);
        Class mockServiceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(mockServiceClazz1, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz2, null, null);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(mockServiceClazz1, null, null);
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));
    }

    // Edge cases testing
    // Test constructor with null serviceClazz
    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorConstructorWithNullServiceClazz() {
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
        new WebServiceDescriptor(null, targetNamespace, typesNamespace);
    }

    // Test constructor with empty targetNamespace and typesNamespace
    @Test
    public void testWebServiceDescriptorConstructorWithEmptyNamespaces() {
        Class mockServiceClazz = Mockito.mock(Class.class);
        String targetNamespace = "";
        String typesNamespace = "";
        new WebServiceDescriptor(mockServiceClazz, targetNamespace, typesNamespace);
        assertEquals(mockServiceClazz, new WebServiceDescriptor(mockServiceClazz, targetNamespace, typesNamespace).getServiceClazz());
        assertEquals(targetNamespace, new WebServiceDescriptor(mockServiceClazz, targetNamespace, typesNamespace).getTargetNamespace());
        assertEquals(typesNamespace, new WebServiceDescriptor(mockServiceClazz, targetNamespace, typesNamespace).getTypesNamespace());
    }

    // Test equals method with null obj
    @Test
    public void testEqualsWithNullObj() {
        Class mockServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, null, null);
        assertFalse(webServiceDescriptor.equals(null));
    }

    // Test equals method with different classes
    @Test
    public void testEqualsWithDifferentClasses() {
        Class mockServiceClazz1 = Mockito.mock(Class.class);
        Class mockServiceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(mockServiceClazz1, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz2, null, null);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    // Test equals method with same objects
    @Test
    public void testEqualsWithSameObjects() {
        Class mockServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(mockServiceClazz, null, null);
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
}