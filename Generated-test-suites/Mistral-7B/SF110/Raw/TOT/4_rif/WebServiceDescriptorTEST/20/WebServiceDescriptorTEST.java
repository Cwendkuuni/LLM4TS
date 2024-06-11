
package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert.*;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = SomeService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Class returnedServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(serviceClazz, returnedServiceClazz);
    }

    @Test
    public void testSetAndGetServiceClazz() {
        Class originalServiceClazz = SomeService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        webServiceDescriptor.setServiceClazz(originalServiceClazz);
        Class returnedServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(originalServiceClazz, returnedServiceClazz);
    }

    @Test
    public void testEquals() {
        Class serviceClazz = SomeService.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace2");

        // Test equals with same object
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));

        // Test equals with null
        assertFalse(webServiceDescriptor1.equals(null));

        // Test equals with different class
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(OtherService.class, "targetNamespace1", "typesNamespace1");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));

        // Test equals with same values
        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor4));
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = SomeService.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace2");

        // Test hashcode with same object
        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor1.hashCode();
        assertEquals(hashCode1, hashCode2);

        // Test hashcode with different objects
        int hashCode3 = webServiceDescriptor1.hashCode();
        int hashCode4 = webServiceDescriptor2.hashCode();
        assertNotEquals(hashCode3, hashCode4);
    }
}