package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class WebServiceDescriptorTest {

 private Class mockServiceClazz;
 private String mockTargetNamespace;
 private String mockTypesNamespace;
 private WebServiceDescriptor webServiceDescriptor;

 @Before
 public void setUp() {
 mockServiceClazz = Mockito.mock(Class.class);
 mockTargetNamespace = "testNamespace";
 mockTypesNamespace = "testTypesNamespace";
 webServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, mockTargetNamespace, mockTypesNamespace);
 }

 @Test
 public void testWebServiceDescriptorConstructor() {
 // Basic functionality test
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(mockServiceClazz, mockTargetNamespace, mockTypesNamespace);
 assertNotNull(webServiceDescriptor);
 }

 @Test(expected = NullPointerException.class)
 public void testWebServiceDescriptorConstructorWithNullServiceClazz() {
 // Edge case test for null serviceClazz
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(null, mockTargetNamespace, mockTypesNamespace);
 }

 @Test
 public void testGetServiceClazz() {
 // Basic functionality test
 assertEquals(mockServiceClazz, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testSetServiceClazz() {
 // Basic functionality test
 webServiceDescriptor.setServiceClazz(new Class("testClass"));
 assertEquals(new Class("testClass"), webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testGetTargetNamespace() {
 // Basic functionality test
 assertEquals(mockTargetNamespace, webServiceDescriptor.getTargetNamespace());
 }

 @Test
 public void testSetTargetNamespace() {
 // Basic functionality test
 webServiceDescriptor.setTargetNamespace("newNamespace");
 assertEquals("newNamespace", webServiceDescriptor.getTargetNamespace());
 }

 @Test
 public void testGetTypesNamespace() {
 // Basic functionality test
 assertEquals(mockTypesNamespace, webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testSetTypesNamespace() {
 // Basic functionality test
 webServiceDescriptor.setTypesNamespace("newTypesNamespace");
 assertEquals("newTypesNamespace", webServiceDescriptor.getTypesNamespace());
 }

 @Test
 public void testHashCode() {
 // Basic functionality test
 int hashCode = webServiceDescriptor.hashCode();
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz, mockTargetNamespace, mockTypesNamespace);
 int hashCode2 = webServiceDescriptor2.hashCode();
 assertNotSame(hashCode, hashCode2);
 }

 @Test
 public void testEquals() {
 // Basic functionality test
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz, mockTargetNamespace, mockTypesNamespace);
 assertTrue(webServiceDescriptor.equals(webServiceDescriptor2));
 }

 @Test
 public void testEqualsWithDifferentClass() {
 // Edge case test for different classes
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(new Class("testClass2"), mockTargetNamespace, mockTypesNamespace);
 assertFalse(webServiceDescriptor.equals(webServiceDescriptor2));
 }

 @Test
 public void testEqualsWithNull() {
 // Edge case test for null object
 assertFalse(webServiceDescriptor.equals(null));
 }

 @Test
 public void testEqualsWithDifferentTargetNamespace() {
 // Edge case test for different targetNamespaces
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz, "differentTargetNamespace", mockTypesNamespace);
 assertFalse(webServiceDescriptor.equals(webServiceDescriptor2));
 }

 @Test
 public void testEqualsWithDifferentTypesNamespace() {
 // Edge case test for different typesNamespaces
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz, mockTargetNamespace, "differentTypesNamespace");
 assertFalse(webServiceDescriptor.equals(webServiceDescriptor2));
 }

 @Test
 public void testEqualsWithSameServiceClazzButDifferentHashCode() {
 // Edge case test for same serviceClazz but different hashcodes
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(mockServiceClazz, mockTargetNamespace, mockTypesNamespace);
 webServiceDescriptor2.hashCode(); // force different hashcode
 assertFalse(webServiceDescriptor.equals(webServiceDescriptor2));
 }

}