import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String testStr = "testValue";
        Object testObj = new Object();
        Object result = TypeHandler.createValue(testStr, testObj);
        assertEquals(testStr, result.toString());
    }

    @Test
    public void testCreateValueStringClass() {
        String testStr = "testValue";
        Class<String> testClazz = String.class;
        String result = (String) TypeHandler.createValue(testStr, testClazz);
        assertEquals(testStr, result);
    }

    @Test
    public void testCreateValueNullStr() {
        Object obj = new Object();
        assertThrows(ParseException.class, () -> TypeHandler.createValue(null, obj));
    }

    @Test
    public void testCreateValueEmptyStr() {
        Object obj = new Object();
        assertThrows(ParseException.class, () -> TypeHandler.createValue("", obj));
    }

    @Test
    public void testCreateValueNonExistentClass() {
        String classname = "NonExistentClass";
        assertThrows(ParseException.class, () -> TypeHandler.createValue("test", classname));
    }

    // ... add tests for other methods and edge cases

    @Test
    public void testCreateNumber() {
        String testStr = "123";
        Number result = TypeHandler.createNumber(testStr);
        assertEquals(123, result.intValue());

        String testStrWithDecimal = "123.45";
        Double resultWithDecimal = TypeHandler.createNumber(testStrWithDecimal);
        assertEquals(123.45, resultWithDecimal.doubleValue(), 0.01);
    }

    // ... add tests for other methods and edge cases

    @Test
    public void testCreateDate() {
        String testStr = "2022-12-31";
        Date result = TypeHandler.createDate(testStr);
        assertEquals(new Date(2022, 11, 30), result); // note: month index starts at 0

        // Add more tests for different date formats and edge cases
    }

    // ... add tests for other methods and edge cases

}