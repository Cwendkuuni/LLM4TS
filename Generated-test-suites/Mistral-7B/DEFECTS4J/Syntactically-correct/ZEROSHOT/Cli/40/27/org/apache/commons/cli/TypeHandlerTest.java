package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;

import org.junit.Test;

/**
 * TypeHandlerTest class file.
 */
public class TypeHandlerTest
{
    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} using String type.
     *
     * @throws ParseException if an error occurs
     */
    @Test
    public void testCreateValueStringObject() throws ParseException
    {
        Object obj = new Object();
        String str = "testString";
        assertEquals(str, TypeHandler.createValue(str, obj.getClass()));
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} using Object type.
     *
     * @throws ParseException if an error occurs
     */
    @Test
    public void testCreateValueObjectObject() throws ParseException
    {
        assertNull(TypeHandler.createValue("", Object.class));
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} using Class type.
     *
     * @throws ParseException if an error occurs
     */
    @Test
    public void testCreateValueClassObject() throws ParseException
    {
        Class<String> clazz = String.class;
        assertEquals("testString", TypeHandler.createValue("testString", clazz));
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} using Number type.
     *
     * @throws ParseException if an error occurs
     */
    @Test
    public void testCreateValueNumberObject() throws ParseException
    {
        long l = 1234;
        assertEquals(l, TypeHandler.createValue("1234", Long.TYPE));
        double d = 1234.5;
        assertEquals(d, TypeHandler.createValue("1234.5", Double.TYPE));
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} using Date type.
     *
     * @throws ParseException if an error occurs
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDateObject() throws ParseException, UnsupportedOperationException
    {
        TypeHandler.createValue("2023-03-31", Date.class);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} using Class type.
     *
     * @throws ParseException if an error occurs
     */
    @Test
    public void testCreateValueClass() throws ParseException
    {
        String className = "java.lang.String";
        assertEquals(className, TypeHandler.createValue(className, String.class).getClass().getName());
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} using File type.
     *
     * @throws ParseException if an error occurs
     * @throws IOException if an error occurs
     */
    @Test
    public void testCreateValueFileObject() throws ParseException, IOException
    {
        File file = new File("testFile.txt");
        assertEquals(file, TypeHandler.createValue("testFile.txt", File.class));
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} using existing File type.
     *
     * @throws ParseException if an error occurs
     * @throws FileNotFoundException if an error occurs
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateValueExistingFileObject() throws ParseException, FileNotFoundException
    {
        String filePath = "nonExistingFile.txt";
        TypeHandler.createValue(filePath, File.class);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} using Files type.
     *
     * @throws UnsupportedOperationException always
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFilesObject() throws UnsupportedOperationException
    {
        String filePaths = "testFile1.txt,testFile2.txt";
        TypeHandler.createValue(filePaths, File[].class);
    }

    /**
     * Test method for {@link TypeHandler#createValue(String, Object)} using URL type.
     *
     * @throws ParseException if an error occurs
     */
    @Test
    public void testCreateValueURLObject() throws ParseException
    {
        String urlString = "https://www.apache.org/";
        URL url = new URL(urlString);
        assertEquals(urlString, TypeHandler.createValue(urlString, URL.class).toExternalForm());
    }
}