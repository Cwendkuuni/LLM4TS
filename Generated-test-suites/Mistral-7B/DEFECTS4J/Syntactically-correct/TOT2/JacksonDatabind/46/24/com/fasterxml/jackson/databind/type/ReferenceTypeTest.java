
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<String> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, false);
        assertEquals(String.class, referenceType.getClazz());
        assertEquals(int[].class, referenceType.getReferencedType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithInvalidSubClass() {
        JavaType referenceType = JavaType.construct(Integer.class);
        Class<?> invalidSubclass = String.class;
        JavaType narrowedType = new ReferenceType(referenceType)._narrow(invalidSubclass);
    }

    @Test
    public void testGetReferencedType() {
        JavaType referenceType = JavaType.construct(Integer.class);
        assertEquals(Integer.class, referenceType.getReferencedType());
    }

    // Add tests for other methods here

    @Test
    public void testToString() {
        JavaType referenceType = JavaType.construct(Integer.class);
        StringBuilder expected = new StringBuilder("[reference type, class [I, class java.lang.Integer]");
        assertEquals(expected.toString(), referenceType.toString());
    }

    @Test
    public void testEquals() {
        JavaType referenceType1 = JavaType.construct(Integer.class);
        JavaType referenceType2 = JavaType.construct(Integer.class);
        assertEquals(referenceType1, referenceType2);
    }

    @Test
    public void testEqualsDifferentClasses() {
        JavaType referenceType1 = JavaType.construct(Integer.class);
        JavaType referenceType2 = JavaType.construct(String.class);
        assertFalse(referenceType1.equals(referenceType2));
    }

    @Test
    public void testEqualsSelf() {
        JavaType referenceType = JavaType.construct(Integer.class);
        assertTrue(referenceType.equals(referenceType));
    }

    // Add tests for other methods here
}