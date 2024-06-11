import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TypeHandlerTest
{
    private TypeHandler typeHandler;

    @Before
    public void setup()
    {
        typeHandler = new TypeHandler();
    }

    @Test
    public void testCreateValue_String_String() throws ParseException
    {
        String str = "test";
        Object obj = typeHandler.createValue(str, String.class);
        assertEquals(str, obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_String_InvalidClass() throws ParseException
    {
        String str = "test";
        typeHandler.createValue(str, Integer.class);
    }

    @Test
    public void testCreateValue_String_Class_String() throws ParseException
    {
        String str = "test";
        Class<?> clazz = String.class;
        Object obj = typeHandler.createValue(str, clazz);
        assertEquals(str, obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_Class_InvalidClass() throws ParseException
    {
        String str = "test";
        Class<?> clazz = Integer.class;
        typeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObject_String() throws ParseException
    {
        String classname = "java.util.Date";
        Object obj = typeHandler.createObject(classname);
        assertNotNull(obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_String_InvalidClassName() throws ParseException
    {
        String classname = "InvalidClassName";
        typeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_String_Long() throws ParseException
    {
        String str = "123";
        Number number = typeHandler.createNumber(str);
        assertEquals(123L, number.longValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_String_InvalidNumber() throws ParseException
    {
        String str = "abcd";
        typeHandler.createNumber(str);
    }

    @Test
    public void testCreateNumber_String_Double() throws ParseException
    {
        String str = "123.45";
        Number number = typeHandler.createNumber(str);
        assertEquals(123.45, number.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateDate_String() throws ParseException
    {
        String str = "2023-03-01";
        Date date = typeHandler.createDate(str);
        assertNull(date);
    }

    @Test
    public void testCreateURL_String() throws ParseException
    {
        String str = "http://www.example.com";
        URL url = typeHandler.createURL(str);
        assertNotNull(url);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_String_InvalidURL() throws ParseException
    {
        String str = "invalid:/url";
        typeHandler.createURL(str);
    }

    @Test
    public void testCreateFile_String() throws ParseException
    {
        String str = "test.txt";
        File file = typeHandler.createFile(str);
        assertNotNull(file);
    }

    @Test(expected = ParseException.class)
    public void testOpenFile_String_InvalidFile() throws ParseException
    {
        String str = "invalid_file.txt";
        typeHandler.openFile(str);
    }
}