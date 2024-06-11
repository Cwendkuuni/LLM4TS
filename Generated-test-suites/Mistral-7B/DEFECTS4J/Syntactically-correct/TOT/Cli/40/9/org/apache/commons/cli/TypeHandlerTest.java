import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.apache.commons.cli.PatternOptionBuilder.*;

/**
 * Test cases for TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue(String, Class) method for String type.
     */
    @Test
    public void testCreateValueString()
    {
        String strValue = "test string";
        Class<String> stringClass = String.class;

        try
        {
            String result = (String) TypeHandler.createValue(strValue, stringClass);
            Assert.assertEquals("Expected string value to be equal to the input string.", strValue, result);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test createValue(String, Class) method for Object type.
     */
    @Test
    public void testCreateValueObject()
    {
        String className = "java.lang.Integer";
        Class<Integer> integerClass = Integer.class;

        try
        {
            Object result = TypeHandler.createValue("42", integerClass);
            Assert.assertEquals("Expected integer value to be equal to the input number.", 42, result);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }

        try
        {
            Object result = TypeHandler.createValue("java.lang.String", integerClass);
            Assert.fail("Expected ParseException for string input to Object type.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test createValue(String, Class) method for Number type.
     */
    @Test
    public void testCreateValueNumber()
    {
        String strNumber = "42.5";
        Class<Double> doubleClass = Double.class;

        try
        {
            Double result = (Double) TypeHandler.createValue(strNumber, doubleClass);
            Assert.assertEquals("Expected double value to be equal to the input number.", 42.5, result, 0.001);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }

        try
        {
            Object result = TypeHandler.createValue("invalid number", doubleClass);
            Assert.fail("Expected ParseException for invalid number input to Double type.");
        }
        catch (ParseException e)
        {
            // Expected
        }

        try
        {
            Long result = (Long) TypeHandler.createValue("42", long.class);
            Assert.assertEquals("Expected Long value to be equal to the input number.", 42L, result);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
    }

    /**
     * Test createValue(String, Class) method for Date type.
     */
    @Test
    public void testCreateValueDate()
    {
        String dateString = "2022-12-31";
        Class<Date> dateClass = Date.class;

        try
        {
            Date result = (Date) TypeHandler.createValue(dateString, dateClass);
            Assert.assertNotNull("Expected Date object to be not null.", result);
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }

        try
        {
            Object result = TypeHandler.createValue("invalid date format", dateClass);
            Assert.fail("Expected ParseException for invalid date format input to Date type.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test createValue(String, Class) method for Class type.
     */
    @Test
    public void testCreateValueClass()
    {
        String className = "java.lang.String";
        Class<String> stringClass = String.class;

        try
        {
            Object result = TypeHandler.createValue(className, stringClass);
            Assert.assertEquals("Expected String object to be equal to the input class name.", className, result.toString());
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }

        try
        {
            Object result = TypeHandler.createValue("java.lang.Integer", stringClass);
            Assert.fail("Expected ParseException for input class name to String type.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test createValue(String, Class) method for File type.
     */
    @Test
    public void testCreateValueFile()
    {
        String filePath = "testFile.txt";
        Class<File> fileClass = File.class;

        try
        {
            File result = (File) TypeHandler.createValue(filePath, fileClass);
            Assert.assertNotNull("Expected File object to be not null.", result);
            Assert.assertTrue("Expected File object to exist.", result.exists());
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }

        try
        {
            Object result = TypeHandler.createValue("non-existing-file", fileClass);
            Assert.fail("Expected ParseException for non-existing file input to File type.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test createValue(String, Class) method for FileInputStrem type.
     */
    @Test
    public void testCreateValueFileInputstream()
    {
        String filePath = "testFile.txt";
        Class<FileInputStream> fileInputStreamClass = FileInputStream.class;

        try
        {
            File file = new File(filePath);
            FileInputStream result = (FileInputStream) TypeHandler.createValue(filePath, fileInputStreamClass);
            Assert.assertNotNull("Expected FileInputStream object to be not null.", result);
            Assert.assertTrue("Expected File to exist.", file.exists());
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
        catch (IOException e)
        {
            Assert.fail("Unexpected IOException: " + e.getMessage());
        }

        try
        {
            Object result = TypeHandler.createValue("non-existing-file", fileInputStreamClass);
            Assert.fail("Expected ParseException for non-existing file input to FileInputStream type.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }

    /**
     * Test createValue(String, Class) method for URL type.
     */
    @Test
    public void testCreateValueUrl()
    {
        String urlString = "http://www.example.com";
        Class<URL> urlClass = URL.class;

        try
        {
            URL result = (URL) TypeHandler.createValue(urlString, urlClass);
            Assert.assertNotNull("Expected URL object to be not null.", result);
            Assert.assertEquals("Expected URL string to be equal to the input URL string.", urlString, result.toString());
        }
        catch (ParseException e)
        {
            Assert.fail("Unexpected ParseException: " + e.getMessage());
        }
        catch (MalformedURLException e)
        {
            Assert.fail("Unexpected MalformedURLException: " + e.getMessage());
        }

        try
        {
            Object result = TypeHandler.createValue("invalid url format", urlClass);
            Assert.fail("Expected ParseException for invalid URL format input to URL type.");
        }
        catch (ParseException e)
        {
            // Expected
        }
    }
}