package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Assert.assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
        Assert.assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        webServiceDescriptor.setServiceClazz(serviceClazz);
        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        Assert.assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, "typesNamespace");
        webServiceDescriptor.setTargetNamespace("targetNamespace");
        Assert.assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", null);
        Assert.assertEquals(null, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", null);
        webServiceDescriptor.setTypesNamespace("typesNamespace");
        Assert.assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        int hashCode = webServiceDescriptor.hashCode();
        Assert.fail("HashCode should have thrown NullPointerException for null serviceClazz");
    }

    @Test
    public void testEqualsWithSameObject() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;
        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Class serviceClazz1 = WebServiceDescriptor.class;
        Class serviceClazz2 = String.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace2", "typesNamespace2");
        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithDifferentServiceClazz() {
        Class serviceClazz1 = WebServiceDescriptor.class;
        Class serviceClazz2 = Object.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace2", "typesNamespace2");
        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithSameData() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testHashCodeAndEquals() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
        Assert.assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

}