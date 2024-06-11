package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.util.ClassUtils;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests for the TypeHandler class.
 */
public class TypeHandlerTest
{
    private static Class<?> clazz;

    @BeforeClass
    public static void setup() throws Exception
    {
        clazz = ClassUtils.loadClass("org.apache.commons.cli.TypeHandler", TypeHandlerTest.class.getClassLoader());
    }

    /**
     * Test createObject.
     */
    @Test
    public void testCreateObject() throws ParseException, InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        Object obj = TypeHandler.createObject("java.lang.String");
        assertInstanceOf(String.class, obj);

        obj = TypeHandler.createObject("java.lang.Integer");
        assertInstanceOf(Integer.class, obj);

        obj = TypeHandler.createObject("java.util.Date");
        assertInstanceOf(Date.class, obj);

        obj = TypeHandler.createObject("org.apache.commons.cli.TestClass");
        assertInstanceOf(TestClass.class, obj);
    }

    /**
     * Test createNumber.
     */
    @Test
    public void testCreateNumber() throws ParseException
    {
        Number number = TypeHandler.createNumber("1234567890");
        assertEquals(Long.valueOf(1234567890L), number);

        number = TypeHandler.createNumber("123.45");
        assertEquals(Double.valueOf(123.45), number);
    }

    /**
     * Test createDate.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws ParseException
    {
        Date date = TypeHandler.createDate("2022-12-31");
    }

    /**
     * Test createURL.
     */
    @Test
    public void testCreateURL() throws ParseException
    {
        URL url = TypeHandler.createURL("http://example.com");
        assertNotNull(url);
    }

    /**
     * Test createFile.
     */
    @Test
    public void testCreateFile() throws ParseException
    {
        File file = TypeHandler.createFile("test.txt");
        assertTrue(file.exists());
    }

    /**
     * Test createClass.
     */
    @Test(expected = ParseException.class)
    public void testCreateClassInvalidClassName() throws ParseException
    {
        TypeHandler.createClass("java.lang.InvalidClassName");
    }

    /**
     * Test createFiles.
     * @throws IOException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() throws ParseException
    {
        File[] files = TypeHandler.createFiles("test.txt,test2.txt");
    }

    /**
     * Test createValue with String.
     */
    @Test
    public void testCreateValueString() throws ParseException
    {
        String value = (String) TypeHandler.createValue("test", String.class);
        assertEquals("test", value);
    }

    /**
     * Test createValue with Integer.
     */
    @Test
    public void testCreateValueInteger() throws ParseException
    {
        Integer value = (Integer) TypeHandler.createValue("123", Integer.class);
        assertEquals(Integer.valueOf(123), value);
    }

    /**
     * Test createValue with Double.
     */
    @Test
    public void testCreateValueDouble() throws ParseException
    {
        Double value = (Double) TypeHandler.createValue("123.45", Double.class);
        assertEquals(Double.valueOf(123.45), value);
    }

    /**
     * Test createValue with Date.
     * @throws ParseException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDate() throws ParseException
    {
        Date value = (Date) TypeHandler.createValue("2022-12-31", Date.class);
    }

    /**
     * Test createValue with Class.
     */
    @Test
    public void testCreateValueClass() throws ParseException, ClassNotFoundException, InstantiationException, IllegalAccessException
    {
        Class<?> clazz = TypeHandler.createValue("java.lang.String", Class.class).getClass();
        assertEquals(String.class, clazz);
    }

    /**
     * Test createValue with File.
     */
    @Test
    public void testCreateValueFile() throws ParseException
    {
        File file = new File("test.txt");
        File value = (File) TypeHandler.createValue("test.txt", File.class);
        assertEquals(file, value);
    }

    /**
     * Test createValue with FileInputStream.
     * @throws FileNotFoundException
     * @throws IOException
     */
    @Test(expected = ParseException.class)
    public void testCreateValueFileInputStream() throws FileNotFoundException, IOException
    {
        FileInputStream inputStream = new FileInputStream("test.txt");
        Object value = TypeHandler.createValue("test.txt", FileInputStream.class);
    }

    /**
     * Test createValue with URL.
     */
    @Test
    public void testCreateValueURL() throws ParseException, MalformedURLException
    {
        URL url = new URL("http://example.com");
        URL value = (URL) TypeHandler.createValue("http://example.com", URL.class);
        assertEquals(url, value);
    }

    /**
     * Test createValue with PatternOptionBuilder.STRING_VALUE.
     */
    @Test
    public void testCreateValueStringValue() throws ParseException
    {
        String value = (String) TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE.getClass());
        assertEquals("test", value);
    }

    /**
     * Test createValue with PatternOptionBuilder.OBJECT_VALUE.
     */
    @Test
    public void testCreateValueObjectValue() throws ParseException
    {
        Object value = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.OBJECT_VALUE.getClass());
        assertInstanceOf(String.class, value);
    }

    /**
     * Test createValue with PatternOptionBuilder.NUMBER_VALUE.
     */
    @Test
    public void testCreateValueNumberValue() throws ParseException
    {
        Number value = (Number) TypeHandler.createValue("1234567890", PatternOptionBuilder.NUMBER_VALUE.getClass());
        assertEquals(Long.valueOf(1234567890L), value);
    }

    /**
     * Test createValue with PatternOptionBuilder.DATE_VALUE.
     * @throws ParseException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueDateValue() throws ParseException
    {
        Date value = (Date) TypeHandler.createValue("2022-12-31", PatternOptionBuilder.DATE_VALUE.getClass());
    }

    /**
     * Test createValue with PatternOptionBuilder.CLASS_VALUE.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueClassValue() throws ParseException
    {
        Class<?> value = (Class<?>) TypeHandler.createValue("java.lang.String", PatternOptionBuilder.CLASS_VALUE.getClass());
    }

    /**
     * Test createValue with PatternOptionBuilder.FILE_VALUE.
     */
    @Test
    public void testCreateValueFileValue() throws ParseException
    {
        File file = new File("test.txt");
        File value = (File) TypeHandler.createValue(file, PatternOptionBuilder.FILE_VALUE.getClass());
        assertEquals(file, value);
    }

    /**
     * Test createValue with PatternOptionBuilder.EXISTING_FILE_VALUE.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueExistingFileValue() throws ParseException
    {
        File file = new File("test.txt");
        File value = (File) TypeHandler.createValue(file, PatternOptionBuilder.EXISTING_FILE_VALUE.getClass());
    }

    /**
     * Test createValue with PatternOptionBuilder.FILES_VALUE.
     * @throws IOException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueFilesValue() throws ParseException, IOException
    {
        File[] files = new File[1];
        files[0] = new File("test.txt");
        File[] value = (File[]) TypeHandler.createValue(files, PatternOptionBuilder.FILES_VALUE.getClass());
    }

    /**
     * Test createValue with PatternOptionBuilder.URL_VALUE.
     */
    @Test
    public void testCreateValueURLValue() throws ParseException, MalformedURLException
    {
        URL url = new URL("http://example.com");
        URL value = (URL) TypeHandler.createValue(url, PatternOptionBuilder.URL_VALUE.getClass());
        assertEquals(url, value);
    }
}