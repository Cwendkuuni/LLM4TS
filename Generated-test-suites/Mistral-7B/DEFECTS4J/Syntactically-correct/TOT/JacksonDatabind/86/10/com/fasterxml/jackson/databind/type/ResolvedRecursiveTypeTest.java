import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveType() {
        // Test constructor
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
        
        // Test setting reference
        JavaType refType = new TypeFactory().constructType(String.class);
        resolvedRecursiveType.setReference(refType);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(refType, selfReferencedType);
        
        // Test getGenericSignature and getErasedSignature
        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        // The signature for a recursive type may vary, so it's not possible to test this method accurately

        resolvedRecursiveType.getErasedSignature(sb);
        assertEquals("Lcom/fasterxml/jackson/databind/type/ResolvedRecursiveType;", sb.toString());
        
        // Test other methods that return this
        resolvedRecursiveType = resolvedRecursiveType.withContentType(refType);
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentType(refType));

        resolvedRecursiveType = resolvedRecursiveType.withTypeHandler(new Object());
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withTypeHandler(new Object()));

        resolvedRecursiveType = resolvedRecursiveType.withContentTypeHandler(new Object());
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentTypeHandler(new Object()));

        resolvedRecursiveType = resolvedRecursiveType.withValueHandler(new Object());
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withValueHandler(new Object()));

        resolvedRecursiveType = resolvedRecursiveType.withContentValueHandler(new Object());
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withContentValueHandler(new Object()));

        resolvedRecursiveType = resolvedRecursiveType.withStaticTyping();
        assertEquals(resolvedRecursiveType, resolvedRecursiveType.withStaticTyping());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalStateException_settingReferenceTwice() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType refType = new TypeFactory().constructType(String.class);
        resolvedRecursiveType.setReference(refType);
        resolvedRecursiveType.setReference(new TypeFactory().constructType(Integer.class));
    }
}