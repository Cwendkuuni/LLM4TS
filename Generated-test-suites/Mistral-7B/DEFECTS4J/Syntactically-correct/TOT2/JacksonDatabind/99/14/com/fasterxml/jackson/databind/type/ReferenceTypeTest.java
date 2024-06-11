import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstructor() {
        JavaType referencedType = new SimpleType(int[].class);
        JavaType anchorType = new SimpleType(String.class);
        ReferenceType referenceType = new ReferenceType(int[].class, TypeBindings.emptyBindings(), null, null, referencedType, anchorType, null, null, false);
        assertNotNull(referenceType);
    }
}