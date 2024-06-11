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

        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        Assert.assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorNullServiceClazz() {
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        new WebServiceDescriptor(null, targetNamespace, typesNamespace);
    }

    @Test
    public void testWebServiceDescriptorHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();

        Assert.assertNotSame(hashCode1, hashCode2);

        Assert.assertEquals(hashCode1, webServiceDescriptor1.hashCode());
        Assert.assertEquals(hashCode2, webServiceDescriptor2.hashCode());

        Assert.assertEquals(serviceClazz1.hashCode(), hashCode1);
        Assert.assertEquals(serviceClazz2.hashCode(), hashCode2);
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);

        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));
        Assert.assertTrue(webServiceDescriptor3.equals(webServiceDescriptor1));

        Assert.assertFalse(webServiceDescriptor1.equals(null));
        Assert.assertFalse(webServiceDescriptor1.equals(new Object()));

        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));

        Assert.assertFalse(webServiceDescriptor1.equals(new WebServiceDescriptor(serviceClazz2, targetNamespace, "otherTypesNamespace")));
    }
}