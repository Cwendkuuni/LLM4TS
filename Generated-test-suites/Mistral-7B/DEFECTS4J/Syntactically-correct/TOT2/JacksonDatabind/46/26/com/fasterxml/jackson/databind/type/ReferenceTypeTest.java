import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.ReferenceType;

public class ReferenceTypeTest {

    @Test
    public void testReferenceTypeConstruction() {
        Class<?> cls = String.class;
        JavaType refType = JavaType.construct(int[].class);
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        ReferenceType referenceType = new ReferenceType(cls, refType, valueHandler, typeHandler, asStatic);

        assertEquals(String.class, referenceType.getClass());
        assertEquals(int[].class.getCanonicalName(), referenceType.getReferencedType().getRawClass().getName());
        assertFalse(referenceType.isStatic());
    }
}