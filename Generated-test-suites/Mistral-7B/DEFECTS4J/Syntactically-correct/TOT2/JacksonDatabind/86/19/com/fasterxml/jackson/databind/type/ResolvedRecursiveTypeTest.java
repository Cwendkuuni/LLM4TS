import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testResolvedRecursiveTypeConstructor() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }

    @Test
    public void testSetReference() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleType("java.lang.Integer");
        resolvedRecursiveType.setReference(javaType);
        assertNotNull(resolvedRecursiveType.getSelfReferencedType());
        assertEquals(javaType, resolvedRecursiveType.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleType("java.lang.Integer");
        resolvedRecursiveType.setReference(javaType);
        resolvedRecursiveType.setReference(javaType);
    }

    @Test
    public void testGetSelfReferencedTypeNullReference() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNull(resolvedRecursiveType.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedTypeSameReference() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleType("java.lang.Integer");
        resolvedRecursiveType1.setReference(javaType);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType2.setReference(javaType);
        assertEquals(javaType, resolvedRecursiveType1.getSelfReferencedType());
        assertEquals(javaType, resolvedRecursiveType2.getSelfReferencedType());
    }

    @Test
    public void testGetSelfReferencedTypeDifferentReferences() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType1 = new SimpleType("java.lang.Integer");
        JavaType javaType2 = new SimpleType("java.lang.Long");
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType1.setReference(javaType1);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType2.setReference(javaType2);
        assertNotEquals(javaType1, resolvedRecursiveType2.getSelfReferencedType());
    }

    @Test
    public void testEqualsSameInstance() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        JavaType javaType = new SimpleType("java.lang.Integer");
        resolvedRecursiveType.setReference(javaType);
        ResolvedRecursiveType resolvedRecursiveType2 = resolvedRecursiveType;
        assertTrue(resolvedRecursiveType.equals(resolvedRecursiveType2));
    }

    @Test
    public void testEqualsDifferentInstancesSameType() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType = new SimpleType("java.lang.Integer");
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType1.setReference(javaType);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType2.setReference(javaType);
        assertTrue(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    @Test
    public void testEqualsDifferentInstancesDifferentTypes() {
        Class<Integer> erasedType = Integer.class;
        TypeBindings bindings = new TypeBindings();
        JavaType javaType1 = new SimpleType("java.lang.Integer");
        JavaType javaType2 = new SimpleType("java.lang.String");
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType1.setReference(javaType1);
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(erasedType, bindings);
        resolvedRecursiveType2.setReference(javaType2);
        assertFalse(resolvedRecursiveType1.equals(resolvedRecursiveType2));
    }

    @Test
    public void testEqualsNullObject() {
        Class<String> erasedType = String.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(resolvedRecursiveType.equals(null));
    }

    @Test
    public void testEqualsDifferentClasses() {
        Class<String> stringClass = String.class;
        Class<Integer> integerClass = Integer.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveTypeString = new ResolvedRecursiveType(stringClass, bindings);
        ResolvedRecursiveType resolvedRecursiveTypeInteger = new ResolvedRecursiveType(integerClass, bindings);
        assertFalse(resolvedRecursiveTypeString.equals(resolvedRecursiveTypeInteger));
    }

    // Add test cases for overridden methods
}