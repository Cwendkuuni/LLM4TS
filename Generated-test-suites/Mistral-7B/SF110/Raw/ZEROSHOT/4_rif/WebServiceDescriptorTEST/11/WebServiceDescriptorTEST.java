package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

 private WebServiceDescriptor createWebServiceDescriptor(Class serviceClazz, String targetNamespace, String typesNamespace) {
 return new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
 }

 @Test
 public void testConstructor() {
 Class serviceClazz = WebServiceDescriptor.class;
 WebServiceDescriptor webServiceDescriptor = createWebServiceDescriptor(serviceClazz, "targetNamespace", "typesNamespace");
 assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
 assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
 assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testSettersAndGetters() {
 Class serviceClazz = WebServiceDescriptor.class;
 WebServiceDescriptor webServiceDescriptor = createWebServiceDescriptor(serviceClazz, null, null);

 webServiceDescriptor.setTargetNamespace("targetNamespace");
 webServiceDescriptor.setTypesNamespace("typesNamespace");
 webServiceDescriptor.setServiceClazz(serviceClazz);

 assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
 assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
 assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
 }

 @Test(expected = NullPointerException.class)
 public void testSettersWithNullArguments() {
 Class serviceClazz = WebServiceDescriptor.class;
 WebServiceDescriptor webServiceDescriptor = createWebServiceDescriptor(serviceClazz, null, null);

 webServiceDescriptor.setTargetNamespace(null);
 webServiceDescriptor.setTypesNamespace(null);
 webServiceDescriptor.setServiceClazz(null);
 }

 @Test
 public void testEquals() {
 Class serviceClazz = WebServiceDescriptor.class;
 WebServiceDescriptor webServiceDescriptor1 = createWebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = createWebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor3 = createWebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace1");

 assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
 assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
 assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
 }

 @Test
 public void testHashCode() {
 Class serviceClazz = WebServiceDescriptor.class;
 WebServiceDescriptor webServiceDescriptor1 = createWebServiceDescriptor(serviceClazz, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = createWebServiceDescriptor(serviceClazz, "targetNamespace2", "typesNamespace1");

 int hashCode1 = webServiceDescriptor1.hashCode();
 int hashCode2 = webServiceDescriptor2.hashCode();

 assertNotSame(hashCode1, hashCode2);
 }
}