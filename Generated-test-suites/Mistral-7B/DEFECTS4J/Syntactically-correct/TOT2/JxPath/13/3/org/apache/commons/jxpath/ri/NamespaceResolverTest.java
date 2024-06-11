import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testEmptyResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI("prefix"));
        assertNull(resolver.getPrefix("namespaceURI"));
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
        assertEquals("prefix", resolver.getPrefix("namespaceURI"));
    }

    @Test
    public void testGetNamespaceURIWithPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer("namespaceURI", "prefix");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("namespaceURI", resolver.getNamespaceURI("prefix"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("prefix", "namespaceURI");
        resolver.seal();
        try {
            resolver.registerNamespace("prefix2", "namespaceURI2");
        } catch (IllegalStateException e) {
            assertEquals("Cannot register namespaces on a sealed NamespaceResolver", e.getMessage());
        }
    }
}