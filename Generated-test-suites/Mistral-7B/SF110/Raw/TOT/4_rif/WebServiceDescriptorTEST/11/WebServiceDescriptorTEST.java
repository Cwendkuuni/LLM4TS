import org.junit.Test;
import static org.junit.Assert.*;

import com.densebrain.rif.server.transport.WebServiceDescriptor;

public class WebServiceDescriptorTest {

 @Test
 public void testWebServiceDescriptorConstructor() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, "http://example.com/targetNamespace", "http://example.com/typesNamespace");
 assertEquals("http://example.com/targetNamespace", webServiceDescriptor.getTargetNamespace());
 assertEquals("http://example.com/typesNamespace", webServiceDescriptor.getTypesNamespace());
 assertEquals(WebService.class, webServiceDescriptor.getServiceClazz());
 }

 @Test
 public void testWebServiceDescriptorSettersAndGetters() {
 WebServiceDescriptor webServiceDescriptor = new WebServiceDescriptor(WebService.class, null, null);
 webServiceDescriptor.setTargetNamespace("http://example.com/targetNamespace");
 webServiceDescriptor.setTypesNamespace("http://example.com/typesNamespace");
 webServiceDescriptor.setServiceClazz(WebServiceImpl.class);
 assertEquals("http://example.com/targetNamespace", webServiceDescriptor.getTargetNamespace());
 assertEquals("http://example.com/typesNamespace", webServiceDescriptor.getTypesNamespace());
 assertEquals(WebServiceImpl.class, webServiceDescriptor.getServiceClazz());
 }

 @Test(expected = NullPointerException.class)
 public void testWebServiceDescriptorNullConstructor() {
 new WebServiceDescriptor(null, null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testWebServiceDescriptorNullServiceClazz() {
 new WebServiceDescriptor(null, "http://example.com/targetNamespace", "http://example.com/typesNamespace");
 }

 @Test(expected = NullPointerException.class)
 public void testWebServiceDescriptorNullTargetNamespace() {
 new WebServiceDescriptor(WebService.class, null, "http://example.com/typesNamespace");
 }

 @Test(expected = NullPointerException.class)
 public void testWebServiceDescriptorNullTypesNamespace() {
 new WebServiceDescriptor(WebService.class, "http://example.com/targetNamespace", null);
 }

 @Test
 public void testWebServiceDescriptorHashCode() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "http://example.com/targetNamespace1", "http://example.com/typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "http://example.com/targetNamespace1", "http://example.com/typesNamespace2");
 WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(WebService.class, "http://example.com/targetNamespace2", "http://example.com/typesNamespace1");
 assertEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor1.hashCode());
 assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor2.hashCode());
 assertNotEquals(webServiceDescriptor1.hashCode(), webServiceDescriptor3.hashCode());
 }

 @Test
 public void testWebServiceDescriptorEquals() {
 WebServiceDescriptor webServiceDescriptor1 = new WebServiceDescriptor(WebService.class, "http://example.com/targetNamespace1", "http://example.com/typesNamespace1");
 WebServiceDescriptor webServiceDescriptor2 = new WebServiceDescriptor(WebService.class, "http://example.com/targetNamespace1", "http://example.com/typesNamespace1");
 WebServiceDescriptor webServiceDescriptor3 = new WebServiceDescriptor(WebService.class, "http://example.com/targetNamespace2", "http://example.com/typesNamespace1");
 assertTrue(webServiceDescriptor1.equals(webServiceDescriptor1));
 assertTrue(webServiceDescriptor1.equals(webServiceDescriptor2));
 assertFalse(webServiceDescriptor1.equals(webServiceDescriptor3));
 }
}