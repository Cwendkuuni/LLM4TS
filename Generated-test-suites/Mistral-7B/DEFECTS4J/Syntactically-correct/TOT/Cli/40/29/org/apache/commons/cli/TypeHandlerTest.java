package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Test cases for TypeHandler class
 */
public class TypeHandlerTest
{
    /**
     * Test createValue(String, Class) method with String type
     */
    @Test
    public void testCreateValueString() throws ParseException
    {
        String str = "test string";
        String expected = "test string";
        Class<String> clazz = String.class;

        String result = TypeHandler.createValue(str, clazz).toString();

        Assert.assertEquals(expected, result);
    }

    /**
     * Test createValue(String, Class) method with Object type
     */
    @Test
    public void testCreateValueObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.Date";
        Class<Date> clazz = Date.class;

        Date expected = new Date();
        Date result = (Date) TypeHandler.createValue(classname, clazz);

        Assert.assertEquals(expected.getClass(), result.getClass());
        Assert.assertNotSame(expected, result); // ensure new instance is created
    }

    /**
     * Test createValue(String, Class) method with Number type
     */
    @Test
    public void testCreateValueNumber() throws ParseException
    {
        String strLong = "123";
        Long expectedLong = 123L;
        Class<Long> clazzLong = Long.class;

        Long resultLong = TypeHandler.createValue(strLong, clazzLong);

        Assert.assertEquals(expectedLong, resultLong);

        String strDouble = "123.45";
        Double expectedDouble = 123.45;
        Class<Double> clazzDouble = Double.class;

        Double resultDouble = TypeHandler.createValue(strDouble, clazzDouble);

        Assert.assertEquals(expectedDouble, resultDouble);
    }

    /**
     * Test createValue(String, Class) method with Date type
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate() throws ParseException, UnsupportedOperationException
    {
        String str = "2022-10-01";
        Class<Date> clazz = Date.class;

        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test createValue(String, Class) method with Class type
     */
    @Test
    public void testCreateValueClass() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.Date";
        Class<Date> clazz = Date.class;

        Date expected = new Date();
        Date result = (Date) TypeHandler.createValue(classname, clazz);

        Assert.assertEquals(expected.getClass(), result.getClass());
        Assert.assertNotSame(expected, result); // ensure new instance is created
    }

    /**
     * Test createValue(String, Class) method with File type
     */
    @Test
    public void testCreateValueFile() throws ParseException, FileNotFoundException
    {
        String str = "/path/to/file.txt";
        Class<File> clazz = File.class;

        File expected = new File("/path/to/file.txt");
        File result = (File) TypeHandler.createValue(str, clazz);

        Assert.assertEquals(expected.getAbsolutePath(), result.getAbsolutePath());
    }

    /**
     * Test createValue(String, Class) method with existing File type
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateValueExistingFile() throws ParseException, FileNotFoundException
    {
        String str = "/path/to/non_existing_file.txt";
        Class<File> clazz = File.class;

        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test createValue(String, Class) method with Files type
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFiles() throws ParseException, UnsupportedOperationException
    {
        String str = "/path/to/file1.txt,/path/to/file2.txt";
        Class<File[]> clazz = File[].class;

        TypeHandler.createValue(str, clazz);
    }

    /**
     * Test createValue(String, Class) method with URL type
     */
    @Test
    public void testCreateValueURL() throws ParseException, MalformedURLException
    {
        String str = "http://example.com";
        Class<URL> clazz = URL.class;

        URL expected = new URL("http://example.com");
        URL result = TypeHandler.createValue(str, clazz);

        Assert.assertEquals(expected.toExternalForm(), result.toExternalForm());
    }
}