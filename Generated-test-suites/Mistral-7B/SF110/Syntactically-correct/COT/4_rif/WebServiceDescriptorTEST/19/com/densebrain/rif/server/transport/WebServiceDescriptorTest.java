package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class WebServiceDescriptorTest {

 // Test constructor
 @Test
 public void testWebServiceDescriptorConstructor() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 assertEquals("targetNamespace1", webServiceDescriptor.getTargetNamespace());
 assertEquals("typesNamespace1", webServiceDescriptor.getTypesNamespace());
 assertSame(WebService.class, webServiceDescriptor.getServiceClazz());
 }

 // Test getters and setters for targetNamespace

 @Test
 public void testGetTargetNamespace() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 assertEquals("targetNamespace1", webServiceDescriptor.getTargetNamespace());
 }

 @Test
 public void testSetTargetNamespace() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 webServiceDescriptor.setTargetNamespace("targetNamespace2");
 assertEquals("targetNamespace2", webServiceDescriptor.getTargetNamespace());
 }

 @Test
 public void testEmptyGetTargetNamespace() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, null, "typesNamespace1");
 assertNull(webServiceDescriptor.getTargetNamespace());
 }

 @Test
 public void testNullGetTargetNamespace() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, null, "typesNamespace1");
 webServiceDescriptor.setTargetNamespace(null);
 assertNull(webServiceDescriptor.getTargetNamespace());
 }

 @Test
 public void testGetTargetNamespaceWithSameValue() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 assertSame(webServiceDescriptor1, webServiceDescriptor2);
 }

 @Test
 public void testGetTargetNamespaceWithDifferentValues() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "targetNamespace2", "typesNamespace1");
 assertNotSame(webServiceDescriptor1, webServiceDescriptor2);
 }

 // Test getters and setters for typesNamespace

 @Test
 public void testGetTypesNamespace() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 assertEquals("typesNamespace1", webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testSetTypesNamespace() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 webServiceDescriptor.setTypesNamespace("typesNamespace2");
 assertEquals("typesNamespace2", webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testEmptyGetTypesNamespace() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", null);
 assertNull(webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testNullGetTypesNamespace() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", null);
 webServiceDescriptor.setTypesNamespace(null);
 assertNull(webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testGetTypesNamespaceWithSameValue() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 assertSame(webServiceDescriptor1, webServiceDescriptor2);
 }

 @Test
 public void testGetTypesNamespaceWithDifferentValues() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace2");
 assertNotSame(webServiceDescriptor1, webServiceDescriptor2);
 }

 // Test getters and setters for serviceClazz

 @Test
 public void testGetServiceClazz() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 assertSame(WebService.class, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testSetServiceClazz() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 webServiceDescriptor.setServiceClazz(JaxRsService.class);
 assertSame(JaxRsService.class, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testEmptyGetServiceClazz() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace1", "typesNamespace1");
 assertNull(webServiceDescriptor.getServiceClazz());
 }

 @Test(expected = NullPointerException.class)
 public void testNullGetServiceClazz() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, "targetNamespace1", "typesNamespace1");
 webServiceDescriptor.setServiceClazz(null);
 }

 @Test
 public void testGetServiceClazzWithSameClass() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 assertSame(webServiceDescriptor1, webServiceDescriptor2);
 }

 @Test
 public void testGetServiceClazzWithDifferentClasses() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(JaxRsService.class, "targetNamespace1", "typesNamespace1");
 assertNotSame(webServiceDescriptor1, webServiceDescriptor2);
 }

 // Test equals method

 @Test
 public void testEqualsWithSameObject() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;
 assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
 }

 @Test
 public void testEqualsWithSameValues() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
 }

 @Test
 public void testEqualsWithDifferentObjects() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(JaxRsService.class, "targetNamespace1", "typesNamespace1");
 assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
 }

 @Test
 public void testEqualsWithNullObject() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 assertFalse(webServiceDescriptor.equals(null));
 }

 @Test
 public void testEqualsWithDifferentClassesAndSameValues() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(JaxRsService.class, "targetNamespace1", "typesNamespace1");
 assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
 }

 @Test
 public void testEqualsWithDifferentClassesAndDifferentValues() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(JaxRsService.class, "targetNamespace2", "typesNamespace2");
 assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
 }

 // Test hashCode method

 @Test
 public void testHashCodeWithSameObject() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = webServiceDescriptor1;
 assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
 }

 @Test
 public void testHashCodeWithSameValues() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
 }

 @Test
 public void testHashCodeWithDifferentObjectsAndSameValues() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(JaxRsService.class, "targetNamespace1", "typesNamespace1");
 assertNotSame(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
 }

 @Test
 public void testHashCodeWithDifferentObjectsAndDifferentValues() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(JaxRsService.class, "targetNamespace2", "typesNamespace2");
 assertNotSame(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
 }

}