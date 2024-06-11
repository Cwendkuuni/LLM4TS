import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.TypeParser;
import com.fasterxml.jackson.databind.TypeFactory;

public class TypeParserTest {

    @Test
    public void testParseMethod() {
        String canonical = "java.util.List<java.lang.String>";
        TypeParser parser = new TypeParser(new TypeFactory());
        JavaType javaType = parser.parse(canonical);
        assertNotNull(javaType);
    }
}