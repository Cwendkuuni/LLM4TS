import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest1 {
    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals("java.lang.String", simpleType.getClassName());
        assertEquals(String.class, simpleType.getRawClass());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        SimpleType copiedSimpleType = new SimpleType(simpleType);
        assertEquals(simpleType.getClassName(), copiedSimpleType.getClassName());
        assertEquals(simpleType.getRawClass(), copiedSimpleType.getRawClass());
    }
}