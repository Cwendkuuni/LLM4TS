package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.util.ClassLoaderUtils;

import org.junit.Test;

/**
 * Test Class for TypeHandler
 */
public class TypeHandlerTest
{
    /**
     * Test createValue(String, Class) method with String argument.
     */
    @Test
    public void testCreateValueStringString()
    {
        String str = "test";
        String expected = "test";
        Class<?> clazz = String.class;

        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(expected, result);
    }

    /**
     * Test createValue(String, Class) method with Object argument.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueStringObject()
    {
        String str = "test";
        Class<?> clazz = Object.class;

        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test createValue(String, Class) method with class ObjectValue.
     */
    @Test
    public void testCreateValueStringObjectValue()
    {
        String str = "test";
        Class<?> clazz = PatternOptionBuilder.OBJECT_VALUE;

        Object result = TypeHandler.createValue(str, clazz);
        assertInstanceOf(Object.class, result);
    }

    /**
     * Test createValue(String, Class) method with class StringValue.
     */
    @Test
    public void testCreateValueStringStringValue()
    {
        String str = "test";
        Class<?> clazz = PatternOptionBuilder.STRING_VALUE;

        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    /**
     * Test createValue(String, Class) method with class NumberValue.
     */
    @Test
    public void testCreateValueStringNumberValue()
    {
        String str = "123";
        Class<?> clazz = PatternOptionBuilder.NUMBER_VALUE;

        Number result = (Number) TypeHandler.createValue(str, clazz);
        assertEquals(123L, result.longValue());
    }

    /**
     * Test createValue(String, Class) method with class NumberValue and Double.
     */
    @Test
    public void testCreateValueStringNumberValueDouble()
    {
        String str = "123.45";
        Class<?> clazz = PatternOptionBuilder.NUMBER_VALUE;

        Number result = (Number) TypeHandler.createValue(str, clazz);
        assertEquals(123.45, result.doubleValue(), 0.01);
    }

    /**
     * Test createValue(String, Class) method with class DateValue.
     * This method is not yet implemented and always throws an
     * UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringDateValue()
    {
        String str = "2022-12-31";
        Class<?> clazz = PatternOptionBuilder.DATE_VALUE;

        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test createValue(String, Class) method with class ClassValue.
     */
    @Test
    public void testCreateValueStringClassValue()
    {
        String classname = "java.lang.String";
        Class<?> expected = String.class;

        Class<?> result = (Class<?>) TypeHandler.createValue(classname, Class.class);
        assertEquals(expected, result);
    }

    /**
     * Test createValue(String, Class) method with class FileValue.
     */
    @Test
    public void testCreateValueStringFileValue()
    {
        String filePath = "/test/TestFile.txt";
        File expectedFile = new File(filePath);

        File result = (File) TypeHandler.createValue(filePath, File.class);
        assertEquals(expectedFile, result);
    }

    /**
     * Test createValue(String, Class) method with class ExistingFileValue.
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateValueStringExistingFileValue()
    {
        String filePath = "/test/NotFoundFile.txt";
        Class<?> clazz = PatternOptionBuilder.EXISTING_FILE_VALUE;

        TypeHandler.createValue(filePath, clazz);
    }

    /**
     * Test createValue(String, Class) method with class FilesValue.
     * This method is not yet implemented and always throws an
     * UnsupportedOperationException.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueStringFilesValue()
    {
        String filePaths = "/test/TestFile1.txt,/test/TestFile2.txt";
        Class<?> clazz = PatternOptionBuilder.FILES_VALUE;

        TypeHandler.createValue(filePaths, clazz);
    }

    /**
     * Test createValue(String, Class) method with class UrlValue.
     */
    @Test
    public void testCreateValueStringUrlValue()
    {
        String urlStr = "http://test.com";
        URL expectedUrl = new URL(urlStr);

        URL result = TypeHandler.createValue(urlStr, URL.class);
        assertEquals(expectedUrl, result);
    }

    /**
     * Test createObject method with valid classname.
     */
    @Test
    public void testCreateObjectValidClassname()
    {
        String classname = "java.lang.String";
        Class<?> expected = String.class;

        Class<?> result = TypeHandler.createObject(classname);
        assertEquals(expected, result);
    }

    /**
     * Test createObject method with invalid classname.
     */
    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassname()
    {
        String classname = "java.lang.TestClass";

        TypeHandler.createObject(classname);
    }

    /**
     * Test createNumber method with valid number.
     */
    @Test
    public void testCreateNumberValidNumber()
    {
        String numberStr = "123";
        Number expected = 123;
        Number result = TypeHandler.createNumber(numberStr);

        assertEquals(expected.longValue(), result.longValue());
    }

    /**
     * Test createNumber method with invalid number.
     */
    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidNumber()
    {
        String numberStr = "test";

        TypeHandler.createNumber(numberStr);
    }
}