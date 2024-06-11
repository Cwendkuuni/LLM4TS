package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class WebServiceDescriptorTest
{
    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp()
    {
        Class serviceClazz = Mockito.mock(Class.class);
        webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
    }

    @Test
    public void testConstructor()
    {
        assertNotNull(webServiceDescriptor);
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
        assertEquals(serviceClazz.getClass(), webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetServiceClazz()
    {
        assertEquals(serviceClazz.getClass(), webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz()
    {
        Class anotherServiceClazz = Mockito.mock(Class.class);
        webServiceDescriptor.setServiceClazz(anotherServiceClazz);
        assertEquals(anotherServiceClazz.getClass(), webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace()
    {
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace()
    {
        String anotherTargetNamespace = "anotherTargetNamespace";
        webServiceDescriptor.setTargetNamespace(anotherTargetNamespace);
        assertEquals(anotherTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace()
    {
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace()
    {
        String anotherTypesNamespace = "anotherTypesNamespace";
        webServiceDescriptor.setTypesNamespace(anotherTypesNamespace);
        assertEquals(anotherTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode()
    {
        Class anotherServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor anotherWebServiceDescriptor = new WebServiceDescriptor(anotherServiceClazz, "targetNamespace", "typesNamespace");
        assertEquals(webServiceDescriptor.hashCode(), webServiceDescriptor.hashCode());
        assertNotEquals(webServiceDescriptor.hashCode(), anotherWebServiceDescriptor.hashCode());
    }

    @Test
    public void testEquals()
    {
        Class anotherServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor anotherWebServiceDescriptor = new WebServiceDescriptor(anotherServiceClazz, "targetNamespace", "typesNamespace");
        assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
        assertFalse(webServiceDescriptor.equals(null));
        assertFalse(webServiceDescriptor.equals("not an object"));
        assertFalse(webServiceDescriptor.equals(anotherWebServiceDescriptor));
        webServiceDescriptor.setServiceClazz(serviceClazz);
        assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
        assertTrue(webServiceDescriptor.equals(new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace")));
        assertFalse(webServiceDescriptor.equals(new WebServiceDescriptor(anotherServiceClazz, "targetNamespace", "typesNamespace")));
    }
}