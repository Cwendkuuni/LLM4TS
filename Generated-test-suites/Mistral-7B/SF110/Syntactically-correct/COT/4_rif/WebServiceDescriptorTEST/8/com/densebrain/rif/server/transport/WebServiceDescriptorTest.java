package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebServiceDescriptorTest {

 private WebServiceDescriptor webServiceDescriptor;

 @Test
 public void testWebServiceDescriptorConstructor() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace", "typesNamespace");
 assertNotNull(webServiceDescriptor.getServiceClazz());
 assertNotNull(webServiceDescriptor.getTargetNamespace());
 assertNotNull(webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testGetServiceClazz() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace", "typesNamespace");
 Class serviceClazz = webServiceDescriptor.getServiceClazz();
 assertEquals(WebService.class, serviceClazz);
 }

 @Test
 public void testSetServiceClazz() {
 webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
 webServiceDescriptor.setServiceClazz(WebService.class);
 Class serviceClazz = webServiceDescriptor.getServiceClazz();
 assertEquals(WebService.class, serviceClazz);
 }

 @Test
 public void testGetTargetNamespace() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace", "typesNamespace");
 String targetNamespace = webServiceDescriptor.getTargetNamespace();
 assertEquals("targetNamespace", targetNamespace);
 }

 @Test
 public void testSetTargetNamespace() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, null, "typesNamespace");
 webServiceDescriptor.setTargetNamespace("targetNamespace");
 String targetNamespace = webServiceDescriptor.getTargetNamespace();
 assertEquals("targetNamespace", targetNamespace);
 }

 @Test
 public void testGetTypesNamespace() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace", "typesNamespace");
 String typesNamespace = webServiceDescriptor.getTypesNamespace();
 assertEquals("typesNamespace", typesNamespace);
 }

 @Test
 public void testSetTypesNamespace() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace", null);
 webServiceDescriptor.setTypesNamespace("typesNamespace");
 String typesNamespace = webServiceDescriptor.getTypesNamespace();
 assertEquals("typesNamespace", typesNamespace);
 }

 @Test(expected = NullPointerException.class)
 public void testSetServiceClazzNull() {
 webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
 webServiceDescriptor.setServiceClazz(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetTargetNamespaceNull() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, null, "typesNamespace");
 webServiceDescriptor.setTargetNamespace(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetTypesNamespaceNull() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace", null);
 webServiceDescriptor.setTypesNamespace(null);
 }

 @Test
 public void testHashCode() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 int hashCode = webServiceDescriptor.hashCode();
 WebServiceDescriptor newWebServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 int newHashCode = newWebServiceDescriptor.hashCode();
 assertEquals(hashCode, newHashCode);
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace2", "typesNamespace1");
 newHashCode = newWebServiceDescriptor.hashCode();
 assertNotEquals(hashCode, newHashCode);
 }

 @Test
 public void testEquals() {
 WebServiceDescriptor firstWebServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor secondWebServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 assertTrue(firstWebServiceDescriptor.equals(secondWebServiceDescriptor));
 WebServiceDescriptor thirdWebServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace2", "typesNamespace1");
 assertFalse(firstWebServiceDescriptor.equals(thirdWebServiceDescriptor));
 }
}