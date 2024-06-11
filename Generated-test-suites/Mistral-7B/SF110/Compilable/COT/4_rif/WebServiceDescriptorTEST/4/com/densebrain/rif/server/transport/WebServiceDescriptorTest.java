package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = String[].class;
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        Assert.assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = String[].class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);
        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class originalServiceClazz = String[].class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        Class newServiceClazz = Integer[].class;
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        Assert.assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", null);
        Assert.assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        webServiceDescriptor.setTargetNamespace("newTargetNamespace");
        Assert.assertEquals("newTargetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, "typesNamespace");
        Assert.assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        webServiceDescriptor.setTypesNamespace("newTypesNamespace");
        Assert.assertEquals("newTypesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        int hashCode = webServiceDescriptor.hashCode();
    }

    @Test
    public void testEqualsWithSameObject() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(String[].class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;
        boolean equals = webServiceDescriptor1.equals(webServiceDescriptor2);
        Assert.assertTrue(equals);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(String[].class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Integer[].class, "targetNamespace", "typesNamespace");
        boolean equals = webServiceDescriptor1.equals(webServiceDescriptor2);
        Assert.assertFalse(equals);
    }

    @Test
    public void testEqualsWithDifferentTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(String[].class, "targetNamespace1", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(String[].class, "targetNamespace2", "typesNamespace");
        boolean equals = webServiceDescriptor1.equals(webServiceDescriptor2);
        Assert.assertFalse(equals);
    }

    @Test
    public void testEqualsWithDifferentTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(String[].class, "targetNamespace", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(String[].class, "targetNamespace", "typesNamespace2");
        boolean equals = webServiceDescriptor1.equals(webServiceDescriptor2);
        Assert.assertFalse(equals);
    }

    @Test
    public void testEqualsWithNullObject() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(String[].class, "targetNamespace", "typesNamespace");
        boolean equals = webServiceDescriptor.equals(null);
        Assert.assertFalse(equals);
    }
}