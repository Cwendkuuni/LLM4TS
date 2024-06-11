package org.apache.commons.cli.test;

import static org.apache.commons.cli.TypeHandler.createValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * TypeHandlerTest class for testing TypeHandler class methods.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method for String type.
     */
    @Test
    public void testCreateValueString() throws ParseException
    {
        String str = "test";
        assertEquals("test", createValue(str, String.class));
    }

    /**
     * Test createValue method for Object type.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueObject() throws ParseException
    {
        String objName = "TestObject";
        createValue(null, Object.class);
    }

    /**
     * Test createValue method for Object type with custom class.
     */
    @Test
    public void testCreateValueObjectCustomClass() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        String objName = "java.lang.Long";
        Long obj = (Long) createValue("42", Long.class);
        assertEquals(42, obj.longValue());

        Class<?> clazz = Class.forName(objName);
        Object obj2 = createValue("42", clazz);
        assertEquals(42, obj2.longValue());
    }

    /**
     * Test createValue method for Number type.
     */
    @Test
    public void testCreateValueNumber() throws ParseException
    {
        String strNumber = "42";
        Number number = createValue(strNumber, Number.class);
        assertEquals(42, number.intValue());

        String strDouble = "42.3";
        Number doubleNumber = createValue(strDouble, Number.class);
        assertEquals(42.3, doubleNumber.doubleValue(), 0.0);
    }

    /**
     * Test createValue method for Date type.
     * This method is not yet implemented and always throws an
     * {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate() throws ParseException
    {
        String strDate = "2022-01-01";
        Date date = createValue(strDate, Date.class);
    }

    /**
     * Test createValue method for Class type.
     */
    @Test
    public void testCreateValueClass() throws ParseException, ClassNotFoundException
    {
        String className = "java.lang.String";
        Class<?> clazz = createValue(className, Class.class);
        assertEquals(String.class, clazz);
    }

    /**
     * Test createValue method for File type.
     */
    @Test
    public void testCreateValueFile() throws ParseException, FileNotFoundException
    {
        String filePath = "testFile.txt";
        File file = createValue(filePath, File.class);
        assertNotNull(file);
        assertEquals(new File(filePath), file);
    }

    /**
     * Test createValue method for ExistingFile type.
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateValueExistingFile() throws ParseException, FileNotFoundException
    {
        String filePath = "notExistingFile.txt";
        File file = createValue(filePath, File.class);
        assertNull(file);
    }

    /**
     * Test createValue method for Files type.
     * This method is not yet implemented and always throws an
     * {@link UnsupportedOperationException}.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFiles() throws ParseException
    {
        String filePaths = "testFile.txt,testFile2.txt";
        File[] files = createValue(filePaths, File[].class);
    }

    /**
     * Test createValue method for URL type.
     */
    @Test
    public void testCreateValueURL() throws ParseException, MalformedURLException
    {
        String urlString = "https://www.test.com/";
        URL url = createValue(urlString, URL.class);
        assertNotNull(url);
        assertEquals(new URL("https://www.test.com/"), url);
    }
}