package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class WebServiceDescriptorTest
{
    @Test
    public void testWebServiceDescriptorConstructor()
    {
        Class mockServiceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, targetNamespace, typesNamespace);

        assertNotNull(webServiceDescriptor);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertSame(mockServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSettersAndGetters()
    {
        Class mockServiceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, targetNamespace, typesNamespace);

        webServiceDescriptor.setServiceClazz(null);
        assertNull(webServiceDescriptor.getServiceClazz());

        webServiceDescriptor.setTargetNamespace("newTargetNamespace");
        assertEquals("newTargetNamespace", webServiceDescriptor.getTargetNamespace());

        webServiceDescriptor.setTypesNamespace("newTypesNamespace");
        assertEquals("newTypesNamespace", webServiceDescriptor.getTypesNamespace());

        webServiceDescriptor.setServiceClazz(mockServiceClazz);
        assertSame(mockServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testHashCode()
    {
        Class mockServiceClazz1 = Mockito.mock(Class.class);
        Class mockServiceClazz2 = Mockito.mock(Class.class);

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(mockServiceClazz1, "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz2, "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(mockServiceClazz1, "newTargetNamespace", "typesNamespace");

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor1.hashCode());
        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());
    }

    @Test
    public void testEquals()
    {
        Class mockServiceClazz1 = Mockito.mock(Class.class);
        Class mockServiceClazz2 = Mockito.mock(Class.class);

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(mockServiceClazz1, "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz2, "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(mockServiceClazz1, "newTargetNamespace", "typesNamespace");

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
    }
}