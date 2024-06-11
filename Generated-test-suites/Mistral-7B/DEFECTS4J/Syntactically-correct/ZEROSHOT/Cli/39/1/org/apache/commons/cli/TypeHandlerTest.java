package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;

import org.junit.Test;

/**
 * Test for TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with string type.
     */
    @Test
    public void testCreateValueString()
    {
        String str = "test";
        String expected = "test";
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, String.class);
        }
        catch (ParseException e)
        {
            // Should not throw an exception
        }

        assertEquals(expected, result);
    }

    /**
     * Test createValue method with Object type.
     */
    @Test
    public void testCreateValueObject()
    {
        String classname = "java.lang.Integer";
        Integer expected = 0;
        Object result = null;

        try
        {
            result = TypeHandler.createValue(classname, Object.class);
        }
        catch (ParseException e)
        {
            // Should not throw an exception
        }

        assertEquals(expected.getClass(), result.getClass());
        assertEquals(expected, result);
    }

    /**
     * Test createValue method with number type.
     */
    @Test
    public void testCreateValueNumber()
    {
        String str = "123";
        Long expected = 123L;
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, Long.class);
        }
        catch (ParseException e)
        {
            // Should not throw an exception
        }

        assertEquals(expected, result);
    }

    /**
     * Test createValue method with date type.
     */
    @Test
    public void testCreateValueDate()
    {
        String str = "2022-12-31";
        Date expected = new Date(2022, 11, 31);
        Object result = null;

        // UnsupportedOperationException is thrown in TypeHandler.createDate() method
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(str, Date.class));

        // Test with expected exception
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, Date[].class));

        // Test null result
        result = TypeHandler.createValue(str, String.class);
        assertNull(result);
    }

    /**
     * Test createValue method with class type.
     */
    @Test
    public void testCreateValueClass()
    {
        String classname = "java.lang.Integer";
        Integer expected = 0;
        Object result = null;

        try
        {
            result = TypeHandler.createValue(classname, Integer.class);
        }
        catch (ParseException e)
        {
            // Should not throw an exception
        }

        assertEquals(expected.getClass(), result.getClass());
        assertEquals(expected, result);
    }

    /**
     * Test createValue method with URL type.
     */
    @Test
    public void testCreateValueURL()
    {
        String str = "http://example.com";
        URL expected = new URL("http://example.com");
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, URL.class);
        }
        catch (ParseException e)
        {
            // Should not throw an exception
        }

        assertEquals(expected, result);
    }

    /**
     * Test createValue method with file type.
     */
    @Test
    public void testCreateValueFile()
    {
        String str = "test.txt";
        File expected = new File(str);
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, File.class);
        }
        catch (ParseException e)
        {
            // Should not throw an exception
        }

        assertEquals(expected, result);
    }

    /**
     * Test createValue method with existing file value type.
     */
    @Test
    public void testCreateValueExistingFile()
    {
        String str = "test.txt";
        File expected = new File(str);
        Object result = null;

        try
        {
            result = TypeHandler.createValue(str, File.class, true);
        }
        catch (ParseException e)
        {
            // Should not throw an exception
        }

        assertEquals(expected, result);
    }

    /**
     * Test createValue method with file[] type.
     */
    @Test
    public void testCreateValueFiles()
    {
        String str = "test1.txt, test2.txt";
        File[] expected = new File[] { new File("test1.txt"), new File("test2.txt") };
        Object result = null;

        // UnsupportedOperationException is thrown in TypeHandler.createFiles() method
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(str, File[].class));

        // Test null result
        result = TypeHandler.createValue(str, String[].class);
        assertNull(result);

        // Test empty array result
        result = TypeHandler.createValue(str, File[].class);
        assertNull(result);

        // Test with expected exception
        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, String[].class));
    }

    /**
     * Test createObject method.
     */
    @Test
    public void testCreateObject()
    {
        String classname = "java.lang.Integer";
        Integer expected = 0;
        Object result = null;

        try
        {
            result = TypeHandler.createObject(classname);
        }
        catch (ParseException e)
        {
            // Should not throw an exception
        }

        assertEquals(expected.getClass(), result.getClass());
        assertEquals(expected, result);
    }

    /**
     * Test createNumber method.
     */
    @Test
    public void testCreateNumber()
    {
        String str = "123";
        Number expected = 123;
        Number result = null;

        try
        {
            result = TypeHandler.createNumber(str);
        }
        catch (ParseException e)
        {
            // Should not throw an exception
        }

        assertEquals(expected.getClass(), result.getClass());
        assertEquals(expected, result);
    }
}