import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringString() throws ParseException
    {
        Object result = TypeHandler.createValue("testString", String.class);
        assertEquals("testString", result);
    }

    @Test
    public void testCreateValueStringClass() throws ParseException
    {
        Object result = TypeHandler.createValue("testString", String.class);
        assertEquals(result.getClass(), String.class);
    }

    @Test
    public void testCreateObjectClassname() throws ParseException
    {
        try
        {
            Class<?> clazz = Class.forName("java.util.Date");
            Object result = TypeHandler.createObject("java.util.Date");
            assertEquals(result.getClass(), clazz);
        }
        catch (ClassNotFoundException e)
        {
            throw new ParseException("Unable to find the class: java.util.Date");
        }
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectNonExistentClassname() throws ParseException
    {
        TypeHandler.createObject("NonExistentClass");
    }

    @Test
    public void testCreateNumberLongString() throws ParseException
    {
        Object result = TypeHandler.createNumber("123");
        assertEquals(result.getClass(), Long.class);
        assertEquals(result, Long.valueOf(123));
    }

    @Test
    public void testCreateNumberDoubleStringWithDecimalPoint() throws ParseException
    {
        Object result = TypeHandler.createNumber("123.45");
        assertEquals(result.getClass(), Double.class);
        assertEquals(result, Double.valueOf(123.45));
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberNonNumericString() throws ParseException
    {
        TypeHandler.createNumber("abc");
    }

    @Test
    public void testCreateDateString() throws ParseException, IOException
    {
        Object result = TypeHandler.createValue("2023-02-01", Date.class);
        Assert.assertNotNull(result);
        assertEquals(((Date) result).getTime(), 1675104000000L); // 2023-02-01 00:00:00
    }

    @Test(expected = ParseException.class)
    public void testCreateDateInvalidFormatString() throws ParseException
    {
        TypeHandler.createValue("invalidFormatString", Date.class);
    }

    @Test
    public void testCreateURLString() throws ParseException
    {
        Object result = TypeHandler.createValue("http://example.com", URL.class);
        assertEquals(result.getClass(), URL.class);
        assertEquals(result.toString(), "http://example.com");
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidFormatString() throws ParseException
    {
        TypeHandler.createValue("invalidFormatURL", URL.class);
    }

    @Test
    public void testCreateFileString()
    {
        File result = TypeHandler.createFile("testFile.txt");
        assertEquals(result.getAbsolutePath(), "testFile.txt");
        File expectedFile = new File("testFile.txt");
        Assert.assertTrue(expectedFile.exists());
        expectedFile.delete();
    }

    @Test(expected = ParseException.class)
    public void testCreateFileNonExistentFile()
    {
        TypeHandler.createFile("/noSuchFile.txt");
    }

    @Test(expected = ParseException.class)
    public void testCreateFileNullString()
    {
        TypeHandler.createFile(null);
    }

    @Test
    public void testCreateURLsWithProtocol() throws ParseException
    {
        Object result = TypeHandler.createValue("file:testFile.txt", URL.class);
        assertEquals(result.getClass(), URL.class);
        assertEquals(result.toString(), "file:testFile.txt");
    }

    @Test(expected = ParseException.class)
    public void testCreateURLsWithInvalidProtocol() throws ParseException
    {
        TypeHandler.createValue("ftp:testFile.txt", URL.class);
    }
}