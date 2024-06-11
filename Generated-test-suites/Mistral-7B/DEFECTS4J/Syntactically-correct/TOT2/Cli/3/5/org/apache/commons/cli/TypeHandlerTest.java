import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String str = "testString";
        Object obj = String.class;
        Object result = TypeHandler.createValue(str, obj);
        assertEquals("testString", result);
    }

    // Add test cases for edge cases and exceptions here
}