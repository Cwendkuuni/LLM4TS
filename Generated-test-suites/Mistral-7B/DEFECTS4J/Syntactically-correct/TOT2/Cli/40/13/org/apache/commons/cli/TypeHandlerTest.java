import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {
    @Test
    public void testCreateValueStringObject() {
        String str = "test";
        Object obj = new Object(); // Can be any valid object

        Object result = TypeHandler.createValue(str, obj);

        assertEquals(str, result); // Basic functionality test
    }
}