package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Test cases for TypeHandler class
 */
public class TypeHandlerTest
{
    @Test
    public void testStringValue() throws ParseException
    {
        String str = "Test String";
        String expected = "Test String";
        Class<?> clazz = String.class;

        Object actual = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testObjectValueNull() throws ParseException
    {
        String str = null;
        Class<?> clazz = Object.class;

        try
        {
            Object actual = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            // expected
        }
    }

    @Test
    public void testObjectValueEmpty() throws ParseException, InstantiationException, IllegalAccessException
    {
        String str = "";
        Class<?> clazz = String.class;

        try
        {
            Class<?> cl = Class.forName("java.lang.String");
            Object actual = TypeHandler.createValue(str, (Class<Object>) cl);
            Assert.assertEquals("", actual);
        }
        catch (ClassNotFoundException e)
        {
            throw new ParseException("Unable to find the class: java.lang.String");
        }
    }

    @Test
    public void testObjectValueNonExistentClass() throws ParseException
    {
        String str = "com.test.NonExistentClass";
        Class<?> clazz = Object.class;

        try
        {
            Object actual = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            String expectedMessage = "Unable to find the class: com.test.NonExistentClass";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testObjectValue() throws ParseException, InstantiationException, IllegalAccessException
    {
        String str = "java.lang.String";
        Class<?> clazz = String.class;

        Object actual = TypeHandler.createValue(str, clazz);
        Assert.assertEquals("", (String) actual);
    }

    @Test
    public void testNumberValueInt() throws ParseException
    {
        String str = "123";
        int expected = 123;
        Class<?> clazz = int.class;

        Number actual = (Number) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, actual.intValue());
    }

    @Test
    public void testNumberValueLong() throws ParseException
    {
        String str = "456";
        long expected = 456;
        Class<?> clazz = long.class;

        Number actual = (Number) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, actual.longValue());
    }

    @Test
    public void testNumberValueDouble() throws ParseException
    {
        String str = "123.45";
        double expected = 123.45;
        Class<?> clazz = double.class;

        Number actual = (Number) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, actual.doubleValue(), 0.001);
    }

    @Test
    public void testDateValue() throws ParseException
    {
        String str = "2022-03-01";
        Date expected = new Date(1646300800000L); // 1st March 2022
        Class<?> clazz = Date.class;

        Date actual = (Date) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDateValueInvalidFormat() throws ParseException
    {
        String str = "2022-3-01"; // missing month separator
        Class<?> clazz = Date.class;

        try
        {
            Date actual = (Date) TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            String expectedMessage = "Unable to parse date: 2022-3-01";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testClassValue() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String str = "java.lang.String";
        Class<?> clazz = String.class;

        Class<?> actual = (Class<?>) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(String.class, actual);
    }

    @Test
    public void testClassValueNonExistentClass() throws ParseException
    {
        String str = "com.test.NonExistentClass";
        Class<?> clazz = Object.class;

        try
        {
            Object actual = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            String expectedMessage = "Unable to find the class: com.test.NonExistentClass";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testFileValue() throws ParseException, IOException
    {
        String str = "test.txt";
        File expected = new File("test.txt");
        Class<?> clazz = File.class;

        File actual = (File) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFileValueNonExistentFile() throws ParseException
    {
        String str = "non-existent.txt";
        Class<?> clazz = File.class;

        try
        {
            Object actual = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            String expectedMessage = "Unable to find file: non-existent.txt";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testFileValueInvalidPath() throws ParseException, IOException
    {
        String str = "..\\test.txt"; // relative path
        Class<?> clazz = File.class;

        try
        {
            Object actual = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            String expectedMessage = "Unable to parse file path: ../test.txt";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }

    @Test
    public void testURLValue() throws ParseException, MalformedURLException
    {
        String str = "https://www.example.com";
        URL expected = new URL("https", "www.example.com", 443);
        Class<?> clazz = URL.class;

        URL actual = (URL) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testURLValueInvalidScheme() throws ParseException, MalformedURLException
    {
        String str = "ftp://www.example.com";
        Class<?> clazz = URL.class;

        try
        {
            Object actual = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            String expectedMessage = "Unable to parse URL: ftp://www.example.com";
            Assert.assertEquals(expectedMessage, e.getMessage());
        }
    }
}