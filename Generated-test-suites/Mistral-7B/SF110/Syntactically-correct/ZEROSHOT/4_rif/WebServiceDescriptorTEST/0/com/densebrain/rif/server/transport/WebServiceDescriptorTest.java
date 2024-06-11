package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstruction() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
        Assert.assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testGettersAndSetters() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        descriptor.setTargetNamespace("newTargetNamespace");
        descriptor.setTypesNamespace("newTypesNamespace");

        Assert.assertEquals("newTargetNamespace", descriptor.getTargetNamespace());
        Assert.assertEquals("newTypesNamespace", descriptor.getTypesNamespace());

        descriptor.setServiceClazz(Mockito.mock(Class.class));

        Assert.assertNotEquals(serviceClazz, descriptor.getServiceClazz());
        Assert.assertEquals(Mockito.mock(Class.class), descriptor.getServiceClazz());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace2", "typesNamespace2");

        Assert.assertNotEquals(descriptor1.hashCode(), descriptor2.hashCode());

        descriptor1.setServiceClazz(serviceClazz2);

        Assert.assertEquals(descriptor2.hashCode(), descriptor1.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, "targetNamespace2", "typesNamespace2");

        Assert.assertNotEquals(descriptor1, descriptor2);
        Assert.assertNotEquals(descriptor1, descriptor3);

        descriptor1.setServiceClazz(serviceClazz2);

        Assert.assertEquals(descriptor2, descriptor1);
    }
}