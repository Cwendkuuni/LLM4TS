package com.densebrain.rif.server.transport;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceDescriptor.class;
        String targetNamespace = "http://example.com/target";
        String typesNamespace = "http://example.com/types";
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        Class returnedServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(serviceClazz, returnedServiceClazz);
    }
}