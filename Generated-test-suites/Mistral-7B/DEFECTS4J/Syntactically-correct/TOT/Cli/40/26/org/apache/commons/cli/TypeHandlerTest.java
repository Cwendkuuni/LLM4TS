package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class TypeHandlerTest
{
    @Test
    public void testCreateStringValue() throws ParseException
    {
        String str = "Test String";
        String expected = "Test String";
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCreateObjectValueNull() throws ParseException
    {
        String classname = null;
        Class<?> clazz = Object.class;
        Object result = TypeHandler.createValue(classname, clazz);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateObjectValueValid() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.Date";
        Class<?> clazz = Date.class;
        Object result = TypeHandler.createValue(classname, clazz);
        Assert.assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberValueInvalid() throws ParseException
    {
        String str = "abc";
        Class<?> clazz = Number.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateNumberValueLong() throws ParseException
    {
        String str = "123";
        Class<?> clazz = Long.class;
        Long expected = 123L;
        Long result = (Long) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCreateNumberValueDouble() throws ParseException
    {
        String str = "123.45";
        Class<?> clazz = Double.class;
        Double expected = 123.45;
        Double result = (Double) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassValueInvalid() throws ParseException
    {
        String classname = "NonExistentClass";
        Class<?> clazz = String.class;
        TypeHandler.createValue(classname, clazz);
    }

    @Test
    public void testCreateClassValueValid() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.Date";
        Class<?> clazz = Date.class;
        Object result = TypeHandler.createValue(classname, clazz);
        Assert.assertInstanceOf(Date.class, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateValue() throws ParseException
    {
        String str = "2022-01-01";
        Class<?> clazz = Date.class;
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURLValueValid() throws ParseException, MalformedURLException
    {
        String str = "https://www.google.com";
        Class<?> clazz = URL.class;
        URL expected = new URL("https://www.google.com");
        URL result = (URL) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLValueInvalid() throws ParseException
    {
        String str = "abc";
        Class<?> clazz = URL.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateFileValueValid() throws ParseException, IOException
    {
        String str = "path/to/file.txt";
        Class<?> clazz = File.class;
        File expected = new File(str);
        File result = (File) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateFileValueInvalid() throws ParseException
    {
        String str = "abc";
        Class<?> clazz = File.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testOpenFileValueValid() throws ParseException, FileNotFoundException, IOException
    {
        String str = "path/to/file.txt";
        Class<?> clazz = FileInputStream.class;
        FileInputStream expected = new FileInputStream(new File(str));
        FileInputStream result = (FileInputStream) TypeHandler.openFile(str);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileValueInvalid() throws ParseException, FileNotFoundException
    {
        String str = "abc";
        Class<?> clazz = FileInputStream.class;
        TypeHandler.openFile(str);
    }
}