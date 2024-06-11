package com.densebrain.rif.server.transport;

import org.junit.Test;
import static org.junit.Assert.*;

public class WebServiceDescriptorTest {

 private WebServiceDescriptor webServiceDescriptor;
 private static final Class<SomeService> SOME_SERVICE_CLASS = SomeService.class;
 private static final String TARGET_NAMESPACE = "http://example.com/some_service";
 private static final String TYPES_NAMESPACE = "http://example.com/types";

 @Test
 public void testWebServiceDescriptorCreation() {
 webServiceDescriptor = new WebServiceDescriptor(SOME_SERVICE_CLASS, TARGET_NAMESPACE, TYPES_NAMESPACE);
 assertNotNull(webServiceDescriptor);
 assertEquals(TARGET_NAMESPACE, webServiceDescriptor.getTargetNamespace());
 assertEquals(TYPES_NAMESPACE, webServiceDescriptor.getTypesNamespace());
 assertEquals(SOME_SERVICE_CLASS, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testWebServiceDescriptorSetters() {
 webServiceDescriptor = new WebServiceDescriptor(null, null, null);
 webServiceDescriptor.setServiceClazz(SOME_SERVICE_CLASS);
 webServiceDescriptor.setTargetNamespace(TARGET_NAMESPACE);
 webServiceDescriptor.setTypesNamespace(TYPES_NAMESPACE);
 assertEquals(TARGET_NAMESPACE, webServiceDescriptor.getTargetNamespace());
 assertEquals(TYPES_NAMESPACE, webServiceDescriptor.getTypesNamespace());
 assertEquals(SOME_SERVICE_CLASS, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testWebServiceDescriptorHashcode() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(SOME_SERVICE_CLASS, TARGET_NAMESPACE, TYPES_NAMESPACE);
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(SOME_SERVICE_CLASS, TARGET_NAMESPACE, TYPES_NAMESPACE);
 assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
 webServiceDescriptor2.setServiceClazz(AnotherService.class);
 assertNotSame(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
 }

 @Test
 public void testWebServiceDescriptorEquals() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(SOME_SERVICE_CLASS, TARGET_NAMESPACE, TYPES_NAMESPACE);
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(SOME_SERVICE_CLASS, TARGET_NAMESPACE, TYPES_NAMESPACE);
 assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
 webServiceDescriptor2.setServiceClazz(AnotherService.class);
 assertFalse(webServiceDescriptor1.equals(webServiceDescriptor2));
 }
}