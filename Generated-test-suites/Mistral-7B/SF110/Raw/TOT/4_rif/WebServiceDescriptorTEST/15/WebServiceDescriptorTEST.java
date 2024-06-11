package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Class actualServiceClazz = descriptor.getServiceClazz();
        assertEquals(serviceClazz, actualServiceClazz);
    }

    @Test
    public void testSetAndGetServiceClazz() {
        Class originalServiceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        Class newServiceClazz = SomeOtherClass.class;
        descriptor.setServiceClazz(newServiceClazz);
        Class actualServiceClazz = descriptor.getServiceClazz();
        Assert.assertNotSame(originalServiceClazz, actualServiceClazz);
        Assert.assertEquals(newServiceClazz, actualServiceClazz);
    }

    @Test
    public void testGetAndSetTargetNamespace() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        String originalTargetNamespace = descriptor.getTargetNamespace();
        String newTargetNamespace = "new_target_namespace";
        descriptor.setTargetNamespace(newTargetNamespace);
        String actualTargetNamespace = descriptor.getTargetNamespace();
        assertEquals(newTargetNamespace, actualTargetNamespace);
        assertEquals(originalTargetNamespace, descriptor.getTargetNamespace()); // check if old value is replaced
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(WebServiceDescriptor.class, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(WebServiceDescriptor.class, "targetNamespace1", "typesNamespace1");
        assertEquals(descriptor1.hashCode(), descriptor2.hashCode());

        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(WebServiceDescriptor.class, "targetNamespace2", "typesNamespace1");
        assertNotEquals(descriptor1.hashCode(), descriptor3.hashCode());

        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(Object.class, "targetNamespace1", "typesNamespace1");
        assertNotEquals(descriptor1.hashCode(), descriptor4.hashCode());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(WebServiceDescriptor.class, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(WebServiceDescriptor.class, "targetNamespace1", "typesNamespace1");
        assertTrue(descriptor1.equals(descriptor2));

        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(WebServiceDescriptor.class, "targetNamespace2", "typesNamespace1");
        assertFalse(descriptor1.equals(descriptor3));

        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(Object.class, "targetNamespace1", "typesNamespace1");
        assertFalse(descriptor1.equals(descriptor4));

        // test self equality
        assertTrue(descriptor1.equals(descriptor1));

        // test null object
        assertFalse(descriptor1.equals(null));
    }
}