package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.commons.BaseParser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    private static BaseParser parser;

    @BeforeClass
    public static void setUp()
    {
        parser = new BaseParser();
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with a String.
     */
    @Test
    public void testCreateValueString()
    {
        Object value = null;
        try
        {
            value = TypeHandler.createValue("test", String.class);
            assertEquals("test", value);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with a Object.
     */
    @Test
    public void testCreateValueObject()
    {
        try
        {
            Class<?> clazz = Class.forName("java.lang.Integer");
            Object value = TypeHandler.createValue("42", clazz);
            assertEquals(42, value);
        }
        catch (ClassNotFoundException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with a Number.
     */
    @Test
    public void testCreateValueNumber()
    {
        Object value = null;
        try
        {
            value = TypeHandler.createValue("42", Number.class);
            assertEquals(42L, value);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        try
        {
            value = TypeHandler.createValue("42.3", Number.class);
            assertEquals(42.3, value);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with a Date.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate()
    {
        TypeHandler.createValue("2022-01-01", Date.class);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with a Class.
     */
    @Test
    public void testCreateValueClass()
    {
        try
        {
            Class<?> clazz = Class.forName("java.lang.Integer");
            Object value = TypeHandler.createValue("java.lang.Integer", clazz);
            assertEquals(Integer.class, value.getClass());
        }
        catch (ClassNotFoundException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with a URL.
     */
    @Test
    public void testCreateValueURL()
    {
        Object value = null;
        try
        {
            value = TypeHandler.createValue("http://localhost:8080", URL.class);
            URL url = (URL) value;
            assertEquals("http", url.getProtocol());
            assertEquals("//localhost:8080", url.toString());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
        catch (MalformedURLException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with a File.
     */
    @Test
    public void testCreateValueFile()
    {
        Object value = null;
        try
        {
            value = TypeHandler.createValue("/path/to/file.txt", File.class);
            File file = (File) value;
            assertTrue(file.exists() || new File("/path/to/file.txt").isDirectory());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
        catch (FileNotFoundException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Class)} with a FileInputStream.
     */
    @Test
    public void testCreateValueFileInputStream()
    {
        Object value = null;
        String filePath = "/path/to/file.txt";
        try
        {
            File file = new File(filePath);
            value = TypeHandler.createValue(filePath, FileInputStream.class);
            FileInputStream inputStream = (FileInputStream) value;
            byte[] bytes = new byte[1024];
            int length = inputStream.read(bytes, 0, bytes.length);
            assertNotNull(length);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
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
     * Test method for {@link TypeHandler#createValue(String, Class)} with a File[] (not yet implemented).
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFiles()
    {
        TypeHandler.createValue("path/to/file1,path/to/file2", File[].class);
    }
}