import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest {
    @Test
    public void testCreateValueWithStringObject() {
        String str = "test";
        Object obj = new Object() {
            @Override
            public String toString() {
                return "testObject";
            }
        };
        Object result = TypeHandler.createValue(str, obj);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueWithStringClassStringValue() {
        String str = "testStringValue";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals("testStringValue", result);
    }

    @Test
    public void testCreateValueWithStringClassObjectValue() {
        String str = "testObjectValue";
        Class clazz = Object.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(null, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueWithStringClassInvalidClass() {
        String str = "testInvalidClass";
        Class clazz = Integer.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObjectWithValidClassName() {
        String str = "java.lang.String";
        Object result = TypeHandler.createObject(str);
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testCreateObjectWithInvalidClassName() {
        String str = "java.lang.InvalidClassName";
        Object result = TypeHandler.createObject(str);
        assertEquals(null, result);
    }

    @Test
    public void testCreateNumberWithValidNumber() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123, result.intValue());
    }

    @Test
    public void testCreateNumberWithValidDouble() {
        String str = "123.45";
        Number result = TypeHandler.createNumber(str);
        assertEquals(123.45, result.doubleValue(), 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberWithInvalidNumber() {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateDateWithValidDateString() {
        String str = "2022-12-31";
        Date result = TypeHandler.createDate(str);
        assertNotNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDateWithInvalidDateString() {
        String str = "invalidDate";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURLWithValidURLString() {
        String str = "http://example.com";
        URL result = TypeHandler.createURL(str);
        assertNotNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURLWithInvalidURLString() {
        String str = "invalidURL";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFileWithValidFileString() {
        String str = "test.txt";
        File result = TypeHandler.createFile(str);
        assertNotNull(result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileWithInvalidFileString() {
        String str = "nonExistent.txt";
        TypeHandler.createFile(str);
    }

    @Test
    public void testCreateFilesWithValidFileStrings() {
        String str = "test1.txt, test2.txt";
        File[] result = TypeHandler.createFiles(str);
        assertNotNull(result);
        assertEquals(2, result.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFilesWithInvalidFileStrings() {
        String str = "test.txt, nonExistent.txt";
        TypeHandler.createFiles(str);
    }
}