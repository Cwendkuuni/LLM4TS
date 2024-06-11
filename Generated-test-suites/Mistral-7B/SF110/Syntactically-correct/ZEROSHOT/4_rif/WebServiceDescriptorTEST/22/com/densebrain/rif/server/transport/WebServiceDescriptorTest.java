package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertSame(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "", "");

        assertSame(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class oldServiceClazz = Mockito.mock(Class.class);
        Class newServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(oldServiceClazz, "", "");

        webServiceDescriptor.setServiceClazz(newServiceClazz);

        assertSame(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "");

        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "", "newTargetNamespace");

        webServiceDescriptor.setTargetNamespace("newTargetNamespace");

        assertEquals("newTargetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "", "typesNamespace");

        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "", "");

        webServiceDescriptor.setTypesNamespace("newTypesNamespace");

        assertEquals("newTypesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "", "");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "", "");

        int hashcode1 = webServiceDescriptor1.hashCode();
        int hashcode2 = webServiceDescriptor2.hashCode();

        assertNotSame(hashcode1, hashcode2);

        webServiceDescriptor1.setServiceClazz(serviceClazz2);
        int hashcode3 = webServiceDescriptor1.hashCode();

        assertEquals(hashcode2, hashcode3);
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "", "");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "", "");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, "", "");

        assertFalse(webServiceDescriptor1.equals(null));
        assertFalse(webServiceDescriptor1.equals(""));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));

        webServiceDescriptor1.setServiceClazz(serviceClazz2);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
    }

    @Test
    public void testNullConstructor() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "", "");

        assertNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testEmptyConstructor() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "", "");

        assertEquals("", webServiceDescriptor.getTargetNamespace());
        assertEquals("", webServiceDescriptor.getTypesNamespace());
        assertNull(webServiceDescriptor.getServiceClazz());
    }

}