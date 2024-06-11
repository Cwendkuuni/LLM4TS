package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstruction() {
        Class mockServiceClass = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(mockServiceClass, targetNamespace, typesNamespace);

        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
        Assert.assertEquals(mockServiceClass, descriptor.getServiceClazz());
    }

    @Test
    public void testSettersAndGetters() {
        Class mockServiceClass = Mockito.mock(Class.class);
        WebServiceDescriptor descriptor = new WebServiceDescriptor(mockServiceClass, null, null);

        String targetNamespace = "targetNamespace";
        descriptor.setTargetNamespace(targetNamespace);
        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());

        String typesNamespace = "typesNamespace";
        descriptor.setTypesNamespace(typesNamespace);
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());

        Class newServiceClass = Mockito.mock(Class.class);
        descriptor.setServiceClazz(newServiceClass);
        Assert.assertEquals(newServiceClass, descriptor.getServiceClazz());
    }

    @Test
    public void testHashCode() {
        Class mockServiceClass1 = Mockito.mock(Class.class);
        Class mockServiceClass2 = Mockito.mock(Class.class);

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(mockServiceClass1, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(mockServiceClass2, null, null);

        int hashCode1 = descriptor1.hashCode();
        descriptor1.setServiceClazz(mockServiceClass2);
        int hashCode2 = descriptor1.hashCode();

        Assert.notEqual(hashCode1, hashCode2);
        Assert.assertEquals(hashCode1, descriptor1.hashCode());
    }

    @Test
    public void testEquals() {
        Class mockServiceClass1 = Mockito.mock(Class.class);
        Class mockServiceClass2 = Mockito.mock(Class.class);

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(mockServiceClass1, null, null);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(mockServiceClass2, null, null);

        Assert.assertFalse(descriptor1.equals(descriptor2));

        descriptor1.setServiceClazz(mockServiceClass2);
        Assert.assertTrue(descriptor1.equals(descriptor2));
    }
}