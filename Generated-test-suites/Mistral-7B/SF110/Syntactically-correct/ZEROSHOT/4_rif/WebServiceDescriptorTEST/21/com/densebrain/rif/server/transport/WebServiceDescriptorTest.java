package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

 private WebServiceDescriptor webServiceDescriptor;

 @Test
 public void testWebServiceDescriptorConstruction() {
     Class serviceClazz = TestWebService.class;
     String targetNamespace = "http://test.com/testService";
     String typesNamespace = "http://test.com/testTypes";

     webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

     assertNotNull(webServiceDescriptor);
     assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
     assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
     assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testSettersAndGetters() {
     Class serviceClazz = TestWebService.class;
     String targetNamespace = "http://test.com/testService";
     String typesNamespace = "http://test.com/testTypes";

     webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

     webServiceDescriptor.setServiceClazz(TestWebService2.class);
     webServiceDescriptor.setTargetNamespace("http://test.com/newTestService");
     webServiceDescriptor.setTypesNamespace("http://test.com/newTestTypes");

     assertEquals(TestWebService2.class, webServiceDescriptor.getServiceClazz());
     assertEquals("http://test.com/newTestService", webServiceDescriptor.getTargetNamespace());
     assertEquals("http://test.com/newTestTypes", webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testHashCode() {
     Class serviceClazz = TestWebService.class;
     String targetNamespace1 = "http://test.com/testService1";
     String targetNamespace2 = "http://test.com/testService2";
     String typesNamespace = "http://test.com/testTypes";

     WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace1, typesNamespace);
     WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace2, typesNamespace);

     assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
 }

 @Test
 public void testEquals() {
     Class serviceClazz = TestWebService.class;
     String targetNamespace1 = "http://test.com/testService1";
     String targetNamespace2 = "http://test.com/testService2";
     String typesNamespace = "http://test.com/testTypes";

     WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(serviceClazz, targetNamespace1, typesNamespace);
     WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(serviceClazz, targetNamespace2, typesNamespace);
     WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(serviceClazz, targetNamespace1, typesNamespace);

     assertTrue(webServiceDescriptor1.equals(webServiceDescriptor3));
     assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
 }

}

class TestWebService {
}

class TestWebService2 {
}