package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

    @Test
    public void testWebServiceDescriptorConstruction() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://mycompany.com/myservice";
        String typesNamespace = "http://mycompany.com/mytypes";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
        assertEquals(serviceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
    }

    @Test
    public void testWebServiceDescriptorSettersAndGetters() {
        Class serviceClazz = MyService.class;
        String targetNamespace = "http://mycompany.com/myservice";
        String typesNamespace = "http://mycompany.com/mytypes";
        WebServiceDescriptor descriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

        Class newServiceClazz = MyOtherService.class;
        descriptor.setServiceClazz(newServiceClazz);
        assertEquals(newServiceClazz, descriptor.getServiceClazz());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());

        String newTargetNamespace = "http://mycompany.com/newservice";
        descriptor.setTargetNamespace(newTargetNamespace);
        assertEquals(newTargetNamespace, descriptor.getTargetNamespace());
        assertEquals(typesNamespace, descriptor.getTypesNamespace());
        assertEquals(serviceClazz, descriptor.getServiceClazz());

        String newTypesNamespace = "http://mycompany.com/newtypes";
        descriptor.setTypesNamespace(newTypesNamespace);
        assertEquals(newTypesNamespace, descriptor.getTypesNamespace());
        assertEquals(targetNamespace, descriptor.getTargetNamespace());
        assertEquals(serviceClazz, descriptor.getServiceClazz());
    }

    @Test
    public void testWebServiceDescriptorHashcode() {
        Class serviceClazz1 = MyService.class;
        String targetNamespace1 = "http://mycompany.com/myservice1";
        String typesNamespace1 = "http://mycompany.com/mytypes1";
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);

        Class serviceClazz2 = MyService.class;
        String targetNamespace2 = "http://mycompany.com/myservice2";
        String typesNamespace2 = "http://mycompany.com/mytypes2";
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);

        int hashcode1 = descriptor1.hashCode();
        int hashcode2 = descriptor2.hashCode();
        assertNotEquals(hashcode1, hashcode2);

        descriptor1.setServiceClazz(serviceClazz2);
        descriptor1.setTargetNamespace(targetNamespace2);
        descriptor1.setTypesNamespace(typesNamespace2);
        int hashcode3 = descriptor1.hashCode();
        assertEquals(hashcode2, hashcode3);
    }

    @Test
    public void testWebServiceDescriptorEquals() {
        Class serviceClazz1 = MyService.class;
        String targetNamespace1 = "http://mycompany.com/myservice1";
        String typesNamespace1 = "http://mycompany.com/mytypes1";
        WebServiceDescriptor descriptor1 = new WebServiceDescriptor(serviceClazz1, targetNamespace1, typesNamespace1);

        Class serviceClazz2 = MyService.class;
        String targetNamespace2 = "http://mycompany.com/myservice2";
        String typesNamespace2 = "http://mycompany.com/mytypes2";
        WebServiceDescriptor descriptor2 = new WebServiceDescriptor(serviceClazz2, targetNamespace2, typesNamespace2);

        assertFalse(descriptor1.equals(descriptor2));

        descriptor1.setServiceClazz(serviceClazz2);
        descriptor1.setTargetNamespace(targetNamespace2);
        descriptor1.setTypesNamespace(typesNamespace2);

        assertTrue(descriptor1.equals(descriptor2));
    }
}