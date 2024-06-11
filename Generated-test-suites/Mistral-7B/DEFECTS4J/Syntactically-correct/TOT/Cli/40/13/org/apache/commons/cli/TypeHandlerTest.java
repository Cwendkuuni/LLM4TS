import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * TypeHandlerTest class contains test cases for the TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with String type.
     */
    @Test
    public void testCreateValueString()
    {
        String str = "testString";
        String expected = "testString";

        Object result = null;
        try
        {
            result = TypeHandler.createValue(str, String.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected, result);
    }

    /**
     * Test createValue method with Object type.
     */
    @Test
    public void testCreateValueObject()
    {
        String className = "java.util.Date";
        Date expected = new Date(); // current date

        Object result = null;
        try
        {
            result = TypeHandler.createValue(className, Object.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertTrue(result instanceof Date);
        assertEquals(expected.getClass(), result.getClass());
    }

    /**
     * Test createValue method with Number type.
     */
    @Test
    public void testCreateValueNumber()
    {
        String value = "1234567890.123456";
        double expected = 1234567890.123456;

        Object result = null;
        try
        {
            result = TypeHandler.createValue(value, Double.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected, result);
    }

    /**
     * Test createValue method with Date type.
     */
    @Test
    public void testCreateValueDate()
    {
        String dateString = "2022-01-01";
        Date expected = new Date(0); // default date

        Object result = null;
        try
        {
            result = TypeHandler.createValue(dateString, Date.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertTrue(result instanceof Date);
        assertEquals(expected.getClass(), result.getClass());
    }

    /**
     * Test createValue method with Class type.
     */
    @Test
    public void testCreateValueClass()
    {
        String className = "java.util.Date";
        Class<?> expected = Date.class;

        Object result = null;
        try
        {
            result = TypeHandler.createValue(className, Class.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected, result);
    }

    /**
     * Test createValue method with File type.
     */
    @Test
    public void testCreateValueFile()
    {
        String filePath = "testFile.txt";
        File expected = new File(filePath);

        Object result = null;
        try
        {
            result = TypeHandler.createValue(filePath, File.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected, result);
    }

    /**
     * Test createValue method with existing File type.
     */
    @Test
    public void testCreateValueExistingFile()
    {
        String filePath = "testFile.txt";
        File expected = new File(filePath);

        FileInputStream fileInputStream = null;
        try
        {
            fileInputStream = new FileInputStream(expected);
        }
        catch (FileNotFoundException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        Object result = null;
        try
        {
            result = TypeHandler.createValue(filePath, FileInputStream.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(fileInputStream, result);
    }

    /**
     * Test createValue method with File[] type.
     */
    @Test
    public void testCreateValueFiles()
    {
        String filePaths = "testFile1.txt,testFile2.txt";
        String[] expectedFileNames = {"testFile1.txt", "testFile2.txt"};
        File[] expectedFiles = new File[2];
        expectedFiles[0] = new File(expectedFileNames[0]);
        expectedFiles[1] = new File(expectedFileNames[1]);

        Object result = null;
        try
        {
            result = TypeHandler.createValue(filePaths, File[].class);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertArrayEquals(expectedFiles, result);
    }

    /**
     * Test createValue method with URL type.
     */
    @Test
    public void testCreateValueURL()
    {
        String urlString = "http://www.example.com";
        URL expected = new URL(urlString);

        Object result = null;
        try
        {
            result = TypeHandler.createValue(urlString, URL.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(expected, result);
    }
}