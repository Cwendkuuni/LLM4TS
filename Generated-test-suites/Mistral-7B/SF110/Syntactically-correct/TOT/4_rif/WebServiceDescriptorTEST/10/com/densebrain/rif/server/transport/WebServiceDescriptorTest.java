package com.densebrain.rif.server.transport;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class originalServiceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(originalServiceClazz, "targetNamespace", "typesNamespace");
        Class newServiceClazz = Mockito.mock(Class.class);
        descriptor.setServiceClazz(newServiceClazz);
        assertEquals(newServiceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = WebServiceDescriptor.class;
        Class serviceClazz2 = Object.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace2", "typesNamespace2");
        assertFalse(descriptor1.equals(descriptor2));

        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        assertTrue(descriptor1.equals(descriptor3));

        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(serviceClazz1, null, "typesNamespace1");
        assertFalse(descriptor1.equals(descriptor4));
    }
}