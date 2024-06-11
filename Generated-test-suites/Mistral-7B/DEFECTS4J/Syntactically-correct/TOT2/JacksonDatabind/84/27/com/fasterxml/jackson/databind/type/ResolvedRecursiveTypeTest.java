import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<Integer> integerClass = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(integerClass, bindings);
        assertNotNull(resolvedRecursiveType);
        assertEquals(integerClass, resolvedRecursiveType.getRawType());
        assertEquals(bindings, resolvedRecursiveType.getTypeBindings());
    }
}