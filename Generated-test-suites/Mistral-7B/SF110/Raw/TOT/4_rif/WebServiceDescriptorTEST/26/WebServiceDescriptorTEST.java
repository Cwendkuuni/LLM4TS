
package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {
    @Test
    public void testGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://www.example.com/ws/target", "http://www.example.com/ws/types");
        Class serviceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(WebService.class, serviceClazz);
    }

    @Test
    public void testSetAndGetServiceClazz() {
        WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, null, null);
        Class oldServiceClazz = webServiceDescriptor.getServiceClazz();
        webServiceDescriptor.setServiceClazz(WebService.class);
        Class newServiceClazz = webServiceDescriptor.getServiceClazz();
        assertEquals(WebService.class, newServiceClazz);
        assertEquals(null, oldServiceClazz);
    }

    @Test
    public void testEqualsAndHashCode() {
        Class serviceClazz1 = WebService.class;
        Class serviceClazz2 = WebService2.class;
        String targetNamespace1 = "http://www.example.com/ws/target1";
        String targetNamespace2 = "http://www.example.com/ws/target2";
        String typesNamespace1 = "http://www.example.com/ws/types1";
        String typesNamespace2 = "http://www.example.com/ws/types2";

        WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz1, targetNamespace2, typesNamespace1);
        WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(serviceClazz2, targetNamespace1, typesNamespace2);

        assertEquals(webServiceDescriptor1, webServiceDescriptor2);
        assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
        assertNotEquals(webServiceDescriptor1, webServiceDescriptor3);
        assertNotEquals(webServiceDescriptor1, webServiceDescriptor4);
    }
}