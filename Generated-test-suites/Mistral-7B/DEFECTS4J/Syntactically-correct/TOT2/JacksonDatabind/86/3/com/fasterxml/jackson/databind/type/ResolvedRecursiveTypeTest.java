import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }
}