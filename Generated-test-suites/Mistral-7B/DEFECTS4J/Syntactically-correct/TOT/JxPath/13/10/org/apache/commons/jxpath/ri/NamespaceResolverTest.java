import org.apache.commons.jxpath.Name;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

 private static final String PREFIX_X = "x";
 private static final String URI_X = "http://example.com/x";
 private static final String PREFIX_Y = "y";
 private static final String URI_Y = "http://example.com/y";

 @Test
 public void testRegisterNamespace() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace(PREFIX_X, URI_X);
 assertEquals(URI_X, resolver.getNamespaceURI(PREFIX_X));
 }

 @Test(expected = IllegalStateException.class)
 public void testRegisterNamespaceOnSealedResolver() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace(PREFIX_X, URI_X);
 resolver.seal();
 resolver.registerNamespace(PREFIX_Y, URI_Y);
 }

 @Test
 public void testGetNamespaceURI() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertNull(resolver.getNamespaceURI(PREFIX_X));
 assertNull(resolver.getNamespaceURI(PREFIX_Y));

 resolver.registerNamespace(PREFIX_X, URI_X);
 assertEquals(URI_X, resolver.getNamespaceURI(PREFIX_X));
 assertNull(resolver.getNamespaceURI(PREFIX_Y));

 resolver.setNamespaceContextPointer(createNodePointerWithNamespace(URI_X, PREFIX_X));
 assertEquals(URI_X, resolver.getNamespaceURI(PREFIX_X));
 assertNull(resolver.getNamespaceURI(PREFIX_Y));

 resolver.registerNamespace(PREFIX_Y, URI_Y);
 assertEquals(URI_X, resolver.getNamespaceURI(PREFIX_X));
 assertEquals(URI_Y, resolver.getNamespaceURI(PREFIX_Y));
 }

 @Test
 public void testGetPrefix() {
 NamespaceResolver resolver = new NamespaceResolver();
 assertNull(resolver.getPrefix(URI_X));
 assertNull(resolver.getPrefix(URI_Y));

 resolver.registerNamespace(PREFIX_X, URI_X);
 assertNull(resolver.getPrefix(URI_X));
 assertNull(resolver.getPrefix(URI_Y));

 resolver.setNamespaceContextPointer(createNodePointerWithNamespace(URI_X, PREFIX_X));
 assertEquals(PREFIX_X, resolver.getPrefix(URI_X));
 assertNull(resolver.getPrefix(URI_Y));

 resolver.registerNamespace(PREFIX_Y, URI_Y);
 assertEquals(PREFIX_X, resolver.getPrefix(URI_X));
 assertEquals(PREFIX_Y, resolver.getPrefix(URI_Y));
 }

 @Test
 public void testSeal() {
 NamespaceResolver resolver = new NamespaceResolver();
 resolver.registerNamespace(PREFIX_X, URI_X);
 resolver.registerNamespace(PREFIX_Y, URI_Y);
 resolver.seal();

 try {
 resolver.registerNamespace(PREFIX_Z, URI_Z);
 fail("Expected an IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testClone() {
 NamespaceResolver original = new NamespaceResolver();
 original.registerNamespace(PREFIX_X, URI_X);
 original.registerNamespace(PREFIX_Y, URI_Y);
 original.seal();

 NamespaceResolver cloned = (NamespaceResolver) original.clone();
 assertEquals(original.getNamespaceURI(PREFIX_X), cloned.getNamespaceURI(PREFIX_X));
 assertEquals(original.getNamespaceURI(PREFIX_Y), cloned.getNamespaceURI(PREFIX_Y));
 assertTrue(cloned.isSealed());
 }

 private NodePointer createNodePointerWithNamespace(String namespaceURI, String prefix) {
 Name namespaceName = new Name("{http://" + namespaceURI + "/}" + prefix);
 return new NodePointer(new Pointer(), namespaceName, null);
 }

}