import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.PatternOptionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class TypeHandlerTest {

    @Test
    public void testCreateStringValue() {
        String str = "test";
        Class clazz = PatternOptionBuilder.STRING_VALUE;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.assertEquals("test", obj);
    }

    @Test
    public void testCreateObjectValue() {
        String className = "java.lang.String";
        String str =className;
        Class clazz = Class.forName(className);
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(className, obj.getClass().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateObjectValueInvalidClass() {
        String str = "123";
        Class clazz = Object.class;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.fail(); //test should throw an IllegalArgumentException
    }

    @Test
    public void testCreateNumberValue() {
        String str = "123.45";
        Number number = TypeHandler.createNumber(str);
        Assert.assertNotNull(number);
        Assert.assertEquals(123.45, number.doubleValue(), 0.01);
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberValueInvalidFormat() {
        String str = "abc";
        Number number = TypeHandler.createNumber(str);
        Assert.fail(); //test should throw a NumberFormatException
    }

    @Test
    public void testCreateDateValue() {
        String str = "2023-02-01";
        Date date = TypeHandler.createDate(str);
        Assert.assertNotNull(date);
        Assert.assertEquals("Thu Feb 01 00:00:00 CET 2023", date.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDateValueInvalidFormat() {
        String str = "2023-02-31";
        Date date = TypeHandler.createDate(str);
        Assert.fail(); //test should throw an IllegalArgumentException
    }

    @Test
    public void testCreateURLValue() {
        String str = "https://www.google.com";
        URL url = TypeHandler.createURL(str);
        Assert.assertNotNull(url);
        Assert.assertEquals("https://www.google.com", url.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURLValueInvalidFormat() {
        String str = "https://www.google.com/abc";
        URL url = TypeHandler.createURL(str);
        Assert.fail(); //test should throw a MalformedURLException
    }

    @Test
    public void testCreateFileValue() {
        String path = "test.txt";
        File file = TypeHandler.createFile(path);
        Assert.assertNotNull(file);
        Assert.assertEquals(path, file.getAbsolutePath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileValueNonExistingFile() {
        String path = "non-existing-file.txt";
        File file = TypeHandler.createFile(path);
        Assert.fail(); //test should throw a FileNotFoundException
    }

    @Test
    public void testCreateFilesValue() {
        String path = "test_folder/test1.txt,test_folder/test2.txt";
        File[] files = TypeHandler.createFiles(path);
        Assert.assertNotNull(files);
        Assert.assertEquals(2, files.length);
        for (File file : files) {
            Assert.assertNotNull(file);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateFilesValueInvalidFormat() {
        String path = "test_folder/test1.txt,test_folder/test2.txt,test.txt";
        File[] files = TypeHandler.createFiles(path);
        Assert.fail(); //test should throw an IllegalArgumentException
    }
}