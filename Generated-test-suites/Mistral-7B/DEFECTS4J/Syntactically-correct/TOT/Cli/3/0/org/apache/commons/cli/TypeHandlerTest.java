package org.apache.commons.cli.test;

import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class TypeHandlerTest {

    @Test
    public void testCreateValue_StringObject() {
        String str = "henri.yandell@generationjava.com";
        Object obj = String.class;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValue_StringClass() {
        String str = "123";
        Class clazz = Integer.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(123, result);

        str = "456.78";
        clazz = Double.class;
        result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(456.78, result, 0.001);
    }

    @Test
    public void testCreateValue_InvalidType() {
        String str = "henri.yandell@generationjava.com";
        Class clazz = Integer.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertNull(result);
    }

    @Test
    public void testCreateObject() {
        String className = "java.util.Date";
        Object result = TypeHandler.createObject(className);
        Assert.assertNotNull(result);
    }

    @Test
    public void testCreateNumber() {
        String str = "123";
        Number result = TypeHandler.createNumber(str);
        Assert.assertEquals(123, result.intValue());

        str = "456.78";
        result = TypeHandler.createNumber(str);
        Assert.assertEquals(456.78, result.doubleValue(), 0.001);
    }

    @Test
    public void testCreateDate() {
        String dateString = "2023-03-01";
        Date result = TypeHandler.createDate(dateString);
        Assert.assertNotNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateDate_InvalidFormat() {
        String dateString = "2023-3-01";
        Date result = TypeHandler.createDate(dateString);
        Assert.fail("Expected NullPointerException");
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://www.example.com";
        URL result = TypeHandler.createURL(urlString);
        Assert.assertNotNull(result);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_InvalidFormat() {
        String urlString = "http://www.example.com/ images/image.jpg";
        URL result = TypeHandler.createURL(urlString);
        Assert.fail("Expected MalformedURLException");
    }

    @Test
    public void testCreateFile() {
        String filePath = "testFile.txt";
        File result = TypeHandler.createFile(filePath);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.exists());
        result.delete();
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_InvalidPath() {
        String filePath = "nonExistentFile.txt";
        File result = TypeHandler.createFile(filePath);
        Assert.fail("Expected FileNotFoundException");
    }

    @Test
    public void testCreateFiles() {
        String filePaths = "testFile1.txt, testFile2.txt";
        File[] files = TypeHandler.createFiles(filePaths);
        Assert.assertNotNull(files);
        Assert.assertEquals(2, files.length);
        for (File file : files) {
            Assert.assertNotNull(file);
            Assert.assertTrue(file.exists());
        }

        // clean up
        for (File file : files) {
            file.delete();
        }
    }
}