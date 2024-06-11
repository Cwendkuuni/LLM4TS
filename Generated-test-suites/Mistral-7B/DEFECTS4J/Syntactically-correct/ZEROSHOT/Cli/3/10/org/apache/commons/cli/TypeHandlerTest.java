package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.net.URL;
import java.util.Date;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.NumberUtils;
import org.junit.Test;

/**
 * TypeHandlerTest
 */
public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        String stringValue = "abc";
        Object obj = new Object() {
        };

        Object result = TypeHandler.createValue(stringValue, obj.getClass());

        assertSame(obj, result);
        assertEquals(stringValue, result.toString());
    }

    @Test
    public void testCreateValueStringClass() {
        String stringValue = "abc";
        Class<?> clazz = String.class;

        Object result = TypeHandler.createValue(stringValue, clazz);

        assertEquals(stringValue, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderStringValue() {
        String stringValue = "abc";

        Object result = TypeHandler.createValue(stringValue,
                PatternOptionBuilder.STRING_VALUE);

        assertEquals(stringValue, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderObjectValue() {
        String stringValue = "ClassName";

        Object result = TypeHandler.createValue(stringValue,
                PatternOptionBuilder.OBJECT_VALUE);

        Class<?> clazz = null;
        try {
            clazz = Class.forName(stringValue);
        } catch (ClassNotFoundException e) {
            // expected
        }

        assertNull(result);

        if (clazz != null) {
            try {
                result = TypeHandler.createValue(stringValue, clazz);
            } catch (Exception e) {
                // expected
            }

            assertNull(result);
        }
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderNumberValue() {
        String stringValue = "123";

        Object result = TypeHandler.createValue(stringValue,
                PatternOptionBuilder.NUMBER_VALUE);

        Number number = NumberUtils.parseNumber(stringValue);
        Long longNumber = number.longValue();

        assertEquals(longNumber, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderDateValue() {
        String stringValue = "2023-03-31";

        Object result = TypeHandler.createValue(stringValue,
                PatternOptionBuilder.DATE_VALUE);

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = dateFormat.parse(stringValue);
        } catch (ParseException e) {
            // expected
        }

        assertSame(date, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderClassValue() {
        String stringValue = "java.lang.String";

        Object result = TypeHandler.createValue(stringValue,
                PatternOptionBuilder.CLASS_VALUE);

        Class<?> clazz = null;
        try {
            clazz = Class.forName(stringValue);
        } catch (ClassNotFoundException e) {
            // expected
        }

        assertSame(clazz, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFileValue() {
        String stringValue = "/path/to/file.txt";

        Object result = TypeHandler.createValue(stringValue,
                PatternOptionBuilder.FILE_VALUE);

        File file = new File(stringValue);

        assertSame(file, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderExistingFileValue() {
        String stringValue = "/path/to/file.txt";

        Object result = TypeHandler.createValue(stringValue,
                PatternOptionBuilder.EXISTING_FILE_VALUE);

        File file = new File(stringValue);

        if (!file.exists()) {
            // expected
            assertNull(result);
        } else {
            assertSame(file, result);
        }
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderFilesValue() {
        String stringValue = "/path/to/file1.txt,/path/to/file2.txt";

        Object result = TypeHandler.createValue(stringValue,
                PatternOptionBuilder.FILES_VALUE);

        File[] files = new File[] {new File("/path/to/file1.txt"),
                new File("/path/to/file2.txt")};

        assertSame(files, result);
    }

    @Test
    public void testCreateValueStringPatternOptionBuilderURLValue() {
        String stringValue = "http://example.com";

        Object result = TypeHandler.createValue(stringValue,
                PatternOptionBuilder.URL_VALUE);

        URL url = new URL(stringValue);

        assertSame(url, result);
    }

    @Test
    public void testCreateObject() {
        String className = "java.lang.String";

        Object result = TypeHandler.createObject(className);

        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            // expected
        }

        assertSame(clazz, result.getClass());
    }

    @Test
    public void testCreateNumber() {
        String stringValue = "123";

        Object result = TypeHandler.createNumber(stringValue);

        Number number = NumberUtils.parseNumber(stringValue);
        Long longNumber = number.longValue();

        assertEquals(longNumber, result);
    }

    @Test
    public void testCreateClass() {
        String className = "java.lang.String";

        Object result = TypeHandler.createClass(className);

        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            // expected
        }

        assertSame(clazz, result);
    }

    @Test
    public void testCreateDate() {
        String dateString = "2023-03-31";

        Object result = TypeHandler.createDate(dateString);

        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // expected
        }

        assertSame(date, result);
    }

    @Test
    public void testCreateURL() {
        String urlString = "http://example.com";

        Object result = TypeHandler.createURL(urlString);

        URL url = new URL(urlString);

        assertSame(url, result);
    }

    @Test
    public void testCreateFile() {
        String filePath = "/path/to/file.txt";

        Object result = TypeHandler.createFile(filePath);

        File file = new File(filePath);

        assertSame(file, result);
    }

    @Test
    public void testCreateFiles() {
        String filePaths = "/path/to/file1.txt,/path/to/file2.txt";

        Object result = TypeHandler.createFiles(filePaths);

        File[] files = new File[] {new File("/path/to/file1.txt"),
                new File("/path/to/file2.txt")};

        assertSame(files, result);
    }
}