import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for {@link NamespaceResolver}.
 */
public class NamespaceResolverTest {

 @Test
 public void testNamespaceRegistration() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 String uri = resolver.getNamespaceURI("x");
 String prefix = resolver.getPrefix("http://example.com/x");
 Assert.assertEquals("http://example.com/x", uri);
 Assert.assertEquals("x", prefix);
 }

 @Test
 public void testGetNamespaceURIWithRegisteredPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 String uri = resolver.getNamespaceURI("x");
 Assert.assertEquals("http://example.com/x", uri);
 }

 @Test
 public void testGetNamespaceURIWithUnregisteredPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 String uri = resolver.getNamespaceURI("y");
 Assert.assertNull(uri);
 }

 @Test
 public void testGetPrefixWithRegisteredNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 String prefix = resolver.getPrefix("http://example.com/x");
 Assert.assertEquals("x", prefix);
 }

 @Test
 public void testGetPrefixWithUnregisteredNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 String prefix = resolver.getPrefix("http://example.com/y");
 Assert.assertNull(prefix);
 }

 @Test
 public void testSetNamespaceContextPointer() {
 NamespaceResolver resolver = new NamespaceResolver();
 NodePointer pointer = new NodePointer("root");
 resolver.setNamespaceContextPointer(pointer);
 String uri = resolver.getNamespaceURI("x");
 if (uri == null) {
 resolver.registerNamespace("x", "http://example.com/x");
 }
 String prefix = resolver.getPrefix("http://example.com/x");
 Assert.assertEquals("x", prefix);
 }

 @Test
 public void testSeal() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 resolver.seal();
 Assert.assertTrue(resolver.isSealed());
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceAfterSeal() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 resolver.seal();
 resolver.registerNamespace("y", "http://example.com/y");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace(null, "http://example.com/x");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", null);
 }
}