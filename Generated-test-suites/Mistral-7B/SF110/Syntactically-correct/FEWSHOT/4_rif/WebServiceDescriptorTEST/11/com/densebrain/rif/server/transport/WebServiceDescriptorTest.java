package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptor_ValidConstruction() {
        Class serviceClazz = Object.class;
        String targetNamespace = "http://example.com/service";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertNotNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptor_NullServiceClazz() {
        String targetNamespace = "http://example.com/service";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, targetNamespace, typesNamespace);

        assertNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptor_NullTargetNamespace() {
        Class serviceClazz = Object.class;
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, typesNamespace);

        assertNull(webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testWebServiceDescriptor_NullTypesNamespace() {
        Class serviceClazz = Object.class;
        String targetNamespace = "http://example.com/service";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, null);

        assertNull(webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptor_SameValues() {
        Class serviceClazz = Object.class;
        String targetNamespace = "http://example.com/service";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(webServiceDescriptor1, webServiceDescriptor2);
    }

    @Test
    public void testWebServiceDescriptor_DifferentValues() {
        Class serviceClazz1 = Object.class;
        Class serviceClazz2 = String.class;
        String targetNamespace1 = "http://example.com/service1";
        String targetNamespace2 = "http://example.com/service2";
        String typesNamespace1 = "http://example.com/types1";
        String typesNamespace2 = "http://example.com/types2";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);

        assertNotSame(webServiceDescriptor1, webServiceDescriptor2);
    }

    @Test
    public void testWebServiceDescriptor_SameServiceClazz() {
        Class serviceClazz = Object.class;
        String targetNamespace1 = "http://example.com/service1";
        String targetNamespace2 = "http://example.com/service2";
        String typesNamespace1 = "http://example.com/types1";
        String typesNamespace2 = "http://example.com/types2";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace1, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace2, typesNamespace2);

        assertNotSame(webServiceDescriptor1, webServiceDescriptor2);
    }

    @Test
    public void testWebServiceDescriptor_DifferentServiceClazzes() {
        Class serviceClazz1 = Object.class;
        Class serviceClazz2 = String.class;
        String targetNamespace = "http://example.com/service";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertNotEquals(webServiceDescriptor1, webServiceDescriptor2);
    }

    @Test
    public void testWebServiceDescriptor_HashCode() {
        Class serviceClazz = Object.class;
        String targetNamespace = "http://example.com/service";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        int expectedHashCode = 31 * (31 * serviceClazz.hashCode() + targetNamespace.hashCode()) + typesNamespace.hashCode();
        int actualHashCode = webServiceDescriptor.hashCode();

        assertEquals(expectedHashCode, actualHashCode);
    }

    @Test
    public void testWebServiceDescriptor_Equals_Null() {
        Class serviceClazz = Object.class;
        String targetNamespace = "http://example.com/service";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testWebServiceDescriptor_Equals_DifferentTypes() {
        Class serviceClazz1 = Object.class;
        Class serviceClazz2 = String.class;
        String targetNamespace = "http://example.com/service";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        Object otherObject = new Object();

        assertFalse(webServiceDescriptor1.equals(otherObject));
    }

    @Test
    public void testWebServiceDescriptor_Equals_DifferentWebServiceDescriptors() {
        Class serviceClazz1 = Object.class;
        Class serviceClazz2 = String.class;
        String targetNamespace1 = "http://example.com/service1";
        String targetNamespace2 = "http://example.com/service2";
        String typesNamespace1 = "http://example.com/types1";
        String typesNamespace2 = "http://example.com/types2";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testWebServiceDescriptor_Equals_SameWebServiceDescriptor() {
        Class serviceClazz = Object.class;
        String targetNamespace = "http://example.com/service";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
}