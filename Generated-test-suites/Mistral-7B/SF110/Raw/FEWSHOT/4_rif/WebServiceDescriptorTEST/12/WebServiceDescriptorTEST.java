package com.densebrain.rif.server.transport;

import org.junit.Test;
import org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptor_ValidConstruction() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Assert.assertEquals(targetNamespace, descriptor.getTargetNamespace());
        Assert.assertEquals(typesNamespace, descriptor.getTypesNamespace());
        Assert.assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptor_NullServiceClazz() {
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        try {
            new WebServiceDescriptor(null, targetNamespace, typesNamespace);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testWebServiceDescriptor_NullTargetNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String typesNamespace = "typesNamespace";

        try {
            new WebServiceDescriptor(serviceClazz, null, typesNamespace);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testWebServiceDescriptor_NullTypesNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";

        new WebServiceDescriptor(serviceClazz, targetNamespace, null);
    }

    @Test
    public void testWebServiceDescriptor_Equals_SameObject() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = descriptor1;

        assertTrue(descriptor1.equals(descriptor2));
    }

    @Test
    public void testWebServiceDescriptor_Equals_DifferentClass() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertFalse(descriptor1.equals(descriptor2));
    }

    @Test
    public void testWebServiceDescriptor_Equals_DifferentTargetNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace1 = "targetNamespace1";
        String targetNamespace2 = "targetNamespace2";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace1, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace2, typesNamespace);

        assertFalse(descriptor1.equals(descriptor2));
    }

    @Test
    public void testWebServiceDescriptor_Equals_DifferentTypesNamespace() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace1 = "typesNamespace1";
        String typesNamespace2 = "typesNamespace2";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace1);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace2);

        assertFalse(descriptor1.equals(descriptor2));
    }

    @Test
    public void testWebServiceDescriptor_Equals_SameValues() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertTrue(descriptor1.equals(descriptor2));
    }

    @Test
    public void testWebServiceDescriptor_HashCode_SameObject() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = descriptor1;

        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testWebServiceDescriptor_HashCode_DifferentObjects() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testWebServiceDescriptor_HashCode_SameValues() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        int hashCode1 = descriptor1.hashCode();
        int hashCode2 = descriptor2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }
}