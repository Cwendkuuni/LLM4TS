import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.cli.PatternOptionBuilder.*;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        Object obj = new Object();
        String str = "test";
        Object result = TypeHandler.createValue(str, obj.getClass());

        Assert.assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClassString() throws ParseException
    {
        String expected = "test";
        Class<String> clazz = String.class;
        Object result = TypeHandler.createValue(expected, clazz);

        Assert.assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidClass() throws ParseException
    {
        String str = "test";
        Class<Integer> clazz = Integer.class;

        TypeHandler.createValue(str, clazz);
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String className = "java.util.Date";
        Object result = TypeHandler.createObject(className);

        Assert.assertNotNull(result);
        Assert.assertTrue(result.getClass().getName().equals(className));
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassName() throws ParseException
    {
        String className = "java.util.DateX";

        TypeHandler.createObject(className);
    }

    @Test
    public void testCreateNumber() throws ParseException
    {
        String number = "123";
        Number result = TypeHandler.createNumber(number);

        Assert.assertEquals(Long.parseLong(number), result.longValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidNumber() throws ParseException
    {
        String number = "abc";

        TypeHandler.createNumber(number);
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException
    {
        String className = "java.util.Date";
        Class<?> result = TypeHandler.createClass(className);

        Assert.assertEquals(Class.forName(className), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassInvalidClassName() throws ParseException
    {
        String className = "java.util.DateX";

        TypeHandler.createClass(className);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws ParseException
    {
        String date = "2022-12-31";

        TypeHandler.createDate(date);
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException
    {
        String urlString = "http://www.example.com";
        URL result = TypeHandler.createURL(urlString);

        Assert.assertEquals(new URL("http://www.example.com"), result);
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidURL() throws ParseException
    {
        String urlString = "http://www.example.com/image.jpg";
        String invalidPart = urlString.substring(urlString.lastIndexOf('/') + 1);
        urlString = urlString.substring(0, urlString.lastIndexOf('/'));

        TypeHandler.createURL(invalidPart);
    }

    @Test
    public void testCreateFile()
    {
        String filePath = "test.txt";
        File result = TypeHandler.createFile(filePath);

        Assert.assertNotNull(result);
        Assert.assertEquals(filePath, result.getAbsolutePath());
    }

    @Test(expected = ParseException.class)
    public void testOpenFileInvalidFilePath() throws ParseException, FileNotFoundException
    {
        String invalidFilePath = "nonexistent.txt";

        TypeHandler.openFile(invalidFilePath);
    }

    @Test
    public void testOpenFileValidFilePath() throws ParseException, FileNotFoundException, IOException
    {
        String validFilePath = "test.txt";
        FileInputStream fileInput = new FileInputStream(new File(validFilePath));

        FileInputStream result = TypeHandler.openFile(validFilePath);

        Assert.assertEquals(fileInput, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateFilesInvalidFilePath() throws ParseException
    {
        String invalidFilePath = "nonexistent.txt";

        TypeHandler.createFiles(invalidFilePath);
    }
}