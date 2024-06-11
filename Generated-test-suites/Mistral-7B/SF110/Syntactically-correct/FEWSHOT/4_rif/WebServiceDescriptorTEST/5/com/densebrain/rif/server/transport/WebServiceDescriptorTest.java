package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

public class WebServiceDescriptorTest {
    @Test
    public void testWebServiceDescriptorDefaults() {
        WebServiceDescriptor descriptor = new WebServiceDescriptor(null, null, null);
        assertNull(descriptor.getServiceClazz());
        assertNull(descriptor.getTargetNamespace());
        assertNull(descriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorInitialization() {
        Class mockServiceClass = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(mockServiceClass, targetNamespace, typesNamespace);
        assertSame(mockServiceClass, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorGettersAndSetters() {
        Class mockServiceClass = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(mockServiceClass, targetNamespace, typesNamespace);

        Class serviceClazz = mockServiceClass;
        descriptor.setServiceClazz(serviceClazz);
        assertSame(serviceClazz, descriptor.getServiceClazz());

        String newTargetNamespace = "newTargetNamespace";
        descriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, descriptor.getTargetNamespace());

        String newTypesNamespace = "newTypesNamespace";
        descriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class mockServiceClass1 = Mockito.mock(Class.class);
        Class mockServiceClass2 = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(mockServiceClass1, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(mockServiceClass2, targetNamespace, typesNamespace);
        assertNotSame(descriptor1, descriptor2);

        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(mockServiceClass1, targetNamespace, typesNamespace);
        assertSame(descriptor1, descriptor3);

        descriptor1.setServiceClazz(mockServiceClass2);
        assertNotSame(descriptor1, descriptor3);
    }

    @Test
    public void testWebServiceDescriptorHashCode() {
        Class mockServiceClass = Mockito.mock(Class.class);
        Class mockServiceClass2 = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(mockServiceClass, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(mockServiceClass2, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor3 = new WebServiceDescriptor(mockServiceClass, targetNamespace, typesNamespace);

        int hashCode1 = descriptor1.hashCode();
        assertEquals(hashCode1, descriptor1.hashCode());
        assertNotSame(hashCode1, descriptor2.hashCode());
        assertNotSame(hashCode1, descriptor3.hashCode());

        descriptor1.setServiceClazz(mockServiceClass2);
        int hashCode2 = descriptor1.hashCode();
        assertNotSame(hashCode1, hashCode2);
    }
}