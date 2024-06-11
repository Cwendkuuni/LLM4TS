
package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebService.class; // Replace this with the actual service class
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Class returnedServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(serviceClazz, returnedServiceClazz);
    }

    @Test
    public void testSetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
        Class serviceClazz = WebService.class;
        webServiceDescriptor.setServiceClazz(serviceClazz);
        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testEquals() {
        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "targetNamespace2", "typesNamespace2");
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");

        assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
        assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));
    }

    @Test
    public void testConstructor() {
        Class serviceClazz = WebService.class;
        String targetNamespace = "targetNamespace";
        String typesNamespace = "typesNamespace";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }
}
