package com.densebrain.rif.server.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class WebServiceDescriptorTest {

 // Test constructor
 @Test
 public void testWebServiceDescriptorConstructor() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(DummyService.class, "targetNamespace", "typesNamespace");
 assertNotNull(webServiceDescriptor);
 assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());
 assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());
 assertSame(DummyService.class, webServiceDescriptor.getServiceClazz());
 }

 // Test getters and setters for targetNamespace
 @Test
 public void testGetTargetNamespace() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(DummyService.class, "targetNamespace", "typesNamespace");
 assertNotNull(webServiceDescriptor.getTargetNamespace());
 assertEquals("targetNamespace", webServiceDescriptor.getTargetNamespace());

 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(DummyService.class, null, "typesNamespace");
 assertNull(webServiceDescriptor2.getTargetNamespace());

 WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(DummyService.class, "", "typesNamespace");
 assertEquals("", webServiceDescriptor3.getTargetNamespace());

 WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(DummyService.class, "targetNamespace", null);
 assertNull(webServiceDescriptor4.getTargetNamespace());

 WebServiceDescriptor webServiceDescriptor5 = new WebServiceDescriptor(DummyService.class, "targetNamespace", "");
 assertEquals("targetNamespace", webServiceDescriptor5.getTargetNamespace());

 // Test setters for targetNamespace
 WebServiceDescriptor webServiceDescriptor6 = new WebServiceDescriptor(DummyService.class, "oldTargetNamespace", "typesNamespace");
 webServiceDescriptor6.setTargetNamespace("newTargetNamespace");
 assertEquals("newTargetNamespace", webServiceDescriptor6.getTargetNamespace());
 }

 // Test getters and setters for typesNamespace
 @Test
 public void testGetTypesNamespace() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(DummyService.class, "targetNamespace", "typesNamespace");
 assertNotNull(webServiceDescriptor.getTypesNamespace());
 assertEquals("typesNamespace", webServiceDescriptor.getTypesNamespace());

 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(DummyService.class, "targetNamespace", null);
 assertNull(webServiceDescriptor2.getTypesNamespace());

 WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(DummyService.class, "targetNamespace", "");
 assertEquals("", webServiceDescriptor3.getTypesNamespace());

 WebServiceDescriptor webServiceDescriptor4 = new WebServiceDescriptor(DummyService.class, "targetNamespace", "oldTypesNamespace");
 webServiceDescriptor4.setTypesNamespace("newTypesNamespace");
 assertEquals("newTypesNamespace", webServiceDescriptor4.getTypesNamespace());
 }

 // Test hashCode and equals methods
 @Test
 public void testHashCodeAndEquals() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(DummyService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(DummyService.class, "targetNamespace1", "typesNamespace1");
 WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(DummyService.class, "targetNamespace2", "typesNamespace1");

 assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
 assertEquals(webServiceDescriptor1, webServiceDescriptor2);

 assertNotSame(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());
 assertNotEquals(webServiceDescriptor1, webServiceDescriptor3);
 }

 // Test null arguments in constructor
 @Test(expected = NullPointerException.class)
 public void testWebServiceDescriptorConstructorNullServiceClazz() {
 new WebServiceDescriptor(null, "targetNamespace", "typesNamespace");
 }

 // Test empty string arguments in constructor
 @Test
 public void testWebServiceDescriptorConstructorEmptyStringArguments() {
 new WebServiceDescriptor(DummyService.class, "", "");
 }

}