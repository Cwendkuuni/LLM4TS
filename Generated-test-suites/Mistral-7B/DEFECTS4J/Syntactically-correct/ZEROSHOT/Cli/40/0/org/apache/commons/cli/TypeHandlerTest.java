package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Date;

/**
 * Tests for TypeHandler class
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringString() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValueStringClass() throws ParseException
    {
        String str = "test";
        String className = "java.lang.String";
        Object obj = TypeHandler.createValue(str, Class.forName(className));
        assertEquals(str, obj.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidType() throws ParseException
    {
        String str = "test";
        Object obj = TypeHandler.createValue(str, Integer.class);
        fail("Should throw ParseException for invalid type");
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String className = "java.lang.String";
        Object obj = TypeHandler.createObject(className);
        Assert.assertNotNull(obj);
        Assert.assertEquals(className, obj.getClass().getName());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassName() throws ParseException, ClassNotFoundException
    {
        String className = "java.lang.Integer";
        TypeHandler.createObject(className);
        fail("Should throw ParseException for invalid class name");
    }

    @Test
    public void testCreateNumber() throws ParseException
    {
        String numStr = "123";
        Number num = TypeHandler.createNumber(numStr);
        assertEquals(123L, num.longValue());

        String numStr2 = "123.4";
        Number num2 = TypeHandler.createNumber(numStr2);
        assertEquals(123.4, num2.doubleValue(), 0.0);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidFormat() throws ParseException
    {
        String numStr = "test";
        TypeHandler.createNumber(numStr);
        fail("Should throw ParseException for invalid number format");
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException
    {
        String className = "java.lang.String";
        Class<?> clazz = TypeHandler.createClass(className);
        assertEquals(String.class, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassInvalidClassName() throws ParseException, ClassNotFoundException
    {
        String className = "java.lang.Integer";
        TypeHandler.createClass(className);
        fail("Should throw ParseException for invalid class name");
    }

    @Test
    public void testCreateDate() throws ParseException
    {
        String dateStr = "2022-03-01";
        Date date = TypeHandler.createDate(dateStr);
        assertEquals("Wed Mar 01 00:00:00 CET 2022", date.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateUnsupported() throws ParseException
    {
        String dateStr = "2022-03-01";
        TypeHandler.createDate(dateStr);
        fail("Should throw UnsupportedOperationException for unsupported date creation");
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException
    {
        String urlStr = "http://www.example.com";
        URL url = TypeHandler.createURL(urlStr);
        assertEquals(urlStr, url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidFormat() throws ParseException, MalformedURLException
    {
        String urlStr = "http://www.example.com/test";
        TypeHandler.createURL(urlStr);
        fail("Should throw ParseException for invalid URL format");
    }

    @Test
    public void testCreateFile() throws ParseException
    {
        String fileStr = "/path/to/file.txt";
        File file = TypeHandler.createFile(fileStr);
        Assert.assertNotNull(file);
    }

    @Test(expected = ParseException.class)
    public void testCreateFileInvalidFormat() throws ParseException
    {
        String fileStr = "path/to/file.txt";
        TypeHandler.createFile(fileStr);
        fail("Should throw ParseException for invalid file format");
    }

    @Test
    public void testOpenFile() throws ParseException, FileNotFoundException
    {
        String fileStr = "/path/to/file.txt";
        FileInputStream inputStream = TypeHandler.openFile(fileStr);
        Assert.assertNotNull(inputStream);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileInvalidFormat() throws ParseException, FileNotFoundException
    {
        String fileStr = "path/to/file.txt";
        TypeHandler.openFile(fileStr);
        fail("Should throw ParseException for invalid file format");
    }
}