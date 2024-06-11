package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebServiceDescriptorTest {

 private WebServiceDescriptor webServiceDescriptor;

 @Before
 public void setUp() {
 this.webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", "http://example.com/types");
 }

 @Test
 public void testConstructorWithValidArguments() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", "http://example.com/types");
 assertNotNull(webServiceDescriptor.getServiceClazz());
 assertNotNull(webServiceDescriptor.getTargetNamespace());
 assertNotNull(webServiceDescriptor.getTypesNamespace());
 }

 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullServiceClazz() {
 webServiceDescriptor = new WebServiceDescriptor(null, "http://example.com/namespace", "http://example.com/types");
 }

 @Test
 public void testGetServiceClazz() {
 Class serviceClazz = webServiceServiceDescriptor.getServiceClazz();
 assertEquals(WebService.class, serviceClazz);
 }

 @Test
 public void testSetServiceClazz() {
 webServiceDescriptor.setServiceClazz(JaxWsService.class);
 assertEquals(JaxWsService.class, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testGetTargetNamespace() {
 String targetNamespace = webServiceDescriptor.getTargetNamespace();
 assertEquals("http://example.com/namespace", targetNamespace);
 }

 @Test
 public void testSetTargetNamespace() {
 webServiceDescriptor.setTargetNamespace("http://new-example.com/namespace");
 assertEquals("http://new-example.com/namespace", webServiceDescriptor.getTargetNamespace());
 }

 @Test
 public void testGetTypesNamespace() {
 String typesNamespace = webServiceDescriptor.getTypesNamespace();
 assertEquals("http://example.com/types", typesNamespace);
 }

 @Test
 public void testSetTypesNamespace() {
 webServiceDescriptor.setTypesNamespace("http://new-example.com/types");
 assertEquals("http://new-example.com/types", webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testHashCode() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", "http://example.com/types");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", "http://example.com/types");
 WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(JaxWsService.class, "http://example.com/namespace", "http://example.com/types");
 assertEquals(webServiceDescriptor.hashCode(), webServiceDescriptor1.hashCode());
 assertNotEquals(webServiceDescriptor.hashCode(), webServiceDescriptor2.hashCode());
 assertNotEquals(webServiceDescriptor.hashCode(), webServiceDescriptor3.hashCode());
 }

 @Test
 public void testEquals() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", "http://example.com/types");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "http://example.com/namespace", "http://example.com/types");
 WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(JaxWsService.class, "http://example.com/namespace", "http://example.com/types");
 assertTrue(webServiceDescriptor.equals(webServiceDescriptor1));
 assertTrue(webServiceDescriptor1.equals(webServiceDescriptor));
 assertFalse(webServiceDescriptor.equals(webServiceDescriptor2));
 assertFalse(webServiceDescriptor.equals(webServiceDescriptor3));
 }

}