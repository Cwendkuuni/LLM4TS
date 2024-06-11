package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptor_WithValidArguments() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptor_WithNullServiceClazz() {
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, targetNamespace, typesNamespace);

        assertNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptor_WithEmptyTargetNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals("", webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptor_WithEmptyTypesNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals("", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testEquals_WithSameObject() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEquals_WithDifferentClasses() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEquals_WithDifferentTargetNamespaces() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace1 = "http://example.com/target1";
        String targetNamespace2 = "http://example.com/target2";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace1, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace2, typesNamespace);

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEquals_WithDifferentTypesNamespaces() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace1 = "http://example.com/types1";
        String typesNamespace2 = "http://example.com/types2";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace2);

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEquals_WithNullObject() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testHashCode_WithSameObjects() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    @Test
    public void testHashCode_WithDifferentClasses() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    @Test
    public void testHashCode_WithDifferentTargetNamespaces() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace1 = "http://example.com/target1";
        String targetNamespace2 = "http://example.com/target2";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace1, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace2, typesNamespace);

        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    @Test
    public void testHashCode_WithDifferentTypesNamespaces() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace1 = "http://example.com/types1";
        String typesNamespace2 = "http://example.com/types2";
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace2);

        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }
}