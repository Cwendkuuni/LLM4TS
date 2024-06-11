import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.JXPathContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for {@link NamespaceResolver} class.
 */
public class NamespaceResolverTest {

 private NamespaceResolver namespaceResolver;
 private NamespaceResolver sealedNamespaceResolver;
 private Map<String, String> namespaceMap;
 private JXPathContext context;

 @Before
 public void setUp() {
 namespaceResolver = new NamespaceResolver();
 sealedNamespaceResolver = new NamespaceResolver(namespaceResolver);
 sealedNamespaceResolver.seal();
 namespaceMap = new HashMap<>();
 context = new JXPathContext(new NodeIterator(new NodePointer("", "", null)));
 }

 /**
 * Test getNamespaceURI method with registered prefix.
 */
 @Test
 public void testGetNamespaceURIRegisteredPrefix() {
 // Given
 namespaceResolver.registerNamespace("x", "http://example.com/x");
 String prefix = "x";
 String expectedNamespaceURI = "http://example.com/x";

 // When
 String actualNamespaceURI = namespaceResolver.getNamespaceURI(prefix);

 // Then
 Assert.assertEquals("Unexpected namespace URI for prefix 'x'", expectedNamespaceURI, actualNamespaceURI);
 }

 /**
 * Test getNamespaceURI method with unregistered prefix.
 */
 @Test
 public void testGetNamespaceURIUnregisteredPrefix() {
 // Given
 String prefix = "x";
 String expectedNamespaceURI = null;

 // When
 String actualNamespaceURI = namespaceResolver.getNamespaceURI(prefix);

 // Then
 Assert.assertNull("Unexpected namespace URI for unregistered prefix 'x'", actualNamespaceURI);
 }

 /**
 * Test getNamespaceURI method with pointer's namespace.
 */
 @Test
 public void testGetNamespaceURIWithPointer() {
 // Given
 NodePointer pointer = new NodePointer("", "http://example.com", null);
 context.setNodePointer(pointer);
 namespaceResolver.setNamespaceContextPointer(context.getNodePointer());
 namespaceResolver.registerNamespace("x", "http://example.com/x");
 String prefix = "x";
 String expectedNamespaceURI = "http://example.com/x";

 // When
 String actualNamespaceURI = namespaceResolver.getNamespaceURI(prefix);

 // Then
 Assert.assertEquals("Unexpected namespace URI for pointer's namespace 'x'", expectedNamespaceURI, actualNamespaceURI);
 }

 /**
 * Test getNamespaceURI method with parent's namespace.
 */
 @Test
 public void testGetNamespaceURIWithParent() {
 // Given
 NamespaceResolver parentNamespaceResolver = new NamespaceResolver();
 parentNamespaceResolver.registerNamespace("x", "http://example.com/x");
 namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
 String prefix = "x";
 String expectedNamespaceURI = "http://example.com/x";

 // When
 String actualNamespaceURI = namespaceResolver.getNamespaceURI(prefix);

 // Then
 Assert.assertEquals("Unexpected namespace URI for parent's namespace 'x'", expectedNamespaceURI, actualNamespaceURI);
 }

 /**
 * Test getNamespaceURI method with sealed resolver.
 */
 @Test(expected = IllegalStateException.class)
 public void testGetNamespaceURIWithSealedResolver() {
 // Given
 String prefix = "x";
 String expectedExceptionMessage = "Cannot register namespaces on a sealed NamespaceResolver";

 // When
 namespaceResolver.registerNamespace("x", "http://example.com/x");

 // Then
 Assert.assertThrows(IllegalStateException.class, () -> namespaceResolver.getNamespaceURI(prefix));
 Assert.assertEquals(expectedExceptionMessage, namespaceResolver.getMessage());
 }

 /**
 * Test getPrefix method with registered namespace URI.
 */
 @Test
 public void testGetPrefixRegisteredNamespaceURI() {
 // Given
 namespaceResolver.registerNamespace("x", "http://example.com/x");
 String namespaceURI = "http://example.com/x";
 String expectedPrefix = "x";

 // When
 String actualPrefix = namespaceResolver.getPrefix(namespaceURI);

 // Then
 Assert.assertEquals("Unexpected prefix for namespace URI 'http://example.com/x'", expectedPrefix, actualPrefix);
 }

 /**
 * Test getPrefix method with unregistered namespace URI.
 */
 @Test
 public void testGetPrefixUnregisteredNamespaceURI() {
 // Given
 String namespaceURI = "http://example.com/x";
 String expectedPrefix = null;

 // When
 String actualPrefix = namespaceResolver.getPrefix(namespaceURI);

 // Then
 Assert.assertNull("Unexpected prefix for unregistered namespace URI 'http://example.com/x'", actualPrefix);
 }

 /**
 * Test getPrefix method with parent's namespace.
 */
 @Test
 public void testGetPrefixWithParent() {
 // Given
 NamespaceResolver parentNamespaceResolver = new NamespaceResolver();
 parentNamespaceResolver.registerNamespace("x", "http://example.com/x");
 namespaceResolver = new NamespaceResolver(parentNamespaceResolver);
 String namespaceURI = "http://example.com/x";
 String expectedPrefix = "x";

 // When
 String actualPrefix = namespaceResolver.getPrefix(namespaceURI);

 // Then
 Assert.assertEquals("Unexpected prefix for parent's namespace 'http://example.com/x'", expectedPrefix, actualPrefix);
 }

 /**
 * Test getPrefix method with sealed resolver.
 */
 @Test
 public void testGetPrefixWithSealedResolver() {
 // Given
 NamespaceResolver sealedNamespaceResolver = new NamespaceResolver(namespaceResolver);
 sealedNamespaceResolver.seal();
 String namespaceURI = "http://example.com/x";
 String expectedExceptionMessage = "Cannot register namespaces on a sealed NamespaceResolver";

 // When
 namespaceResolver.registerNamespace("x", "http://example.com/x");

 // Then
 Assert.assertThrows(IllegalStateException.class, () -> namespaceResolver.getPrefix(namespaceURI));
 Assert.assertEquals(expectedExceptionMessage, namespaceResolver.getMessage());
 }
}