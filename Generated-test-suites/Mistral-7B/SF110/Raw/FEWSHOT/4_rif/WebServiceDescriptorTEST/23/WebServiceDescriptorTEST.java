package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorInstance() {
        Class serviceClazz = Object.class;
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertNotNull(descriptor);
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
        assertNotNull(descriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptorHashCode() {
        Class serviceClazz1 = Object.class;
        Class serviceClazz2 = String.class;
        String targetNamespace1 = "http://example.com/namespace1";
        String targetNamespace2 = "http://example.com/namespace2";
        String typesNamespace1 = "http://example.com/types1";
        String typesNamespace2 = "http://example.com/types2";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);

        assertEquals(descriptor1.hashCode(), descriptor1.hashCode());
        assertEquals(descriptor1.hashCode(), new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1).hashCode());
        assertNotEquals(descriptor1.hashCode(), descriptor2.hashCode());
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class serviceClazz1 = Object.class;
        Class serviceClazz2 = String.class;
        String targetNamespace1 = "http://example.com/namespace1";
        String targetNamespace2 = "http://example.com/namespace2";
        String typesNamespace1 = "http://example.com/types1";
        String typesNamespace2 = "http://example.com/types2";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace2, typesNamespace2);

        assertEquals(descriptor1, descriptor1);
        assertEquals(descriptor1, new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1));
        assertNotEquals(descriptor1, descriptor2);
        assertNotEquals(descriptor1, descriptor3);
        assertNull(descriptor1.equals(null));
    }

    @Test
    public void testWebServiceDescriptorSetters() {
        Class serviceClazz = Object.class;
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        descriptor.setServiceClazz(String.class);
        assertEquals(String.class, descriptor.getServiceClazz());

        descriptor.setTargetNamespace("http://example.com/namespace2");
        assertEquals("http://example.com/namespace2", descriptor.getTargetNamespace());

        descriptor.setTypesNamespace("http://example.com/types2");
        assertEquals("http://example.com/types2", descriptor.getTypesNamespace());
    }
}