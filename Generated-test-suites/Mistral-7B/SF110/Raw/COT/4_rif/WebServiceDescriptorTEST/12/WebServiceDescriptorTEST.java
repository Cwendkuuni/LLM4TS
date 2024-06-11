package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = String[].class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        Assert.assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
        Assert.assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = String[].class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        Class otherServiceClazz = Object[].class;
        webServiceDescriptor.setServiceClazz(otherServiceClazz);
        Assert.assertEquals(otherServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = String[].class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
       Assert.assertNull(webServiceDescriptor.getServiceClazz());
        webServiceDescriptor.setServiceClazz(serviceClazz);
        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, "typesNamespace");
        Assert.assertNull(webServiceDescriptor.getTargetNamespace());
        webServiceDescriptor.setTargetNamespace("targetNamespace");
        Assert.assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        Assert.assertNull(webServiceDescriptor.getTargetNamespace());
        webServiceDescriptor.setTargetNamespace("targetNamespace");
        Assert.assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", null);
        Assert.assertNull(webServiceDescriptor.getTypesNamespace());
        webServiceDescriptor.setTypesNamespace("typesNamespace");
        Assert.assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        Assert.assertNull(webServiceDescriptor.getTypesNamespace());
        webServiceDescriptor.setTypesNamespace("typesNamespace");
        Assert.assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullServiceClazz() {
        new WebServiceDescriptor(null, "targetNamespace", "typesNamespace").hashCode();
    }

    @Test
    public void testEqualsWithSameInstance() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(String[].class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;
        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithNullObject() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(String[].class, "targetNamespace", "typesNamespace");
        Assert.assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(String[].class, "targetNamespace", "typesNamespace");
        Object otherObject = new Object();
        Assert.assertFalse(webServiceDescriptor1.equals(otherObject));
    }

    @Test
    public void testEqualsWithDifferentTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(String[].class, "targetNamespace1", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(String[].class, "targetNamespace2", "typesNamespace");
        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithDifferentTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(String[].class, "targetNamespace", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(String[].class, "targetNamespace", "typesNamespace2");
        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithSameServiceClazzButDifferentHashCodes() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(String[].class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(String[].class, "targetNamespace", "typesNamespace");
        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
        byte[] hashcodeArray1 = webServiceDescriptor1.getClass().getName().getBytes();
        byte[] hashcodeArray2 = webServiceDescriptor2.getClass().getName().getBytes();
        Arrays.fill(hashcodeArray1, (byte)0);
        Arrays.fill(hashcodeArray2, (byte)1);
        webServiceDescriptor1.hashCode();
        webServiceDescriptor2.hashCode();
        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
}