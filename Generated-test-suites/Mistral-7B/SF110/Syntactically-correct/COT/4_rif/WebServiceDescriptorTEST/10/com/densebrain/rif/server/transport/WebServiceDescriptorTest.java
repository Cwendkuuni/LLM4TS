package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorConstructorNullServiceClazz() {
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        new WebServiceDescriptor(null, targetNamespace, typesNamespace);
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorConstructorNullTargetNamespace() {
        Class serviceClazz = MyService.class;
        String typesNamespace = "http://example.com/types";
        new WebServiceDescriptor(serviceClazz, null, typesNamespace);
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorConstructorNullTypesNamespace() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/target";
        new WebServiceDescriptor(serviceClazz, targetNamespace, null);
    }

    @Test
    public void testWebServiceDescriptorGettersAndSetters() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        descriptor.setServiceClazz(MyOtherService.class);
        assertEquals(MyOtherService.class, descriptor.getServiceClazz());

        descriptor.setTargetNamespace("http://example.com/new-target");
        assertEquals("http://example.com/new-target", descriptor.getTargetNamespace());

        descriptor.setTypesNamespace("http://example.com/new-types");
        assertEquals("http://example.com/new-types", descriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorHashCode() {
        Class serviceClazz1 = MyService.class;
        Class serviceClazz2 = MyOtherService.class;
        String targetNamespace1 = "http://example.com/target1";
        String targetNamespace2 = "http://example.com/target2";
        String typesNamespace1 = "http://example.com/types1";
        String typesNamespace2 = "http://example.com/types2";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace2);
        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(serviceClazz2, targetNamespace1, typesNamespace1);

        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();
        int hashCode3 = descriptor3.hashCode();
        int hashCode4 = descriptor4.hashCode();

        assertEquals(hashCode1, descriptor1.hashCode());
        assertNotSame(hashCode1, hashCode2);
        assertNotSame(hashCode1, hashCode3);
        assertNotSame(hashCode1, hashCode4);
        assertNotSame(hashCode2, hashCode3);
        assertNotSame(hashCode2, hashCode4);
        assertNotSame(hashCode3, hashCode4);
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class serviceClazz1 = MyService.class;
        Class serviceClazz2 = MyOtherService.class;
        String targetNamespace1 = "http://example.com/target1";
        String targetNamespace2 = "http://example.com/target2";
        String typesNamespace1 = "http://example.com/types1";
        String typesNamespace2 = "http://example.com/types2";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor4 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace2);

        assertTrue(descriptor1.equals(descriptor1));
        assertTrue(descriptor1.equals(descriptor3));
        assertFalse(descriptor1.equals(descriptor2));
        assertFalse(descriptor1.equals(descriptor4));
    }
}