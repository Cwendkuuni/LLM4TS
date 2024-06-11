package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class mockServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, "targetNamespace", "typesNamespace");
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
        assertEquals(mockServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetServiceClazz() {
        Class mockServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, "targetNamespace", "typesNamespace");
        assertEquals(mockServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class mockServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        webServiceDescriptor.setServiceClazz(mockServiceClazz);
        assertEquals(mockServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, "typesNamespace");
        webServiceDescriptor.setTargetNamespace("targetNamespace");
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", null);
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", null);
        webServiceDescriptor.setTypesNamespace("typesNamespace");
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        webServiceDescriptor.hashCode();
    }

    @Test
    public void testEqualsWithSameObject() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace2", "typesNamespace2");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithDifferentServiceClazz() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Mockito.mock(Class.class, "anotherClass"), "targetNamespace1", "typesNamespace1");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithNull() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "typesNamespace");
        assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClassType() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "typesNamespace");
        assertFalse(webServiceDescriptor.equals("anotherObject"));
    }
}