import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object obj = new String("test");
        String str = "test";
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }
}