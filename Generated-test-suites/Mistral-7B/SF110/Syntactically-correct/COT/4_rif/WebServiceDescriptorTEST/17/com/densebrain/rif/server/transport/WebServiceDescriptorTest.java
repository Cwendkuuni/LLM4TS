package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
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
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);
        Class returnedServiceClazz = webServiceDescriptor.getServiceClazz();

        Assert.assertEquals(serviceClazz, returnedServiceClazz);
    }

    @Test
    public void testSetServiceClazz() {
        Class oldServiceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        Class newServiceClazz = Mockito.mock(Class.class);

        webServiceDescriptor.setServiceClazz(newServiceClazz);
        Class returnedServiceClazz = webServiceDescriptor.getServiceClazz();

        Assert.assertEquals(newServiceClazz, returnedServiceClazz);
        Assert.assertNotSame(oldServiceClazz, newServiceClazz);
        Assert.assertNotSame(oldServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", null);
        String returnedTargetNamespace = webServiceDescriptor.getTargetNamespace();

        Assert.assertEquals("targetNamespace", returnedTargetNamespace);
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        String newTargetNamespace = "newTargetNamespace";

        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        String returnedTargetNamespace = webServiceDescriptor.getTargetNamespace();

        Assert.assertEquals(newTargetNamespace, returnedTargetNamespace);
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, "typesNamespace");
        String returnedTypesNamespace = webServiceDescriptor.getTypesNamespace();

        Assert.assertEquals("typesNamespace", returnedTypesNamespace);
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        String newTypesNamespace = "newTypesNamespace";

        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        String returnedTypesNamespace = webServiceDescriptor.getTypesNamespace();

        Assert.assertEquals(newTypesNamespace, returnedTypesNamespace);
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        int hashCode = webServiceDescriptor.hashCode();

        Assert.fail("Hash code should not be computed with a null serviceClazz");
    }

    @Test
    public void testEqualsWithSameObject() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, null, null);
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;

        boolean result = webServiceDescriptor1.equals(webServiceDescriptor2);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsWithNullObject() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);

        boolean result = webServiceDescriptor.equals(null);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWithDifferentClass() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        Object other = new Object();

        boolean result = webServiceDescriptor1.equals(other);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWithSameClassDifferentState() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, null, null);

        boolean result = webServiceDescriptor1.equals(webServiceDescriptor2);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqualsWithDifferentClassDifferentState() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, null, null);

        boolean result = webServiceDescriptor1.equals(webServiceDescriptor2);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualsWithSameClassDifferentServiceClazz() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, null, null);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, null, null);

        boolean result = webServiceDescriptor1.equals(webServiceDescriptor2);

        Assert.assertFalse(result);
    }
}