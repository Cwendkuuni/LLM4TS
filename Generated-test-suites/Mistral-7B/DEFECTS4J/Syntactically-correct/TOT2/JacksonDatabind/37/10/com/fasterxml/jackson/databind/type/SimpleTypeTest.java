import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.SimpleType;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructorWithClass() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test
    public void testSimpleTypeNarrowing() {
        Class<?> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        Class<?> subclass = Object.class;
        JavaType narrowedType = simpleType._narrow(subclass);
        assertNull(narrowedType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleTypeConstructWithInvalidClass() {
        Class<?> cls = HashMap.class;
        SimpleType simpleType = new SimpleType(cls);
    }
}