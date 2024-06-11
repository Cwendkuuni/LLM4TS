import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeParserTest {
    @Test
    public void testParse() {
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType expectedType = parser._factory._fromClass(null, String.class, null);
        JavaType actualType = parser.parse("String");
        assertEquals(expectedType, actualType);
    }
}