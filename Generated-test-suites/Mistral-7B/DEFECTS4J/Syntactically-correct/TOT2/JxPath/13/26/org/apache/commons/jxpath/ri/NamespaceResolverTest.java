import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testConstructor() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNotNull(resolver);
    }

    @Test
    public void testConstructorWithParent() {
        NamespaceResolver parent = new NamespaceResolver();
        NamespaceResolver resolver = new NamespaceResolver(parent);
        assertNotNull(resolver);
    }

    @Test
    public void testRegisterNamespace_EmptyPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "";
        String namespaceURI = "http://www.w3.org/2000/xmlns/";
        try {
            resolver.registerNamespace(prefix, namespaceURI);
            fail("Expected IllegalArgumentException with empty prefix");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testRegisterNamespace_ExistingPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "xmlns";
        String namespaceURI = "http://www.w3.org/2000/xmlns/";
        resolver.registerNamespace(prefix, namespaceURI);
        String existedPrefix = resolver.getPrefix(namespaceURI);
        assertEquals(prefix, existedPrefix);
    }

    @Test
    public void testRegisterNamespace_NullURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "xmlns";
        String namespaceURI = null;
        try {
            resolver.registerNamespace(prefix, namespaceURI);
            fail("Expected NullPointerException with null URI");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testRegisterNamespace_URIWithWhitespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "xmlns";
        String namespaceURI = "http://www.w3.org/2000/xmlns/ ";
        try {
            resolver.registerNamespace(prefix, namespaceURI);
            fail("Expected IllegalArgumentException with whitespace in URI");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetNamespaceContextPointer_NullPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = null;
        try {
            resolver.setNamespaceContextPointer(pointer);
            fail("Expected NullPointerException with null pointer");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testGetNamespaceURI_NullPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = null;
        assertEquals(null, resolver.getNamespaceURI(prefix));
    }

    @Test
    public void testGetNamespaceURI_EmptyPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "";
        assertEquals(null, resolver.getNamespaceURI(prefix));
    }

    @Test
    public void testGetNamespaceURI_PrefixNotRegistered() {
        NamespaceResolver resolver = new NamespaceResolver();
        String prefix = "prefix";
        assertEquals(null, resolver.getNamespaceURI(prefix));
    }

    @Test
    public void testGetPrefix_NullNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = null;
        assertEquals(null, resolver.getPrefix(namespaceURI));
    }

    @Test
    public void testGetPrefix_EmptyNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "";
        assertEquals(null, resolver.getPrefix(namespaceURI));
    }

    @Test
    public void testGetPrefix_NamespaceURINotRegistered() {
        NamespaceResolver resolver = new NamespaceResolver();
        String namespaceURI = "namespaceURI";
        assertEquals(null, resolver.getPrefix(namespaceURI));
    }

    @Test
    public void testIsSealed_WhenNotSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testSeal_WhenNotSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testSeal_WhenAlreadySealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals(resolver.getNamespaceMap(), clonedResolver.getNamespaceMap());

        // Test shallow cloning
        NamespaceResolver shallowClonedResolver = resolver;
        assertNotSame(resolver.getNamespaceMap(), shallowClonedResolver.getNamespaceMap());
    }

}