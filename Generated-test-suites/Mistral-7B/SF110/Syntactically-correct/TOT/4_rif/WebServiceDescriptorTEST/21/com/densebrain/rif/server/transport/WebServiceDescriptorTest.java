package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(MyService.class, "targetNamespace", "typesNamespace");
        Class serviceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(MyService.class, serviceClazz);
    }

    private WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setup() {
        webServiceDescriptor = new WebServiceDescriptor(null, null, null);
    }

    @Test
    public void testSetServiceClazz() {
        Class expectedServiceClazz = MyService.class;
        webServiceDescriptor.setServiceClazz(expectedServiceClazz);
        Class actualServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(expectedServiceClazz, actualServiceClazz);
    }

    @Test
    public void testGetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(MyService.class, "targetNamespace", "typesNamespace");
        String expectedTargetNamespace = "targetNamespace";
        String actualTargetNamespace = webServiceDescriptor.getTargetNamespace();
        assertEquals(expectedTargetNamespace, actualTargetNamespace);
    }

    @Test
    public void testSetTargetNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(MyService.class, null, null);
        String expectedTargetNamespace = "newTargetNamespace";
        webServiceDescriptor.setTargetNamespace(expectedTargetNamespace);
        String actualTargetNamespace = webServiceDescriptor.getTargetNamespace();
        assertEquals(expectedTargetNamespace, actualTargetNamespace);
    }

    @Test
    public void testGetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(MyService.class, "targetNamespace", "typesNamespace");
        String expectedTypesNamespace = "typesNamespace";
        String actualTypesNamespace = webServiceDescriptor.getTypesNamespace();
        assertEquals(expectedTypesNamespace, actualTypesNamespace);
    }

    @Test
    public void testSetTypesNamespace() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(MyService.class, null, null);
        String expectedTypesNamespace = "newTypesNamespace";
        webServiceDescriptor.setTypesNamespace(expectedTypesNamespace);
        String actualTypesNamespace = webServiceDescriptor.getTypesNamespace();
        assertEquals(expectedTypesNamespace, actualTypesNamespace);
    }

    @Test
    public void testHashCode() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(MyService.class, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(MyService.class, "targetNamespace2", "typesNamespace2");

        int expectedHashCode1 = webServiceDescriptor1.hashCode();
        int expectedHashCode2 = webServiceDescriptor2.hashCode();

        assertNotEquals(expectedHashCode1, expectedHashCode2);
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(MyService.class, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(MyService.class, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(MyService.class, "targetNamespace2", "typesNamespace1");

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
    }
}