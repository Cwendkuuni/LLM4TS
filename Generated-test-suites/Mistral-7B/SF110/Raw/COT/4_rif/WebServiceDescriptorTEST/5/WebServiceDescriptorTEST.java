package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert.*;

public class WebServiceDescriptorTest {

    // Basic tests for constructors and getters/setters
    @Test
    public void testWebServiceDescriptorConstruction() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Assert.assertEquals("targetNamespace", descriptor.getTargetNamespace());
        Assert.assertEquals("typesNamespace", descriptor.getTypesNamespace());
        Assert.assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSettersAndGetters() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        String newTargetNamespace = "newTargetNamespace";
        String newTypesNamespace = "newTypesNamespace";
        descriptor.setTargetNamespace(newTargetNamespace);
        descriptor.setTypesNamespace(newTypesNamespace);
        Assert.assertEquals(newTargetNamespace, descriptor.getTargetNamespace());
        Assert.assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
        descriptor.setServiceClazz(null);
        Assert.assertNull(descriptor.getServiceClazz());
    }

    // Tests for hashCode and equals methods
    @Test
    public void testEqualsAndHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace2", "typesNamespace2");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        Assert.assertEquals(descriptor1, descriptor3);
        Assert.assertNotEquals(descriptor1, descriptor2);
        descriptor3.setServiceClazz(serviceClazz2);
        Assert.assertEquals(descriptor1, descriptor3);
    }

    // Edge cases testing

    @Test(expected = NullPointerException.class)
    public void testNullServiceClazz() {
        new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
    }

    @Test(expected = NullPointerException.class)
    public void testNullTargetNamespace() {
        new WebServiceDescriptor(Mockito.mock(Class.class), null, "typesNamespace");
    }

    @Test(expected = NullPointerException.class)
    public void testNullTypesNamespace() {
        new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTargetNamespace() {
        new WebServiceDescriptor(Mockito.mock(Class.class), "", "typesNamespace");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyTypesNamespace() {
        new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "");
    }

    @Test
    public void testHashCodeConsistency() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace2");
        descriptor2.setServiceClazz(descriptor1.getServiceClazz());
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();
        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeDifferentObjects() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace2", "typesNamespace2");
        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();
        Assert.assertNotEquals(hashCode1, hashCode2);
    }
}