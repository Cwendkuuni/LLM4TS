import org.apache.commons.cli.*; // Import the class under test and necessary dependencies
import static org.junit.Assert.*; // Import assertion methods

public class TypeHandlerTest {

    

    @Test
    public void testCreateValue_StringObject() {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringClass() throws ParseException {
        String str = "test";
        Class<String> clazz = String.class;
        String result = (String) TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringClassObjectThrowsParseException() throws ParseException {
        String str = "123abc";
        Class<String> clazz = String.class;
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateObject_ValidClassName() throws ParseException, InstantiationException, IllegalAccessException {
        String classname = "java.lang.String";
        Object result = TypeHandler.createObject(classname);
        assertTrue(result instanceof String);
    }

    @Test
    public void testCreateObject_InvalidClassName() {
        String classname = "InvalidClassName";
        assertThrows(ParseException.class, () -> TypeHandler.createObject(classname));
    }

    @Test
    public void testCreateNumber_ValidNumber() throws ParseException {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123L, result.longValue());
    }

    @Test
    public void testCreateNumber_InvalidNumber() throws ParseException {
        String str = "abc";
        assertThrows(ParseException.class, () -> TypeHandler.createNumber(str));
    }

    
}