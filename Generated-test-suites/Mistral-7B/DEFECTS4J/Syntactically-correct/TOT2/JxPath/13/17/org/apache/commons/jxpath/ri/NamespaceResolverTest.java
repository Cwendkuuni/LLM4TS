import org.apache.commons.jxpath.ri.NamespaceResolver;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NamespaceResolverTest {

    @Test
    public void testNewInstance() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertNull(resolver.getNamespaceContextPointer());
        assertFalse(resolver.isSealed());
    }

    @Test
    public void testRegisterNamespace() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolver.getNamespaceURI("xsi"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRegisterNamespaceOnSealedResolver() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        resolver.seal();
        try {
            resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            fail();
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testGetNamespaceURI() {
        NamespaceResolver resolver = new NamespaceResolver();
        assertEquals(null, resolver.getNamespaceURI("xsi"));
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", resolver.getNamespaceURI("xsi"));
    }

    @Test
    public void testGetNamespaceURIWithPointer() {
        NamespaceResolver resolver = new NamespaceResolver();
        NodePointer pointer = new NodePointer("namespace:test");
        resolver.setNamespaceContextPointer(pointer);
        assertEquals("namespace", resolver.getNamespaceURI("test"));
    }

    @Test
    public void testGetNamespaceURIWithParent() {
        NamespaceResolver child = new NamespaceResolver();
        child.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        NamespaceResolver parent = new NamespaceResolver(child);
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", parent.getNamespaceURI("xsi"));
    }

    @Test
    public void testGetPrefix() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        assertEquals("xsi", resolver.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
    }

    @Test
    public void testGetPrefixWithParent() {
        NamespaceResolver child = new NamespaceResolver();
        child.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        NamespaceResolver parent = new NamespaceResolver(child);
        assertEquals("xsi", parent.getPrefix("http://www.w3.org/2001/XMLSchema-instance"));
    }

    @Test
    public void testSeal() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        resolver.seal();
        try {
            resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            fail();
        } catch (Exception e) {
            // expected
        }
        assertTrue(resolver.isSealed());
    }

    @Test
    public void testClone() {
        NamespaceResolver resolver = new NamespaceResolver();
        resolver.registerNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        NamespaceResolver clone = (NamespaceResolver) resolver.clone();
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", clone.getNamespaceURI("xsi"));
    }
}