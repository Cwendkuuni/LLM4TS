package com.densebrain.rif.server.transport;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(String.class, "targetNamespace", "typesNamespace");
        Class serviceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(String.class, serviceClazz);
    }
}