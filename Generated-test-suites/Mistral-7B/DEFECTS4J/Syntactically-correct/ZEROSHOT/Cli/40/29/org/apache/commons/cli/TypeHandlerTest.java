import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringString() throws ParseException
    {
        Object result = TypeHandler.createValue("testString", String.class);
        assertEquals("testString", result);
    }

    @Test
    public void testCreateValueStringClassString() throws ParseException
    {
        String className = "java.util.Date";
        Class<?> clazz = Class.forName(className);
        Object result = TypeHandler.createValue("testDate", clazz);
        assertTrue(result instanceof Date);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueStringClassInvalid() throws ParseException
    {
        String className = "java.util.InvalidClass"; // non-existent class
        Class<?> clazz = Class.forName(className);
        TypeHandler.createValue("testString", clazz);
    }

    @Test
    public void testCreateValueStringClassEmpty() throws ParseException
    {
        Class<?> clazz = String.class;
        Object result = TypeHandler.createValue("", clazz);
        assertEquals("", result);
    }

    @Test
    public void testCreateNumber() throws ParseException
    {
        Object result = TypeHandler.createNumber("123");
        assertEquals(123L, result);
    }

    @Test
    public void testCreateNumberWithDecimal() throws ParseException
    {
        Object result = TypeHandler.createNumber("123.45");
        assertEquals(123.45, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalid() throws ParseException
    {
        TypeHandler.createNumber("abc");
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException
    {
        String className = "java.util.Date";
        Object result = TypeHandler.createValue("testDate", Class.forName(className));
        assertTrue(result instanceof Date);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws ParseException
    {
        String dateString = "2023-03-01";
        TypeHandler.createDate(dateString);
    }

    @Test
    public void testCreateURL() throws ParseException
    {
        String urlString = "http://www.example.com";
        URL result = TypeHandler.createURL(urlString);
        assertNotNull(result);
        assertEquals(urlString, result.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalid() throws ParseException
    {
        String urlString = "invalid_url";
        TypeHandler.createURL(urlString);
    }

    @Test
    public void testCreateFile() throws ParseException
    {
        String filePath = "testFile.txt";
        File result = TypeHandler.createFile(filePath);
        assertNotNull(result);
        assertEquals(filePath, result.getAbsolutePath());
    }

    @Test(expected = ParseException.class)
    public void testCreateFileInvalid() throws ParseException
    {
        String filePath = "nonexistent_file";
        TypeHandler.createFile(filePath);
    }

    @Test
    public void testOpenFile() throws ParseException, FileNotFoundException
    {
        String filePath = "testFile.txt";
        FileInputStream result = TypeHandler.openFile(filePath);
        assertNotNull(result);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileInvalid() throws ParseException, FileNotFoundException
    {
        String filePath = "nonexistent_file";
        TypeHandler.openFile(filePath);
    }
}