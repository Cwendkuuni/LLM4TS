package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = TestService.class;
        String targetNamespace = "http://example.com/service";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testSetGetServiceClazz() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);

        descriptor.setServiceClazz(serviceClazz);
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testSetGetTargetNamespace() {
        String targetNamespace = "http://example.com/service";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);

        descriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
    }

    @Test
    public void testSetGetTypesNamespace() {
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);

        descriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz1 = TestService.class;
        Class serviceClazz2 = TestService.class;

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "http://example.com/service", "http://example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "http://example.com/service", "http://example.com/types");

        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();

        assertEquals(hashCode1, hashCode2);

        descriptor1.setServiceClazz(null);
        int hashCode3 = descriptor1.hashCode();
        assertNotSame(hashCode1, hashCode3);

        descriptor2.setServiceClazz(TestService2.class);
        int hashCode4 = descriptor2.hashCode();
        assertNotSame(hashCode1, hashCode4);
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = TestService.class;
        Class serviceClazz2 = TestService.class;

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, "http://example.com/service", "http://example.com/types");
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, "http://example.com/service", "http://example.com/types");
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, "http://example.com/service", "http://example.com/types");
        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(serviceClazz1, "http://example.com/service", "http://example.com/types2");
        WebServiceDescriptor descriptor5 = new WebServiceDescriptor(null, "http://example.com/service", "http://example.com/types");

        assertTrue(descriptor1.equals(descriptor1));
        assertTrue(descriptor1.equals(descriptor3));
        assertFalse(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(descriptor4));
        assertFalse(descriptor1.equals(descriptor5));
    }

    static class TestService {
        // empty class
    }

    static class TestService2 {
        // empty class
    }
}