import org.apache.commons.jxpath.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI("foo"));
        assertNull(resolver.getPrefix("http://example.com/"));
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/");
        assertEquals("http://example.com/", resolver.getNamespaceURI("x"));
        assertEquals("x", resolver.getPrefix("http://example.com/"));
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/");
        NodePointer nodePointer = new NodePointer("", "http://example.com/");
        resolver.setNamespaceContextPointer(nodePointer);
        assertEquals("http://example.com/", resolver.getNamespaceURI("x"));
        assertEquals("http://example.com/", resolver.getNamespaceURI("x", nodePointer));
        assertNull(resolver.getNamespaceURI("y"));
        assertNull(resolver.getNamespaceURI("y", nodePointer));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/");
        assertEquals("x", resolver.getPrefix("http://example.com/"));
        assertEquals("x", resolver.getPrefix("http://example.com/", null));
        assertEquals(null, resolver.getPrefix("http://other.com/"));
        assertEquals(null, resolver.getPrefix("http://other.com/", null));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/");
        resolver.seal();
        try {
            resolver.registerNamespace("y", "http://example.com/");
            fail();
        } catch (IllegalStateException e) {
            // expected
        }
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("x", "http://example.com/");
        NamespaceResolver clonedResolver = (NamespaceResolver) resolver.clone();
        assertEquals("http://example.com/", clonedResolver.getNamespaceURI("x"));
        assertFalse(resolver.isSealed());
        assertFalse(clonedResolver.isSealed());
    }
}