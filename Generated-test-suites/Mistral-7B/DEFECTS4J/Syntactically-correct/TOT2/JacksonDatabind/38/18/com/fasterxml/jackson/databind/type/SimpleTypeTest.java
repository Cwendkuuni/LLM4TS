import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeFactory;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstructor() {
        Class<String> stringClass = String.class;
        SimpleType simpleType = new SimpleType(stringClass);
        JavaType expectedJavaType = new SimpleType(stringClass);
        assertEquals(expectedJavaType, simpleType);
    }
}