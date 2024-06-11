package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorWithValidArguments() {
        Class serviceClazz = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptorWithNullArguments() {
        Class serviceClazz = null;
        String targetNamespace = null;
        String typesNamespace = null;

        Assert.assertThrows(NullPointerException.class, () -> new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace));
    }

    @Test
    public void testWebServiceDescriptorWithNullServiceClazz() {
        Class serviceClazz = null;
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertNull(webServiceDescriptor.getServiceClazz());
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

        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor1.hashCode());
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class serviceClazz1 = Mockito.mock(Class.class);
        Class serviceClazz2 = Mockito.mock(Class.class);
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace, typesNamespace);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace, typesNamespace);

        assertFalse(webServiceDescriptor1.equals(null));
        assertFalse(webServiceDescriptor1.equals(new Object()));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));

        // When serviceClazz is null, it should not match
        webServiceDescriptor1.setServiceClazz(null);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));

        // When targetNamespace is null, it should not match
        webServiceDescriptor1.setTargetNamespace(null);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));

        // When targetNamespace is different, it should not match
        webServiceDescriptor1.setTargetNamespace("differentNamespace");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));

        // When typesNamespace is null, it should not match
        webServiceDescriptor1.setTypesNamespace(null);
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));

        // When typesNamespace is different, it should not match
        webServiceDescriptor1.setTypesNamespace("differentTypesNamespace");
        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));

        // When serviceClazz is the same, it should match
        webServiceDescriptor1.setServiceClazz(serviceClazz2);
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
    }
}