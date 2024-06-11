package com.densebrain.rif.server.transport;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    // Initialize WebServiceDescriptor with default values
    WebServiceDescriptor webServiceDescriptor;

    @Before
    public void setup() {
        webServiceDescriptor = new WebServiceDescriptor(WebService.class, "defaultTargetNamespace", "defaultTypesNamespace");
    }

    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebService.class; // Replace with actual service class
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        Class returnedServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(serviceClazz, returnedServiceClazz);
    }

    @Test
    public void testSetServiceClazz() {
        Class newServiceClazz = JaxWsService.class; // Replace with actual service class
        webServiceDescriptor.setServiceClazz(newServiceClazz);
        assertEquals(newServiceClazz, webServiceDescriptor.getServiceClazz());
    }

    @Test
    public void testGetTargetNamespace() {
        String targetNamespace = "newTargetNamespace";
        webServiceDescriptor.setTargetNamespace(targetNamespace);
        assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testSetTargetNamespace() {
        String newTargetNamespace = "newTargetNamespace";
        webServiceDescriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, webServiceDescriptor.getTargetNamespace());
    }

    @Test
    public void testGetTypesNamespace() {
        String typesNamespace = "newTypesNamespace";
        webServiceDescriptor.setTypesNamespace(typesNamespace);
        assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test
    public void testSetTypesNamespace() {
        String newTypesNamespace = "newTypesNamespace";
        webServiceDescriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, webServiceDescriptor.getTypesNamespace());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetServiceClazzNull() {
        webServiceDescriptor.setServiceClazz(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTargetNamespaceNull() {
        webServiceDescriptor.setTargetNamespace(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetTypesNamespaceNull() {
        webServiceDescriptor.setTypesNamespace(null);
    }
}