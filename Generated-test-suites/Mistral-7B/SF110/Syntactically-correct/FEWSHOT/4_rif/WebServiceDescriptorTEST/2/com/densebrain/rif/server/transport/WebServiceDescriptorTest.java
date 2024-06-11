package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstruction() {
        Class mockServiceClazz = new ClassNotFoundException("MockServiceClass").getClass();
        String targetNamespace = "http://www.example.com/myTargetNamespace";
        String typesNamespace = "http://www.example.com/myTypesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, targetNamespace, typesNamespace);

        assertNotNull(webServiceDescriptor);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertEquals(mockServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorNullServiceClazz() {
        String targetNamespace = "http://www.example.com/myTargetNamespace";
        String typesNamespace = "http://www.example.com/myTypesNamespace";

        new WebServiceDescriptor(null, targetNamespace, typesNamespace);
    }

    @Test
    public void testWebServiceDescriptorHashCode() {
        Class mockServiceClazz1 = new ClassNotFoundException("MockServiceClass1").getClass();
        Class mockServiceClazz2 = new ClassNotFoundException("MockServiceClass2").getClass();
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(mockServiceClazz1, "http://www.example.com/myTargetNamespace1", "http://www.example.com/myTypesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz2, "http://www.example.com/myTargetNamespace2", "http://www.example.com/myTypesNamespace2");

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor1.hashCode());
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class mockServiceClazz1 = new ClassNotFoundException("MockServiceClass1").getClass();
        Class mockServiceClazz2 = new ClassNotFoundException("MockServiceClass2").getClass();
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(mockServiceClazz1, "http://www.example.com/myTargetNamespace1", "http://www.example.com/myTypesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz2, "http://www.example.com/myTargetNamespace1", "http://www.example.com/myTypesNamespace1");

        assertEquals(webServiceDescriptor1, webServiceDescriptor1);
        assertEquals(webServiceDescriptor1, webServiceDescriptor2);
        assertNotEquals(webServiceDescriptor1, new Object());
    }

    @Test
    public void testWebServiceDescriptorSameReference() {
        Class mockServiceClazz = new ClassNotFoundException("MockServiceClass").getClass();
        String targetNamespace = "http://www.example.com/myTargetNamespace";
        String typesNamespace = "http://www.example.com/myTypesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, targetNamespace, typesNamespace);

        assertEquals(webServiceDescriptor, webServiceDescriptor);
    }

    @Test
    public void testWebServiceDescriptorDifferentClasses() {
        Class mockServiceClazz1 = new ClassNotFoundException("MockServiceClass1").getClass();
        Class mockServiceClazz2 = new ClassNotFoundException("MockServiceClass2").getClass();
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(mockServiceClazz1, "http://www.example.com/myTargetNamespace", "http://www.example.com/myTypesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz2, "http://www.example.com/myTargetNamespace", "http://www.example.com/myTypesNamespace");

        assertNotEquals(webServiceDescriptor1, webServiceDescriptor2);
    }

    @Test
    public void testWebServiceDescriptorDifferentTargetNamespaces() {
        Class mockServiceClazz = new ClassNotFoundException("MockServiceClass").getClass();
        String targetNamespace1 = "http://www.example.com/myTargetNamespace1";
        String targetNamespace2 = "http://www.example.com/myTargetNamespace2";
        String typesNamespace = "http://www.example.com/myTypesNamespace";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(mockServiceClazz, targetNamespace1, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz, targetNamespace2, typesNamespace);

        assertNotEquals(webServiceDescriptor1, webServiceDescriptor2);
    }

    @Test
    public void testWebServiceDescriptorDifferentTypesNamespaces() {
        Class mockServiceClazz = new ClassNotFoundException("MockServiceClass").getClass();
        String targetNamespace = "http://www.example.com/myTargetNamespace";
        String typesNamespace1 = "http://www.example.com/myTypesNamespace1";
        String typesNamespace2 = "http://www.example.com/myTypesNamespace2";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(mockServiceClazz, targetNamespace, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz, targetNamespace, typesNamespace2);

        assertNotEquals(webServiceDescriptor1, webServiceDescriptor2);
    }
}