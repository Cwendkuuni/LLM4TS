package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceClassWithTargetAndTypesNamespaces.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Class returnedServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(serviceClazz, returnedServiceClazz);
    }

    @Test
    public void testSetAndGetServiceClazz() {
        Class originalServiceClazz = WebServiceClassWithTargetAndTypesNamespaces.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        webServiceDescriptor.setServiceClazz(originalServiceClazz);
        Class returnedServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(originalServiceClazz, returnedServiceClazz);
    }

    @Test
    public void testEquals() {
        Class serviceClazz = WebServiceClassWithTargetAndTypesNamespaces.class;
        WebServiceDescriptor firstWebServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor secondWebServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace2");
        assertFalse(firstWebServiceDescriptor.equals(secondWebServiceDescriptor));

        WebServiceDescriptor thirdWebServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        assertTrue(firstWebServiceDescriptor.equals(thirdWebServiceDescriptor));
    }

    static class WebServiceClassWithTargetAndTypesNamespaces {
        // A placeholder class for testing purposes
    }
}