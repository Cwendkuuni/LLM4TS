package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstruction() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(MyService.class, "targetNamespace", "typesNamespace");
        assertEquals("targetNamespace", descriptor.getTargetNamespace());
        assertEquals("typesNamespace", descriptor.getTypesNamespace());
        assertEquals(MyService.class, descriptor.getServiceClazz());
    }

    @Test
    public void testGettersAndSetters() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(MyService.class, "targetNamespace", "typesNamespace");
        descriptor.setTargetNamespace("newTargetNamespace");
        descriptor.setTypesNamespace("newTypesNamespace");
        descriptor.setServiceClazz(AnotherService.class);
        assertEquals("newTargetNamespace", descriptor.getTargetNamespace());
        assertEquals("newTypesNamespace", descriptor.getTypesNamespace());
        assertEquals(AnotherService.class, descriptor.getServiceClazz());
        assertNotSame(MyService.class, descriptor.getServiceClazz());
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(MyService.class, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(MyService.class, "targetNamespace2", "typesNamespace2");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(AnotherService.class, "targetNamespace3", "typesNamespace3");

        int hashCode1 = descriptor1.hashCode();
        descriptor1.setServiceClazz(AnotherService.class);
        int hashCode2 = descriptor1.hashCode();

        int expectedHashCode1 = 31 * (31 * MyService.class.hashCode() + "targetNamespace1".hashCode()) + "typesNamespace1".hashCode();
        int expectedHashCode2 = 31 * (31 * AnotherService.class.hashCode() + "targetNamespace1".hashCode()) + "typesNamespace1".hashCode();
        int expectedHashCode3 = 31 * (31 * MyService.class.hashCode() + "targetNamespace2".hashCode()) + "typesNamespace2".hashCode();
        int expectedHashCode4 = 31 * (31 * AnotherService.class.hashCode() + "targetNamespace3".hashCode()) + "typesNamespace3".hashCode();

        assertEquals(expectedHashCode1, hashCode1);
        assertEquals(expectedHashCode2, hashCode2);
        assertNotEquals(expectedHashCode1, hashCode3);
        assertNotEquals(expectedHashCode1, hashCode4);
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(MyService.class, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(MyService.class, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(AnotherService.class, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(MyService.class, "targetNamespace2", "typesNamespace2");

        assertTrue(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(descriptor3));
        assertFalse(descriptor1.equals(descriptor4));
        assertFalse(descriptor1.equals(null));
        assertFalse(descriptor1.equals("String"));
    }

    private static class MyService {}
    private static class AnotherService {}
}