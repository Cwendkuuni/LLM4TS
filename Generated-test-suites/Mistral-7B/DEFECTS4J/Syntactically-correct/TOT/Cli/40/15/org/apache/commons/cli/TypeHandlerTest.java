package org.apache.commons.cli.test;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class TypeHandlerTest
{
    @Test
    public void testCreateValueForString() throws ParseException
    {
        Object value = TypeHandler.createValue("testString", String.class);
        Assert.assertEquals("testString", value);
    }

    @Test
    public void testCreateValueForObject() throws ParseException, ClassNotFoundException, InstantiationException
    {
        Object value = TypeHandler.createValue("java.lang.String", String.class);
        Assert.assertNotNull(value);
        Assert.assertEquals("", ((String) value).isEmpty());

        Class<?> clazz = Class.forName("java.util.Date");
        Object dateValue = TypeHandler.createValue("2022-12-31", clazz);
        Assert.assertNotNull(dateValue);
    }

    @Test
    public void testCreateValueForNumber() throws ParseException
    {
        Object longValue = TypeHandler.createValue("123", Long.class);
        Assert.assertEquals(123L, longValue);

        Object doubleValue = TypeHandler.createValue("123.45", Double.class);
        Assert.assertEquals(123.45, doubleValue, 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateValueForInvalidNumber() throws ParseException
    {
        TypeHandler.createValue("abc", Integer.class);
    }

    @Test
    public void testCreateValueForDate() throws ParseException
    {
        Object dateValue = TypeHandler.createValue("2022-12-31", Date.class);
        Assert.assertNotNull(dateValue);
        Assert.assertEquals(new Date(2022 - 1900, 11, 31).getTime(), dateValue.getTime());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueForUnsupportedType() throws ParseException
    {
        Object value = TypeHandler.createValue("testValue", Integer[].class);
        Assert.fail("Unexpected value: " + value);
    }

    @Test
    public void testCreateObject() throws ParseException, ClassNotFoundException, InstantiationException
    {
        Class<?> clazz = Class.forName("java.util.Date");
        Object dateObject = TypeHandler.createObject("java.util.Date");
        Assert.assertNotNull(dateObject);
        Assert.assertEquals(clazz, dateObject.getClass());
    }

    @Test(expected = ParseException.class)
    public void testCreateObjectForInvalidClassName() throws ParseException, ClassNotFoundException
    {
        TypeHandler.createObject("java.util.InvalidClassName");
    }

    @Test
    public void testCreateNumber() throws ParseException
    {
        Object longValue = TypeHandler.createNumber("123");
        Assert.assertEquals(123L, longValue);

        Object doubleValue = TypeHandler.createNumber("123.45");
        Assert.assertEquals(123.45, doubleValue, 0.001);
    }

    @Test(expected = ParseException.class)
    public void testCreateNumberForInvalidFormat() throws ParseException
    {
        TypeHandler.createNumber("abc");
    }

    @Test
    public void testCreateClass() throws ParseException, ClassNotFoundException
    {
        Class<?> clazz = TypeHandler.createClass("java.util.Date");
        Assert.assertEquals(Class.forName("java.util.Date"), clazz);
    }

    @Test(expected = ParseException.class)
    public void testCreateClassForNonExistentClass() throws ParseException, ClassNotFoundException
    {
        TypeHandler.createClass("java.util.NonExistentClass");
    }

    @Test
    public void testCreateDate() throws ParseException
    {
        Date date = TypeHandler.createDate("2022-12-31");
        Assert.assertNotNull(date);
        Assert.assertEquals(new Date(2022 - 1900, 11, 31).getTime(), date.getTime());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDateForUnsupportedFormat() throws ParseException
    {
        TypeHandler.createDate("2022-12-31T12:34:56");
    }

    @Test
    public void testCreateURL() throws ParseException, MalformedURLException
    {
        URL url = TypeHandler.createURL("http://example.com");
        Assert.assertNotNull(url);
        Assert.assertEquals("http://example.com", url.toString());
    }

    @Test(expected = ParseException.class)
    public void testCreateURLForInvalidFormat() throws ParseException, MalformedURLException
    {
        TypeHandler.createURL("http://example.com/invalidFormat");
    }

    @Test
    public void testCreateFile()
    {
        File file = new File("testFile.txt");
        File createdFile = TypeHandler.createFile("testFile.txt");
        Assert.assertEquals(file, createdFile);
    }

    @Test(expected = ParseException.class)
    public void testCreateFileForInvalidPath() throws ParseException
    {
        TypeHandler.createFile("testFile/../testFile.txt");
    }

    @Test
    public void testOpenFile() throws FileNotFoundException, ParseException
    {
        File testFile = new File("testFile.txt");
        FileInputStream fileInput = new FileInputStream(testFile);
        FileInputStream openedFile = TypeHandler.openFile("testFile.txt");
        Assert.assertEquals(fileInput, openedFile);
    }

    @Test(expected = ParseException.class)
    public void testOpenFileForInvalidPath() throws FileNotFoundException, ParseException
    {
        TypeHandler.openFile("testFile/../testFile.txt");
    }
}