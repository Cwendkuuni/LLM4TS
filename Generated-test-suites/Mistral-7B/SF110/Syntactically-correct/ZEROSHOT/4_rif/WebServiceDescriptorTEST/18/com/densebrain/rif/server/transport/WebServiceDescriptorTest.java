package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstruction() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorNullServiceClazz() {
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        new WebServiceDescriptor(null, targetNamespace, typesNamespace);
    }

    @Test
    public void testWebServiceDescriptorHashCode() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        int hashCode1 = webServiceDescriptor1.hashCode();
        int hashCode2 = webServiceDescriptor2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);

        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));
        assertFalse(webServiceDescriptor1.equals(null));
        assertFalse(webServiceDescriptor1.equals(new Object()));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertFalse(webServiceDescriptor1.equals(new WebServiceDescriptor(serviceClazz2, targetNamespace, "typesNamespace2")));
    }

    @Test
    public void testWebServiceDescriptorSetters() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        String newTargetNamespace = "newTargetNamespace";
        String newTypesNamespace = "newTypesNamespace";

        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);

        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());

        Class newServiceClazz = Mockito.mock(Class.class);

        webServiceDescriptor.setServiceClazz(newServiceClazz);

        assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test(expected = NullPointerException.class)
    public void testWebServiceDescriptorSettersNullParameters() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(Mockito.mock(Class.class), null, null);

        webServiceDescriptor.setTargetNamespace(null);
        webServiceDescriptor.setTypesNamespace(null);
        webServiceDescriptor.setServiceClazz(null);
    }
}