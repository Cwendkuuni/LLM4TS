package com.densebrain.rif.server.transport;

import org.junit. Assert;
import org.junit.Test;

public class WebServiceDescriptorTest {

 @Test
 public void testWebServiceDescriptorWithValidArguments() {
 WebServiceDescriptor descriptor = new WebServiceDescriptor(JAXWSService.class, "http://example.com/ns/target", "http://example.com/ns/types");
 Assert.assertEquals("http://example.com/ns/target", descriptor.getTargetNamespace());
 Assert.assertEquals("http://example.com/ns/types", descriptor.getTypesNamespace());
 Assert.assertEquals(JAXWSService.class, descriptor.getServiceClazz());
 }

 @Test
 public void testWebServiceDescriptorWithNullArguments() {
 Assert.assertNull(new WebServiceDescriptor(null, null, null));
 }

 @Test
 public void testWebServiceDescriptorWithEmptyStrings() {
 WebServiceDescriptor descriptor = new WebServiceDescriptor(JAXWSService.class, "", "");
 Assert.assertEquals("", descriptor.getTargetNamespace());
 Assert.assertEquals("", descriptor.getTypesNamespace());
 Assert.assertEquals(JAXWSService.class, descriptor.getServiceClazz());
 }

 @Test
 public void testWebServiceDescriptorWithNullServiceClass() {
 Assert.assertNull(new WebServiceDescriptor(null, "http://example.com/ns/target", "http://example.com/ns/types"));
 }

 @Test
 public void testWebServiceDescriptorHashCode() {
 WebServiceDescriptor descriptor1 = new WebServiceDescriptor(JAXWSService.class, "http://example.com/ns/target", "http://example.com/ns/types");
 WebServiceDescriptor descriptor2 = new WebServiceDescriptor(JAXWSService.class, "http://example.com/ns/target", "http://example.com/ns/types");
 WebServiceDescriptor descriptor3 = new WebServiceDescriptor(JAXWSService2.class, "http://example.com/ns/target", "http://example.com/ns/types");
 Assert.assertEquals(descriptor1.hashCode(), descriptor2.hashCode());
 Assert.neq(descriptor1.hashCode(), descriptor3.hashCode());
 }

 @Test
 public void testWebServiceDescriptorEquals() {
 WebServiceDescriptor descriptor1 = new WebServiceDescriptor(JAXWSService.class, "http://example.com/ns/target", "http://example.com/ns/types");
 WebServiceDescriptor descriptor2 = new WebServiceDescriptor(JAXWSService.class, "http://example.com/ns/target", "http://example.com/ns/types");
 WebServiceDescriptor descriptor3 = new WebServiceDescriptor(JAXWSService2.class, "http://example.com/ns/target", "http://example.com/ns/types");
 Assert.assertTrue(descriptor1.equals(descriptor2));
 Assert.assertFalse(descriptor1.equals(descriptor3));
 }
}