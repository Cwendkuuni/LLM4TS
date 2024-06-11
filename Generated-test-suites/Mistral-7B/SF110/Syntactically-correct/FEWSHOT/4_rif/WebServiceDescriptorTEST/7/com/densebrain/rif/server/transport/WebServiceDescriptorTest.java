package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptor() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        Assert.assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetAndGetServiceClazz() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);

        webServiceDescriptor.setServiceClazz(serviceClazz);

        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetAndGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);

        String targetNamespace = "http://example.com/target";
        webServiceDescriptor.setTargetNamespace(targetNamespace);

        Assert.assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetAndGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);

        String typesNamespace = "http://example.com/types";
        webServiceDescriptor.setTypesNamespace(typesNamespace);

        Assert.assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCodeWithNullServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);

        int hashCode = webServiceDescriptor.hashCode();

        Assert.assertEquals(hashCode, webServiceDescriptor.hashCode());
    }

    @Test
    public void testHashCodeWithNonnullServiceClazz() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);

        int hashCode1 = webServiceDescriptor.hashCode();

        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, null, null);

        int hashCode2 = webServiceDescriptor2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEqualsWithNullObject() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);

        Assert.assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClassType() {
        Object anotherObject = new Object();
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);

        Assert.assertFalse(webServiceDescriptor.equals(anotherObject));
    }

    @Test
    public void testEqualsWithDifferentServiceClazz() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, null, null);

        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithSameServiceClazzAndDifferentOtherParameters() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "namespace1", null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "namespace2", null);

        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithSameServiceClazzAndSameOtherParameters() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, "namespace1", null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, "namespace1", null);

        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
}