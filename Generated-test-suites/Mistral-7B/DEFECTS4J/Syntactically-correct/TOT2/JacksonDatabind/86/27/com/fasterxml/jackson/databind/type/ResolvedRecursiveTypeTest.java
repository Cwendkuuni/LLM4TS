import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testConstructor() {
        Class<?> erasedType = Object.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(erasedType, bindings);
        assertNotNull(resolvedRecursiveType);
    }

    @Test
    public void testSettingReferenceTwice() {
        JavaType ref = new TypeReference<Object>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedRecursiveType.setReference(ref);
        resolvedRecursiveType.setReference(ref); // This should throw an exception
    }

    @Test(expected = IllegalStateException.class)
    public void testSettingNullReference() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(Object.class, new TypeBindings());
        resolvedRecursiveType.setReference(null);
    }

    // Test other methods and cases here

    @Test
    public void testGetSelfReferencedType() {
        JavaType refType = new TypeReference<String>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(refType);
        JavaType result = resolvedRecursiveType.getSelfReferencedType();
        assertEquals(refType, result);
    }

    // Add more test cases as needed

    @Test
    public void testEquals() {
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        ResolvedRecursiveType resolvedRecursiveType3 = new ResolvedRecursiveType(Integer.class, new TypeBindings());

        assertEquals(resolvedRecursiveType1, resolvedRecursiveType1);
        assertEquals(resolvedRecursiveType1, resolvedRecursiveType2); // Should be equal since they have the same reference type
        assertNotEquals(resolvedRecursiveType1, resolvedRecursiveType3);
    }

    // Add more test cases as needed

    @Test
    public void testToString() {
        JavaType refType = new TypeReference<String>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(refType);

        String result = resolvedRecursiveType.toString();
        String expected = "[recursive type; " + refType.getRawClass().getName() + "]";
        assertEquals(expected, result);
    }

    // Add more test cases as needed

    @Test
    public void testHashCode() {
        JavaType refType = new TypeReference<String>() {}.getType();
        ResolvedRecursiveType resolvedRecursiveType1 = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType1.setReference(refType);

        ResolvedRecursiveType resolvedRecursiveType2 = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType2.setReference(refType);

        assertEquals(resolvedRecursiveType1.hashCode(), resolvedRecursiveType2.hashCode());
    }

    // Add more test cases as needed

    @Test
    public void testGetGenericSignature() {
        JavaType refType = new TypeReference<String>() {}.getType();
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(refType);

        resolvedRecursiveType.getGenericSignature(sb);
        String expected = "[Ljava/lang/Class;Lcom/fasterxml/jackson/databind/TypeBindings;Ljava/lang/Class<T>;]";
        assertEquals(expected, sb.toString());
    }

    // Add more test cases as needed

    @Test
    public void testGetErasedSignature() {
        JavaType refType = new TypeReference<String>() {}.getType();
        StringBuilder sb = new StringBuilder();
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(String.class, new TypeBindings());
        resolvedRecursiveType.setReference(refType);

        resolvedRecursiveType.getErasedSignature(sb);
        String expected = "Ljava/lang/Class;Lcom/fasterxml/jackson/databind/TypeBindings;Ljava/lang/Class<T>;";
        assertEquals(expected, sb.toString());
    }

    // Add more test cases as needed

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType resolvedRecursiveType = new ResolvedRecursiveType(List.class, new TypeBindings());
        assertFalse(resolvedRecursiveType.isContainerType());
    }

    // Add more test cases as needed

}