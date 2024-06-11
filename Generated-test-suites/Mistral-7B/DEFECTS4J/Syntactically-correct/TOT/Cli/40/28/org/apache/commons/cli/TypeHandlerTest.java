import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * TypeHandlerTest class for testing the methods in TypeHandler class.
 */
public class TypeHandlerTest
{
    /**
     * Test for createValue(String, String) method.
     */
    @Test
    public void testCreateValueWithStringObject()
    {
        String str = "test";
        Object obj = new Object();

        try
        {
            Object actual = TypeHandler.createValue(str, obj.getClass());
            Assert.assertEquals(str, actual);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue(String, Class) method with String type.
     */
    @Test
    public void testCreateValueWithStringStringValue()
    {
        String str = "test";

        String expected = str;
        String actual;

        try
        {
            actual = TypeHandler.createValue(str, String.class);
            Assert.assertEquals(expected, actual);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue(String, Class) method with Object type.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueWithStringObjectValue()
    {
        String str = "test";

        Object obj = new Object();

        TypeHandler.createValue(str, obj.getClass());
        fail("Expected ParseException but no exception was thrown.");
    }

    /**
     * Test for createValue(String, Class) method with ObjectType.
     */
    @Test
    public void testCreateValueWithObjectValue()
    {
        String str = "test.Class";

        Class<?> clazz = TestObject.class;

        Object expected = new TestObject();

        try
        {
            Object actual = TypeHandler.createValue(str, clazz);
            Assert.assertEquals(expected, actual);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue(String, Class) method with number type (Integer).
     */
    @Test
    public void testCreateValueWithNumberValue()
    {
        String str = "123";

        int expected = 123;
        int actual;

        try
        {
            actual = ((Number) TypeHandler.createValue(str, Integer.class)).intValue();
            Assert.assertEquals(expected, actual);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue(String, Class) method with number type (Double).
     */
    @Test
    public void testCreateValueWithNumberValueDouble()
    {
        String str = "123.45";

        double expected = 123.45;
        double actual;

        try
        {
            actual = ((Number) TypeHandler.createValue(str, Double.class)).doubleValue();
            Assert.assertEquals(expected, actual, 0.001);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue(String, Class) method with date type.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueWithDateValue()
    {
        String str = "2022-12-31";

        Date expected = new Date();

        try
        {
            Date actual = (Date) TypeHandler.createValue(str, Date.class);
            Assert.fail("Expected UnsupportedOperationException but no exception was thrown.");
        }
        catch (UnsupportedOperationException e)
        {
            // Expected
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue(String, Class) method with URL type.
     */
    @Test
    public void testCreateValueWithURLValue()
    {
        String str = "http://www.example.com";

        URL expected = new URL("http://www.example.com");
        URL actual;

        try
        {
            actual = (URL) TypeHandler.createValue(str, URL.class);
            Assert.assertEquals(expected, actual);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue(String, Class) method with File type.
     */
    @Test
    public void testCreateValueWithFileValue()
    {
        String str = "test.txt";

        File expected = new File("test.txt");
        File actual;

        try
        {
            actual = (File) TypeHandler.createValue(str, File.class);
            Assert.assertEquals(expected, actual);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue(String, Class) method with existing File value.
     */
    @Test
    public void testCreateValueWithExistingFileValue()
    {
        String str = "test.txt";

        File expected = new File("test.txt");
        File actual;

        try
        {
            actual = (File) TypeHandler.openFile(str);
            Assert.assertEquals(expected, actual);
        }
        catch (IOException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test for createValue(String, Class) method with non-existent File value.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueWithNonExistentFileValue()
    {
        String str = "non_existent_file.txt";

        File actual;

        try
        {
            actual = (File) TypeHandler.openFile(str);
            fail("Expected ParseException but no exception was thrown.");
        }
        catch (FileNotFoundException e)
        {
            // Expected
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}

class TestObject
{
}