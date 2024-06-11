package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.util.ClassLoaderUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringString() throws Exception
    {
        Object result = TypeHandler.createValue("test", String.class);
        Assert.assertEquals("test", result);
    }

    @Test
    public void testCreateValueStringClass() throws Exception
    {
        String className = "java.lang.Integer";
        Object result = TypeHandler.createValue("42", Class.forName(className));
        Assert.assertEquals(42, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidStringClass() throws Exception
    {
        String className = "java.lang.InvalidClass";
        TypeHandler.createValue("42", Class.forName(className));
    }

    @Test
    public void testCreateObjectString() throws Exception
    {
        String className = "java.lang.String";
        Object result = TypeHandler.createObject(className);
        Assert.assertNotNull(result);
        Assert.assertTrue(result instanceof String);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateObjectNullClass() throws Exception
    {
        TypeHandler.createObject(null);
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClass() throws Exception
    {
        String className = "java.lang.InvalidClass";
        TypeHandler.createObject(className);
    }

    @Test
    public void testCreateNumberString() throws Exception
    {
        Object result = TypeHandler.createNumber("42");
        Assert.assertEquals(42L, result);

        Object resultDouble = TypeHandler.createNumber("42.2");
        Assert.assertEquals(42.2, resultDouble.doubleValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidString() throws Exception
    {
        TypeHandler.createNumber("invalid");
    }

    @Test
    public void testCreateClassString() throws Exception
    {
        String className = "java.lang.Integer";
        Class<?> result = TypeHandler.createClass(className);
        Assert.assertNotNull(result);
        Assert.assertEquals(Integer.class, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassInvalidString() throws Exception
    {
        String className = "java.lang.InvalidClass";
        TypeHandler.createClass(className);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateString() throws Exception
    {
        TypeHandler.createDate("2022-01-01");
    }

    @Test
    public void testCreateURLString() throws Exception
    {
        String urlString = "http://example.com";
        URL result = TypeHandler.createURL(urlString);
        Assert.assertNotNull(result);
        Assert.assertEquals("http://example.com", result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidString() throws Exception
    {
        String urlString = "invalid";
        TypeHandler.createURL(urlString);
    }

    @Test
    public void testCreateFileString() throws Exception
    {
        String filePath = "test.txt";
        File result = TypeHandler.createFile(filePath);
        Assert.assertNotNull(result);
        Assert.assertEquals(new File(filePath), result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testOpenFileNonExistentFile() throws Exception
    {
        String filePath = "nonexistent.txt";
        FileInputStream result = TypeHandler.openFile(filePath);
        Assert.assertNull(result);
    }

    @Test
    public void testOpenFileExistentFile() throws Exception
    {
        String filePath = "test.txt";
        byte[] fileContent = new byte[1];
        File testFile = new File(filePath);
        testFile.createNewFile();
        try (FileOutputStream output = new FileOutputStream(testFile))
        {
            output.write(fileContent);
        }

        FileInputStream result = TypeHandler.openFile(filePath);
        try
        {
            int read = result.read();
            Assert.assertEquals(fileContent[0], read);
        }
        finally
        {
            result.close();
        }

        testFile.delete();
    }

    @Test(expected = ParseException.class)
    public void testCreateFilesInvalidString() throws Exception
    {
        String filePaths = "invalid";
        TypeHandler.createFiles(filePaths);
    }
}