package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        assertEquals("targetNamespace1", descriptor.getTargetNamespace());
        assertEquals("typesNamespace1", descriptor.getTypesNamespace());
        assertSame(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSettersAndGetters() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");

        String newTargetNamespace = "targetNamespace2";
        descriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, descriptor.getTargetNamespace());

        String newTypesNamespace = "typesNamespace2";
        descriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());

        Class newServiceClazz = Mockito.mock(Class.class);
        descriptor.setServiceClazz(newServiceClazz);
        assertSame(newServiceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, "targetNamespace2", "typesNamespace2");

        assertEquals(descriptor1.hashCode(), descriptor1.hashCode());
        assertNotSame(descriptor1.hashCode(), descriptor2.hashCode());
        assertNotSame(descriptor1.hashCode(), descriptor3.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, "targetNamespace2", "typesNamespace2");

        assertTrue(descriptor1.equals(descriptor1));
        assertFalse(descriptor1.equals(null));
        assertFalse(descriptor1.equals(new Object()));
        assertFalse(descriptor1.equals(descriptor2));
        assertTrue(descriptor1.equals(descriptor2)); // assuming Mockito returns same mocks
        assertFalse(descriptor1.equals(descriptor3));
    }
}