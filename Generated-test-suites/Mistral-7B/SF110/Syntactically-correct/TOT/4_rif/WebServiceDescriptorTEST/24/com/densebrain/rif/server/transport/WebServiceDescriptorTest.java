package com.densebrain.rif.server.transport;

import org.junit.*;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "http://example.com/targetNamespace", "http://example.com/typesNamespace");
        Class returnedServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(serviceClazz, returnedServiceClazz);
    }

    @Test
    public void testSetAndGetServiceClazz() {
        Class originalServiceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(originalServiceClazz, "http://example.com/targetNamespace", "http://example.com/typesNamespace");
        Class newServiceClazz = AnotherClass.class;
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        Class returnedServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(newServiceClazz, returnedServiceClazz);
    }

    @Test
    public void testEquals() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "http://example.com/targetNamespace1", "http://example.com/typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "http://example.com/targetNamespace2", "http://example.com/typesNamespace2");
        WebServiceDescriptor webServiceDescriptor3 = webServiceDescriptor1;

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));
        assertFalse(webServiceDescriptor1.equals(null));
        assertFalse(webServiceDescriptor1.equals(new Object()));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
}

class AnotherClass {
    // A dummy class for testing
}