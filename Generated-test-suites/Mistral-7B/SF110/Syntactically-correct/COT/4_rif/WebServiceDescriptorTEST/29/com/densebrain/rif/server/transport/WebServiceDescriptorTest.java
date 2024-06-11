package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
* WebServiceDescriptorTest.java
*/
public class WebServiceDescriptorTest {

 // Test constructor
 @Test
 public void testWebServiceDescriptorConstructor() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://localhost:8080/ws", "http://localhost:8080/types");
 assertNotNull(webServiceDescriptor);
 assertSame(WebService.class, webServiceDescriptor.getServiceClazz());
 assertEquals("http://localhost:8080/ws", webServiceDescriptor.getTargetNamespace());
 assertEquals("http://localhost:8080/types", webServiceDescriptor.getTypesNamespace());
 }

 // Test getters and setters for serviceClazz
 @Test
 public void testGetServiceClazz() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://localhost:8080/ws", "http://localhost:8080/types");
 assertSame(WebService.class, webServiceDescriptor.getServiceClazz());

 WebServiceAnother webServiceAnother = new WebServiceAnother();
 webServiceDescriptor.setServiceClazz(webServiceAnother.getClass());
 assertSame(WebServiceAnother.class, webServiceDescriptor.getServiceClazz());
 }

 // Test getters and setters for targetNamespace
 @Test
 public void testGetTargetNamespace() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://localhost:8080/ws", "http://localhost:8080/types");
 assertEquals("http://localhost:8080/ws", webServiceDescriptor.getTargetNamespace());

 webServiceDescriptor.setTargetNamespace("http://another.com/ws");
 assertEquals("http://another.com/ws", webServiceDescriptor.getTargetNamespace());
 }

 // Test getters and setters for typesNamespace
 @Test
 public void testGetTypesNamespace() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://localhost:8080/ws", "http://localhost:8080/types");
 assertEquals("http://localhost:8080/types", webServiceDescriptor.getTypesNamespace());

 webServiceDescriptor.setTypesNamespace("http://another.com/types");
 assertEquals("http://another.com/types", webServiceDescriptor.getTypesNamespace());
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "http://localhost:8080/ws", "http://localhost:8080/types");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "http://localhost:8080/ws", "http://localhost:8080/types");
 WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(WebServiceAnother.class, "http://localhost:8080/ws", "http://localhost:8080/types");

 assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor1.hashCode());
 assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
 assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());
 }

 // Test equals method
 @Test
 public void testEquals() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "http://localhost:8080/ws", "http://localhost:8080/types");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "http://localhost:8080/ws", "http://localhost:8080/types");
 WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(WebServiceAnother.class, "http://localhost:8080/ws", "http://localhost:8080/types");

 assertEquals(webServiceDescriptor1, webServiceDescriptor1);
 assertEquals(webServiceDescriptor1, webServiceDescriptor2);
 assertNotEquals(webServiceDescriptor1, webServiceDescriptor3);
 }

 // Test null values
 @Test(expected = NullPointerException.class)
 public void testSetServiceClazzNull() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://localhost:8080/ws", "http://localhost:8080/types");
 webServiceDescriptor.setServiceClazz(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetTargetNamespaceNull() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://localhost:8080/ws", "http://localhost:8080/types");
 webServiceDescriptor.setTargetNamespace(null);
 }

 @Test(expected = NullPointerException.class)
 public void testSetTypesNamespaceNull() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://localhost:8080/ws", "http://localhost:8080/types");
 webServiceDescriptor.setTypesNamespace(null);
 }

 // Test empty strings
 @Test
 public void testSetEmptyStrings() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://localhost:8080/ws", "http://localhost:8080/types");
 webServiceDescriptor.setTargetNamespace("");
 webServiceDescriptor.setTypesNamespace("");
 assertEquals("", webServiceDescriptor.getTargetNamespace());
 assertEquals("", webServiceDescriptor.getTypesNamespace());
 }
}