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

    // Test getServiceClazz()
    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "", "");
        Class serviceClazz = webServiceDescriptor.getServiceClazz();

        assertNotNull(serviceClazz);
    }

    // Test setServiceClazz()
    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "", "");
        Class newServiceClazz = Mockito.mock(Class.class);

        webServiceDescriptor.setServiceClazz(newServiceClazz);

        assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    // Test getTargetNamespace()
    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "", "");
        String targetNamespace = "newTargetNamespace";

        webServiceDescriptor.setTargetNamespace(targetNamespace);

        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    // Test setTargetNamespace()
    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "", "");
        String newTargetNamespace = "newTargetNamespace";

        webServiceDescriptor.setTargetNamespace(newTargetNamespace);

        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    // Test getTypesNamespace()
    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "", "");
        String typesNamespace = "newTypesNamespace";

        webServiceDescriptor.setTypesNamespace(typesNamespace);

        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    // Test setTypesNamespace()
    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "", "");
        String newTypesNamespace = "newTypesNamespace";

        webServiceDescriptor.setTypesNamespace(newTypesNamespace);

        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    // Test hashCode()
    @Test
    public void testHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());

        serviceClazz1 = Mockito.mock(Class.class);
        serviceClazz2 = Mockito.mock(Class.class);
        targetNamespace = "newTargetNamespace";
        typesNamespace = "newTypesNamespace";

        webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());

        serviceClazz1 = Mockito.mock(Class.class);
        serviceClazz2 = Mockito.mock(Class.class);
        targetNamespace = "targetNamespace";
        typesNamespace = "newTypesNamespace";

        webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());

        serviceClazz1 = Mockito.mock(Class.class);
        serviceClazz2 = Mockito.mock(Class.class);
        targetNamespace = "targetNamespace";
        typesNamespace = "typesNamespace";

        webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        // Check if serviceClazz is null
        serviceClazz1 = null;
        serviceClazz2 = Mockito.mock(Class.class);

        webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    // Test equals() when objects are equal
    @Test
    public void testEqualsWhenObjectsAreEqual() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    // Test equals() when objects are not equal due to different serviceClazz
    @Test
    public void testEqualsWhenObjectsAreNotEqualDueToDifferentServiceClazz() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    // Test equals() when objects are not equal due to different targetNamespace
    @Test
    public void testEqualsWhenObjectsAreNotEqualDueToDifferentTargetNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace1 = "targetNamespace1";
        String targetNamespace2 = "targetNamespace2";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace1, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace2, typesNamespace);

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    // Test equals() when objects are not equal due to different typesNamespace
    @Test
    public void testEqualsWhenObjectsAreNotEqualDueToDifferentTypesNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace1 = "typesNamespace1";
        String typesNamespace2 = "typesNamespace2";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace2);

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    // Test equals() when comparing with null
    @Test
    public void testEqualsWhenComparingWithNull() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "", "");

        assertFalse(webServiceDescriptor.equals(null));
    }

    // Test equals() when comparing with different class
    @Test
    public void testEqualsWhenComparingWithDifferentClass() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "", "");

        assertFalse(webServiceDescriptor.equals(new Object()));
    }

}