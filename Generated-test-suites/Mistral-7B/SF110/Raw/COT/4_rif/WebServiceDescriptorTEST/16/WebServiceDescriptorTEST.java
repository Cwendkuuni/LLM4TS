package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertSame(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = MyService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);

        assertSame(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class oldServiceClazz = MyService.class;
        Class newServiceClazz = AnotherService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(oldServiceClazz, null, null);

        webServiceDescriptor.setServiceClazz(newServiceClazz);
        assertSame(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "http://example.com/target";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, targetNamespace, null);

        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String oldTargetNamespace = "http://example.com/old";
        String newTargetNamespace = "http://example.com/new";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, oldTargetNamespace, null);

        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, typesNamespace);

        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String oldTypesNamespace = "http://example.com/old";
        String newTypesNamespace = "http://example.com/new";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, oldTypesNamespace);

        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullServiceClazz() {
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        new WebServiceDescriptor(null, targetNamespace, typesNamespace);
    }

    @Test(expected = NullPointerException.class)
    public void testSetServiceClazzNull() {
        Class oldServiceClazz = MyService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(oldServiceClazz, null, null);

        webServiceDescriptor.setServiceClazz(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetTargetNamespaceNull() {
        Class serviceClazz = MyService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);

        webServiceDescriptor.setTargetNamespace(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetTypesNamespaceNull() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/target";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, null);

        webServiceDescriptor.setTypesNamespace(null);
    }

    @Test
    public void testHashCode() {
        Class serviceClazz1 = MyService.class;
        Class serviceClazz2 = AnotherService.class;

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, null, null);

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor1.hashCode());
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());

        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, null, null);
        webServiceDescriptor3.setServiceClazz(serviceClazz2);
        assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = MyService.class;
        Class serviceClazz2 = AnotherService.class;

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz2, null, null);

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));

        webServiceDescriptor3.setServiceClazz(serviceClazz1);
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));

        webServiceDescriptor1.setServiceClazz(serviceClazz2);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
    }

    @Test
    public void testEqualsNull() {
        Class serviceClazz = MyService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);

        assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        Object obj = new Object();
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(MyService.class, null, null);

        assertFalse(webServiceDescriptor.equals(obj));
    }

    // Add more test cases if necessary based on the actual implementation and requirements

}