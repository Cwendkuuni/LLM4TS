package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorWithValidArguments() {
        Class<SampleWebService> serviceClazz = SampleWebService.class;
        String targetNamespace = "http://densebrain.com/rif/sample";
        String typesNamespace = "http://densebrain.com/rif/sample/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertNotNull(webServiceDescriptor);
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorWithNullArguments() {
        Class<?> nullServiceClazz = null;
        String nullTargetNamespace = null;
        String nullTypesNamespace = null;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(nullServiceClazz, nullTargetNamespace, nullTypesNamespace);
        assertNull(webServiceDescriptor);
    }

    @Test
    public void testWebServiceDescriptorHashCode() {
        Class<SampleWebService> serviceClazz1 = SampleWebService.class;
        Class<SampleWebService2> serviceClazz2 = SampleWebService2.class;
        String targetNamespace1 = "http://densebrain.com/rif/sample";
        String targetNamespace2 = "http://densebrain.com/rif/sample2";
        String typesNamespace1 = "http://densebrain.com/rif/sample/types";
        String typesNamespace2 = "http://densebrain.com/rif/sample/types2";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);

        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();

        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class<SampleWebService> serviceClazz1 = SampleWebService.class;
        Class<SampleWebService> serviceClazz2 = SampleWebService.class;
        String targetNamespace1 = "http://densebrain.com/rif/sample";
        String targetNamespace2 = "http://densebrain.com/rif/sample";
        String typesNamespace1 = "http://densebrain.com/rif/sample/types";
        String typesNamespace2 = "http://densebrain.com/rif/sample/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        assertFalse(webServiceDescriptor1.equals(null));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testWebServiceDescriptorWithSameServiceClassButDifferentNamespaces() {
        Class<SampleWebService> serviceClazz = SampleWebService.class;
        String targetNamespace1 = "http://densebrain.com/rif/sample";
        String targetNamespace2 = "http://densebrain.com/rif/sample2";
        String typesNamespace1 = "http://densebrain.com/rif/sample/types";
        String typesNamespace2 = "http://densebrain.com/rif/sample/types2";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace1, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace2, typesNamespace2);

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testWebServiceDescriptorWithDifferentServiceClassesButSameNamespaces() {
        Class<SampleWebService> serviceClazz1 = SampleWebService.class;
        Class<SampleWebService2> serviceClazz2 = SampleWebService2.class;
        String targetNamespace = "http://densebrain.com/rif/sample";
        String typesNamespace = "http://densebrain.com/rif/sample/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    private static class SampleWebService {
    }

    private static class SampleWebService2 {
    }
}