import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(str, obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringUnsupportedType() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, Integer.class);
        fail("This method should throw ParseException when given unsupported type");
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        fail("This method should throw ParseException when given unsupported type");
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() throws ParseException
    {
        String str = "123";
        Number num = TypeHandler.createValue(str, Number.class);
        assertEquals(123, num.longValue());

        str = "456.78";
        num = TypeHandler.createValue(str, Double.class);
        assertEquals(456.78, num.doubleValue(), 0.0);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderDateValue() throws ParseException
    {
        String str = "2023-03-01";
        Date date = TypeHandler.createValue(str, Date.class);
        fail("This method should throw ParseException when given unsupported type or not yet implemented");
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String str = "java.lang.String";
        Class<?> clazz = TypeHandler.createValue(str, Class.class);
        assertEquals(String.class, clazz);

        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(String.class, obj.getClass());
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue()
    {
        String str = "/tmp/test.txt";
        File file = TypeHandler.createValue(str, File.class);
        assertNotNull(file);
        assertTrue(file.exists());

        str = "/tmp/test_does_not_exist.txt";
        file = TypeHandler.createValue(str, File.class);
        assertNull(file);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringPatternOptionBuilderFileValueInvalidPath() throws ParseException
    {
        String str = "/tmp:test.txt";
        File file = TypeHandler.createValue(str, File.class);
        fail("This method should throw ParseException when given invalid path");
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderExistingFileValue()
    {
        String str = "/tmp/test.txt";
        File file = TypeHandler.createValue(str, File.class);
        assertNotNull(file);
        assertTrue(file.exists());

        str = "/tmp/test_does_not_exist.txt";
        file = TypeHandler.createValue(str, File.class);
        assertNull(file);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValueStringPatternOptionBuilderExistingFileValueNonExistentFile()
            throws FileNotFoundException, ParseException
    {
        String str = "/tmp/test_does_not_exist.txt";
        File file = TypeHandler.createValue(str, File.class);
        fail("This method should throw FileNotFoundException when given non-existent file");
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderURLValue() throws MalformedURLException, ParseException
    {
        String str = "http://www.example.com";
        URL url = TypeHandler.createValue(str, URL.class);
        assertEquals("http://www.example.com", url.toString());

        str = "file:/tmp/test.txt";
        url = TypeHandler.createValue(str, URL.class);
        assertEquals("file:/tmp/test.txt", url.toString());

        str = "/tmp/test.txt";
        url = TypeHandler.createValue(str, URL.class);
        fail("This method should throw ParseException when given invalid URL format");
    }
}