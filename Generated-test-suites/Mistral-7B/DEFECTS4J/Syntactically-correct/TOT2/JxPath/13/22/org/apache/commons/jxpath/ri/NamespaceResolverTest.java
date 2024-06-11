import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.NodePointer;

public class NamespaceResolverTest {

    @Test
    public void testConstructor() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        assertNotNull(namespaceResolver);
    }

    @Test
    public void testConstructorWithParent() {
        NamespaceResolver parentResolver = new NamespaceResolver();
        NamespaceResolver namespaceResolver = new NamespaceResolver(parentResolver);
        assertNotNull(namespaceResolver);
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        String prefix = "testPrefix";
        String namespaceURI = "testNamespaceURI";
        namespaceResolver.registerNamespace(prefix, namespaceURI);
        assertEquals(namespaceURI, namespaceResolver.getNamespaceURI(prefix));
    }

    @Test
    public void testSetNamespaceContextPointer() {
        NodePointer pointer = new NodePointer();
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        namespaceResolver.setNamespaceContextPointer(pointer);
        assertEquals(pointer, namespaceResolver.getNamespaceContextPointer());
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        String prefix = "testPrefix";
        String namespaceURI = "testNamespaceURI";
        namespaceResolver.registerNamespace(prefix, namespaceURI);
        assertEquals(namespaceURI, namespaceResolver.getNamespaceURI(prefix));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        String namespaceURI = "testNamespaceURI";
        String prefix = namespaceResolver.getPrefix(namespaceURI);
        assertNull(prefix);

        namespaceResolver.registerNamespace("testPrefix", namespaceURI);
        prefix = namespaceResolver.getPrefix(namespaceURI);
        assertEquals("testPrefix", prefix);
    }

    @Test
    public void testIsSealed() {
        NamespaceResolver namespaceResolver = new NamespaceResolver();
        assertFalse(namespaceResolver.isSealed());

        namespaceResolver.seal();
        assertTrue(namespaceResolver.isSealed());
    }

    // Add test cases for edge cases and exception handling scenarios here

}