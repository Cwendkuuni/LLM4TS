import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, null);
        JavaType referenceType = new SimpleType(String[].class);

        resolvedRecursiveType.setReference(referenceType);

        JavaType actualSelfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(referenceType, actualSelfReferencedType);

        // Test for exception when trying to re-set self reference
        try {
            resolvedRecursiveType.setReference(referenceType);
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Trying to re-set self reference"));
        }
    }
}