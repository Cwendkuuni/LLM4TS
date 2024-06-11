import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);
        JavaType expected = ref;
        JavaType actual = type.getSelfReferencedType();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);
        type.setReference(new SimpleType(Integer.class));
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        assertNull(type.getSelfReferencedType());
    }

    @Test
    public void testGetStringBuilder() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        StringBuilder sb = new StringBuilder();
        type.getGenericSignature(sb);
        String expected = "[Ljava.lang.Class;";
        assertEquals(expected, sb.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullObject() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(String.class, null);
        type.withContentTypeHandler(null);
    }

    // Test other methods with similar test cases
    
}