import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 private NamespaceResolver namespaceResolver;
 private NamespaceResolver parentNamespaceResolver;
 private NodePointer nodePointer;

 @Before
 public void setUp() {
 namespaceResolver = new NamespaceResolver();
 parentNamespaceResolver = new NamespaceResolver(namespaceResolver);
 nodePointer = new NodePointer();
 }

 @Test
 public void testEmptyResolver() {
 NamespaceResolver emptyResolver = new NamespaceResolver();
 assertEquals(0, emptyResolver.getNamespaces().length);
 }

 @Test
 public void testRegisterNamespace() {
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
 assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 }

 @Test
 public void testRegisterNamespaceWithParent() {
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 parentNamespaceResolver.registerNamespace("foo", "http://example.com");
 assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
 assertEquals("http://example.com", namespaceResolver.getNamespaceURI("foo"));
 assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 assertEquals("foo", namespaceResolver.getPrefix("http://example.com"));
 }

 @Test
 public void testGetNamespaceURIWithParent() {
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 parentNamespaceResolver.registerNamespace("foo", "http://example.com");
 nodePointer.setNamespaceURI("http://www.w3.org/2001/XMLSchema", "xsd");
 namespaceResolver.setNamespaceContextPointer(nodePointer);
 assertEquals("http://www.w3.org/2001/XMLSchema", namespaceResolver.getNamespaceURI("xsd"));
 assertEquals("http://example.com", namespaceResolver.getNamespaceURI("foo"));
 }

 @Test
 public void testGetPrefixWithParent() {
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 parentNamespaceResolver.registerNamespace("foo", "http://example.com");
 nodePointer.setNamespaceURI("http://www.w3.org/2001/XMLSchema", "xsd");
 namespaceResolver.setNamespaceContextPointer(nodePointer);
 assertEquals("xsd", namespaceResolver.getPrefix("http://www.w3.org/2001/XMLSchema"));
 assertEquals("foo", namespaceResolver.getPrefix("http://example.com"));
 }

 @Test
 public void testSeal() {
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 namespaceResolver.seal();
 assertEquals(true, namespaceResolver.isSealed());
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceAfterSealed() {
 namespaceResolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
 namespaceResolver.seal();
 namespaceResolver.registerNamespace("foo", "http://example.com");
 }

 @Test
 public void testClone() {
 NamespaceResolver clonedResolver = (NamespaceResolver) namespaceResolver.clone();
 assertEquals(namespaceResolver.getNamespaces().length, clonedResolver.getNamespaces().length);
 }
}