import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ResolvedRecursiveTypeTest {

    @Test
    public void testSetReference() {
        ResolvedRecursiveType type = new ResolvedRecursiveType(Object.class, null);
        JavaType ref = new SimpleType(String.class);
        type.setReference(ref);
        assertNotNull(type.getSelfReferencedType());
    }
}