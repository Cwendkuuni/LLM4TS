import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 private NamespaceResolver resolver;

 @Before
 public void setUp() {
 resolver = new NamespaceResolver();
 }

 /**
 * Test registering a namespace with a prefix.
 */
 @Test
 public void testRegisterNamespace() {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
 }

 /**
 * Test getting a namespace URI with an undefined prefix.
 */
 @Test
 public void testGetNamespaceURIUndefinedPrefix() {
 assertNull(resolver.getNamespaceURI("unknown"));
 }

 /**
 * Test getting a namespace URI with a defined prefix.
 */
 @Test
 public void testGetNamespaceURIDefinedPrefix() {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
 }

 /**
 * Test getting a namespace URI from a NodePointer.
 */
 @Test
 public void testGetNamespaceURIFromNodePointer() {
 NodePointer pointer = new NodePointer("http://example.com", "root");
 resolver.setNamespaceContextPointer(pointer);
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("http://www.w3.org/2001/XMLSchema", resolver.getNamespaceURI("xsd"));
 }

 /**
 * Test getting a namespace prefix for a URI.
 */
 @Test
 public void testGetPrefix() {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 /**
 * Test getting a namespace prefix for an undefined URI.
 */
 @Test
 public void testGetPrefixUndefinedURI() {
 assertNull(resolver.getPrefix("http://example.com"));
 }

 /**
 * Test getting a namespace prefix for a URI with a defined prefix.
 */
 @Test
 public void testGetPrefixDefinedURI() {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("xsd", resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 /**
 * Test sealing the NamespaceResolver.
 */
 @Test
 public void testSeal() {
 resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 resolver.seal();
 try {
 resolver.registerNamespace("foobar", "http://example.com");
 fail("Expected an IllegalStateException when registering namespaces on a sealed NamespaceResolver");
 } catch (IllegalStateException expected) {
 // Expected
 }
 }
}