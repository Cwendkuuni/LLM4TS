package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Date;

/**
 * Test for TypeHandler class.
 */
public class TypeHandlerTest {

    /**
     * Test createValue method with String type.
     */
    @Test
    public void testCreateValueString() {
        String str = "test";
        Object obj = TypeHandler.createValue(str, String.class);
        assertEquals("test", obj);
    }

    /**
     * Test createValue method with Object type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueObject() {
        Object obj = TypeHandler.createValue("test", Object.class);
        fail("Expected IllegalArgumentException but got: " + obj);
    }

    /**
     * Test createValue method with PatternOptionBuilder.STRING_VALUE type.
     */
    @Test
    public void testCreateValueStringValue() {
        Object obj = TypeHandler.createValue("test", PatternOptionBuilder.STRING_VALUE);
        assertEquals("test", obj);
    }

    /**
     * Test createValue method with PatternOptionBuilder.OBJECT_VALUE type.
     */
    @Test
    public void testCreateValueObjectValue() {
        Class<?> clazz = ClassUtils.forName("java.lang.String");
        Object obj = TypeHandler.createValue("java.lang.String", PatternOptionBuilder.OBJECT_VALUE);
        assertEquals(clazz, obj.getClass());
    }

    /**
     * Test createValue method with PatternOptionBuilder.NUMBER_VALUE type.
     */
    @Test
    public void testCreateValueNumber() {
        Object obj = TypeHandler.createValue("42", Number.class);
        assertTrue(obj instanceof Number);
        Number number = (Number) obj;
        assertEquals(42, number.intValue());

        obj = TypeHandler.createValue("3.14", Number.class);
        assertTrue(obj instanceof Number);
        number = (Number) obj;
        assertEquals(3, number.byteValue());
        assertEquals(14, number.intValue());
        assertEquals(3.14F, number.floatValue(), 0.001F);
        assertEquals(3.14D, number.doubleValue(), 0.001D);
    }

    /**
     * Test createValue method with PatternOptionBuilder.DATE_VALUE type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueDateInvalidFormat() {
        Object obj = TypeHandler.createValue("2022/33/01", Date.class);
        fail("Expected IllegalArgumentException but got: " + obj);
    }

    /**
     * Test createValue method with PatternOptionBuilder.DATE_VALUE type.
     */
    @Test
    public void testCreateValueDate() {
        Object obj = TypeHandler.createValue("2022-11-25", Date.class);
        assertTrue(obj instanceof Date);
        Date date = (Date) obj;
        assertEquals("Fri Nov 25 00:00:00 CET 2022", date.toString());
    }

    /**
     * Test createValue method with PatternOptionBuilder.CLASS_VALUE type.
     */
    @Test
    public void testCreateValueClass() {
        String className = "java.lang.String";
        Object obj = TypeHandler.createValue(className, Class.class);
        assertEquals(className, obj.toString());
    }

    /**
     * Test createValue method with PatternOptionBuilder.FILE_VALUE type.
     */
    @Test
    public void testCreateValueFile() {
        String filePath = "test.txt";
        Object obj = TypeHandler.createValue(filePath, File.class);
        assertTrue(obj instanceof File);
        File file = (File) obj;
        assertTrue(file.exists() || new File(filePath).mkdirs());
        assertEquals(filePath, file.getAbsolutePath());
    }

    /**
     * Test createValue method with PatternOptionBuilder.EXISTING_FILE_VALUE type.
     */
    @Test(expected = FileNotFoundException.class)
    public void testCreateValueExistingFileNotFound() {
        String filePath = "test_not_exists.txt";
        Object obj = TypeHandler.createValue(filePath, File.class);
        fail("Expected FileNotFoundException but got: " + obj);
    }

    /**
     * Test createValue method with PatternOptionBuilder.URL_VALUE type.
     */
    @Test
    public void testCreateValueURL() {
        String urlString = "https://apache.org";
        Object obj = TypeHandler.createValue(urlString, URL.class);
        assertTrue(obj instanceof URL);
        URL url = (URL) obj;
        assertEquals(urlString, url.toString());
    }

    /**
     * Test createValue method with invalid type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCreateValueInvalidType() {
        Object obj = TypeHandler.createValue("test", Integer.TYPE);
        fail("Expected IllegalArgumentException but got: " + obj);
    }
}