package com.densebrain.rif.server.transport;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        Class serviceClazz = WebServiceDescriptor.class;
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }
}