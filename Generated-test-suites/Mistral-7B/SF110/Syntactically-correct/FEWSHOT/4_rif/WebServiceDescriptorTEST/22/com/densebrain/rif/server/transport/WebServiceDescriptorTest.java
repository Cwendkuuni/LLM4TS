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
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);

        assertSame(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class oldServiceClazz = Mockito.mock(Class.class);
        Class newServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(oldServiceClazz, null, null);

        webServiceDescriptor.setServiceClazz(newServiceClazz);

        assertSame(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", null);

        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String oldTargetNamespace = "oldTargetNamespace";
        String newTargetNamespace = "newTargetNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, oldTargetNamespace, null);

        webServiceDescriptor.setTargetNamespace(newTargetNamespace);

        assertEquals("newTargetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, "typesNamespace");

        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String oldTypesNamespace = "oldTypesNamespace";
        String newTypesNamespace = "newTypesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, oldTypesNamespace);

        webServiceDescriptor.setTypesNamespace(newTypesNamespace);

        assertEquals("newTypesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());

        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(null, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(serviceClazz, null, typesNamespace);
        WebServiceDescriptor webServiceDescriptor5 = new WebServiceDescriptor(serviceClazz, targetNamespace, null);

        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor4.hashCode());
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor5.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));

        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(null, targetNamespace, typesNamespace);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));

        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(serviceClazz, null, typesNamespace);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor4));

        WebServiceDescriptor webServiceDescriptor5 = new WebServiceDescriptor(serviceClazz, targetNamespace, null);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor5));

        WebServiceDescriptor webServiceDescriptor6 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        webServiceDescriptor6.setServiceClazz(null);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor6));
    }
}