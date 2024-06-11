import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import java.lang.reflect.GenericSignatureParser;
import java.lang.reflect.Type;
import java.util.Arrays;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }

    @Test
    public void testSetReferenceMultipleTimes() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(new ResolvedRecursiveType(int.class, new TypeBindings()));
        resolvedRecursiveType.setReference(new ResolvedRecursiveType(double.class, new TypeBindings()));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceMultipleTimes() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(new ResolvedRecursiveType(int.class, new TypeBindings()));
        resolvedRecursiveType.setReference(new ResolvedRecursiveType(double.class, new TypeBindings()));
    }

    @Test(expected = NullPointerException.class)
    public void testResolvedRecursiveTypeConstructorNullErasedType() {
        TypeBindings bindings = new TypeBindings();
        new ResolvedRecursiveType(null, bindings);
    }

    @Test(expected = NullPointerException.class)
    public void testResolvedRecursiveTypeConstructorNullBindings() {
        Class<String> erasedType = String.class;
        new ResolvedRecursiveType(erasedType, null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetReferenceNullRef() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceDifferentType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(new ResolvedRecursiveType(int.class, new TypeBindings()));
    }

    @Test
    public void testGetSelfReferencedType() {
        Class<String> erasedType = String.class;
        JavaType javaType = new SimpleType("java.util.List", new TypeFactory());
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);
        JavaType selfReferencedType = resolvedRecursiveType.getSelfReferencedType();
        assertSame(javaType, selfReferencedType);
    }

    @Test
    public void testGetGenericSignature() {
        Class<String> erasedType = String.class;
        JavaType javaType = new SimpleType("java.util.List", new TypeFactory());
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);

        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getGenericSignature(sb);
        String expected = "java.util.List<T>";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        Class<String> erasedType = String.class;
        JavaType javaType = new SimpleType("java.util.List", new TypeFactory());
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);

        StringBuilder sb = new StringBuilder();
        resolvedRecursiveType.getErasedSignature(sb);
        String expected = "java.util.List";
        assertEquals(expected, sb.toString());
    }

    @Test
    public void testWithContentType() {
        Class<String> erasedType = String.class;
        JavaType javaType = new SimpleType("java.util.List", new TypeFactory());
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);

        JavaType withContentType = resolvedRecursiveType.withContentType(javaType);
        assertSame(resolvedRecursiveType, withContentType);
    }

    // Add test cases for other methods here

    @Test
    public void testToString() {
        Class<String> erasedType = String.class;
        JavaType javaType = new SimpleType("java.util.List", new TypeFactory());
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType.setReference(javaType);

        String expected = "[recursive type; java.util.List]";
        String actual = resolvedRecursiveType.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<String> erasedType1 = String.class;
        JavaType javaType1 = new SimpleType("java.util.List", new TypeFactory());
        TypeBindings bindings1 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType1, bindings1);
        resolvedRecursiveType1.setReference(javaType1);

        Class<String> erasedType2 = String.class;
        JavaType javaType2 = new SimpleType("java.util.List", new TypeFactory());
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        resolvedRecursiveType2.setReference(javaType2);

        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));

        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(erasedType1, bindings1);
        assertSame(resolvedRecursiveType1, resolvedRecursiveType3);

        ResolvedRecursiveType resolvedRecursiveType4 = new ResolvedRecursiveType(erasedType1, bindings1);
        assertSame(resolvedRecursiveType1, resolvedRecursiveType4);

        assertFalse(resolvedRecursiveType1.equals(new Object()));

        resolvedRecursiveType1.setReference(javaType1);
        resolvedRecursiveType1.setReference(javaType1);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType1));
    }

    @Test
    public void testHashCode() {
        Class<String> erasedType = String.class;
        JavaType javaType = new SimpleType("java.util.List", new TypeFactory());
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType1.setReference(javaType);

        Class<String> erasedType2 = String.class;
        JavaType javaType2 = new SimpleType("java.util.List", new TypeFactory());
        TypeBindings bindings2 = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType2, bindings2);
        resolvedRecursiveType2.setReference(javaType2);

        int hashCode1 = resolvedRecursiveType1.hashCode();
        int hashCode2 = resolvedRecursiveType2.hashCode();

        assertNotSame(hashCode1, hashCode2);

        resolvedRecursiveType1.setReference(javaType);
        resolvedRecursiveType1.setReference(javaType);
        int hashCode3 = resolvedRecursiveType1.hashCode();
        assertEquals(hashCode3, hashCode3);
    }
}