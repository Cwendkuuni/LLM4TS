import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testCreateValue_StringObject()
    {
        Object value = null;
        String str = "sampleString";
        Class<?> clazz = String.class;

        try
        {
            value = TypeHandler.createValue(str, clazz);
            assertEquals("sampleString", value.toString());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testCreateValue_StringClassObject()
    {
        Object value = null;
        String str = "sampleString";
        Class<?> clazz = String.class;

        try
        {
            value = TypeHandler.createValue(str, clazz);
            assertEquals("sampleString", value.toString());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        try
        {
            value = TypeHandler.createValue(str, Integer.class);
            fail("Expected ParseException but got " + value.getClass().getName() + " instead.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    @Test
    public void testCreateValue_StringClassNumber()
    {
        Number value = null;
        String str = "123";
        Class<?> clazz = Integer.class;

        try
        {
            value = (Number) TypeHandler.createValue(str, clazz);
            assertEquals(123, value.intValue());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        try
        {
            value = (Number) TypeHandler.createValue("123.4", clazz);
            fail("Expected ParseException but got " + value.getClass().getName() + " instead.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    @Test
    public void testCreateValue_StringClassDate()
    {
        Date value = null;
        String str = "2022-12-31";
        Class<?> clazz = Date.class;

        try
        {
            value = (Date) TypeHandler.createValue(str, clazz);
            assertNotNull(value);
            assertEquals(2022, value.getYear());
            assertEquals(12, value.getMonth());
            assertEquals(31, value.getDay());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        try
        {
            value = (Date) TypeHandler.createValue("Invalid date", clazz);
            fail("Expected ParseException but got " + value.getClass().getName() + " instead.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    @Test
    public void testCreateValue_StringClassClass()
    {
        Class<?> value = null;
        String str = "java.lang.String";
        Class<?> clazz = String.class;

        try
        {
            value = (Class<?>) TypeHandler.createValue(str, clazz);
            assertEquals(String.class, value);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        try
        {
            value = (Class<?>) TypeHandler.createValue("java.util.Date", clazz);
            fail("Expected ParseException but got " + value.getClass().getName() + " instead.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    @Test
    public void testCreateFile()
    {
        File value = null;
        String str = "/path/to/sample.txt";

        try
        {
            value = TypeHandler.createFile(str);
            assertTrue(value.exists());
            assertTrue(value.isFile());
            assertTrue(value.canRead());
            assertTrue(value.canWrite());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
        finally
        {
            if (value != null)
            {
                try
                {
                    value.delete();
                }
                catch (IOException e)
                {
                    fail("Unexpected exception: " + e.getMessage());
                }
            }
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateFile_InvalidPath()
    {
        String str = "Invalid path";
        try
        {
            TypeHandler.createFile(str);
        }
        catch (FileNotFoundException e)
        {
            fail("Expected ParseException but got " + e.getClass().getName() + " instead.");
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateURL()
    {
        String str = "http://www.example.com/sample?query=test";
        try
        {
            URL value = TypeHandler.createURL(str);
            assertTrue(value.toString().startsWith(str));
        }
        catch (MalformedURLException e)
        {
            fail("Expected ParseException but got " + e.getClass().getName() + " instead.");
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidFormat()
    {
        String str = "invalidURL";
        try
        {
            URL value = TypeHandler.createURL(str);
            fail("Expected ParseException but got " + value.getClass().getName() + " instead.");
        }
        catch (MalformedURLException e)
        {
            // Expected
        }
    }
}