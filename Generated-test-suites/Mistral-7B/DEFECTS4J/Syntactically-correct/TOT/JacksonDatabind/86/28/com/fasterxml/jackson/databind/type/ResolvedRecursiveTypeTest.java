import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.ResolvedRecursiveType;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        ResolvedRecursiveType instance = new ResolvedRecursiveType(type, null);

        JavaType refType = new JavaTypeImpl(int[].class);
        instance.setReference(refType);

        JavaType selfRefType = instance.getSelfReferencedType();
        assertEquals(refType, selfRefType);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetReferenceTwice() {
        Class<ResolvedRecursiveType> type = ResolvedRecursiveType.class;
        ResolvedRecursiveType instance = new ResolvedRecursiveType(type, null);

        JavaType refType = new JavaTypeImpl(int[].class);
        instance.setReference(refType);

        try {
            instance.setReference(new JavaTypeImpl(String.class));
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }
}