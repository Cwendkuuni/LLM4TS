package com.densebrain.rif.server.transport;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

 private WebServiceDescriptor webServiceDescriptor;
 private Class mockServiceClazz;

 @Before
 public void setUp() {
 mockServiceClazz = Mockito.mock(Class.class);
 webServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, "targetNamespace", "typesNamespace");
 }

 @Test
 public void testWebServiceDescriptorInstance() {
 assertNotNull(webServiceDescriptor);
 }

 @Test
 public void testSetAndGetTargetNamespace() {
 String targetNamespace = "newTargetNamespace";
 webServiceDescriptor.setTargetNamespace(targetNamespace);
 assertEquals(targetNamespace, webServiceDescriptor.getTargetNamespace());
 }

 @Test
 public void testSetAndGetTypesNamespace() {
 String typesNamespace = "newTypesNamespace";
 webServiceDescriptor.setTypesNamespace(typesNamespace);
 assertEquals(typesNamespace, webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testSetAndGetServiceClazz() {
 Class expectedServiceClazz = Class.forName("com.example.ExpectedServiceClass");
 webServiceDescriptor.setServiceClazz(expectedServiceClazz);
 assertEquals(expectedServiceClazz, webServiceDescriptor.getServiceClazz());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTargetNamespaceWithNullValue() {
 webServiceDescriptor.setTargetNamespace(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetTypesNamespaceWithNullValue() {
 webServiceDescriptor.setTypesNamespace(null);
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testHashcode() {
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebServiceDescriptor.class, "targetNamespace", "typesNamespace");
 assertEquals(webServiceDescriptor.hashCode(), webServiceDescriptor2.hashCode());
 webServiceDescriptor.setServiceClazz(mockServiceClazz);
 webServiceDescriptor2.setServiceClazz(mockServiceClazz);
 assertEquals(webServiceDescriptor.hashCode(), webServiceDescriptor2.hashCode());
 webServiceDescriptor.setServiceClazz(WebServiceDescriptor.class);
 assertNotEquals(webServiceDescriptor.hashCode(), webServiceDescriptor2.hashCode());
 }

 @Test
 public void testEquals() {
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebServiceDescriptor.class, "targetNamespace", "typesNamespace");
 assertTrue(webServiceDescriptor.equals(webServiceDescriptor2));
 webServiceDescriptor2.setServiceClazz(mockServiceClazz);
 assertFalse(webServiceDescriptor.equals(webServiceDescriptor2));
 webServiceDescriptor2.setServiceClazz(mockServiceClazz);
 assertTrue(webServiceDescriptor.equals(webServiceDescriptor2));
 }

 @Test
 public void testEqualsWithNullObject() {
 assertFalse(webServiceDescriptor.equals(null));
 }

 @Test
 public void testEqualsWithDifferentClass() {
 Object differentObject = new Object();
 assertFalse(webServiceDescriptor.equals(differentObject));
 }
}