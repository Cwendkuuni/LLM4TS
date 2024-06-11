package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.utils.Patterns;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest
{
    @org.junit.Test
    public void testCreateValue_StringObject() throws ParseException
    {
        String str = "Test String";
        Object obj = new Object();
        Object value = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, value);
    }

    @org.junit.Test
    public void testCreateValue_StringString() throws ParseException
    {
        String str = "Test String";
        String expected = "Test String";
        Object value = TypeHandler.createValue(str, String.class);
        assertEquals(expected, value);
    }

    @org.junit.Test
    public void testCreateValue_StringClassObject() throws ParseException
    {
        String str = "Test String";
        Class<?> clazz = String.class;
        Object obj = new Object();
        Object value = TypeHandler.createValue(str, clazz);
        assertEquals(str, value);
    }

    @org.junit.Test(expected = ParseException.class)
    public void testCreateValue_InvalidClass() throws ParseException
    {
        String str = "Test String";
        Class<?> clazz = Integer.class;
        Object obj = new Object();
        TypeHandler.createValue(str, clazz);
        fail("Expected ParseException");
    }

    @org.junit.Test
    public void testCreateObject_Classname() throws ParseException
    {
        String classname = String.class.getName();
        Object obj = TypeHandler.createObject(classname);
        assertTrue(obj instanceof String);
    }

    @org.junit.Test(expected = ParseException.class)
    public void testCreateObject_InvalidClassname() throws ParseException
    {
        String classname = "InvalidClassname";
        TypeHandler.createObject(classname);
        fail("Expected ParseException");
    }

    @org.junit.Test
    public void testCreateNumber_String() throws ParseException
    {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        assertEquals(123L, number.longValue());
    }

    @org.junit.Test
    public void testCreateNumber_StringWithDecimal() throws ParseException
    {
        String str = "123.45";
        Number number = TypeHandler.createNumber(str);
        assertEquals(123.45, number.doubleValue(), 0.001);
    }

    @org.junit.Test(expected = ParseException.class)
    public void testCreateNumber_InvalidString() throws ParseException
    {
        String str = "InvalidNumber";
        TypeHandler.createNumber(str);
        fail("Expected ParseException");
    }

    @org.junit.Test
    public void testCreateDate_InvalidString() throws ParseException
    {
        String str = "InvalidDate";
        Date date = TypeHandler.createDate(str);
        assertNull(date);
    }

    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testCreateDate_ValidString() throws ParseException
    {
        String str = "2023-03-21";
        Date date = TypeHandler.createDate(str);
        fail("Expected UnsupportedOperationException");
    }

    @org.junit.Test
    public void testCreateURL_ValidString() throws ParseException
    {
        String str = "http://www.example.com";
        URL url = TypeHandler.createURL(str);
        assertNotNull(url);
    }

    @org.junit.Test(expected = ParseException.class)
    public void testCreateURL_InvalidString() throws ParseException
    {
        String str = "InvalidURL";
        TypeHandler.createURL(str);
        fail("Expected ParseException");
    }

    @org.junit.Test
    public void testCreateFile_ValidString() throws ParseException, IOException
    {
        String str = "testFile.txt";
        File file = TypeHandler.createFile(str);
        assertNotNull(file);
        assertTrue(file.exists());
        file.delete();
    }

    @org.junit.Test(expected = ParseException.class)
    public void testCreateFile_InvalidString() throws ParseException
    {
        String str = "InvalidFilePath";
        TypeHandler.createFile(str);
        fail("Expected ParseException");
    }
}