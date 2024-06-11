package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    @Test
    public void testCreateValue_String_String() throws Exception
    {
        String str = "test";
        String objType = String.class.getName();
        Object result = TypeHandler.createValue(str, objType);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_String_Object() throws Exception
    {
        String str = "test";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        fail("Expected ParseException, but got " + result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_InvalidClass() throws Exception
    {
        String str = "test";
        Object obj = new Object();
        TypeHandler.createValue(str, Integer.class);
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_STRING_VALUE() throws Exception
    {
        String str = "test";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_OBJECT_VALUE() throws Exception
    {
        String str = "test.class";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertNotNull(result);
        assertTrue(result.getClass().isInstance(new Class[]{String.class, ClassLoader.class}));
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_NUMBER_VALUE() throws Exception
    {
        String str = "123";
        Number result = (Number) TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(123L, result.longValue());

        str = "123.5";
        result = (Number) TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(123.5, result.doubleValue(), 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_PatternOptionBuilder_NUMBER_VALUE_Invalid() throws Exception
    {
        String str = "invalid";
        TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_DATE_VALUE() throws Exception
    {
        String str = "2022-01-01";
        Date result = (Date) TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertNotNull(result);

        str = "2022-01-32";
        try
        {
            TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
            fail("Expected ParseException, but got " + result);
        }
        catch (ParseException pe)
        {
            // expected
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValue_String_PatternOptionBuilder_DATE_VALUE_NotImplemented() throws Exception
    {
        String str = "2022-01-01";
        TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_CLASS_VALUE() throws Exception
    {
        String className = "java.lang.String";
        Class<?> expectedClass = Class.forName(className);
        Object result = TypeHandler.createValue(className, PatternOptionBuilder.CLASS_VALUE);
        assertEquals(expectedClass, result.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_PatternOptionBuilder_CLASS_VALUE_InvalidClass() throws Exception
    {
        String className = "java.lang.InvalidClass";
        TypeHandler.createValue(className, PatternOptionBuilder.CLASS_VALUE);
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_FILE_VALUE() throws Exception
    {
        String filePath = "test.txt";
        File result = (File) TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_VALUE);
        assertEquals(new File(filePath), result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValue_String_PatternOptionBuilder_FILE_VALUE_FileNotFound() throws Exception
    {
        String filePath = "nonexistentfile.txt";
        TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_VALUE);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_PatternOptionBuilder_FILE_VALUE_Invalid() throws Exception
    {
        String filePath = "/test.txt";
        TypeHandler.createValue(filePath, PatternOptionBuilder.FILE_VALUE);
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_EXISTING_FILE_VALUE() throws Exception
    {
        String filePath = "test.txt";
        File expectedFile = new File(filePath);
        File result = (File) TypeHandler.createValue(filePath, PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertEquals(expectedFile, result);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateValue_String_PatternOptionBuilder_EXISTING_FILE_VALUE_FileNotFound() throws Exception
    {
        String filePath = "nonexistentfile.txt";
        TypeHandler.createValue(filePath, PatternOptionBuilder.EXISTING_FILE_VALUE);
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_FILES_VALUE() throws Exception
    {
        String filePaths = "test.txt,test2.txt";
        File[] expectedFiles = {new File("test.txt"), new File("test2.txt")};
        File[] result = (File[]) TypeHandler.createValue(filePaths, PatternOptionBuilder.FILES_VALUE);
        assertArrayEquals(expectedFiles, result);
    }

    @Test(expected = ParseException.class)
    public void testCreateValue_String_PatternOptionBuilder_FILES_VALUE_Invalid() throws Exception
    {
        String filePaths = "test.txt,nonexistentfile.txt,test3.txt";
        TypeHandler.createValue(filePaths, PatternOptionBuilder.FILES_VALUE);
    }

    @Test
    public void testCreateValue_String_PatternOptionBuilder_URL_VALUE() throws Exception
    {
        String urlString = "http://example.com";
        URL result = (URL) TypeHandler.createValue(urlString, PatternOptionBuilder.URL_VALUE);
        assertEquals(new URL(urlString), result);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateValue_String_PatternOptionBuilder_URL_VALUE_MalformedURL() throws Exception
    {
        String urlString = "http://example.com/invalid";
        TypeHandler.createValue(urlString, PatternOptionBuilder.URL_VALUE);
    }

    @Test(expected = IOException.class)
    public void testCreateValue_String_PatternOptionBuilder_URL_VALUE_IOException() throws Exception
    {
        String urlString = "file:nonexistentfile.txt";
        TypeHandler.createValue(urlString, PatternOptionBuilder.URL_VALUE);
    }
}