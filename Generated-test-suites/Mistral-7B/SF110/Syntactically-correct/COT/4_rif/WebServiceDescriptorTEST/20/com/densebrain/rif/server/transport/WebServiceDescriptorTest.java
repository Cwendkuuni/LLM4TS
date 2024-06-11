package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = TestService.class;
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        Assert.assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = TestService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);

        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class oldServiceClazz = TestService.class;
        Class newServiceClazz = AnotherTestService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(oldServiceClazz, null, null);

        webServiceDescriptor.setServiceClazz(newServiceClazz);
        Assert.assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "targetNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, targetNamespace, null);

        Assert.assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String oldTargetNamespace = "oldTargetNamespace";
        String newTargetNamespace = "newTargetNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, oldTargetNamespace, null);

        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        Assert.assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, typesNamespace);

        Assert.assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String oldTypesNamespace = "oldTypesNamespace";
        String newTypesNamespace = "newTypesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, oldTypesNamespace);

        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        Assert.assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testHashCodeWithNullServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        int hashCode = webServiceDescriptor.hashCode();
    }

    @Test
    public void testEqualsWithSameInstance() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;

        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithDifferentClass() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(AnotherTestService.class, "targetNamespace", "typesNamespace");

        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithDifferentTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(TestService.class, "targetNamespace1", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(TestService.class, "targetNamespace2", "typesNamespace");

        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithDifferentTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace2");

        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithDifferentServiceClazzAndSameTargetNamespaceAndTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(AnotherTestService.class, "targetNamespace", "typesNamespace");

        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }

    @Test
    public void testEqualsWithNullObject() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");

        Assert.assertFalse(webServiceDescriptor.equals(null));
    }

    @Test
    public void testEqualsWithDifferentClassType() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(TestService.class, "targetNamespace", "typesNamespace");

        Assert.assertFalse(webServiceDescriptor.equals("wrongType"));
    }
}