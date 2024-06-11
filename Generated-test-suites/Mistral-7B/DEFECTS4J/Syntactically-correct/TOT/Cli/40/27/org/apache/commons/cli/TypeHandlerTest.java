package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class TypeHandlerTest
{
    @Test
    public void testCreateStringValue() throws ParseException
    {
        String value = "testValue";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(value, clazz);
        Assert.assertEquals(value, obj);
    }

    @Test
    public void testCreateObjectValue() throws ParseException, ClassNotFoundException, InstantiationException
    {
        String classname = "java.util.Date";
        Class<?> clazz = Class.forName(classname);
        Object obj = TypeHandler.createValue("2022-12-31", clazz);
        Assert.assertNotNull(obj);
        Assert.assertTrue(obj instanceof Date);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberValueInvalidString() throws ParseException
    {
        String str = "abc";
        Class<?> clazz = Long.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateNumberValueLong() throws ParseException
    {
        String str = "123";
        Class<?> clazz = Long.class;
        Long number = (Long) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(123L, number.longValue());
    }

    @Test
    public void testCreateNumberValueDouble() throws ParseException
    {
        String str = "3.14";
        Class<?> clazz = Double.class;
        Double number = (Double) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(3.14, number.doubleValue(), 0.01);
    }

    @Test(expected = ParseException.class)
    public void testCreateDateValueInvalidString() throws ParseException
    {
        String str = "invalidDate";
        Class<?> clazz = Date.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateDateValue() throws ParseException
    {
        String str = "2022-12-31";
        Class<?> clazz = Date.class;
        Date date = (Date) TypeHandler.createValue(str, clazz);
        Assert.assertNotNull(date);
        Assert.assertEquals("Sat Dec 31 00:00:00 MST 2022", date.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateClassValue() throws ParseException, UnsupportedOperationException
    {
        String classname = "java.util.Date";
        Class<?> clazz = Class.forName(classname);
        TypeHandler.createValue("java.util.Date", clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateFileValueInvalidString() throws ParseException
    {
        String str = "invalidFile";
        Class<?> clazz = File.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateFileValue() throws ParseException, IOException
    {
        String filePath = "test.txt";
        Class<?> clazz = File.class;
        File file = (File) TypeHandler.createValue(filePath, clazz);
        Assert.assertNotNull(file);
        Assert.assertTrue(new File(filePath).exists());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLValueInvalidString() throws ParseException
    {
        String str = "http://example.com?invalid";
        Class<?> clazz = URL.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateURLValue() throws ParseException
    {
        String urlString = "http://example.com";
        Class<?> clazz = URL.class;
        URL url = (URL) TypeHandler.createValue(urlString, clazz);
        Assert.assertNotNull(url);
        Assert.assertEquals("http", url.getProtocol());
        Assert.assertEquals("example.com", url.getHost());
    }
}