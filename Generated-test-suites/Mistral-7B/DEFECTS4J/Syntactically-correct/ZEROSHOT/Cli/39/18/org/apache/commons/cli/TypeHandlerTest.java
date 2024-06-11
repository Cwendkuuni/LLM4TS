package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValueStringString() throws ParseException
    {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);
    }

    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        Object result = TypeHandler.createValue("test", Object.class);
        assertNull(result);
    }

    @Test
    public void testCreateValueStringClassString() throws ParseException
    {
        Object result = TypeHandler.createValue("test", String.class);
        assertEquals("test", result);

        result = TypeHandler.createValue("123", Integer.class);
        assertEquals(123, result);

        result = TypeHandler.createValue("123.45", Double.class);
        assertEquals(123.45, result);

        result = TypeHandler.createValue("true", Boolean.class);
        assertEquals(true, result);

        result = TypeHandler.createValue("java.lang.String", String.class);
        assertEquals("java.lang.String", result);

        Class<?> clazz = TypeHandler.createClass("java.util.Date");
        result = TypeHandler.createValue("2022-12-31", clazz);
        assertEquals(new Date(2022, 11, 31), result);

        clazz = TypeHandler.createClass("java.net.URL");
        result = TypeHandler.createValue("http://www.example.com", clazz);
        assertEquals(new URL("http://www.example.com"), result);

        File file = new File("test.txt");
        result = TypeHandler.createValue(file.getAbsolutePath(), File.class);
        assertEquals(file, result);

        result = TypeHandler.createValue("/test.txt", File.class);
        assertEquals(new File("/test.txt"), result);

        result = TypeHandler.createValue("/test.txt", File[].class);
        assertNull(result);

        result = TypeHandler.createValue("test.txt,test2.txt", File[].class);
        File[] files = new File[] { new File("test.txt"), new File("test2.txt") };
        assertEquals(files, result);

        result = TypeHandler.createValue("http://www.example.com/test.txt,http://www.example.com/test2.txt", URL[].class);
        URL[] urls = new URL[] { new URL("http://www.example.com/test.txt"), new URL("http://www.example.com/test2.txt") };
        assertEquals(urls, result);
    }

    @Test
    public void testCreateValueStringClassNull() throws ParseException
    {
        assertThrows(ParseException.class, () -> TypeHandler.createValue(null, String.class));
    }

    @Test
    public void testCreateValueStringClassInvalidType() throws ParseException
    {
        assertThrows(ParseException.class, () -> TypeHandler.createValue("test", Integer.class));
    }

    @Test
    public void testCreateValueStringClassInvalidValue() throws ParseException, IOException
    {
        assertThrows(ParseException.class, () -> TypeHandler.createValue("invalid", FileInputStream.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws ParseException
    {
        Date result = TypeHandler.createDate("2022-12-31");
        assertNull(result);
    }
}