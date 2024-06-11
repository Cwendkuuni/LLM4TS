package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

public class WebServiceDescriptorTest
{
    private WebServiceDescriptor webServiceDescriptor;
    private Class serviceClazzMock;

    @Before
    public void setup()
    {
        serviceClazzMock = mock(Class.class);
        webServiceDescriptor = new WebServiceDescriptor(serviceClazzMock, "targetNamespace", "typesNamespace");
    }

    @Test
    public void testWebServiceDescriptorWithValidArguments()
    {
        assertSame(serviceClazzMock, webServiceDescriptor.getServiceClazz());
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorNullServiceClazz()
    {
        new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorNullTargetNamespace()
    {
        new WebServiceDescriptor(serviceClazzMock, null, "typesNamespace");
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorNullTypesNamespace()
    {
        new WebServiceDescriptor(serviceClazzMock, "targetNamespace", null);
    }

    @Test
    public void testWebServiceDescriptorHashcode()
    {
        Class otherServiceClazzMock = mock(Class.class);
        WebServiceDescriptor other = new WebServiceDescriptor(otherServiceClazzMock, "targetNamespace", "typesNamespace");
        assertNotSame(webServiceDescriptor.hashCode(), other.hashCode());
        webServiceDescriptor.setServiceClazz(serviceClazzMock);
        other.setServiceClazz(serviceClazzMock);
        assertEquals(webServiceDescriptor.hashCode(), other.hashCode());
    }

    @Test
    public void testWebServiceDescriptorEqualsSelf()
    {
        assertEquals(webServiceDescriptor, webServiceDescriptor);
    }

    @Test
    public void testWebServiceDescriptorEqualsSameObject()
    {
        assertEquals(webServiceDescriptor, webServiceDescriptor);
    }

    @Test
    public void testWebServiceDescriptorEqualsOtherSameClassAndValues()
    {
        WebServiceDescriptor other = new WebServiceDescriptor(serviceClazzMock, "targetNamespace", "typesNamespace");
        assertEquals(webServiceDescriptor, other);
    }

    @Test
    public void testWebServiceDescriptorEqualsOtherDifferentClass()
    {
        Object other = new Object();
        assertNotSame(webServiceDescriptor, other);
    }

    @Test
    public void testWebServiceDescriptorEqualsOtherDifferentClassAndValues()
    {
        WebServiceDescriptor other = new WebServiceDescriptor(otherServiceClazzMock, "targetNamespace1", "typesNamespace1");
        assertNotSame(webServiceDescriptor, other);
    }

    @Test
    public void testWebServiceDescriptorEqualsOtherNull()
    {
        assertNotSame(webServiceDescriptor, null);
    }

    @Test
    public void testWebServiceDescriptorEqualsOtherNullServiceClazz()
    {
        WebServiceDescriptor other = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        assertNotSame(webServiceDescriptor, other);
    }

    @Test
    public void testWebServiceDescriptorGettersSetters()
    {
        String targetNamespace = "newTargetNamespace";
        String typesNamespace = "newTypesNamespace";
        webServiceDescriptor.setTargetNamespace(targetNamespace);
        webServiceDescriptor.setTypesNamespace(typesNamespace);
        assertNull(webServiceDescriptor.getTargetNamespace());
        assertNull(webServiceDescriptor.getTypesNamespace());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }
}