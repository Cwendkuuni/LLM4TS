package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.cli.PatternOptionBuilder.*;

/**
 * Test for TypeHandler class
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringString() throws ParseException
    {
        String str = "testString";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, obj);
    }

    @Test
    public void testCreateValueStringClass() throws ParseException
    {
        String str = "testString";
        Class<String> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str.getClass(), obj.getClass());
        Assert.assertEquals(str, obj.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidType() throws ParseException
    {
        String str = "testString";
        Class<Integer> clazz = Integer.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateValueStringPatternStringValue() throws ParseException
    {
        String str = "testString";
        Class<?> clazz = STRING_VALUE;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, obj);
    }

    @Test
    public void testCreateValueStringPatternObjectValue()
    {
        String str = "testString";
        Class<?> clazz = OBJECT_VALUE;
        try
        {
            Object obj = TypeHandler.createValue(str, clazz);
            Assert.fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    @Test
    public void testCreateValueStringPatternNumberValue() throws ParseException
    {
        String str = "123";
        Class<?> clazz = NUMBER_VALUE;
        Number expectedNumber = 123;
        Number obj = (Number) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expectedNumber.getClass(), obj.getClass());
        Assert.assertEquals(expectedNumber.doubleValue(), obj.doubleValue(), 0.0);
    }

    @Test
    public void testCreateValueStringPatternDateValue() throws ParseException, UnsupportedOperationException
    {
        String str = "2022-12-31";
        Class<?> clazz = DATE_VALUE;
        Date expectedDate = new Date(Long.parseLong("2022-12-31") * 24 * 60 * 60 * 1000);
        Date obj = (Date) TypeHandler.createValue(str, clazz);
        Assert.assertEquals(expectedDate.getClass(), obj.getClass());
        Assert.assertEquals(expectedDate.getTime(), obj.getTime());
    }

    @Test
    public void testCreateValueStringPatternClassValue() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String className = "java.lang.String";
        Class<?> clazz = CLASS_VALUE;
        Class<?> expectedClass = Class.forName(className);
        Object obj = TypeHandler.createValue(className, clazz);
        Assert.assertEquals(expectedClass, obj.getClass());
    }

    @Test
    public void testCreateValueStringPatternFileValue() throws ParseException, FileNotFoundException
    {
        String filePath = "testFile.txt";
        Class<?> clazz = FILE_VALUE;
        File expectedFile = new File(filePath);
        File obj = (File) TypeHandler.createValue(filePath, clazz);
        Assert.assertEquals(expectedFile, obj);
    }

    @Test
    public void testCreateValueStringPatternExistingFileValue() throws ParseException, FileNotFoundException
    {
        String filePath = "testFile.txt";
        Class<?> clazz = EXISTING_FILE_VALUE;
        File expectedFile = new File(filePath);
        FileInputStream inputStream = new FileInputStream(expectedFile);
        FileInputStream obj = (FileInputStream) TypeHandler.createValue(filePath, clazz);
        Assert.assertEquals(inputStream.available(), obj.available());
    }

    @Test
    public void testCreateValueStringPatternFilesValue() throws ParseException
    {
        String filePaths = "testFile1.txt,testFile2.txt";
        Class<?> clazz = FILES_VALUE;
        File[] expectedFiles = new File[] { new File("testFile1.txt"), new File("testFile2.txt") };
        File[] obj = (File[]) TypeHandler.createValue(filePaths, clazz);
        Assert.assertArrayEquals(expectedFiles, obj);
    }

    @Test
    public void testCreateValueStringPatternURLValue() throws ParseException, MalformedURLException
    {
        String urlString = "http://example.com";
        Class<?> clazz = URL_VALUE;
        URL expectedURL = new URL(urlString);
        URL obj = (URL) TypeHandler.createValue(urlString, clazz);
        Assert.assertEquals(expectedURL.toString(), obj.toString());
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String className = "java.lang.String";
        Class<?> clazz = Object.class;
        Class<?> expectedClass = Class.forName(className);
        Object obj = TypeHandler.createObject(className);
        Assert.assertEquals(expectedClass, obj.getClass());
    }

    @Test
    public void testCreateNumber() throws ParseException
    {
        String numberString = "123";
        Class<?> clazz = Number.class;
        Number expectedNumber = 123;
        Number obj = (Number) TypeHandler.createValue(numberString, clazz);
        Assert.assertEquals(expectedNumber.getClass(), obj.getClass());
        Assert.assertEquals(expectedNumber.doubleValue(), obj.doubleValue(), 0.0);
    }

    @Test(expected = ParseException.class)
    public void testCreateDate() throws ParseException, UnsupportedOperationException
    {
        String dateString = "2022-12-31";
        Class<?> clazz = Date.class;
        TypeHandler.createValue(dateString, clazz);
    }
}