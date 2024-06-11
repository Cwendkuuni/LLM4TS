package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstruction() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
        Assert.assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptorGettersAndSetters() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        descriptor.setTargetNamespace("http://new.com/target");
        Assert.assertEquals("http://new.com/target", descriptor.getTargetNamespace());

        descriptor.setTypesNamespace("http://new.com/types");
        Assert.assertEquals("http://new.com/types", descriptor.getTypesNamespace());

        descriptor.setServiceClazz(Mockito.mock(Class.class));
        Assert.assertNotSame(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptorHashCode() {
        Class serviceClazz = Mockito.mock(Class.class);
        Class otherServiceClazz = Mockito.mock(Class.class);

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "namespace1", "namespace2");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(otherServiceClazz, "namespace1", "namespace2");

        Assert.assertNotSame(descriptor1.hashCode(), descriptor2.hashCode());

        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz, "namespace1", "namespace2");
        Assert.assertEquals(descriptor1.hashCode(), descriptor3.hashCode());

        descriptor3.setServiceClazz(otherServiceClazz);
        Assert.assertNotEqual(descriptor1.hashCode(), descriptor3.hashCode());
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class serviceClazz = Mockito.mock(Class.class);
        Class otherServiceClazz = Mockito.mock(Class.class);

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, "namespace1", "namespace2");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, "namespace1", "namespace2");

        Assert.assertTrue(descriptor1.equals(descriptor2));

        descriptor2.setServiceClazz(otherServiceClazz);
        Assert.assertFalse(descriptor1.equals(descriptor2));

        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(otherServiceClazz, "namespace1", "namespace2");
        Assert.assertFalse(descriptor1.equals(descriptor3));
    }
}