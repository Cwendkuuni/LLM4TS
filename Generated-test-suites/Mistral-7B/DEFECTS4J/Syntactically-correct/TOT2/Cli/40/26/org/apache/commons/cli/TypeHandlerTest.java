import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_ValidStringAndObject() throws ParseException {
        Object expected = new Object();
        String str = "test";
        Object obj = expected.getClass();
        Object actual = TypeHandler.createValue(str, obj);
        assertEquals(expected, actual);
    }
}