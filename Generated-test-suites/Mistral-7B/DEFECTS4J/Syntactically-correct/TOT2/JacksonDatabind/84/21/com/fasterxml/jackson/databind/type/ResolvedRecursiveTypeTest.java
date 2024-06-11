import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testConstructor() {
        Class<?> erasedType = TestClass.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType actual = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(erasedType, actual.getErasedType());
        assertNull(actual.getSelfReferencedType());
    }
}