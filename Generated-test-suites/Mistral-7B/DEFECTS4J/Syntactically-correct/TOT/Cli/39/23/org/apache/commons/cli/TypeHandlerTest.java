import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class TypeHandlerTest
{
    @Test
    public void testCreateValue_String_String() throws ParseException
    {
        String str = "testString";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, obj);
    }

    @Test
    public void testCreateValue_String_Class_StringType() throws ParseException
    {
        String str = "testString";
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str.getClass(), obj.getClass());
        Assert.assertEquals(str, obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_Class_InvalidType() throws ParseException
    {
        String str = "testString";
        Class<?> clazz = Integer.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObject_Classname() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.Date";
        Object obj = TypeHandler.createObject(classname);
        Assert.assertNotNull(obj);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_InvalidClassname() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String classname = "java.util.InvalidClass";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumber_LongNumber() throws ParseException
    {
        String str = "1234567890";
        Number number = TypeHandler.createNumber(str);
        Assert.assertNotNull(number);
        Assert.assertEquals(Long.parseLong(str), number.longValue());
    }

    @Test
    public void testCreateNumber_DoubleNumber() throws ParseException
    {
        String str = "123.45";
        Number number = TypeHandler.createNumber(str);
        Assert.assertNotNull(number);
        Assert.assertEquals(Double.parseDouble(str), number.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidString() throws ParseException
    {
        String str = "abc";
        TypeHandler.createNumber(str);
    }

    @Test
    public void testCreateClass_ValidClassname() throws ParseException, ClassNotFoundException
    {
        String classname = "java.util.Date";
        Class<?> clazz = TypeHandler.createClass(classname);
        Assert.assertNotNull(clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateClass_InvalidClassname() throws ParseException, ClassNotFoundException
    {
        String classname = "java.util.InvalidClass";
        TypeHandler.createClass(classname);
    }

    @Test
    public void testCreateDate_ValidDateString() throws ParseException, UnsupportedOperationException
    {
        String dateStr = "2022-03-31";
        Date date = TypeHandler.createDate(dateStr);
        Assert.assertNotNull(date);
    }

    @Test
    public void testCreateDate_InvalidDateString() throws ParseException, UnsupportedOperationException
    {
        String dateStr = "2022-35-31";
        TypeHandler.createDate(dateStr);
    }

    @Test
    public void testCreateURL_ValidURLString() throws ParseException
    {
        String urlStr = "http://www.example.com";
        URL url = TypeHandler.createURL(urlStr);
        Assert.assertNotNull(url);
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidURLString() throws ParseException
    {
        String urlStr = "http://www.example.com:12345";
        TypeHandler.createURL(urlStr);
    }

    @Test
    public void testCreateFile_ValidFilePath() throws FileNotFoundException, ParseException
    {
        String filePath = "testFile.txt";
        File file = TypeHandler.createFile(filePath);
        Assert.assertNotNull(file);
        Assert.assertTrue(file.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_InvalidFilePath() throws FileNotFoundException, ParseException
    {
        String filePath = "nonexistentFile.txt";
        TypeHandler.createFile(filePath);
    }

    @Test
    public void testCreateFiles_ValidFilePaths() throws FileNotFoundException, ParseException
    {
        String filePaths = "./testFiles/*.txt";
        File[] files = TypeHandler.createFiles(filePaths);
        Assert.assertNotNull(files);
        Assert.assertArrayNotEmpty(files);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles_InvalidFilePaths() throws FileNotFoundException, ParseException
    {
        String filePaths = "./testFiles/invalidPath.txt";
        TypeHandler.createFiles(filePaths);
    }
}