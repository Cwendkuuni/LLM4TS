package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class WebServiceDescriptorTest {

 private Class serviceClazz;
 private String targetNamespace;
 private String typesNamespace;
 private WebServiceDescriptor webServiceDescriptor;

 @Test
 public void testWebServiceDescriptorConstructor() {
 serviceClazz = mock(Class.class);
 targetNamespace = "targetNamespace";
 typesNamespace = "typesNamespace";

 webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

 assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
 assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
 assertSame(serviceClazz, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testGetServiceClazz() {
 serviceClazz = mock(Class.class);
 webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);

 assertSame(serviceClazz, webServiceDescriptor.getServiceClazz());
 }

 @Test(expected = NullPointerException.class)
 public void testSetServiceClazzWithNull() {
 webServiceDescriptor = new WebServiceDescriptor(null, null, null);

 webServiceDescriptor.setServiceClazz(null);
 }

 @Test
 public void testSetServiceClazz() {
 Class newServiceClazz = mock(Class.class);
 webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);

 webServiceDescriptor.setServiceClazz(newServiceClazz);

 assertSame(newServiceClazz, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testGetTargetNamespace() {
 targetNamespace = "targetNamespace";
 webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

 assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
 }

 @Test(expected = NullPointerException.class)
 public void testSetTargetNamespaceWithNull() {
 webServiceDescriptor = new WebServiceDescriptor(serviceClazz, null, null);

 webServiceDescriptor.setTargetNamespace(null);
 }

 @Test
 public void testSetTargetNamespace() {
 targetNamespace = "newTargetNamespace";
 webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

 webServiceDescriptor.setTargetNamespace("newerTargetNamespace");

 assertEquals("newerTargetNamespace", webServiceDescriptor.getTargetNamespace());
 }

 @Test
 public void testGetTypesNamespace() {
 typesNamespace = "typesNamespace";
 webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

 assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
 }

 @Test(expected = NullPointerException.class)
 public void testSetTypesNamespaceWithNull() {
 webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, null);

 webServiceDescriptor.setTypesNamespace(null);
 }

 @Test
 public void testSetTypesNamespace() {
 typesNamespace = "newTypesNamespace";
 webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);

 webServiceDescriptor.setTypesNamespace("newerTypesNamespace");

 assertEquals("newerTypesNamespace", webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testHashCode() {
 Class anotherServiceClazz = mock(Class.class);
 webServiceDescriptor = new WebServiceDescriptor(serviceClazz, targetNamespace, typesNamespace);
 WebServiceDescriptor anotherWebServiceDescriptor = new WebServiceDescriptor(anotherServiceClazz, targetNamespace, typesNamespace);

 assertEquals(webServiceDescriptor.hashCode(), anotherWebServiceDescriptor.hashCode());

 webServiceDescriptor.setServiceClazz(anotherServiceClazz);

 assertNotEquals(webServiceDescriptor.hashCode(), anotherWebServiceDescriptor.hashCode());
 }

 @Test
 public void testEquals() {
 Class anotherServiceClazz = mock(Class.class);
 WebServiceDescriptor anotherWebServiceDescriptor = new WebServiceDescriptor(anotherServiceClazz, targetNamespace, typesNamespace);

 assertFalse(webServiceDescriptor.equals(null));
 assertFalse(webServiceDescriptor.equals("anotherObject"));
 assertTrue(webServiceDescriptor.equals(webServiceDescriptor));
 assertFalse(webServiceDescriptor.equals(anotherWebServiceDescriptor));

 webServiceDescriptor.setServiceClazz(anotherServiceClazz);
 assertTrue(webServiceDescriptor.equals(anotherWebServiceDescriptor));
 }

}