package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    @Test
    public void testConstructor() {
        Class expectedServiceClazz = SomeService.class;
        String expectedTargetNamespace = "targetNamespace";
        String expectedTypesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(expectedServiceClazz, expectedTargetNamespace, expectedTypesNamespace);
        Class actualServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(expectedServiceClazz, actualServiceClazz);
        assertEquals(expectedTargetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(expectedTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSettersAndGetters() {
        Class originalServiceClazz = SomeService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(originalServiceClazz, "targetNamespace", "typesNamespace");
        String newTargetNamespace = "newTargetNamespace";
        String newTypesNamespace = "newTypesNamespace";
        Class newServiceClazz = AnotherService.class;

        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        webServiceDescriptor.setServiceClazz(newServiceClazz);

        Class actualServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(newServiceClazz, actualServiceClazz);
        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testGetServiceClazz() {
        Class expectedServiceClazz = SomeService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(expectedServiceClazz, "targetNamespace", "typesNamespace");
        Class actualServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(expectedServiceClazz, actualServiceClazz);
    }

    @Test
    public void testSetServiceClazz() {
        Class originalServiceClazz = SomeService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(originalServiceClazz, "targetNamespace", "typesNamespace");
        Class newServiceClazz = AnotherService.class;
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        Class actualServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(newServiceClazz, actualServiceClazz);
    }

    @Test
    public void testHashCodeAndEquals() {
        Class serviceClazz1 = SomeService.class;
        Class serviceClazz2 = AnotherService.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace2", "typesNamespace2");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");

        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();
        int hashCode3 = webServiceDescriptor3.hashCode();

        assertEquals(hashCode1, hashCode3); //same object should have the same hashcode
        assertNotSame(hashCode1, hashCode2); //different objects should have different hashcodes

        assertEquals(webServiceDescriptor1, webServiceDescriptor3); //same object should be equal
        assertNotEquals(webServiceDescriptor1, webServiceDescriptor2); //different objects should not be equal
    }
}