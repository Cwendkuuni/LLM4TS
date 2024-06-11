import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    @Test
    public void testConstructWithClass() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertEquals(null, simpleType.getSuperClass());
        assertEquals(null, simpleType.getSuperInterfaces());
        assertEquals(false, simpleType.isStatic());
    }
}