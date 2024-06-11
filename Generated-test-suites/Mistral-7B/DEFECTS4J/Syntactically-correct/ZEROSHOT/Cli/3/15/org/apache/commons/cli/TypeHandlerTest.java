package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.junit.Test;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String str = "hello";
        Object obj = new Object();
        Object result = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClass() {
        String str = "java.lang.String";
        Class clazz = String.class;
        Object result = TypeHandler.createValue(str, clazz);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringClassNotFound() {
        String str = "org.apache.commons.cli.unknown";
        Class clazz = null;
        Object result = TypeHandler.createValue(str, clazz);
        assertNull(result);
    }

    @Test
    public void testCreateValueStringPatternStringValue() {
        String str = "hello";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, result);
    }

    @Test
    public void testCreateValueStringPatternObjectValue() {
        String str = "java.lang.String";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValueStringPatternNumberValue() {
        String str = "123";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertEquals(Number.class, result.getClass());
        Number number = (Number) result;
        assertEquals(123, number.intValue());
    }

    @Test
    public void testCreateValueStringPatternDateValue() {
        String str = "2022-12-31";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertEquals(Date.class, result.getClass());
    }

    @Test
    public void testCreateValueStringPatternClassValue() {
        String str = "java.util.Date";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        assertEquals(Date.class, result.getClass());
    }

    @Test
    public void testCreateValueStringPatternFileValue() {
        String str = "/path/to/file.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        assertEquals(File.class, result.getClass());
        File file = (File) result;
        assertTrue(file.exists());
    }

    @Test
    public void testCreateValueStringPatternFileValueInexist() {
        String str = "/path/to/nonexistentfile.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValueStringPatternExistingFileValue() {
        String str = "/path/to/existingfile.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertEquals(File.class, result.getClass());
        File file = (File) result;
        assertTrue(file.exists());
    }

    @Test
    public void testCreateValueStringPatternExistingFileValueInexist() {
        String str = "/path/to/nonexistentfile.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
        assertNull(result);
    }

    @Test
    public void testCreateValueStringPatternFilesValue() {
        String str = "/path/to/files/*.txt";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        assertEquals(File[].class, result.getClass());
    }

    @Test
    public void testCreateValueStringPatternURLValue() {
        String str = "http://example.com";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertEquals(URL.class, result.getClass());
    }

    @Test
    public void testCreateValueStringPatternURLValueMalformed() {
        String str = "http://example.com/notavalidurl";
        Object result = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertNull(result);
    }
}