package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testConstructor() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "typesNamespace");
        Class serviceClazz = webServiceDescriptor.getServiceClazz();

        Assert.assertNotNull(serviceClazz);
    }

    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "typesNamespace");
        Class newServiceClazz = Mockito.mock(Class.class);

        webServiceDescriptor.setServiceClazz(newServiceClazz);

        Assert.assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "typesNamespace");

        Assert.assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "initialNamespace", "typesNamespace");
        String newTargetNamespace = "newTargetNamespace";

        webServiceDescriptor.setTargetNamespace(newTargetNamespace);

        Assert.assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "typesNamespace");

        Assert.assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "initialTypesNamespace");
        String newTypesNamespace = "newTypesNamespace";

        webServiceDescriptor.setTypesNamespace(newTypesNamespace);

        Assert.assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullServiceClazz() {
        new WebServiceDescriptor(null, "targetNamespace", "typesNamespace").hashCode();
    }

    @Test
    public void testEqualsWithSameObject() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;

        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "typesNamespace");
        Object obj = new Object();

        Assert.assertFalse(webServiceDescriptor1.equals(obj));
    }

    @Test
    public void testEqualsWithDifferentWebServiceDescriptor() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace2", "typesNamespace2");

        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithSameWebServiceDescriptorButDifferentServiceClazz() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class1), "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Mockito.mock(Class.class2), "targetNamespace", "typesNamespace");

        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testHashCodeWithSameWebServiceDescriptors() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(Mockito.mock(Class.class), "targetNamespace", "typesNamespace");

        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }
}