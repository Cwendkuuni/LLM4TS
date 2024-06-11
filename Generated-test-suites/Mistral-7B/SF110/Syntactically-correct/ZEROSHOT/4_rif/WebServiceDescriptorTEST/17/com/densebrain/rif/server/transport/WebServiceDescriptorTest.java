package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest
{
    @Test
    public void testWebServiceDescriptorConstructor()
    {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorConstructorNullServiceClazz()
    {
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        new WebServiceDescriptor(null, targetNamespace, typesNamespace);
    }

    @Test
    public void testWebServiceDescriptorSettersAndGetters()
    {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        webServiceDescriptor.setServiceClazz(null);
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());

        webServiceDescriptor.setServiceClazz(new Class<?>(){});
        assertEquals(new Class<>(){}, webServiceDescriptor.getServiceClazz());

        webServiceDescriptor.setTargetNamespace("newTargetNamespace");
        assertEquals("newTargetNamespace", webServiceDescriptor.getTargetNamespace());

        webServiceDescriptor.setTypesNamespace("newTypesNamespace");
        assertEquals("newTypesNamespace", webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorHashCode()
    {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, "targetNamespace2", "typesNamespace2");

        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();
        int hashCode3 = webServiceDescriptor3.hashCode();

        assertNotEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
        assertEquals(hashCode1, webServiceDescriptor1.hashCode());
    }

    @Test
    public void testWebServiceDescriptorEquals()
    {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz1, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz2, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(serviceClazz1, "targetNamespace2", "typesNamespace2");

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor4));
    }
}