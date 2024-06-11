import org.apache.commons.jxpath.ri.NamespacesResolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class NamespaceResolverTest {

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();

        // Test with an explicitly registered namespace
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        String uri = resolver.getNamespaceURI("xsd");
        assertEquals("http://www.w3.org/2001/XMLSchema", uri);

        // Test with an undefined prefix
        uri = resolver.getNamespaceURI("foo");
        assertNull(uri);

        // Test with a prefix that's defined in the context node
        resolver.setNamespaceContextPointer(new NodePointer("namespace:test"));
        uri = resolver.getNamespaceURI("xmlns");
        assertEquals("http://www.w3.org/2000/xmlns/", uri);

        // Test with a parent NamespaceResolver
        NamespaceResolver parent = new NamespaceResolver(resolver);
        uri = parent.getNamespaceURI("xsd");
        assertEquals("http://www.w3.org/2001/XMLSchema", uri);

        // Test with empty namespaceMap and reverseMap
        resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceURI("foo"));
        assertNull(resolver.getPrefix("http://www.w3.org/2001/XMLSchema"));

        // Test with null prefix and pointer
        assertNull(resolver.getNamespaceURI(null));
        assertNull(resolver.getNamespaceURI(new NodePointer(null)));

        // Test with an empty pointer
        resolver.setNamespaceContextPointer(new NodePointer());
        assertNull(resolver.getNamespaceURI("xmlns"));

        // Test with a prefix that's defined multiple times
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        resolver.registerNamespace("xsd", "http://example.com/XMLSchema");
        assertThrows(IllegalArgumentException.class, () -> resolver.getNamespaceURI("xsd"));

        // Test with a null or empty namespace URI
        assertNull(resolver.getNamespaceURI(null));
        assertNull(resolver.getNamespaceURI(""));

        // Test with a prefix that's not defined and a null pointer
        assertNull(resolver.getNamespaceURI("foo"));

        // Test with a prefix that's not defined and an empty pointer
        resolver.setNamespaceContextPointer(new NodePointer());
        assertNull(resolver.getNamespaceURI("foo"));

        // Test with a prefix that's not defined and a parent NamespaceResolver
        parent = new NamespaceResolver(resolver);
        assertNull(parent.getNamespaceURI("foo"));

        // Test with an IllegalArgumentException when trying to register an already registered prefix
        resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        assertThrows(IllegalArgumentException.class, () -> resolver.registerNamespace("xsd", "http://example.com/XMLSchema"));

        // Test with a sealed NamespaceResolver
        resolver.seal();
        assertThrows(IllegalStateException.class, () -> resolver.registerNamespace("xsd", "http://www.w3.org/2001/XMLSchema"));
    }
}