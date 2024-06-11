package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/namespace1";
        String typesNamespace = "http://example.com/namespace2";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertEquals("Target namespace should be set", targetNamespace, descriptor.getTargetNamespace());
        assertEquals("Types namespace should be set", typesNamespace, descriptor.getTypesNamespace());
        assertEquals("Service class should be set", serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(MyService.class, "http://example.com/namespace1", "http://example.com/namespace2");
        Class serviceClazz = descriptor.getServiceClazz();
        assertEquals("MyService.class should be returned", MyService.class, serviceClazz);
    }

    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(OldService.class, "http://example.com/namespace1", "http://example.com/namespace2");
        descriptor.setServiceClazz(MyService.class);
        Class serviceClazz = descriptor.getServiceClazz();
        assertEquals("MyService.class should be returned after setting", MyService.class, serviceClazz);
    }
}