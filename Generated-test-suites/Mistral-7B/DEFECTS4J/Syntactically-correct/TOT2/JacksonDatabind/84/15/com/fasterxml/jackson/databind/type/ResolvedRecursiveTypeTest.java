import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        // Basic test
        Assert.assertEquals(erasedType, resolvedRecursiveType.getRawClass());
        Assert.assertNotNull(resolvedRecursiveType._referencedType);
        // Edge case test
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(null, bindings);
        Assert.assertNull(resolvedRecursiveType1.getSelfReferencedType());
        // Exception handling test
        try {
            new ResolvedRecursiveType(erasedType, bindings);
            new ResolvedRecursiveType(erasedType, null);
            fail("Expected exceptions not thrown");
        } catch (IllegalArgumentException e) {
            // Expected exceptions
        }
    }

    // Repeat for other methods
}