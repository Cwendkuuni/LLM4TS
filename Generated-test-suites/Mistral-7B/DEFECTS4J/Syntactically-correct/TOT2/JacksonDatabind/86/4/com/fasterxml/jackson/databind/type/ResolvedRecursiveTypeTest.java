import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertEquals(erasedType, resolvedRecursiveType.getErasedType());
        assertEquals(bindings, resolvedRecursiveType.getTypeBindings());
    }

    @Test(expected = IllegalStateException.class)
    public void testSettingReferenceMultipleTimes() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType ref1 = new SimpleType(int.class);
        JavaType ref2 = new SimpleType(long.class);
        resolvedRecursiveType.setReference(ref1);
        resolvedRecursiveType.setReference(ref2);
    }

    @Test
    public void testGetSelfReferencedTypeNull() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedTypeSet() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType(List.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        assertNotNull(resolvedRecursiveType.getSelfReferencedType());
        assertEquals(ref.getClass(), resolvedRecursiveType.getSelfReferencedType().getClass());
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType(List.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        StringBuilder stringBuilder = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(stringBuilder);
        assertEquals("[recursive type; [java.util.List<*>]", stringBuilder.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType ref = new SimpleType(List.class);
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(ref);
        StringBuilder stringBuilder = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(stringBuilder);
        assertEquals("java.util.List", stringBuilder.toString());
    }

    // Add test cases for other methods
}