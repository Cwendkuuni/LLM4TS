package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.util.PatternOptionBuilder;

import org.junit.Test;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest
{
    /**
     * Test method for {@link TypeHandler#createValue(String, Object)}.
     */
    @Test
    public void testCreateValueStringObject()
    {
        // Test String type
        Object strValue = TypeHandler.createValue("test", String.class);
        assertEquals("test", strValue);

        // Test null object
        assertNull(TypeHandler.createValue(null, Object.class));

        // Test unknown object type
        assertNull(TypeHandler.createValue("test", Integer.class));

        // Test Object type
        Object objValue = TypeHandler.createValue("org.apache.commons.cli.test.DummyClass", DummyClass.class);
        assertEquals(0, ((DummyClass) objValue).getCounter());

        // Test Number type
        Number numberValue = TypeHandler.createValue("123", Integer.class);
        assertEquals(123, numberValue.intValue());

        Number numberValueDouble = TypeHandler.createValue("123.45", Double.class);
        assertEquals(123.45, numberValueDouble.doubleValue(), 0.001);

        // Test Date type (not implemented)
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue("2022-01-01", Date.class));

        // Test Class type
        Class<?> clazzValue = TypeHandler.createValue("java.lang.String", String.class);
        assertEquals(String.class, clazzValue);

        // Test URL type
        URL urlValue = TypeHandler.createValue("http://example.com", URL.class);
        assertEquals("http://example.com", urlValue.toString());

        // Test File type
        File fileValue = TypeHandler.createValue("/path/to/file.txt", File.class);
        assertEquals("/path/to/file.txt", fileValue.getAbsolutePath());

        // Test FileInputStream type
        File file = new File("/path/to/file.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createValue("/path/to/non-existent-file.txt", FileInputStream.class));
        FileInputStream fileInputStreamValue = TypeHandler.createValue("/path/to/file.txt", FileInputStream.class);
        assertEquals(fileInputStream, fileInputStreamValue);

        // Test existing File type
        File existingFileValue = TypeHandler.createValue("/path/to/file.txt", File.class);
        assertTrue(existingFileValue.exists());

        // Test existing Files type (not implemented)
        assertThrows(UnsupportedOperationException.class, () -> TypeHandler.createValue("/path/to/file1.txt,/path/to/file2.txt", File[].class));

        // Test unknown type
        assertNull(TypeHandler.createValue("test", Object.class));
    }

    /**
     * Test method for {@link TypeHandler#createObject(String)}.
     */
    @Test
    public void testCreateObject()
    {
        // Test valid classname
        Object objValue = TypeHandler.createObject("org.apache.commons.cli.test.DummyClass");
        assertEquals(0, ((DummyClass) objValue).getCounter());

        // Test invalid classname
        assertThrows(ParseException.class, () -> TypeHandler.createObject("java.lang.NonExistentClass"));
    }

    /**
     * Test method for {@link TypeHandler#createNumber(String)}.
     */
    @Test
    public void testCreateNumber()
    {
        // Test Long type
        Number numberValue = TypeHandler.createNumber("123");
        assertEquals(123, numberValue.longValue());

        // Test Double type
        Number numberValueDouble = TypeHandler.createNumber("123.45");
        assertEquals(123.45, numberValueDouble.doubleValue(), 0.001);
    }

    /**
     * Test method for {@link TypeHandler#createClass(String)}.
     */
    @Test
    public void testCreateClass()
    {
        // Test valid classname
        Class<?> clazzValue = TypeHandler.createClass("java.lang.String");
        assertEquals(String.class, clazzValue);

        // Test invalid classname
        assertThrows(ParseException.class, () -> TypeHandler.createClass("java.lang.NonExistentClass"));
    }

    /**
     * Test method for {@link TypeHandler#createDate(String)}.
     *
     * @throws ParseException never thrown in this test
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDate() throws ParseException
    {
        // Not implemented, always throws UnsupportedOperationException
        TypeHandler.createDate("2022-01-01");
    }

    /**
     * Test method for {@link TypeHandler#createURL(String)}.
     */
    @Test
    public void testCreateURL()
    {
        // Test well-formed URL
        URL urlValue = TypeHandler.createURL("http://example.com");
        assertEquals("http://example.com", urlValue.toString());

        // Test invalid URL
        assertThrows(ParseException.class, () -> TypeHandler.createURL("http://example.com/"));
    }

    /**
     * Test method for {@link TypeHandler#createFile(String)}.
     */
    @Test
    public void testCreateFile()
    {
        // Test existent file
        File fileValue = TypeHandler.createFile("/path/to/file.txt");
        assertEquals("/path/to/file.txt", fileValue.getAbsolutePath());
        assertTrue(fileValue.exists());

        // Test non-existent file
        assertThrows(FileNotFoundException.class, () -> TypeHandler.createFile("/path/to/non-existent-file.txt"));
    }

    /**
     * Test method for {@link TypeHandler#createFiles(String)}.
     *
     * @throws UnsupportedOperationException never thrown in this test
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testCreateFiles() throws UnsupportedOperationException
    {
        // Not implemented, always throws UnsupportedOperationException
        TypeHandler.createFiles("/path/to/file1.txt,/path/to/file2.txt");
    }
}

/**
 * Dummy class for testing.
 */
class DummyClass
{
    private static int counter;

    static
    {
        counter = 0;
    }

    public DummyClass()
    {
        counter++;
    }

    public int getCounter()
    {
        return counter;
    }
}