package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * WebServiceDescriptorTest class containing JUnit 4 test cases for the WebServiceDescriptor class.
 */
public class WebServiceDescriptorTest
{
    /**
     * Test constructor method.
     */
    @Test
    public void testConstructor()
    {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/myService";
        String typesNamespace = "http://example.com/myServiceTypes";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertNotNull(descriptor);
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    /**
     * Test getter and setter methods for serviceClazz.
     */
    @Test
    public void testGetSetServiceClazz()
    {
        Class originalServiceClazz = MyService.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(originalServiceClazz, null, null);
        assertNotNull(descriptor.getServiceClazz());
        assertEquals(originalServiceClazz, descriptor.getServiceClazz());

        Class newServiceClazz = AnotherService.class;
        descriptor.setServiceClazz(newServiceClazz);
        assertEquals(newServiceClazz, descriptor.getServiceClazz());
    }

    /**
     * Test getter and setter methods for targetNamespace.
     */
    @Test
    public void testGetSetTargetNamespace()
    {
        String originalTargetNamespace = "http://example.com/myService";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, originalTargetNamespace, null);
        assertNotNull(descriptor.getTargetNamespace());
        assertEquals(originalTargetNamespace, descriptor.getTargetNamespace());

        String newTargetNamespace = "http://example.com/anotherService";
        descriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, descriptor.getTargetNamespace());
    }

    /**
     * Test getter and setter methods for typesNamespace.
     */
    @Test
    public void testGetSetTypesNamespace()
    {
        String originalTypesNamespace = "http://example.com/myServiceTypes";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, originalTypesNamespace);
        assertNotNull(descriptor.getTypesNamespace());
        assertEquals(originalTypesNamespace, descriptor.getTypesNamespace());

        String newTypesNamespace = "http://example.com/anotherServiceTypes";
        descriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
    }

    /**
     * Test equals method.
     */
    @Test
    public void testEquals()
    {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/myService";
        String typesNamespace = "http://example.com/myServiceTypes";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(AnotherService.class, targetNamespace, typesNamespace);

        assertEquals(descriptor1, descriptor2);
        assertNotEquals(descriptor1, descriptor3);

        descriptor1.setServiceClazz(null);
        assertNotEquals(descriptor1, descriptor2);

        descriptor2.setServiceClazz(null);
        assertEquals(descriptor1, descriptor2);

        descriptor1 = null;
        assertNull(descriptor1);
        assertNull(descriptor2);
    }

    /**
     * Test hashCode method.
     */
    @Test
    public void testHashCode()
    {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/myService";
        String typesNamespace = "http://example.com/myServiceTypes";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(AnotherService.class, targetNamespace, typesNamespace);

        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();
        int hashCode3 = descriptor3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);

        descriptor1.setServiceClazz(null);
        int hashCode4 = descriptor1.hashCode();
        assertEquals(hashCode1, hashCode4);

        descriptor1.setServiceClazz(serviceClazz);
        descriptor2.setServiceClazz(AnotherService.class);
        assertNotEquals(hashCode1, descriptor2.hashCode());
    }

    /**
     * Test null serviceClazz constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullServiceClazzConstructor()
    {
        new WebServiceDescriptor(null, null, null);
    }

    /**
     * Test null targetNamespace constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullTargetNamespaceConstructor()
    {
        Class serviceClazz = MyService.class;
        new WebServiceDescriptor(serviceClazz, null, null);
    }

    /**
     * Test null typesNamespace constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullTypesNamespaceConstructor()
    {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/myService";
        new WebServiceDescriptor(serviceClazz, targetNamespace, null);
    }
}