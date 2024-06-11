package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class WebServiceDescriptorTest {

 private Class serviceClazz;
 private String targetNamespace;
 private String typesNamespace;
 private WebServiceDescriptor webServiceDescriptor;

 @Test
 public void testWebServiceDescriptorConstruction() {
 serviceClazz = Mockito.mock(Class.class);
 targetNamespace = "http://test.com/";
 typesNamespace = "http://test-types.com/";
 webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

 assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
 assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
 assertSame(serviceClazz, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testSettersGetters() {
 webServiceDescriptor = new WebServiceDescriptor(null, null, null);

 webServiceDescriptor.setServiceClazz(serviceClazz);
 webServiceDescriptor.setTargetNamespace(targetNamespace);
 webServiceDescriptor.setTypesNamespace(typesNamespace);

 assertEquals(serviceClazz, webServiceDescriptor.getServiceClazz());
 assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
 assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testHashCode() {
 Class otherServiceClazz = Mockito.mock(Class.class);
 WebServiceDescriptor other = new WebServiceDescriptor(otherServiceClazz, targetNamespace, typesNamespace);

 assertEquals(webServiceDescriptor.hashCode(), other.hashCode());

 webServiceDescriptor.setServiceClazz(serviceClazz);
 other.setServiceClazz(otherServiceClazz);

 assertNotEquals(webServiceDescriptor.hashCode(), other.hashCode());
 }

 @Test
 public void testEquals() {
 Class otherServiceClazz = Mockito.mock(Class.class);
 WebServiceDescriptor other = new WebServiceDescriptor(otherServiceClazz, targetNamespace, typesNamespace);

 assertFalse(webServiceDescriptor.equals(null));
 assertFalse(webServiceDescriptor.equals(other));

 webServiceDescriptor.setServiceClazz(serviceClazz);
 other.setServiceClazz(serviceClazz);

 assertTrue(webServiceDescriptor.equals(other));

 other.setServiceClazz(otherServiceClazz);

 assertFalse(webServiceDescriptor.equals(other));
 }

 @Test
 public void testNullValues() {
 webServiceDescriptor = new WebServiceDescriptor(null, null, null);

 assertNull(webServiceDescriptor.getServiceClazz());
 assertNull(webServiceDescriptor.getTargetNamespace());
 assertNull(webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testEmptyStrings() {
 webServiceDescriptor = new WebServiceDescriptor(serviceClazz, "", "");

 assertEquals("", webServiceDescriptor.getTargetNamespace());
 assertEquals("", webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testEmptyClass() {
 webServiceDescriptor = new WebServiceDescriptor(null, targetNamespace, typesNamespace);

 assertNull(webServiceDescriptor.getServiceClazz());
 }
}