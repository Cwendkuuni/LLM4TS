import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest
{

    @Test
    public void testCreateValue_StringString() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(str, obj);

        str = "12345";
        obj = TypeHandler.createValue(str, Integer.class);
        assertEquals(12345, obj);

        str = "12345";
        obj = TypeHandler.createValue(str, Long.class);
        assertEquals(12345L, obj);

        str = "123.45";
        obj = TypeHandler.createValue(str, Double.class);
        assertEquals(123.45, obj, 0.001);

        str = "2022-12-31";
        Date date = TypeHandler.createValue(str, Date.class);
        assertNotNull(date);

        str = "java.lang.String";
        Object obj1 = TypeHandler.createValue(str, String.class);
        assertEquals("java.lang.String", obj1.getClass().getName());

        // Unsupported types
        try
        {
            TypeHandler.createValue("12345", boolean.class);
            fail();
        }
        catch (ParseException e)
        {
            // Expected
        }

        try
        {
            TypeHandler.createValue("12345", File.class);
            fail();
        }
        catch (ParseException e)
        {
            // Expected
        }

        try
        {
            TypeHandler.createValue("12345", URL.class);
            fail();
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String className = "java.lang.String";
        Object obj = TypeHandler.createObject(className);
        assertEquals(String.class, obj.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_ClassNotFoundException() throws ParseException
    {
        String className = "java.lang.Xyz";
        TypeHandler.createObject(className);
    }

    @Test(expected = InstantiationException.class)
    public void testCreateObject_InstantiationException() throws ParseException, ClassNotFoundException
    {
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);
        try
        {
            TypeHandler.createObject(clazz);
        }
        catch (InstantiationException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test(expected = IllegalAccessException.class)
    public void testCreateObject_IllegalAccessException() throws ParseException, ClassNotFoundException, InstantiationException
    {
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);
        Object instance = clazz.newInstance();
        TypeHandler.createObject(clazz);
    }

    @Test
    public void testCreateNumber() throws ParseException
    {
        String str = "12345";
        Number number = TypeHandler.createNumber(str);
        assertEquals(12345L, number.longValue());

        str = "123.45";
        number = TypeHandler.createNumber(str);
        assertEquals(123.45, number.doubleValue(), 0.001);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumber_NumberFormatException() throws ParseException
    {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException
    {
        String classname = "java.lang.String";
        Class<?> clazz = TypeHandler.createClass(classname);
        assertEquals(String.class, clazz);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateClass_ClassNotFoundException() throws ParseException
    {
        String classname = "java.lang.Xyz";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate() throws ParseException
    {
        throw new UnsupportedOperationException("testCreateDate method is not implemented yet.");
    }

    @Test
    public void testCreateURL() throws ParseException
    {
        String str = "http://www.example.com";
        URL url = TypeHandler.createURL(str);
        assertEquals("http://www.example.com", url.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_MalformedURLException() throws ParseException
    {
        String str = "http://www.example.com/invalid";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile()
    {
        String path = "test.txt";
        File file = TypeHandler.createFile(path);
        assertTrue(new File(path).exists());
        assertTrue(file.equals(new File(path)));
        file.delete();
    }

    @Test(expected = ParseException.class)
    public void testOpenFile()
    {
        String path = "test.txt";
        TypeHandler.openFile(path);
        File file = new File(path);
        assertFalse(file.exists());
    }

    @Test
    public void testCreateFiles() throws IOException
    {
        throw new UnsupportedOperationException("testCreateFiles method is not implemented yet.");
    }
}