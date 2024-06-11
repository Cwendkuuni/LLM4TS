package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class WebServiceDescriptorTest
{
    @Test
    public void testWebServiceDescriptorConstructor()
    {
        Class serviceClazz = Object.class;
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertSame(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorConstructorWithNullServiceClazz()
    {
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        new WebServiceDescriptor(null, targetNamespace, typesNamespace);
    }

    @Test
    public void testGetServiceClazz()
    {
        Class serviceClazz = Object.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");

        Class retrievedServiceClazz = webServiceDescriptor.getServiceClazz();

        assertNotNull(retrievedServiceClazz);
        assertSame(serviceClazz, retrievedServiceClazz);
    }

    @Test
    public void testSetServiceClazz()
    {
        Class originalServiceClazz = Object.class;
        Class newServiceClazz = String.class;
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(originalServiceClazz, "targetNamespace", "typesNamespace");

        webServiceDescriptor.setServiceClazz(newServiceClazz);

        assertSame(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace()
    {
        String targetNamespace = "targetNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, targetNamespace, "typesNamespace");

        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace()
    {
        String originalTargetNamespace = "originalTargetNamespace";
        String newTargetNamespace = "newTargetNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, originalTargetNamespace, "typesNamespace");

        webServiceDescriptor.setTargetNamespace(newTargetNamespace);

        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace()
    {
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "targetNamespace", typesNamespace);

        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace()
    {
        String originalTypesNamespace = "originalTypesNamespace";
        String newTypesNamespace = "newTypesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Object.class, "targetNamespace", originalTypesNamespace);

        webServiceDescriptor.setTypesNamespace(newTypesNamespace);

        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testHashCode()
    {
        Class serviceClazz1 = Object.class;
        Class serviceClazz2 = String.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace2", "typesNamespace2");

        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();

        assertNotNull(hashCode1);
        assertNotNull(hashCode2);
        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testEquals()
    {
        Class serviceClazz1 = Object.class;
        Class serviceClazz2 = String.class;
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz2, "targetNamespace2", "typesNamespace1");

        assertFalse(webServiceDescriptor1.equals(null));
        assertFalse(webServiceDescriptor1.equals(""));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
    }
}