import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class TypeHandlerTest
{
    @Test
    public void testCreateValue_String_Class_StringValue() throws ParseException
    {
        String str = "testStringValue";
        Class<?> clazz = String.class;
        Object actual = TypeHandler.createValue(str, clazz);
        String expected = str;

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValue_String_Class_ObjectValue_InvalidClass() throws ParseException
    {
        String str = "testObjectValue";
        Class<?> clazz = Integer.class;

        try
        {
            TypeHandler.createValue(str, clazz);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testCreateValue_String_Class_ObjectValue() throws ParseException
    {
        String str = "testObjectValue";
        Class<?> clazz = String.class;
        Object actual = TypeHandler.createValue(str, clazz);
        String expected = str;

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValue_String_Class_NumberValue_Long() throws ParseException
    {
        String str = "123";
        Class<?> clazz = Integer.class;
        Object actual = TypeHandler.createValue(str, clazz);
        int expected = 123;

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValue_String_Class_NumberValue_Double() throws ParseException
    {
        String str = "123.45";
        Class<?> clazz = Double.class;
        Object actual = TypeHandler.createValue(str, clazz);
        double expected = 123.45;

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateValue_String_Class_DateValue() throws ParseException
    {
        String str = "2022-12-31";
        Class<?> clazz = Date.class;

        try
        {
            TypeHandler.createValue(str, clazz);
            fail("Expected UnsupportedOperationException");
        }
        catch (UnsupportedOperationException e)
        {
            // expected
        }
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.lang.String";
        Class<?> clazz = Class.forName(classname);
        Object actual = TypeHandler.createObject(classname);
        String expected = "";

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected, actual.toString());
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException
    {
        String str = "http://example.com";
        URL actual = TypeHandler.createURL(str);
        URL expected = new URL("http://example.com");

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateFile()
    {
        String str = "testFile.txt";
        File actual = TypeHandler.createFile(str);
        File expected = new File(str);

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateFile_InvalidPath()
    {
        String str = "testFile:/";
        File actual = TypeHandler.createFile(str);

        Assert.assertNull(actual);
    }
}