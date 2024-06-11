package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstruction() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/service";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSettersAndGetters() {
        Class serviceClazz = MyService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);

        String targetNamespace = "http://example.com/service";
        String typesNamespace = "http://example.com/types";

        webServiceDescriptor.setTargetNamespace(targetNamespace);
        webServiceDescriptor.setTypesNamespace(typesNamespace);

        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz1 = MyService.class;
        Class serviceClazz2 = MyOtherService.class;

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, null, null);

        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();

        assertNotSame(hashCode1, hashCode2);

        webServiceDescriptor1.setServiceClazz(MyService.class);
        webServiceDescriptor2.setServiceClazz(MyService.class);

        int hashCode1_1 = webServiceDescriptor1.hashCode();
        int hashCode2_1 = webServiceDescriptor2.hashCode();

        assertEquals(hashCode1_1, hashCode2_1);
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = MyService.class;
        Class serviceClazz2 = MyOtherService.class;

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, null, null);
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, null, null);

        assertFalse(webServiceDescriptor1.equals(null));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));

        webServiceDescriptor1.setServiceClazz(MyServiceWithDifferentHashCode.class);
        webServiceDescriptor3.setServiceClazz(MyServiceWithDifferentHashCode.class);

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
    }

    private static class MyService {
        // empty class
    }

    private static class MyOtherService {
        // empty class
    }

    private static class MyServiceWithDifferentHashCode {
        // empty class with different hashcode
    }
}