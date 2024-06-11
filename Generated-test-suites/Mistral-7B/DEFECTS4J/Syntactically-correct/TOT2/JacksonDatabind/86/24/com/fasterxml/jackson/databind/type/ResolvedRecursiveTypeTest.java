import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType reference = JavaType.construct();
        type.setReference(reference);
        assertNotNull(type.getSelfReferencedType());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReference_existingReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType reference = JavaType.construct();
        type.setReference(reference);
        type.setReference(JavaType.construct());
    }

    @Test
    public void testEquals() {
        ResolvedRecursiveType type1 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType type2 = new ResolvedRecursiveType(Object.class, null);
        ResolvedRecursiveType type3 = new ResolvedRecursiveType(Object.class, null);
        type3.setReference(JavaType.construct());

        assertFalse(type1.equals(type2));
        assertFalse(type1.equals(type3));
        assertFalse(type2.equals(type3));

        JavaType reference = JavaType.construct();
        type1.setReference(reference);
        type2.setReference(reference);

        assertTrue(type1.equals(type2));
    }

    @Test
    public void testToString() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertEquals("[recursive type; UNRESOLVED]", type.toString());

        JavaType reference = JavaType.construct();
        type.setReference(reference);

        assertEquals("[recursive type; com.fasterxml.jackson.databind.JavaType@"+ reference.hashCode() +"]", type.toString());
    }

    @Test
    public void testIsContainerType() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        assertFalse(type.isContainerType());
    }
}