import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SimpleTypeTest {

    @Test
    public void testSimpleType() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType javaType = simpleType.getType();
        assertEquals(String.class, javaType.getRawClass());
    }
}