import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testConstructorNoParent() {
        // Test case for constructor with no parent
    }

    @Test
    public void testConstructorWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer();
        resolver.setNamespaceContextPointer(pointer);
    }

    @Test
    public void testGetNamespaceContextPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        Pointer expectedPointer = new NodePointer();
        resolver.setNamespaceContextPointer(expectedPointer);
        Pointer result = resolver.getNamespaceContextPointer();
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "prefix";
        String namespaceURI = "namespaceURI";
        resolver.registerNamespace(prefix, namespaceURI);
        String result = resolver.getNamespaceURI(prefix);
    }

    @Test
    public void testGetNamespaceURINullPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String result = resolver.getNamespaceURI(null);
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "namespaceURI";
        String prefix = resolver.getPrefix(namespaceURI);
    }

    @Test
    public void testGetPrefixNullNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String result = resolver.getPrefix(null);
    }

    @Test
    public void testIsSealedFalse() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testIsSealedTrue() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
    }

    @Test
    public void testClone() {
        NamespaceResolver original = new NamespaceResolver();
        NamespaceResolver clone = original.clone();
    }

    @Test(expected = CloneNotSupportedException.class)
    public void testCloneNotSupportedException() {
        NamespaceResolver original = new NamespaceResolver();
        original.seal();
        NamespaceResolver clone = original.clone();
    }

    // Add test cases for edge cases and exception handling scenarios here

    // Test registering a namespace with empty prefix and URI
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNamespaceEmptyPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String emptyPrefix = "";
        String namespaceURI = "namespaceURI";
        resolver.registerNamespace(emptyPrefix, namespaceURI);
    }

    // Test registering a namespace with already registered prefix
    @Test(expected = IllegalArgumentException.class)
    public void testRegisterNamespaceAlreadyRegisteredPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "prefix";
        String namespaceURI1 = "namespaceURI1";
        String namespaceURI2 = "namespaceURI2";
        resolver.registerNamespace(prefix, namespaceURI1);
        resolver.registerNamespace(prefix, namespaceURI2);
    }

    // Test getting namespace URI for unregistered prefix
    @Test
    public void testGetNamespaceURIUnregisteredPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "prefix";
        String result = resolver.getNamespaceURI(prefix);
        assertEquals(null, result);
    }

    // Test getting prefix for unregistered namespace URI
    @Test
    public void testGetPrefixUnregisteredNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "namespaceURI";
        String result = resolver.getPrefix(namespaceURI);
        assertEquals(null, result);
    }

    // Test setting namespace context pointer to null
    @Test
    public void testSetNamespaceContextPointerNull() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.setNamespaceContextPointer(null);
    }

    // Test getting namespace context pointer from sealed NamespaceResolver
    @Test
    public void testGetNamespaceContextPointerSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        Pointer result = resolver.getNamespaceContextPointer();
        assertEquals(null, result);
    }

    // Test getting namespace context pointer from NamespaceResolver with parent
    @Test
    public void testGetNamespaceContextPointerWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parentResolver);
        Pointer result = resolver.getNamespaceContextPointer();
        assertEquals(parentResolver.getNamespaceContextPointer(), result);
    }

    // Test exception when trying to register namespaces on a sealed NamespaceResolver
    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        String prefix = "prefix";
        String namespaceURI = "namespaceURI";
        resolver.registerNamespace(prefix, namespaceURI);
    }

    // Test exception when trying to clone a sealed NamespaceResolver
    @Test(expected = CloneNotSupportedException.class)
    public void testCloneSealed() {
        NamespaceResolver original = new NamespaceResolver();
        original.seal();
        NamespaceResolver clone = original.clone();
    }

    // Test cloning a NamespaceResolver with parent
    @Test
    public void testCloneWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver original = new NamespaceResolver(parentResolver);
        NamespaceResolver clone = original.clone();
        assertEquals(original, clone);
    }
}