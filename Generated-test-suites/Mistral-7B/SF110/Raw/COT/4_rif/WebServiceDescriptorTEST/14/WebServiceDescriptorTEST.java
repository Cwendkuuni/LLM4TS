package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "testTargetNamespace";
        String typesNamespace = "testTypesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "testTargetNamespace", "testTypesNamespace");
        Class serviceClazz = webServiceDescriptor.getServiceClazz();
        assertNotNull(serviceClazz);
    }

    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "testTargetNamespace", "testTypesNamespace");
        Class newServiceClazz = Mockito.mock(Class.class);
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "testTargetNamespace", "testTypesNamespace");
        assertEquals("testTargetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "testTargetNamespace", "testTypesNamespace");
        String newTargetNamespace = "newTestTargetNamespace";
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "testTargetNamespace", "testTypesNamespace");
        assertEquals("testTypesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "testTargetNamespace", "testTypesNamespace");
        String newTypesNamespace = "newTestTypesNamespace";
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "testTargetNamespace", "testTypesNamespace");
        int hashCode = webServiceDescriptor.hashCode();
        fail("HashCode should not be called with null serviceClazz");
    }

    @Test
    public void testEqualsWithSameObject() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "testTargetNamespace", "testTypesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "testTargetNamespace", "testTypesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Mockito.mock(Class.class), "differentTargetNamespace", "testTypesNamespace");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithDifferentClassTypes() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "testTargetNamespace", "testTypesNamespace");
        Object webServiceDescriptor2 = new Object();
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithNullObject() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "testTargetNamespace", "testTypesNamespace");
        assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testHashCodeAndEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "testTargetNamespace", "testTypesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "testTargetNamespace", "testTypesNamespace");
        assertEquals(webServiceDescriptor1, webServiceDescriptor2);
        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentServiceClazzes() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "testTargetNamespace", "testTypesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "testTargetNamespace", "testTypesNamespace");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }
}