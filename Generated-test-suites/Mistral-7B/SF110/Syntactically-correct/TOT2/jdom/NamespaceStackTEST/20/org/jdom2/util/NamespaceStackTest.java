import org.jdom2.util.Namespaces;
import org.junit.Test;

public class NamespaceStackTest {
    
    @Test
    public void testEmptyNamespaceStack() {
        Namespaces stack = new Namespaces();
        assertNotNull(stack);
        assertFalse(stack.hasNamespaces());
        assertEquals(Namespaces.EMPTY, stack.getScope());
        assertEquals(Namespaces.EMPTY_ITERATOR, stack.addedForward());
        assertEquals(Namespaces.EMPTY_ITERATOR, stack.addedReverse());
    }
}