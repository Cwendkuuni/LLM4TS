import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_Object() throws Exception {
        String str = "example";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj);
        assertEquals(str, result);
    }
}