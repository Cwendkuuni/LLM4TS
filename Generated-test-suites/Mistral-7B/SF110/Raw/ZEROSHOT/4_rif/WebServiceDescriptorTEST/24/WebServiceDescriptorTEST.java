package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class WebServiceDescriptorTest
{
    @Test
    public void testWebServiceDescriptorCreation()
    {
        Class testServiceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(testServiceClazz, targetNamespace, typesNamespace);

        assertNotNull(webServiceDescriptor);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertNotNull(webServiceDescriptor.getServiceClazz());
        assertEquals(testServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testSettersAndGetters()
    {
        Class testServiceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(testServiceClazz, targetNamespace, typesNamespace);

        String newTargetNamespace = "newTargetNamespace";
        String newTypesNamespace = "newTypesNamespace";

        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);

        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());

        webServiceDescriptor.setServiceClazz(null);
        assertNull(webServiceDescriptor.getServiceClazz());

        webServiceDescriptor.setServiceClazz(Mockito.mock(Class.class));
        assertNotNull(webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testEquals()
    {
        Class testServiceClazz1 = Mockito.mock(Class.class);
        Class testServiceClazz2 = Mockito.mock(Class.class);

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(testServiceClazz1, "testNamespace1", "testTypesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(testServiceClazz2, "testNamespace1", "testTypesNamespace1");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(testServiceClazz1, "testNamespace2", "testTypesNamespace1");

        assertEquals(webServiceDescriptor1, webServiceDescriptor2);
        assertNotEquals(webServiceDescriptor1, webServiceDescriptor3);
        assertNotEquals(webServiceDescriptor1, null);
        assertNotEquals(webServiceDescriptor1, new Object());
    }

    @Test
    public void testHashCode()
    {
        Class testServiceClazz = Mockito.mock(Class.class);

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(testServiceClazz, "testNamespace1", "testTypesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(testServiceClazz, "testNamespace2", "testTypesNamespace2");

        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }
}