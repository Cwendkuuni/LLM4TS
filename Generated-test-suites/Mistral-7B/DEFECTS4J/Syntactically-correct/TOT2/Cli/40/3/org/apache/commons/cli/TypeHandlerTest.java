import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.ClassType;
import org.junit.Assert.*;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringAndObject() throws ParseException {
        Object expectedObject = new String("Hello World");
        Object actualObject = TypeHandler.createValue("Hello World", String.class);
        Assert.assertEquals(expectedObject, actualObject);
    }

    @Test
    public void testCreateValueWithStringAndClass() throws ParseException {
        Class<?> expectedClass = String.class;
        Class<?> actualClass = TypeHandler.createValue("java.lang.String", String.class).getClass();
        Assert.assertEquals(expectedClass, actualClass);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueWithInvalidType() throws ParseException {
        TypeHandler.createValue("123", Integer.class);
    }
}