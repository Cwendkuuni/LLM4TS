import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;
import org.apache.commons.cli.TypeHandler;

@RunWith(JUnit4.class)
public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        assertNotNull(result);
    }

    @Test
    public void testCreateNumber() {
        String numberString = "123";
        Number number = TypeHandler.createNumber(numberString);
        assertEquals(123, number.intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueUnsupportedType() {
        String str = "test";
        Object obj = new Object();
        Class<?> unsupportedType = Object.class;
        TypeHandler.createValue(str, unsupportedType);
    }

    @Test
    public void testCreateValueInvalidStringForObject() {
        String str = "invalid";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertNull(result);
    }

    @Test
    public void testCreateValueInvalidStringForNumber() {
        String str = "invalid";
        Number number = TypeHandler.createNumber(str);
        assertNull(number);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObjectInvalidClassName() {
        String className = "java.DoesNotExist";
        Object result = TypeHandler.createObject(className);
        assertNull(result);
    }

    @Test
    public void testCreateObjectClassWithArguments() {
        String className = "java.util.Date";
        Object result = TypeHandler.createObject(className);
        assertNotNull(result);
        // Add test cases for other methods and edge cases here
    }

    @Test
    public void testCreateNumberEmptyString() {
        String numberString = "";
        Number number = TypeHandler.createNumber(numberString);
        assertNull(number);
    }

    @Test
    public void testCreateNumberDecimalNumber() {
        String numberString = "123.45";
        Number number = TypeHandler.createNumber(numberString);
        assertEquals(123.45, number.doubleValue(), 0.001);
    }

    @Test
    public void testCreateNumberInvalidNumberString() {
        String numberString = "invalid";
        Number number = TypeHandler.createNumber(numberString);
        assertNull(number);
    }

    @Test
    public void testCreateNumberNumberTooLarge() {
        String numberString = Long.MAX_VALUE + "";
        Number number = TypeHandler.createNumber(numberString);
        assertNull(number);
    }

    @Test
    public void testCreateNumberNumberTooSmall() {
        String numberString = Long.MIN_VALUE + "";
        Number number = TypeHandler.createNumber(numberString);
        assertNull(number);
    }
}