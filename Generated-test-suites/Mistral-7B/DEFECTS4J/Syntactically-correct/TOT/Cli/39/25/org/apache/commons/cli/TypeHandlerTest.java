import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.util.ClassLoaderUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValue_StringObject() throws Exception
    {
        String str = "sampleString";
        Object obj = new Object() {
        };

        Object actual = TypeHandler.createValue(str, obj.getClass());

        assertEquals(str, actual);
    }

    @Test
    public void testCreateValue_StringClass() throws Exception
    {
        String str = "sampleString";

        Class<?> clazz = String.class;

        Object actual = TypeHandler.createValue(str, clazz);

        assertEquals(str, actual);
    }

    @Test
    public void testCreateValue_StringStringClass() throws Exception
    {
        String str = "sample.class.name";

        Class<?> clazz = Class.forName(str);

        Object actual = TypeHandler.createValue(str, clazz);

        Assert.assertNotNull(actual);
        assertEquals(clazz, actual.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_InvalidStringClass() throws Exception
    {
        String str = "invalidString";
        Class<?> clazz = String.class;

        assertThrows(ParseException.class, () -> TypeHandler.createValue(str, clazz));
    }

    @Test
    public void testCreateObject() throws Exception
    {
        String classname = "java.util.Date";

        Object actual = TypeHandler.createObject(classname);

        Assert.assertNotNull(actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateObject_InvalidClassname() throws Exception
    {
        String classname = "InvalidClass";

        assertThrows(ParseException.class, () -> TypeHandler.createObject(classname));
    }

    @Test
    public void testCreateNumber() throws Exception
    {
        String str = "12345";

        Number actual = TypeHandler.createNumber(str);

        Assert.assertNotNull(actual);
        assertEquals(12345L, actual.longValue());

        str = "123.45";

        actual = TypeHandler.createNumber(str);

        Assert.assertNotNull(actual);
        assertEquals(123.45, actual.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumber_InvalidString() throws Exception
    {
        String str = "invalid";

        assertThrows(ParseException.class, () -> TypeHandler.createNumber(str));
    }

    @Test
    public void testCreateDate() throws Exception
    {
        String str = "2022-12-31";

        Date actual = TypeHandler.createDate(str);

        Assert.assertNotNull(actual);
        assertEquals("Fri Dec 31 00:00:00 EST 2022", actual.toString());
    }

    @Test
    public void testCreateURL() throws Exception
    {
        String str = "http://example.com";

        URL actual = TypeHandler.createURL(str);

        Assert.assertNotNull(actual);
        assertEquals("http://example.com", actual.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURL_InvalidString() throws Exception
    {
        String str = "invalidURL";

        assertThrows(ParseException.class, () -> TypeHandler.createURL(str));
    }

    @Test
    public void testCreateFile() throws IOException, ParseException
    {
        String str = "path/to/file.txt";

        File actual = TypeHandler.createFile(str);

        Assert.assertNotNull(actual);
        assertEquals("path/to/file.txt", actual.getAbsolutePath());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFile_FileNotFound() throws IOException, ParseException
    {
        String str = "non-existent/file.txt";

        assertThrows(FileNotFoundException.class, () -> new FileInputStream(TypeHandler.createFile(str)));
    }

    @Test(expected = ParseException.class)
    public void testCreateFile_InvalidString() throws Exception
    {
        String str = "invalid/path/to/file.txt";

        assertThrows(ParseException.class, () -> TypeHandler.createFile(str));
    }
}