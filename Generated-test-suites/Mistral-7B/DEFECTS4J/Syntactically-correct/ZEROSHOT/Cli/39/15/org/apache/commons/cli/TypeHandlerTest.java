import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.cli.PatternOptionBuilder.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringString() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValueStringClass() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(str, obj);

        // Test for other types
        File file = new File("test.txt");
        Object fileObj = TypeHandler.createValue(file.getAbsolutePath(), File.class);
        assertEquals(file, fileObj);

        Class<?> clazz = String.class;
        Object clazzObj = TypeHandler.createValue("java.lang.String", clazz);
        assertEquals(clazz, clazzObj.getClass());

        Date date = new Date();
        Object dateObj = TypeHandler.createValue(date.toString(), Date.class);
        assertEquals(date, dateObj);

        URL url = new URL("http://www.example.com");
        Object urlObj = TypeHandler.createValue(url.toString(), URL.class);
        assertEquals(url, urlObj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringInvalidType() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, Integer.class);
        // This should throw ParseException
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.lang.String";
        Object obj = TypeHandler.createObject(classname);
        assertNotNull(obj);
        assertEquals(classname, obj.getClass().getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidString() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createNumber(str);
        // This should throw ParseException
    }

    @Test
    public void testCreateNumberLongString() throws ParseException
    {
        String str = "123";
        Object obj = TypeHandler.createNumber(str);
        Long number = (Long) obj;
        assertEquals(123L, number.longValue());
    }

    @Test
    public void testCreateNumberDoubleString() throws ParseException
    {
        String str = "123.45";
        Object obj = TypeHandler.createNumber(str);
        Double number = (Double) obj;
        assertEquals(123.45, number.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateDateInvalidString() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createDate(str);
        // This should throw ParseException
    }

    @Test
    public void testCreateURLValidString() throws ParseException, MalformedURLException
    {
        String str = "http://www.example.com";
        Object obj = TypeHandler.createURL(str);
        URL url = (URL) obj;
        assertEquals("http://www.example.com", url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidString() throws ParseException, MalformedURLException
    {
        String str = "test";
        Object obj = TypeHandler.createURL(str);
        // This should throw ParseException
    }

    @Test
    public void testCreateFileValidString()
    {
        String str = "test.txt";
        Object obj = TypeHandler.createFile(str);
        File file = (File) obj;
        assertEquals(new File(str), file);
    }

    @Test(expected = ParseException.class)
    public void testCreateFileInvalidString()
    {
        String str = "test";
        Object obj = TypeHandler.createFile(str);
        // This should throw ParseException
    }
}