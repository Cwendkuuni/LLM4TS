package com.densebrain.rif.server.transport;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class WebServiceDescriptorTest
{
    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setup()
    {
        webServiceDescriptor = new WebServiceDescriptor(ServiceClass.class, "targetNamespace", "typesNamespace");
    }

    @Test
    public void testConstructor()
    {
        assertNotNull(webServiceDescriptor);
    }

    @Test
    public void testGettersAndSetters_ServiceClazz()
    {
        Class expectedServiceClazz = ServiceClass.class;
        webServiceDescriptor.setServiceClazz(expectedServiceClazz);
        Class actualServiceClazz = webServiceDescriptor.getServiceClazz();

        assertEquals(expectedServiceClazz, actualServiceClazz);
    }

    @Test
    public void testGettersAndSetters_TargetNamespace()
    {
        String expectedTargetNamespace = "targetNamespace";
        webServiceDescriptor.setTargetNamespace(expectedTargetNamespace);
        String actualTargetNamespace = webServiceDescriptor.getTargetNamespace();

        assertEquals(expectedTargetNamespace, actualTargetNamespace);
    }

    @Test
    public void testGettersAndSetters_TypesNamespace()
    {
        String expectedTypesNamespace = "typesNamespace";
        webServiceDescriptor.setTypesNamespace(expectedTypesNamespace);
        String actualTypesNamespace = webServiceDescriptor.getTypesNamespace();

        assertEquals(expectedTypesNamespace, actualTypesNamespace);
    }

    @Test
    public void testHashCode_sameObject()
    {
        WebServiceDescriptor expectedWebServiceDescriptor = webServiceDescriptor;
        int actualHashcode = webServiceDescriptor.hashCode();
        int expectedHashcode = expectedWebServiceDescriptor.hashCode();

        assertEquals(actualHashcode, expectedHashcode);
    }

    @Test
    public void testHashCode_differentObjects()
    {
        WebServiceDescriptor expectedWebServiceDescriptor = new WebServiceDescriptor(ServiceClass2.class, "targetNamespace", "typesNamespace");
        int actualHashcode = webServiceDescriptor.hashCode();
        int expectedHashcode = expectedWebServiceDescriptor.hashCode();

        assertNotSame(actualHashcode, expectedHashcode);
    }

    @Test
    public void testEquals_sameObject()
    {
        WebServiceDescriptor expectedWebServiceDescriptor = webServiceDescriptor;
        boolean actualResult = webServiceDescriptor.equals(expectedWebServiceDescriptor);
        boolean expectedResult = true;

        assertEquals(actualResult, expectedResult);
    }

    @Test
    public void testEquals_nullObject()
    {
        boolean actualResult = webServiceDescriptor.equals(null);
        boolean expectedResult = false;

        assertEquals(actualResult, expectedResult);
    }

    @Test
    public void testEquals_differentClass()
    {
        Object differentObject = new Object();
        boolean actualResult = webServiceDescriptor.equals(differentObject);
        boolean expectedResult = false;

        assertEquals(actualResult, expectedResult);
    }

    @Test
    public void testEquals_differentWebServiceDescriptor()
    {
        WebServiceDescriptor differentWebServiceDescriptor = new WebServiceDescriptor(ServiceClass2.class, null, null);
        boolean actualResult = webServiceDescriptor.equals(differentWebServiceDescriptor);
        boolean expectedResult = false;

        assertEquals(actualResult, expectedResult);
    }

    @Test
    public void testEquals_sameClassDifferentValues()
    {
        WebServiceDescriptor sameClassDifferentValues = new WebServiceDescriptor(ServiceClass.class, null, null);
        boolean actualResult = webServiceDescriptor.equals(sameClassDifferentValues);
        boolean expectedResult = false;

        assertEquals(actualResult, expectedResult);
    }

    private static class ServiceClass { }
    private static class ServiceClass2 { }
}