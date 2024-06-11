package com.densebrain.rif.server.transport;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebServiceDescriptorTest {

 private WebServiceDescriptor webServiceDescriptor;

 @Test
 public void testWebServiceDescriptorConstructor() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://example.com/ns1", "http://example.com/ns2");
 assertEquals("http://example.com/ns1", webServiceDescriptor.getTargetNamespace());
 assertEquals("http://example.com/ns2", webServiceDescriptor.getTypesNamespace());
 assertEquals(WebService.class, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testSetAndGetTargetNamespace() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, null, null);
 webServiceDescriptor.setTargetNamespace("http://example.com/ns1");
 assertEquals("http://example.com/ns1", webServiceDescriptor.getTargetNamespace());
 }

 @Test
 public void testSetAndGetTypesNamespace() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, null, null);
 webServiceDescriptor.setTypesNamespace("http://example.com/ns2");
 assertEquals("http://example.com/ns2", webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testSetAndGetServiceClazz() {
 webServiceDescriptor = new WebServiceDescriptor(null, null, null);
 webServiceDescriptor.setServiceClazz(WebService.class);
 assertEquals(WebService.class, webServiceDescriptor.getServiceClazz());
 }

 @Test(expected = NullPointerException.class)
 public void testNullServiceClazz() {
 webServiceDescriptor = new WebServiceDescriptor(null, null, null);
 webServiceDescriptor.setServiceClazz(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTargetNamespace() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, null, null);
 webServiceDescriptor.setTargetNamespace(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullTypesNamespace() {
 webServiceDescriptor = new WebServiceDescriptor(WebService.class, null, null);
 webServiceDescriptor.setTypesNamespace(null);
 }

 @Test
 public void testHashCode() {
 WebServiceDescriptor descriptor1 = new WebServiceDescriptor(WebService.class, "http://example.com/ns1", "http://example.com/ns2");
 WebServiceDescriptor descriptor2 = new WebServiceDescriptor(WebService.class, "http://example.com/ns1", "http://example.com/ns2");
 WebServiceDescriptor descriptor3 = new WebServiceDescriptor(WebService.class, "http://example.com/ns1", "http://example.com/ns3");
 WebServiceDescriptor descriptor4 = new WebServiceDescriptor(WebService.class, "http://example.com/ns2", "http://example.com/ns2");

 assertEquals(descriptor1.hashCode(), descriptor1.hashCode());
 assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
 assertNotEquals(descriptor1.hashCode(), descriptor3.hashCode());
 assertNotEquals(descriptor1.hashCode(), descriptor4.hashCode());
 }

 @Test
 public void testEquals() {
 WebServiceDescriptor descriptor1 = new WebServiceDescriptor(WebService.class, "http://example.com/ns1", "http://example.com/ns2");
 WebServiceDescriptor descriptor2 = new WebServiceDescriptor(WebService.class, "http://example.com/ns1", "http://example.com/ns2");
 WebServiceDescriptor descriptor3 = new WebServiceDescriptor(WebService.class, "http://example.com/ns1", "http://example.com/ns3");
 WebServiceDescriptor descriptor4 = new WebServiceDescriptor(WebService.class, "http://example.com/ns2", "http://example.com/ns2");
 WebServiceDescriptor descriptor5 = new WebServiceDescriptor(WebService.class, "http://example.com/ns1", "http://example.com/ns2");

 assertTrue(descriptor1.equals(descriptor1));
 assertTrue(descriptor1.equals(descriptor2));
 assertFalse(descriptor1.equals(descriptor3));
 assertFalse(descriptor1.equals(descriptor4));
 assertFalse(descriptor1.equals(descriptor5));
 }

}