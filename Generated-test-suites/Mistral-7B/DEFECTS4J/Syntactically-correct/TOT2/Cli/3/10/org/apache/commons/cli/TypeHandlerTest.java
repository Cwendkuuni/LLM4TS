import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object expectedObject = new String("testString");
        Object actualObject = TypeHandler.createValue("testString", expectedObject.getClass());
        assertEquals(expectedObject, actualObject);
    }
}