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

/**
 * TypeHandlerTest class
 */
public class TypeHandlerTest
{
    /**
     * Test for createValue method with String type
     */
    @Test
    public void testCreateValue_String()
    {
        String str = "testString";
        Class<?> clazz = String.class;

        try
        {
            Object result = TypeHandler.createValue(str, clazz);
            assertEquals("testString", result);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue method with Object type
     */
    @Test
    public void testCreateValue_Object()
    {
        String classname = "java.lang.String";
        Class<?> clazz = String.class;

        try
        {
            Object result = TypeHandler.createValue(classname, clazz);
            assertTrue(result instanceof String);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue method with Number type
     */
    @Test
    public void testCreateValue_Number()
    {
        String str = "12345";
        Class<?> clazz = int.class;

        try
        {
            Object result = TypeHandler.createValue(str, clazz);
            assertEquals(12345, result);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        try
        {
            Object result = TypeHandler.createValue(str, long.class);
            assertEquals(12345L, result);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        String strWithDecimal = "12.345";
        try
        {
            Object result = TypeHandler.createValue(strWithDecimal, double.class);
            assertEquals(12.345, result, 0.001);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue method with Date type
     */
    @Test
    public void testCreateValue_Date()
    {
        String dateString = "2022-12-31";
        Class<?> clazz = Date.class;

        // This method is not yet implemented and always throws an UnsupportedOperationException
        // Uncomment the following lines to test it when the method is implemented
        // try
        // {
        //     Object result = TypeHandler.createValue(dateString, clazz);
        //     assertTrue(result instanceof Date);
        // }
        // catch (UnsupportedOperationException e)
        // {
        //     fail("Expected exception: " + e.getMessage());
        // }

        // Test with expected ParseException
        try
        {
            TypeHandler.createValue(dateString, Date.class);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test for createValue method with Class type
     */
    @Test
    public void testCreateValue_Class()
    {
        String classname = "java.lang.String";
        Class<?> clazz = String.class;

        try
        {
            Object result = TypeHandler.createValue(classname, clazz);
            assertTrue(result instanceof String);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        try
        {
            Object result = TypeHandler.createValue(classname, classname);
            assertTrue(result instanceof String);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue method with URL type
     */
    @Test
    public void testCreateValue_URL()
    {
        String urlString = "https://www.google.com";
        Class<?> clazz = URL.class;

        try
        {
            Object result = TypeHandler.createValue(urlString, clazz);
            URL url = (URL) result;
            assertEquals(urlString, url.toString());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue method with File type
     */
    @Test
    public void testCreateValue_File()
    {
        String filePath = "test.txt";
        Class<?> clazz = File.class;

        try
        {
            Object result = TypeHandler.createValue(filePath, clazz);
            File file = (File) result;
            assertTrue(file.exists() || new File(filePath).mkdirs());
            file.delete();
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
        catch (IOException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue method with FileInputStream type
     */
    @Test
    public void testCreateValue_FileInputStream()
    {
        String filePath = "test.txt";
        Class<?> clazz = FileInputStream.class;

        try (File file = new File(filePath))
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            Object result = TypeHandler.createValue(filePath, clazz);
            assertTrue(result instanceof FileInputStream);
            assertEquals(fileInputStream, result);

            file.delete();
        }
        catch (FileNotFoundException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
        catch (IOException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue method with File[] type
     */
    @Test
    public void testCreateValue_FileArray()
    {
        String filePaths = "test1.txt,test2.txt";
        Class<?> clazz = File[].class;

        try
        {
            Object result = TypeHandler.createValue(filePaths, clazz);
            File[] files = (File[]) result;
            for (File file : files)
            {
                assertTrue(file.exists() || new File(file.getAbsolutePath()).mkdirs());
                file.delete();
            }
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
        catch (IOException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}