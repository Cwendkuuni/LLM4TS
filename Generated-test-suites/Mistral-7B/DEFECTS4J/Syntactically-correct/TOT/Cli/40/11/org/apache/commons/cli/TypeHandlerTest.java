package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * TypeHandlerTest class for testing TypeHandler class methods.
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

        Object actual = null;
        try
        {
            actual = TypeHandler.createValue(str, String.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        assertEquals(expected, actual);
    }

    /**
     * Test createValue method with Object type.
     */
    @Test
    public void testCreateValueObject()
    {
        String className = "java.util.Date";
        Date expected = new Date();

        Object actual = null;
        try
        {
            actual = TypeHandler.createValue(className, Object.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        assertEquals(expected.getClass(), actual.getClass());

        // Test if the created object equals the expected one
        Date createdDate = (Date) actual;
        assertEquals(expected.toString(), createdDate.toString());
    }

    /**
     * Test createValue method with Number type.
     */
    @Test
    public void testCreateValueNumber()
    {
        String numberString = "123";
        long expectedLong = 123;
        double expectedDouble = 123.0;

        Object actual = null;
        try
        {
            actual = TypeHandler.createValue(numberString, Number.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        if (actual instanceof Long)
        {
            assertEquals(expectedLong, ((Long) actual).longValue());
        }
        else if (actual instanceof Double)
        {
            assertEquals(expectedDouble, ((Double) actual).doubleValue(), 0.0);
        }
        else
        {
            fail("Unexpected type of returned object.");
        }
    }

    /**
     * Test createValue method with Date type.
     * This method is not yet implemented and always throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate()
    {
        String dateString = "2022-12-31";
        TypeHandler.createValue(dateString, Date.class);
    }

    /**
     * Test createValue method with Class type.
     */
    @Test
    public void testCreateValueClass()
    {
        String className = "java.util.Date";

        Object actual = null;
        try
        {
            actual = TypeHandler.createValue(className, Class.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        assertEquals(className, actual.toString());
    }

    /**
     * Test createValue method with File type.
     */
    @Test
    public void testCreateValueFile()
    {
        String filePath = "/test/file.txt";
        File expected = new File(filePath);

        File actual = null;
        try
        {
            actual = (File) TypeHandler.createValue(filePath, File.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
    }

    /**
     * Test createValue method with existing File type.
     */
    @Test
    public void testCreateValueExistingFile()
    {
        String filePath = "/test/file.txt";
        File expected = new File(filePath);

        FileInputStream expectedFileInputStream = null;
        try
        {
            expectedFileInputStream = new FileInputStream(expected);
        }
        catch (IOException e)
        {
            fail("Unexpected IOException: " + e.getMessage());
        }

        File actual = null;
        try
        {
            actual = (File) TypeHandler.openFile(filePath);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        FileInputStream actualFileInputStream = null;
        try
        {
            actualFileInputStream = new FileInputStream(actual);
        }
        catch (FileNotFoundException e)
        {
            fail("Unexpected FileNotFoundException: " + e.getMessage());
        }

        assertEquals(expected.getAbsolutePath(), actual.getAbsolutePath());
        assertEquals(expectedFileInputStream, actualFileInputStream);
    }

    /**
     * Test createValue method with File[] type.
     * This method is not yet implemented and always throws an UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFiles()
    {
        String filePathsString = "/test/file1.txt,/test/file2.txt";
        TypeHandler.createValue(filePathsString, File[].class);
    }

    /**
     * Test createValue method with URL type.
     */
    @Test
    public void testCreateValueURL()
    {
        String urlString = "http://www.example.com";
        URL expected = new URL(urlString);

        URL actual = null;
        try
        {
            actual = (URL) TypeHandler.createValue(urlString, URL.class);
        }
        catch (ParseException e)
        {
            fail("Unexpected ParseException: " + e.getMessage());
        }

        assertEquals(expected.toString(), actual.toString());
    }
}