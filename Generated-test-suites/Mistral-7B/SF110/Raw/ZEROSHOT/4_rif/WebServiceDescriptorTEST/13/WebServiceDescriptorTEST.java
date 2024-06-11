package com.densebrain.rif.server.transport;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class WebServiceDescriptorTest
{
    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setUp()
    {
        Class serviceClazz = TestService.class;
        webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
    }

    @Test
    public void testWebServiceDescriptorConstructor()
    {
        assertSame(TestService.class, webServiceDescriptor.getServiceClazz());
        assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
        assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetAndGetServiceClazz()
    {
        Class anotherServiceClazz = AnotherService.class;
        webServiceDescriptor.setServiceClazz(anotherServiceClazz);
        assertSame(anotherServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test(expected = NullPointerException.class)
    public void testSetServiceClazzWithNull()
    {
        webServiceDescriptor.setServiceClazz(null);
    }

    @Test
    public void testSetAndGetTargetNamespace()
    {
        String anotherTargetNamespace = "anotherTargetNamespace";
        webServiceDescriptor.setTargetNamespace(anotherTargetNamespace);
        assertEquals(anotherTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testSetTargetNamespaceWithNull()
    {
        webServiceDescriptor.setTargetNamespace(null);
    }

    @Test
    public void testSetAndGetTypesNamespace()
    {
        String anotherTypesNamespace = "anotherTypesNamespace";
        webServiceDescriptor.setTypesNamespace(anotherTypesNamespace);
        assertEquals(anotherTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testSetTypesNamespaceWithNull()
    {
        webServiceDescriptor.setTypesNamespace(null);
    }

    @Test
    public void testEquals()
    {
        WebServiceDescriptor anotherWebServiceDescriptor = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        assertSame(webServiceDescriptor, anotherWebServiceDescriptor);
    }

    @Test
    public void testHashCode()
    {
        WebServiceDescriptor anotherWebServiceDescriptor = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        assertEquals(webServiceDescriptor.hashCode(), anotherWebServiceDescriptor.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentServiceClazz()
    {
        WebServiceDescriptor anotherWebServiceDescriptor = new WebServiceDescriptor(AnotherService.class, "targetNamespace", "typesNamespace");
        assertNotEquals(webServiceDescriptor.hashCode(), anotherWebServiceDescriptor.hashCode());
    }

    @Test
    public void testHashCodeWithNullServiceClazz()
    {
        WebServiceDescriptor anotherWebServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        assertNotEquals(webServiceDescriptor.hashCode(), anotherWebServiceDescriptor.hashCode());
    }

    @Test
    public void testEqualsWithDifferentServiceClazz()
    {
        assertNotSame(webServiceDescriptor, new WebServiceDescriptor(AnotherService.class, "targetNamespace", "typesNamespace"));
    }

    @Test
    public void testEqualsWithNullObject()
    {
        assertNotSame(webServiceDescriptor, null);
    }

    @Test
    public void testEqualsWithDifferentClass()
    {
        assertNotSame(webServiceDescriptor, new Object());
    }

    private static class TestService {}

    private static class AnotherService {}
}