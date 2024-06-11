package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        String str = "test";
        Object obj = new Object();
        Object actual = TypeHandler.createValue(str, obj.getClass());
        assertNotNull(actual);
        assertEquals(str, actual);
    }

    @Test
    public void testCreateValueStringClass() throws ParseException
    {
        String str = "test";
        Class<?> clazz = String.class;
        Object actual = TypeHandler.createValue(str, clazz);
        assertNotNull(actual);
        assertEquals(str, actual);
    }

    @Test
    public void testCreateValueStringString() throws ParseException
    {
        String str = "test";
        Class<?> clazz = String.class;
        Object actual = TypeHandler.createValue(str, clazz);
        assertNotNull(actual);
        assertEquals(str, actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueInvalidStringClass() throws ParseException
    {
        String str = "test";
        Class<?> clazz = Integer.class;
        TypeHandler.createValue(str, clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueNullStringObject() throws ParseException
    {
        String str = null;
        Object obj = new Object();
        TypeHandler.createValue(str, obj.getClass());
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException
    {
        String classname = "java.lang.String";
        Object actual = TypeHandler.createObject(classname);
        assertNotNull(actual);
        assertEquals("", actual.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectInvalidClassName() throws ParseException, ClassNotFoundException, InstantiationException
    {
        String classname = "InvalidClassName";
        TypeHandler.createObject(classname);
    }

    @Test
    public void testCreateNumberLong() throws ParseException
    {
        String str = "123";
        Long actual = (Long) TypeHandler.createValue(str, Long.class);
        assertNotNull(actual);
        assertEquals(123L, actual.longValue());
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidLongString() throws ParseException
    {
        String str = "test";
        Long actual = (Long) TypeHandler.createValue(str, Long.class);
        assertNull(actual);
    }

    @Test
    public void testCreateNumberDouble() throws ParseException
    {
        String str = "123.45";
        Double actual = (Double) TypeHandler.createValue(str, Double.class);
        assertNotNull(actual);
        assertEquals(123.45, actual.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberInvalidDoubleString() throws ParseException
    {
        String str = "test";
        Double actual = (Double) TypeHandler.createValue(str, Double.class);
        assertNull(actual);
    }

    @Test(expected = ParseException.class)
    public void testCreateDateInvalidString() throws ParseException
    {
        String str = "test";
        TypeHandler.createDate(str);
    }

    @Test
    public void testCreateURL() throws ParseException
    {
        String str = "http://google.com";
        URL actual = TypeHandler.createURL(str);
        assertNotNull(actual);
        assertEquals("http", actual.getProtocol());
        assertEquals("//google.com", actual.getFile());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLInvalidString() throws ParseException
    {
        String str = "test";
        TypeHandler.createURL(str);
    }

    @Test
    public void testCreateFile()
    {
        String str = "test.txt";
        File actual = TypeHandler.createFile(str);
        assertNotNull(actual);
        assertEquals("test.txt", actual.getName());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileNotFound()
    {
        String str = "/nonexistentfile.txt";
        File actual = TypeHandler.createFile(str);
        assertNull(actual);
    }

    @Test(expected = IOException.class)
    public void testCreateFileIOException() throws IOException
    {
        String str = "/testfile.txt";
        File file = new File(str);
        if (file.exists())
        {
            file.delete();
        }
        File actual = TypeHandler.createFile(str);
        assertNull(actual);
    }

    @Test
    public void testCreateFiles() throws ParseException
    {
        String str = "path/to/test/*.txt";
        File[] actual = TypeHandler.createFiles(str);
        assertNotNull(actual);
        assertEquals(0, actual.length);
    }
}