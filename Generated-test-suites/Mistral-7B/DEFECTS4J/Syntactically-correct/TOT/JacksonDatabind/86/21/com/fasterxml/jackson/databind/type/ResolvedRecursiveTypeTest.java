import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReferenceAndGetSelfReferencedType() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType expected = new ResolvedRecursiveType(erasedType, bindings);

        JavaType referencedType = new MyTestType();
        expected.setReference(referencedType);

        ResolvedRecursiveType actual = new ResolvedRecursiveType(erasedType, bindings);
        actual.setReference(referencedType);

        JavaType selfReferencedType = actual.getSelfReferencedType();
        assertEquals(referencedType, selfReferencedType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType testObject = new ResolvedRecursiveType(erasedType, bindings);

        JavaType referencedType = new MyTestType();
        testObject.setReference(referencedType);

        try {
            testObject.setReference(referencedType);
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            String expectedMessage = "Trying to re-set self reference; old value = " + referencedType + ", new = " + referencedType;
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedMessage));
        }
    }

    @Test
    public void testGetGenericSignature() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType testObject = new ResolvedRecursiveType(erasedType, bindings);
        String expectedSignature = "Lcom/fasterxml/jackson/databind/type/ResolvedRecursiveType;";
        StringBuilder sb = new StringBuilder();
        JavaType referencedType = new MyTestType();
        testObject.setReference(referencedType);
        String actualSignature = testObject.getGenericSignature(sb).toString();
        assertEquals(expectedSignature, actualSignature);
    }

    @Test
    public void testGetErasedSignature() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType testObject = new ResolvedRecursiveType(erasedType, bindings);
        String expectedSignature = "Ljava/lang/Class;";
        StringBuilder sb = new StringBuilder();
        JavaType referencedType = new MyTestType().getType();
        testObject.setReference(referencedType);
        String actualSignature = testObject.getErasedSignature(sb).toString();
        assertEquals(expectedSignature, actualSignature);
    }

    @Test
    public void testIsContainerType() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType testObject = new ResolvedRecursiveType(erasedType, bindings);
        assertFalse(testObject.isContainerType());
    }

    @Test
    public void testToString() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();
        ResolvedRecursiveType testObject = new ResolvedRecursiveType(erasedType, bindings);
        String expected = "[recursive type; UNRESOLVED]";
        String actual = testObject.toString();
        assertEquals(expected, actual);

        JavaType referencedType = new MyTestType();
        testObject.setReference(referencedType);
        expected = "[recursive type; " + referencedType.getRawClass().getName() + "]";
        actual = testObject.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Class<ResolvedRecursiveType> erasedType = ResolvedRecursiveType.class;
        TypeBindings bindings = new TypeBindings();

        ResolvedRecursiveType testObject1 = new ResolvedRecursiveType(erasedType, bindings);
        ResolvedRecursiveType testObject2 = new ResolvedRecursiveType(erasedType, bindings);
        assertNotSame(testObject1, testObject2);
        assertFalse(testObject1.equals(testObject2));

        JavaType referencedType = new MyTestType();
        testObject1.setReference(referencedType);
        testObject2.setReference(referencedType);
        assertTrue(testObject1.equals(testObject2));
    }

    private static class MyTestType extends TypeBase {
        public MyTestType() {
            super(String.class, bindings, null, null, 0, null, null, false);
        }

        public static JavaType getType() {
            return new MyTestType().getType();
        }
    }
}