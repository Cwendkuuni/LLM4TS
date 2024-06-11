package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorInstance() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
        assertSame(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptorSettersAndGetters() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, null, null);
        descriptor.setTargetNamespace(targetNamespace);
        descriptor.setTypesNamespace(typesNamespace);
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, null);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace, null);
        int hashCode1 = descriptor1.hashCode();
        assertEquals(hashCode1, descriptor3.hashCode());
        assertNotEquals(hashCode1, descriptor2.hashCode());
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, null);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace, null);
        assertTrue(descriptor1.equals(descriptor3));
        assertFalse(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(null));
        assertFalse(descriptor1.equals(""));
    }
}