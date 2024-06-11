package com.densebrain.rif.server.transport;

import org.junit.Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptor() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(serviceClazz, descriptor.getServiceClazz());
        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorNullServiceClazz() {
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";
        new WebServiceDescriptor(null, targetNamespace, typesNamespace);
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorNullTargetNamespace() {
        Class serviceClazz = MyService.class;
        String typesNamespace = "http://example.com/types";
        new WebServiceDescriptor(serviceClazz, null, typesNamespace);
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorNullTypesNamespace() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/namespace";
        new WebServiceDescriptor(serviceClazz, targetNamespace, null);
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(descriptor1, descriptor2);
    }

    @Test
    public void testWebServiceDescriptorNotEquals() {
        Class serviceClazz = MyService.class;
        String targetNamespace1 = "http://example.com/namespace";
        String typesNamespace1 = "http://example.com/types";
        String targetNamespace2 = "http://example.com/namespace2";
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace1, typesNamespace1);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace2, typesNamespace1);

        Assert.assertNotEquals(descriptor1, descriptor2);
    }

    @Test
    public void testWebServiceDescriptorHashCode() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        int hashCode = descriptor.hashCode();

        Class serviceClazz2 = MyService2.class;
        String targetNamespace2 = "http://example.com/namespace2";
        String typesNamespace2 = "http://example.com/types2";
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);

        Assert.assertNotEquals(hashCode, descriptor2.hashCode());
    }

    @Test
    public void testWebServiceDescriptorHashCodeSameObject() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://example.com/namespace";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(descriptor.hashCode(), descriptor.hashCode());
    }
}

class MyService {}
class MyService2 {}