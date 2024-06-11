package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptor_ValidConstruction() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptor_NullServiceClazz() {
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, targetNamespace, typesNamespace);

        assertNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptor_EmptyTargetNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertTrue(webServiceDescriptor.getTargetNamespace().isEmpty());
    }

    @Test
    public void testWebServiceDescriptor_NullTargetNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = null;
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertNull(webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testWebServiceDescriptor_EmptyTypesNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertTrue(webServiceDescriptor.getTypesNamespace().isEmpty());
    }

    @Test
    public void testWebServiceDescriptor_NullTypesNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = null;

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertNull(webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptor_SameInstance() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertSame(webServiceDescriptor1, webServiceDescriptor2);
    }

    @Test
    public void testWebServiceDescriptor_DifferentInstances() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertNotSame(webServiceDescriptor1, webServiceDescriptor2);
    }

    @Test
    public void testWebServiceDescriptor_EqualHashCodes() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    @Test
    public void testWebServiceDescriptor_DifferentHashCodes() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }
}