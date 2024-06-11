import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ResolvedRecursiveTypeTest {
    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new SimpleType(int.class);
        type.setReference(ref);
        JavaType expected = ref;
        JavaType actual = type.getSelfReferencedType();
        assertSame(expected, actual);
    }
}