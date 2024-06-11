package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.Date;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * TypeHandlerTest class file for JUnit 4 testing
 */
public class TypeHandlerTest {

    @Test
    public void testCreateValue_String_PatternOptionBuilder_STRING_VALUE() {
        String str = "test";
        Object obj = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValue_String_Object_Null() {
        String str = "test";
        Object obj = TypeHandler.createValue(str, null);
        assertNull(obj);
    }

    @Test
    public void testCreateValue_String_Object_Class_String() {
        String str = "test";
        Class<String> clazz = String.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertEquals(str, obj);
    }

    @Test
    public void testCreateValue_String_Object_Class_Object() {
        String str = "test";
        Class<Object> clazz = Object.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertNull(obj);
    }

    @Test
    public void testCreateValue_String_Object_Class_Number() {
        String str = "123";
        Class<Number> clazz = Number.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertNotNull(obj);
        Number number = (Number) obj;
        assertEquals(123, number.intValue());
    }

    @Test
    public void testCreateValue_String_Object_Class_Date() {
        String str = "2023-03-28";
        Class<Date> clazz = Date.class;
        Object obj = TypeHandler.createValue(str, clazz);
        assertNotNull(obj);
        Date date = (Date) obj;
        assertEquals("Tue Mar 28 00:00:00 CET 2023", date.toString());
    }

    @Test
    public void testCreateValue_String_Object_Class_Class() {
        String str = "java.lang.String";
        Class<String> expectedClass = String.class;
        Class<?> clazz = ClassUtils.loadClass(str, this.getClass().getClassLoader());
        Object obj = TypeHandler.createValue(str, expectedClass);
        assertEquals(expectedClass, clazz);
        assertEquals(expectedClass, obj.getClass());
    }

    @Test
    public void testCreateValue_String_Object_Class_File() {
        String str = "testFile.txt";
        Class<File> clazz = File.class;
        Object obj = TypeHandler.createValue(str, clazz);
        File file = (File) obj;
        assertTrue(file.exists() || new File(str).createNewFile());
        assertTrue(file.delete());
    }

    @Test
    public void testCreateValue_String_Object_Class_URL() {
        String str = "http://example.com";
        Class<URL> clazz = URL.class;
        Object obj = TypeHandler.createValue(str, clazz);
        URL url = (URL) obj;
        assertEquals(str, url.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateValue_String_Object_Class_Unknown() {
        String str = "java.lang.UnknownClass";
        Class<? extends Object> clazz = UnknownClass.class;
        Object obj = TypeHandler.createValue(str, clazz);
        fail("Unexpected value: " + obj);
    }

    @Test
    public void testCreateObject_String() {
        String className = "java.lang.String";
        Object obj = TypeHandler.createObject(className);
        assertEquals(String.class, obj.getClass());
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateObject_String_UnknownClass() {
        String className = "java.lang.UnknownClass";
        TypeHandler.createObject(className);
    }

    @Test
    public void testCreateNumber_String_Integer() {
        String str = "123";
        Object obj = TypeHandler.createNumber(str);
        Number number = (Number) obj;
        assertEquals(123, number.intValue());
    }

    @Test
    public void testCreateNumber_String_Long() {
        String str = "456";
        Object obj = TypeHandler.createNumber(str);
        Number number = (Number) obj;
        assertEquals(456, number.longValue());
    }

    @Test
    public void testCreateNumber_String_Float() {
        String str = "123.45";
        Object obj = TypeHandler.createNumber(str);
        Number number = (Number) obj;
        assertEquals(123.45f, number.floatValue(), 0.001f);
    }

    @Test
    public void testCreateNumber_String_Double() {
        String str = "123.45";
        Object obj = TypeHandler.createNumber(str);
        Number number = (Number) obj;
        assertEquals(123.45, number.doubleValue(), 0.001);
    }

    @Test
    public void testCreateDate_String_ValidDate() {
        String str = "2023-03-28";
        Object obj = TypeHandler.createDate(str);
        Date date = (Date) obj;
        assertEquals("Tue Mar 28 00:00:00 CET 2023", date.toString());
    }

    @Test
    public void testCreateDate_String_InvalidDate() {
        String str = "invalidDate";
        Object obj = TypeHandler.createDate(str);
        assertNull(obj);
    }

    @Test
    public void testCreateURL_String_ValidURL() {
        String str = "http://example.com";
        Object obj = TypeHandler.createURL(str);
        URL url = (URL) obj;
        assertEquals(str, url.toString());
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURL_String_InvalidURL() {
        String str = "invalidURL";
        Object obj = TypeHandler.createURL(str);
        fail("Unexpected value: " + obj);
    }

    @Test
    public void testCreateFile_String_ExistingFile() {
        String str = "tempFile.txt";
        Object obj = TypeHandler.createFile(str);
        File file = (File) obj;
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testCreateFile_String_NonExistingFile() {
        String str = "tempFile.txt";
        Object obj = TypeHandler.createFile(str);
        File file = (File) obj;
        assertFalse(file.exists());
    }

    @Test
    public void testCreateFiles_String_SingleFile() {
        String str = "tempFile.txt";
        Object obj = TypeHandler.createFiles(str);
        File[] files = (File[]) obj;
        assertEquals(1, files.length);
        File file = files[0];
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testCreateFiles_String_MultipleFiles() {
        String str = "tempFile1.txt,tempFile2.txt";
        Object obj = TypeHandler.createFiles(str);
        File[] files = (File[]) obj;
        assertEquals(2, files.length);
        for (File file : files) {
            assertTrue(file.exists() || file.createNewFile());
            assertTrue(file.delete());
        }
    }

    @Test
    public void testCreateFiles_String_InvalidPath() {
        String str = "tempFile,tempFile.txt";
        Object obj = TypeHandler.createFiles(str);
        File[] files = (File[]) obj;
        assertEquals(0, files.length);
    }
}