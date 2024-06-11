package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstruction() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGettersAndSetters() {
        Class serviceClazz = Mockito.mock(Class.class);
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");

        String newTargetNamespace = "newTargetNamespace";
        String newTypesNamespace = "newTypesNamespace";
        Class newServiceClazz = Mockito.mock(Class.class);

        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        webServiceDescriptor.setServiceClazz(newServiceClazz);

        Assert.assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
        Assert.assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
        Assert.assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace2", "typesNamespace2");

        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();

        Assert.assertNotEquals(hashCode1, hashCode2);

        // Test with same object
        WebServiceDescriptor webServiceDescriptor3 = webServiceDescriptor1;
        int hashCode3 = webServiceDescriptor3.hashCode();
        Assert.assertEquals(hashCode1, hashCode3);

        // Test with null serviceClazz
        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(null, "targetNamespace3", "typesNamespace3");
        int hashCode4 = webServiceDescriptor4.hashCode();
        Assert.assertEquals(hashCode4, webServiceDescriptor1.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, "targetNamespace2", "typesNamespace2");

        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
        Assert.assertFalse(webServiceDescriptor1.equals(null));
        Assert.assertFalse(webServiceDescriptor1.equals("testString"));
    }
}