package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "http://www.example.com/ns/target", "http://www.example.com/ns/types");
        assertEquals("http://www.example.com/ns/target", descriptor.getTargetNamespace());
        assertEquals("http://www.example.com/ns/types", descriptor.getTypesNamespace());
        assertEquals(TestService.class, descriptor.getServiceClazz());
    }

    @Test
    public void testGettersAndSetters() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "http://www.example.com/ns/target", "http://www.example.com/ns/types");

        descriptor.setServiceClazz(AnotherTestService.class);
        assertEquals(AnotherTestService.class, descriptor.getServiceClazz());

        descriptor.setTargetNamespace("http://www.example2.com/ns/target");
        assertEquals("http://www.example2.com/ns/target", descriptor.getTargetNamespace());

        descriptor.setTypesNamespace("http://www.example2.com/ns/types");
        assertEquals("http://www.example2.com/ns/types", descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz1 = TestService.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "http://www.example.com/ns/target", "http://www.example.com/ns/types");

        Class serviceClazz2 = TestService.class;
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "http://www.example.com/ns/target", "http://www.example.com/ns/types");

        Class serviceClazz3 = AnotherTestService.class;
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz3, "http://www.example.com/ns/target", "http://www.example.com/ns/types");

        Class serviceClazz4 = TestService.class;
        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(serviceClazz4, "http://www.example2.com/ns/target", "http://www.example2.com/ns/types");

        assertEquals(descriptor1.hashCode(), descriptor1.hashCode());
        assertNotEquals(descriptor1.hashCode(), descriptor2.hashCode());
        assertNotEquals(descriptor1.hashCode(), descriptor3.hashCode());
        assertNotEquals(descriptor1.hashCode(), descriptor4.hashCode());
        assertNotEquals(descriptor2.hashCode(), descriptor3.hashCode());
        assertNotEquals(descriptor2.hashCode(), descriptor4.hashCode());
        assertNotEquals(descriptor3.hashCode(), descriptor4.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = TestService.class;
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "http://www.example.com/ns/target", "http://www.example.com/ns/types");

        Class serviceClazz2 = TestService.class;
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "http://www.example.com/ns/target", "http://www.example.com/ns/types");

        Class serviceClazz3 = AnotherTestService.class;
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz3, "http://www.example.com/ns/target", "http://www.example.com/ns/types");

        Class serviceClazz4 = TestService.class;
        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(serviceClazz4, "http://www.example2.com/ns/target", "http://www.example2.com/ns/types");

        assertTrue(descriptor1.equals(descriptor1));
        assertTrue(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(descriptor3));
        assertFalse(descriptor1.equals(descriptor4));
        assertFalse(descriptor2.equals(descriptor3));
        assertFalse(descriptor2.equals(descriptor4));
        assertFalse(descriptor3.equals(descriptor4));
    }

    @Test(expected = NullPointerException.class)
    public void testEqualsWithNull() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, "http://www.example.com/ns/target", "http://www.example.com/ns/types");
        assertNull(descriptor.getServiceClazz());
        assertFalse(descriptor.equals(null));
    }

    @Test(expected = ClassCastException.class)
    public void testEqualsWithDifferentClass() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(TestService.class, "http://www.example.com/ns/target", "http://www.example.com/ns/types");
        Object obj = new Object();
        assertFalse(descriptor.equals(obj));
    }

    private static class TestService {}
    private static class AnotherTestService {}
}