package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebServiceDescriptorTest {

 private WebServiceDescriptor webServiceDescriptor;

 @Test
 public void testWebServiceDescriptorConstructor() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", "http://example.com/types");
 assertEquals("http://example.com/namespace", webServiceDescriptor.getTargetNamespace());
 assertEquals("http://example.com/types", webServiceDescriptor.getTypesNamespace());
 assertEquals(WebService.class, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testSetGetTargetNamespace() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, null, null);
 webServiceDescriptor.setTargetNamespace("http://example.com/namespace");
 assertEquals("http://example.com/namespace", webServiceDescriptor.getTargetNamespace());
 }

 @Test
 public void testSetGetTypesNamespace() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, null, null);
 webServiceDescriptor.setTypesNamespace("http://example.com/types");
 assertEquals("http://example.com/types", webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testSetGetServiceClazz() {
 webServiceDescriptor = new WebServiceDescriptor(null, null, null);
 webServiceDescriptor.setServiceClazz(WebService.class);
 assertEquals(WebService.class, webServiceDescriptor.getServiceClazz());
 }

 @Test(expected = NullPointerException.class)
 public void testNullConstructor() {
 webServiceDescriptor = new WebServiceDescriptor(null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testNullServiceClazz() {
 webServiceDescriptor = new WebServiceDescriptor(null, "http://example.com/namespace", "http://example.com/types");
 }

 @Test(expected = NullPointerException.class)
 public void testNullTargetNamespace() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, null, "http://example.com/types");
 }

 @Test(expected = NullPointerException.class)
 public void testNullTypesNamespace() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", null);
 }

 @Test
 public void testHashCode() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", "http://example.com/types");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", "http://example.com/types");
 WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(WebService.class, "http://example.com/namespace1", "http://example.com/types");
 WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", "http://example.com/types1");

 assertEquals(webServiceDescriptor.hashCode(), webServiceDescriptor1.hashCode());
 assertNotEquals(webServiceDescriptor.hashCode(), webServiceDescriptor2.hashCode());
 assertNotEquals(webServiceDescriptor.hashCode(), webServiceDescriptor3.hashCode());
 assertNotEquals(webServiceDescriptor.hashCode(), webServiceDescriptor4.hashCode());
 }

 @Test
 public void testEquals() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", "http://example.com/types");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", "http://example.com/types");
 WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(WebService.class, "http://example.com/namespace1", "http://example.com/types");
 WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", "http://example.com/types1");

 assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
 assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
 assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
 assertFalse(webServiceDescriptor1.equals(webServiceDescriptor4));
 }

}