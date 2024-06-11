package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstructor() {
        Class serviceClazz = WebService.class;
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        Assert.assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);

        Assert.assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSetServiceClazz() {
        Class originalServiceClazz = WebService.class;
        Class newServiceClazz = WsdlService.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(originalServiceClazz, null, null);

        webServiceDescriptor.setServiceClazz(newServiceClazz);

        Assert.assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "http://example.com/namespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, targetNamespace, null);

        Assert.assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String originalTargetNamespace = "http://example.com/namespace";
        String newTargetNamespace = "http://example.com/new-namespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, originalTargetNamespace, null);

        webServiceDescriptor.setTargetNamespace(newTargetNamespace);

        Assert.assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, typesNamespace);

        Assert.assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String originalTypesNamespace = "http://example.com/types";
        String newTypesNamespace = "http://example.com/new-types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, originalTypesNamespace);

        webServiceDescriptor.setTypesNamespace(newTypesNamespace);

        Assert.assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode() {
        Class serviceClazz = WebService.class;
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(WebService.class, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(serviceClazz, null, typesNamespace);
        WebServiceDescriptor webServiceDescriptor5 = new WebServiceDescriptor(serviceClazz, targetNamespace, null);

        Assert.assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
        Assert.assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());
        Assert.assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor4.hashCode());
        Assert.assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor5.hashCode());
    }

    @Test
    public void testEquals() {
        Class serviceClazz = WebService.class;
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(WebService.class, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(serviceClazz, null, typesNamespace);
        WebServiceDescriptor webServiceDescriptor5 = new WebServiceDescriptor(serviceClazz, targetNamespace, null);

        Assert.assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor4));
        Assert.assertFalse(webServiceDescriptor1.equals(webServiceDescriptor5));
    }
}