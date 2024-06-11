import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        String uri = resolver.getNamespaceURI("namespaceURI");
        assertNull(uri);
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        String uri = resolver.getNamespaceURI("prefix");
        assertEquals("namespaceURI", uri);
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        String prefix = resolver.getPrefix("namespaceURI");
        assertEquals("prefix", prefix);
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertFalse(resolver.isSealed());
        resolver.seal();
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.seal();
        try {
            resolver.registerNamespace("prefix", "namespaceURI");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testClone() {
        NamespaceResolver original = new NamespaceResolver();
        original.registerNamespace("prefix", "namespaceURI");
        NamespaceResolver clone = (NamespaceResolver) original.clone();
        String uri = clone.getNamespaceURI("prefix");
        assertEquals("namespaceURI", uri);
    }
}