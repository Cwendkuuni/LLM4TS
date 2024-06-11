package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptor() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        Assert.assertEquals(serviceClazz, descriptor.getServiceClazz());
        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, "targetNamespace2", "typesNamespace2");
        Assert.assertNotEquals(descriptor1.hashCode(), descriptor2.hashCode());
        Assert.assertNotEquals(descriptor1.hashCode(), descriptor3.hashCode());
        Assert.assertEquals(descriptor2.hashCode(), descriptor2.hashCode());
        Assert.assertEquals(descriptor3.hashCode(), descriptor3.hashCode());
        Assert.assertEquals(descriptor1.hashCode(), descriptor1.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz2, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(serviceClazz1, "targetNamespace2", "typesNamespace2");
        Assert.assertTrue(descriptor1.equals(descriptor1));
        Assert.assertTrue(descriptor1.equals(descriptor2));
        Assert.assertFalse(descriptor1.equals(descriptor3));
        Assert.assertFalse(descriptor1.equals(descriptor4));
        Assert.assertFalse(descriptor1.equals(null));
        Assert.assertFalse(descriptor1.equals("descriptorString"));
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructor() {
        new WebServiceDescriptor(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullServiceClazz() {
        new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
    }

    @Test(expected = NullPointerException.class)
    public void testNullTargetNamespace() {
        new WebServiceDescriptor(Class.class, null, "typesNamespace");
    }

    @Test(expected = NullPointerException.class)
    public void testNullTypesNamespace() {
        new WebServiceDescriptor(Class.class, "targetNamespace", null);
    }
}