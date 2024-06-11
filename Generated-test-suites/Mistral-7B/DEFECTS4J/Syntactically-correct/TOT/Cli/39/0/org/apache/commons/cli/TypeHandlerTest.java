import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringString() throws ParseException
    {
        String str = "testString";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValueStringClassObject() throws ParseException
    {
        String str = "testString";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, Object.class);
        assertNotNull(obj);
        assertEquals(str.getClass(), obj.getClass());
    }

    @Test
    public void testCreateValueInvalidStringClass() throws ParseException
    {
        String str = "testString";
        Class<?> clazz = int.class;
        assertNull(TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateObjectString() throws ParseException
    {
        String classname = "java.util.Date";
        Object obj = TypeHandler.createObject(classname);
        assertNotNull(obj);
        assertEquals(classname, obj.getClass().getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassname() throws ParseException
    {
        String classname = "java.util.DateXXX";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumberString() throws ParseException
    {
        String str = "123";
        Number num = TypeHandler.createNumber(str);
        assertEquals(123, num.longValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidString() throws ParseException
    {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test(expected = ParseException.class)
    public void testCreateDateString() throws ParseException
    {
        String str = "2023-03-01";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURLString() throws ParseException
    {
        String str = "http://www.example.com";
        URL url = TypeHandler.createURL(str);
        assertNotNull(url);
        assertEquals("http", url.getProtocol());
        assertEquals("www.example.com", url.getHost());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidString() throws ParseException
    {
        String str = "http://www.example.com/invalid";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFileString()
    {
        String str = "path/to/file.txt";
        File file = TypeHandler.createFile(str);
        assertNotNull(file);
        assertEquals(str, file.getAbsolutePath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInvalidString()
    {
        String str = "path/to/invalidFile.txt";
        TypeHandler.createFile(str);
    }
}