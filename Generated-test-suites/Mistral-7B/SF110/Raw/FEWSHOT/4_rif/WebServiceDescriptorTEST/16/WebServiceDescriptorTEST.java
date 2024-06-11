package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/services";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorSettersAndGetters() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/services";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, null, null);

        descriptor.setTargetNamespace(targetNamespace);
        descriptor.setTypesNamespace(typesNamespace);

        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());

        descriptor.setServiceClazz(MyOtherService.class);

        assertEquals(MyOtherService.class, descriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptorHashCode() {
        Class serviceClazz1 = MyService.class;
        Class serviceClazz2 = MyOtherService.class;

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, null, null);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, null, null);

        int hashcode1 = descriptor1.hashCode();
        assertEquals(hashcode1, descriptor1.hashCode());
        assertEquals(hashcode1, descriptor3.hashCode());
        assertNotSame(hashcode1, descriptor2.hashCode());

        descriptor1.setServiceClazz(MyOtherService.class);
        assertNotSame(hashcode1, descriptor1.hashCode());

        descriptor2.setServiceClazz(MyService.class);
        assertEquals(hashcode1, descriptor2.hashCode());
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class serviceClazz1 = MyService.class;
        Class serviceClazz2 = MyOtherService.class;

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, null, null);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, null, null);

        assertFalse(descriptor1.equals(descriptor2));
        assertTrue(descriptor1.equals(descriptor3));

        descriptor1.setServiceClazz(MyOtherService.class);
        assertFalse(descriptor1.equals(descriptor3));

        descriptor2.setServiceClazz(MyService.class);
        assertTrue(descriptor1.equals(descriptor2));
    }

    private static class MyService {}
    private static class MyOtherService {}
}