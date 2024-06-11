import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 @Test
 public void testRegisterNamespace() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 resolver.seal();
 resolver.registerNamespace("y", "http://example.com/y");
 }

 @Test
 public void testGetNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 assertEquals(null, resolver.getNamespaceURI("y"));
 }

 @Test
 public void testGetNamespaceURIWithContext() {
 NamespaceResolver resolver = new NamespaceResolver();
 NodePointer context = new NodePointer();
 resolver.setNamespaceContextPointer(context);
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("http://example.com/x", resolver.getNamespaceURI("x"));
 }

 @Test
 public void testGetNamespaceURIWithParent() {
 NamespaceResolver parent = new NamespaceResolver();
 parent.registerNamespace("x", "http://example.com/x");
 NamespaceResolver child = new NamespaceResolver(parent);
 assertEquals("http://example.com/x", child.getNamespaceURI("x"));
 }

 @Test
 public void testGetPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 assertEquals(null, resolver.getPrefix("http://example.com/y"));
 }

 @Test
 public void testGetPrefixWithContext() {
 NodePointer context = new NodePointer();
 NodeIterator ni = context.namespaceIterator();
 ni.addNamespace("x", "http://example.com/x");
 NiTestCase.assertNextNamespaceIs("x", "http://example.com/x", ni);
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.setNamespaceContextPointer(context);
 assertEquals("x", resolver.getPrefix("http://example.com/x"));
 }

 @Test
 public void testGetPrefixWithParent() {
 NamespaceResolver parent = new NamespaceResolver();
 parent.registerNamespace("x", "http://example.com/x");
 NamespaceResolver child = new NamespaceResolver(parent);
 assertEquals("x", child.getPrefix("http://example.com/x"));
 }

 @Test
 public void testSeal() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace("x", "http://example.com/x");
 resolver.seal();
 assertFalse(resolver.isSealed());
 resolver.seal();
 assertTrue(resolver.isSealed());
 }

}