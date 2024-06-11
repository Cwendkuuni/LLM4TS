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
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    // Test getters and setters for serviceClazz
    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "", "");
        Class serviceClazz = webServiceDescriptor.getServiceClazz();
        assertNotNull(serviceClazz);
        webServiceDescriptor.setServiceClazz(null);
        assertNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "", "");
        Class serviceClazz = Mockito.mock(Class.class);
        webServiceDescriptor.setServiceClazz(serviceClazz);
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    // Test getters and setters for targetNamespace
    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "", "");
        String targetNamespace = "targetNamespace";
        webServiceDescriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, "");
        String targetNamespace = "targetNamespace";
        webServiceDescriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    // Test getters and setters for typesNamespace
    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "", "");
        String typesNamespace = "typesNamespace";
        webServiceDescriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        String typesNamespace = "typesNamespace";
        webServiceDescriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    // Test hashCode method
    @Test
    public void testHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace2", "typesNamespace2");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();
        int hashCode3 = webServiceDescriptor3.hashCode();
        assertNotEquals(hashCode1, hashCode2);
        assertEquals(hashCode1, hashCode3);
    }

    // Test equals method with null object
    @Test
    public void testEqualsWithNullObject() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "", "");
        assertFalse(webServiceDescriptor.equals(null));
    }

    // Test equals method with different classes
    @Test
    public void testEqualsWithDifferentClasses() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "", "");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Mockito.mock(Class.class), "", "");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    // Test equals method with same objects
    @Test
    public void testEqualsWithSameObjects() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "", "");
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    // Test equals method with same values but different objects
    @Test
    public void testEqualsWithSameValuesDifferentObjects() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "", "");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "", "");
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    // Test equals method with different values
    @Test
    public void testEqualsWithDifferentValues() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "", "");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "differentTargetNamespace", "");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    // Test equals method with null serviceClazz
    @Test
    public void testEqualsWithNullServiceClazz() {
        String targetNamespace1 = "";
        String targetNamespace2 = "";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(null, targetNamespace1, "");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Mockito.mock(Class.class), targetNamespace2, "");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    // Test equals method with null targetNamespace
    @Test
    public void testEqualsWithNullTargetNamespace() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, null, "");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "", "");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    // Test equals method with null typesNamespace
    @Test
    public void testEqualsWithNullTypesNamespace() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "", null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "", "");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
}