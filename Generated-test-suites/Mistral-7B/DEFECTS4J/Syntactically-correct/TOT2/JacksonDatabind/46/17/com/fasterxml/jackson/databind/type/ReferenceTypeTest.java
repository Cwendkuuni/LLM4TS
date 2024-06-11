import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;

public class ReferenceTypeTest {

    @Test
    public void testDefaultConstructor() {
        ReferenceType referenceType = new ReferenceType(Integer.class, null, null, null, false);
        assertNotNull(referenceType);
    }

    @Test
    public void testCustomConstructor() {
        Class<Integer> integerClass = Integer.class;
        JavaType javaType = new SimpleType(Integer.class);
        ReferenceType referenceType = new ReferenceType(integerClass, javaType, null, null, false);
        assertEquals(integerClass, referenceType.getRawClass());
        assertEquals(javaType, referenceType.getContentType());
    }

    // Add test cases for other methods here
}