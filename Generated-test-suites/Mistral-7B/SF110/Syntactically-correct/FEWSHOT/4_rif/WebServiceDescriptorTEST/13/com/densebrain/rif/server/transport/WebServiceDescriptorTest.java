package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorWithValidArguments() {
        Class serviceClazz = SomeService.class;
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorWithNullServiceClazz() {
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, targetNamespace, typesNamespace);

        assertNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptorWithNullTargetNamespace() {
        Class serviceClazz = SomeService.class;
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, typesNamespace);

        assertNull(webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testWebServiceDescriptorWithNullTypesNamespace() {
        Class serviceClazz = SomeService.class;
        String targetNamespace = "targetNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, null);

        assertNull(webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz1 = SomeService1.class;
        Class serviceClazz2 = SomeService2.class;
        String targetNamespace1 = "targetNamespace1";
        String targetNamespace2 = "targetNamespace2";
        String typesNamespace1 = "typesNamespace1";
        String typesNamespace2 = "typesNamespace2";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);

        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();

        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = SomeService1.class;
        Class serviceClazz2 = SomeService2.class;
        String targetNamespace1 = "targetNamespace1";
        String targetNamespace2 = "targetNamespace2";
        String typesNamespace1 = "typesNamespace1";
        String typesNamespace2 = "typesNamespace2";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
}