package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.utils.PatternOptionBuilder;

import org.junit.Test;

/**
 * Test class for TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test creating value of String type.
     */
    @Test
    public void testCreateValueString()
    {
        String str = "TestString";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(str, obj);
    }

    /**
     * Test creating value of Object type.
     */
    @Test
    public void testCreateValueObject()
    {
        String classname = "java.lang.String";
        Object obj = TypeHandler.createValue("TestString", classname);
        assertInstanceOf(String.class, obj);
    }

    /**
     * Test creating value of Number type.
     */
    @Test
    public void testCreateValueNumber()
    {
        String str = "123";
        Number number = TypeHandler.createValue(str, int.class);
        assertEquals((int) 123, number.intValue());

        str = "123.45";
        number = TypeHandler.createValue(str, double.class);
        assertEquals(123.45, number.doubleValue(), 0.001);
    }

    /**
     * Test creating value of Date type.
     * <p>
     * This test is not yet implemented and always throws an
     * {@link UnsupportedOperationException}.
     */
    @Test
    public void testCreateValueDate()
    {
        String str = "2022-12-31";
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(str, Date.class));
    }

    /**
     * Test creating value of Class type.
     */
    @Test
    public void testCreateValueClass()
    {
        String classname = "java.lang.String";
        Object obj = TypeHandler.createValue("TestString", Class.forName(classname));
        assertEquals(classname, obj.getClass().getName());
    }

    /**
     * Test creating value of File type.
     */
    @Test
    public void testCreateValueFile()
    {
        String filePath = "/test/testFile.txt";
        File file = new File(filePath);
        Object obj = TypeHandler.createValue(filePath, File.class);
        assertEquals(file, obj);
    }

    /**
     * Test creating value of ExistingFile type.
     */
    @Test
    public void testCreateValueExistingFile()
    {
        String filePath = "/test/testFile.txt";
        File file = new File(filePath);
        if (file.exists())
        {
            Object obj = TypeHandler.createValue(filePath, File.class);
            assertEquals(file, obj);
        }
        else
        {
            assertNull(TypeHandler.createValue(filePath, File.class));
        }
    }

    /**
     * Test creating value of Files type.
     * <p>
     * This test is not yet implemented and always throws an
     * {@link UnsupportedOperationException}.
     */
    @Test
    public void testCreateValueFiles()
    {
        String filePath = "/test/testDir/";
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue(filePath, File[].class));
    }

    /**
     * Test creating value of URL type.
     */
    @Test
    public void testCreateValueURL()
    {
        String urlString = "http://example.com";
        URL url = new URL(urlString);
        Object obj = TypeHandler.createValue(urlString, URL.class);
        assertEquals(url, obj);
    }

    /**
     * Test creating value of unsupported type.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueUnsupportedType()
    {
        String str = "TestValue";
        Object obj = TypeHandler.createValue(str, int[].class);
        assertNull(obj);
    }
}