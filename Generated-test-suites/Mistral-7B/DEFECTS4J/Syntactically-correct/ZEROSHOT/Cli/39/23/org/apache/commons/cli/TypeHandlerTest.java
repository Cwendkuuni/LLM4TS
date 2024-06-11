package org.apache.commons.cli.test;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * TypeHandlerTest class.
 */
public class TypeHandlerTest
{
    /**
     * Test createValue method for String class.
     *
     * @throws ParseException when an error occurs
     */
    @Test
    public void testCreateValueString() throws ParseException
    {
        String str = "TestString";
        Object obj = TypeHandler.createValue(str, String.class);
        assertInstanceOf(String.class, obj);
        assertEquals(str, obj);
    }

    /**
     * Test createValue method for Object class.
     *
     * @throws ParseException when an error occurs
     */
    @Test
    public void testCreateValueObject() throws ParseException
    {
        Class<?> clazz = String.class;
        Object obj = TypeHandler.createValue((String) null, clazz);
        assertNull(obj);

        String className = "java.lang.String";
        Object objCreated = TypeHandler.createValue(className, Object.class);
        assertEquals(className, objCreated.getClass().getName());
    }

    /**
     * Test createValue method for Number class.
     *
     * @throws ParseException when an error occurs
     */
    @Test
    public void testCreateValueNumber() throws ParseException
    {
        String strLong = "123";
        Long numberLong = TypeHandler.createValue(strLong, Long.class);
        assertEquals(123L, numberLong.longValue());

        String strDouble = "123.45";
        Double numberDouble = TypeHandler.createValue(strDouble, Double.class);
        assertEquals(123.45, numberDouble.doubleValue(), 0.0);
    }

    /**
     * Test createValue method for Date class.
     *
     * @throws ParseException when an error occurs
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate() throws ParseException
    {
        String dateString = "2022-03-25";
        Date date = TypeHandler.createValue(dateString, Date.class);
        // The createDate method is not implemented and always throws an UnsupportedOperationException
        //assertInstanceOf(Date.class, date);
    }

    /**
     * Test createValue method for Class class.
     *
     * @throws ParseException when an error occurs
     */
    @Test
    public void testCreateValueClass() throws ParseException
    {
        String className = "java.lang.String";
        Class<?> clazz = TypeHandler.createValue(className, Class.class);
        assertEquals(String.class, clazz);
    }

    /**
     * Test createValue method for File class.
     *
     * @throws ParseException when an error occurs
     */
    @Test
    public void testCreateValueFile() throws ParseException, FileNotFoundException, IOException
    {
        String filePath = "testFile.txt";
        File file = TypeHandler.createValue(filePath, File.class);
        assertEquals(new File(filePath), file);

        FileInputStream fis = new FileInputStream(file);
        fis.close();
    }

    /**
     * Test createValue method for FileInputStream class.
     *
     * @throws ParseException when an error occurs
     * @throws FileNotFoundException when the file is not found
     * @throws IOException when an I/O error occurs
     */
    @Test(expected = ParseException.class)
    public void testCreateValueFileInputStream() throws ParseException, FileNotFoundException, IOException
    {
        String filePath = "testFileNotFound.txt";
        TypeHandler.createValue(filePath, FileInputStream.class);
    }

    /**
     * Test createValue method for URL class.
     *
     * @throws ParseException when an error occurs
     */
    @Test
    public void testCreateValueURL() throws ParseException, MalformedURLException
    {
        String urlString = "https://www.example.com";
        URL url = TypeHandler.createValue(urlString, URL.class);
        assertEquals(new URL(urlString), url);
    }
}