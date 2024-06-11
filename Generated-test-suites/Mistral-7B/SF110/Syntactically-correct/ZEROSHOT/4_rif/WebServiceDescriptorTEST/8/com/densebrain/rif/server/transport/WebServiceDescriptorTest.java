package com.densebrain.rif.server.transport;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class WebServiceDescriptorTest
{
    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp()
    {
        Class mockServiceClazz = Mockito.mock(Class.class);
        webServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, "targetNamespace", "typesNamespace");
    }

    @Test
    public void testConstructor()
    {
        assertNotNull(webServiceDescriptor);
    }

    @Test
    public void testGetServiceClazz()
    {
        assertEquals(mockServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz()
    {
        Class anotherMockServiceClazz = Mockito.mock(Class.class);
        webServiceDescriptor.setServiceClazz(anotherMockServiceClazz);
        assertEquals(anotherMockServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace()
    {
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace()
    {
        String newTargetNamespace = "newTargetNamespace";
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace()
    {
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace()
    {
        String newTypesNamespace = "newTypesNamespace";
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode()
    {
        Class anotherMockServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor anotherWebServiceDescriptor = new WebServiceDescriptor(anotherMockServiceClazz, "targetNamespace", "typesNamespace");
        int firstHashCode = webServiceDescriptor.hashCode();
        webServiceDescriptor.setServiceClazz(mockServiceClazz);
        int secondHashCode = webServiceDescriptor.hashCode();
        assertNotSame(firstHashCode, secondHashCode);
        assertEquals(webServiceDescriptor.hashCode(), webServiceDescriptor.hashCode());
        assertTrue(firstHashCode != 0);
    }

    @Test
    public void testEquals()
    {
        Class anotherMockServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor anotherWebServiceDescriptor = new WebServiceDescriptor(anotherMockServiceClazz, "targetNamespace", "typesNamespace");
        WebServiceDescriptor equalWebServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, "targetNamespace", "typesNamespace");
        assertFalse(webServiceDescriptor.equals(null));
        assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
        assertFalse(webServiceDescriptor.equals(anotherWebServiceDescriptor));
        webServiceDescriptor.setServiceClazz(anotherMockServiceClazz);
        assertTrue(webServiceDescriptor.equals(anotherWebServiceDescriptor));
    }
}