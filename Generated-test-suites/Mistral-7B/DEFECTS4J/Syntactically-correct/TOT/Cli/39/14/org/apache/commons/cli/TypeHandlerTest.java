import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.cli.PatternOptionBuilder.*;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValueWithString() throws ParseException
    {
        String str = "test";
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(str, result);

        // edge case: empty string
        str = "";
        result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals("", result);

        // error case: null string
        str = null;
        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to find the class: null"));
        }

        // test with invalid class
        clazz = Integer.class;
        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to find the class: java.lang.String"));
        }
    }

    @Test
    public void testCreateValueWithObject() throws ParseException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        String className = "java.lang.String";
        Class<?> clazz = Class.forName(className);
        Object obj = TypeHandler.createObject(className);

        Object result = TypeHandler.createValue("test", clazz);
        Assert.assertEquals(obj, result);

        // edge case: empty string
        String str = "";
        result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(obj, result);

        // error case: null string
        str = null;
        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to find the class: null"));
        }

        // test with invalid class
        clazz = int[].class;
        try
        {
            result = TypeHandler.createValue("test", clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create an instance of: " + className));
        }
    }

    @Test
    public void testCreateValueWithNumber() throws ParseException
    {
        String str = "123";
        Class<?> clazz = int.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(123, result);

        str = "123.45";
        clazz = double.class;
        result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(123.45, result);

        // edge case: empty string
        str = "";
        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to parse the URL: "));
        }

        // error case: null string
        str = null;
        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to parse the number: "));
        }

        // test with invalid class
        clazz = String.class;
        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create the number from: "));
        }
    }

    @Test
    public void testCreateValueWithDate() throws ParseException, UnsupportedOperationException
    {
        String str = "2022-01-01";
        Class<?> clazz = Date.class;

        // this method is not yet implemented and always throws an UnsupportedOperationException
        Object result = TypeHandler.createDate(str);
        Assert.fail();

        // edge case: empty string
        str = "";
        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to parse the date: "));
        }

        // error case: null string
        str = null;
        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create the date from: "));
        }

        // test with invalid class
        clazz = String.class;
        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create the date from: "));
        }
    }

    @Test
    public void testCreateValueWithURL() throws ParseException, MalformedURLException
    {
        String str = "http://www.example.com";
        Class<?> clazz = URL.class;
        Object result = TypeHandler.createValue(str, clazz);
        Assert.assertEquals(new URL(str), result);

        // edge case: empty string
        str = "";
        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to parse the URL: "));
        }

        // error case: null string
        str = null;
        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to parse the URL: "));
        }

        // test with invalid class
        clazz = String.class;
        try
        {
            result = TypeHandler.createValue(str, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create the URL from: "));
        }
    }

    @Test
    public void testCreateValueWithFile() throws ParseException, FileNotFoundException
    {
        String filePath = "path/to/file.txt";
        Class<?> clazz = File.class;
        Object result = TypeHandler.createFile(filePath);
        URL url = TypeHandler.class.getResource(filePath);
        File expectedFile = new File(url.getPath());
        Assert.assertEquals(expectedFile, result);

        // edge case: empty string
        String emptyFilePath = "";
        try
        {
            result = TypeHandler.createValue(emptyFilePath, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create the file from: "));
        }

        // error case: null string
        String nullFilePath = null;
        try
        {
            result = TypeHandler.createValue(nullFilePath, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create the file from: "));
        }

        // test with non-existent file
        filePath = "non-existent-file.txt";
        try
        {
            result = TypeHandler.createFile(filePath);
            Assert.fail();
        }
        catch (FileNotFoundException e)
        {
            // expected
        }

        // test with invalid class
        clazz = String.class;
        try
        {
            result = TypeHandler.createValue(filePath, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create the file from: "));
        }
    }

    @Test
    public void testCreateValueWithExistingFile() throws ParseException, FileNotFoundException, IOException
    {
        String filePath = "path/to/existing/file.txt";
        File file = new File(filePath);
        Class<?> clazz = FileInputStream.class;

        // create the file for testing
        File tempFile = File.createTempFile("junit-", null);
        String tempFilePath = tempFile.getAbsolutePath();
        try (FileWriter writer = new FileWriter(tempFile))
        {
            writer.write("test data");
        }

        Object result = TypeHandler.createFile(filePath);
        Object expectedResult = new File(filePath);
        Assert.assertEquals(expectedResult, result);

        // test the file input stream
        FileInputStream fileInputStream = (FileInputStream) TypeHandler.createValue(filePath, clazz);
        byte[] bytes = new byte[file.length()];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(bytes)) != -1)
        {
            Assert.assertArrayEquals(bytes, 0, bytesRead);
        }

        // edge case: empty string
        String emptyFilePath = "";
        try
        {
            result = TypeHandler.createValue(emptyFilePath, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create the file input stream from: "));
        }

        // error case: null string
        String nullFilePath = null;
        try
        {
            result = TypeHandler.createValue(nullFilePath, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create the file input stream from: "));
        }

        // test with non-existent file
        filePath = "non-existent-file.txt";
        try
        {
            result = TypeHandler.createValue(filePath, clazz);
            Assert.fail();
        }
        catch (FileNotFoundException e)
        {
            // expected
        }

        // test with invalid class
        clazz = String.class;
        try
        {
            result = TypeHandler.createValue(filePath, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create the file input stream from: "));
        }

        // clean up
        tempFile.delete();
    }

    @Test
    public void testCreateValueWithFiles() throws ParseException, UnsupportedOperationException
    {
        String filePaths = "path/to/file1.txt,path/to/file2.txt";
        Class<?> clazz = File.class;

        // this method is not yet implemented and always throws an UnsupportedOperationException
        Object[] results = TypeHandler.createFiles(filePaths);
        Assert.fail();

        // edge case: empty string
        String emptyFilePaths = "";
        try
        {
            results = TypeHandler.createValue(emptyFilePaths, clazz.getComponentType());
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create the files from: "));
        }

        // error case: null string
        String nullFilePaths = null;
        try
        {
            results = TypeHandler.createValue(nullFilePaths, clazz.getComponentType());
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create the files from: "));
        }

        // test with invalid class
        clazz = String.class;
        try
        {
            results = TypeHandler.createValue(filePaths, clazz);
            Assert.fail();
        }
        catch (ParseException e)
        {
            Assert.assertTrue(e.getMessage().startsWith("Unable to create the files from: "));
        }
    }
}