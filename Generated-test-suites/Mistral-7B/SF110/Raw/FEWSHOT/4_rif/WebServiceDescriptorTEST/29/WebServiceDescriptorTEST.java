package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorWithValidArguments() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        Assert.assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorWithNullServiceClazz() {
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, targetNamespace, typesNamespace);

        Assert.assertNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptorWithNullTargetNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, typesNamespace);

        Assert.assertNull(webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testWebServiceDescriptorWithNullTypesNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/namespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, null);

        Assert.assertNull(webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);

        Assert.assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());
        Assert.assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);

        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));
        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
}