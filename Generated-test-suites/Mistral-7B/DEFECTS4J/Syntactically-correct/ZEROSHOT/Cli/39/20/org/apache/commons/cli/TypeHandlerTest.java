package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
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
     * Test createValue method with String type.
     */
    @Test
    public void testCreateValueWithStringType()
    {
        String str = "test";
        String expected = str;

        Object result = TypeHandler.createValue(str, String.class);

        assertEquals(expected, result);
    }

    /**
     * Test createValue method with Object type.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueWithObjectType()
    {
        String classname = "java.lang.Integer";
        TypeHandler.createValue(classname, Object.class);
    }

    /**
     * Test createValue method with Class type.
     */
    @Test
    public void testCreateValueWithClassType()
    {
        String classname = "java.lang.String";
        Class<?> expectedClass = String.class;

        Class<?> resultClass = TypeHandler.createValue(classname, Class.class);

        assertEquals(expectedClass, resultClass);
    }

    /**
     * Test createValue method with number types.
     */
    @Test
    public void testCreateValueWithNumberTypes()
    {
        String strLong = "123";
        long expectedLong = 123;

        Object resultLong = TypeHandler.createValue(strLong, Long.TYPE);

        assertEquals(expectedLong, resultLong);

        String strDouble = "123.45";
        double expectedDouble = 123.45;

        Object resultDouble = TypeHandler.createValue(strDouble, Double.TYPE);

        assertEquals(expectedDouble, resultDouble);
    }

    /**
     * Test createValue method with Date type.
     * @throws ParseException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueWithDateType() throws ParseException
    {
        String dateString = "2023-03-31";
        Date expectedDate = new Date(2023 - 1900, 2 - 1, 31);

        Date resultDate = TypeHandler.createValue(dateString, Date.class);

        assertEquals(expectedDate, resultDate);
    }

    /**
     * Test createValue method with URL type.
     */
    @Test(expected = ParseException.class)
    public void testCreateValueWithURLType()
    {
        String urlString = "https://example.com";
        TypeHandler.createValue(urlString, URL.class);
    }

    /**
     * Test createValue method with File type.
     */
    @Test
    public void testCreateValueWithFileType()
    {
        String filePath = "/path/to/file.txt";
        File expectedFile = new File(filePath);

        File resultFile = TypeHandler.createValue(filePath, File.class);

        assertEquals(expectedFile, resultFile);
    }

    /**
     * Test createValue method with FileInputStream type.
     * @throws IOException
     */
    @Test(expected = ParseException.class)
    public void testCreateValueWithFileInputStreamType() throws IOException
    {
        String filePath = "/path/to/file.txt";
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);

        TypeHandler.createValue(filePath, java.io.FileInputStream.class);
    }

    /**
     * Test createValue method with File[] type.
     * @throws UnsupportedOperationException
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateValueWithFileArrayType()
    {
        String filePaths = "/path/to/file1.txt,/path/to/file2.txt";
        File[] expectedFiles = new File[] { new File("/path/to/file1.txt"), new File("/path/to/file2.txt") };

        File[] resultFiles = TypeHandler.createValue(filePaths, File[].class);

        assertArrayEquals(expectedFiles, resultFiles);
    }
}