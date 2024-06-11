import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NamespaceStackTest {

    @Test
    public void testEmptyConstructor() {
        NamespaceStack stack = new NamespaceStack();
        Namespace[] expectedEmpty = NamespaceStack.EMPTY;
        assertArrayEquals(expectedEmpty, stack.getScope());
        assertEquals(0, stack.depth);
        assertNotNull(stack);
    }

    // ... other tests for other methods
}