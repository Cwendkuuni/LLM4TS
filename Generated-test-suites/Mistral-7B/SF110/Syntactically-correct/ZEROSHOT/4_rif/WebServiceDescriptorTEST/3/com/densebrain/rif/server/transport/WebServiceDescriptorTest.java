package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://test.com/namespace";
        String typesNamespace = "http://test.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
        Assert.assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetAndGetTargetNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://test.com/namespace";
        String typesNamespace = "http://test.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, null, null);

        descriptor.setTargetNamespace(targetNamespace);
        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetAndGetTypesNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://test.com/namespace";
        String typesNamespace = "http://test.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, null, null);

        descriptor.setTypesNamespace(typesNamespace);
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetAndGetServiceClazz() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz1, null, null);

        descriptor.setServiceClazz(serviceClazz2);
        Assert.assertEquals(serviceClazz2, descriptor.getServiceClazz());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, null, null);

        Assert.assertNotEquals(descriptor.hashCode(), descriptor2.hashCode());

        descriptor.setServiceClazz(serviceClazz2);
        descriptor2.setServiceClazz(serviceClazz);

        Assert.assertEquals(descriptor.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, null, null);

        Assert.assertNotEquals(descriptor, descriptor2);

        descriptor.setServiceClazz(serviceClazz2);
        descriptor2.setServiceClazz(serviceClazz);

        Assert.assertEquals(descriptor, descriptor2);
    }
}