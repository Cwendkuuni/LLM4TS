package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Test cases for TypeHandler class
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method with String class
     */
    @Test
    public void testCreateStringValue() throws ParseException
    {
        String str = "testString";
        Class<?> clazz = String.class;
        Object expected = str;
        Object actual = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test createValue method with Object class
     */
    @Test
    public void testCreateObjectValue() throws ParseException, ClassNotFoundException, InstantiationException
    {
        String className = "java.lang.String";
        Object expected = new String("testObject");
        Object actual = TypeHandler.createValue("testObject", Class.forName(className));
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test createValue method with Number class
     */
    @Test
    public void testCreateNumberValue() throws ParseException
    {
        String numberStr = "123";
        Class<?> clazz = Long.class;
        Long expected = 123L;
        Object actual = TypeHandler.createValue(numberStr, clazz);
        Assert.assertEquals(expected, actual);

        String numberStrWithDecimal = "123.45";
        Class<?> clazzDouble = Double.class;
        Double expectedDouble = 123.45;
        Object actualDouble = TypeHandler.createValue(numberStrWithDecimal, clazzDouble);
        Assert.assertEquals(expectedDouble, actualDouble);
    }

    /**
     * Test createValue method with Date class
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateValue() throws ParseException
    {
        String dateStr = "2023-03-16";
        Class<?> clazz = Date.class;
        TypeHandler.createValue(dateStr, clazz);
    }

    /**
     * Test createValue method with Class class
     */
    @Test
    public void testCreateClassValue() throws ParseException, ClassNotFoundException
    {
        String className = "java.lang.String";
        Class<?> expected = Class.forName(className);
        Class<?> actual = (Class<?>) TypeHandler.createValue(className, Class.class);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test createValue method with File class
     */
    @Test
    public void testCreateFileValue()
    {
        String filePath = "testFile.txt";
        Class<?> clazz = File.class;
        Object expected = new File(filePath);
        Object actual = TypeHandler.createValue(filePath, clazz);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test createValue method with URL class
     */
    @Test
    public void testCreateURLValue() throws ParseException
    {
        String urlString = "http://example.com";
        Class<?> clazz = URL.class;
        Object expected = new URL(urlString);
        Object actual = TypeHandler.createValue(urlString, clazz);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Test createValue method with File value and File class
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateFileValueWithException() throws ParseException
    {
        String filePath = "nonExistentFile.txt";
        Class<?> clazz = File.class;
        Object actual = TypeHandler.createValue(filePath, clazz);
    }

    /**
     * Test createValue method with Class[] value and Class[String] class
     */
    @Test
    public void testCreateClassValueArray() throws ParseException, ClassNotFoundException
    {
        String className = "java.lang.String[]";
        String[] expected = new String[]{"test1", "test2"};
        Object actual = TypeHandler.createValue("[Ljava.lang.String;", Class.forName("java.lang.String[]"));
        Assert.assertArrayEquals(expected, (String[]) actual);
    }
}