package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.net.URL;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class TypeHandlerTest {

    @Test
    public void testCreateValueWithStringObject() {
        String str = "testString";
        Object obj = new Object() {
        };
        Object value = TypeHandler.createValue(str, obj.getClass());
        assertEquals(str, value);
    }

    @Test
    public void testCreateValueWithStringPatternOptionBuilderStringValue() {
        String str = "testString";
        Object value = TypeHandler.createValue(str, PatternOptionBuilder.STRING_VALUE);
        assertEquals(str, value);
    }

    @Test
    public void testCreateValueWithStringPatternOptionBuilderObjectValueNull() {
        String str = "testClass";
        assertNull(TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE));
    }

    @Test
    public void testCreateValueWithStringPatternOptionBuilderObjectValueClass() {
        String str = "java.lang.String";
        Class clazz = ClassUtils.forName(str, TypeHandler.class.getClassLoader());
        Object value = TypeHandler.createValue(str, PatternOptionBuilder.OBJECT_VALUE);
        assertSame(clazz, value.getClass());
    }

    @Test
    public void testCreateValueWithStringPatternOptionBuilderNumberValue() {
        String str = "123";
        Number number = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertSame(Integer.class, number.getClass());
        assertEquals(123, ((Number) number).intValue());

        str = "123.45";
        number = TypeHandler.createValue(str, PatternOptionBuilder.NUMBER_VALUE);
        assertSame(Double.class, number.getClass());
        assertEquals(123.45, ((Number) number).doubleValue());
    }

    @Test
    public void testCreateValueWithStringPatternOptionBuilderDateValue() {
        String str = "2023-03-31";
        Date date = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertNull(date);

        str = "2023-03-31 12:34:56";
        date = TypeHandler.createValue(str, PatternOptionBuilder.DATE_VALUE);
        assertEquals("Sat Mar 31 12:34:56 GMT 2023", date.toString());
    }

    @Test
    public void testCreateValueWithStringPatternOptionBuilderClassValue() {
        String str = "java.util.Date";
        Class clazz = ClassUtils.forName(str, TypeHandler.class.getClassLoader());
        Object value = TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
        assertSame(clazz, value.getClass());
    }

    @Test
    public void testCreateValueWithStringPatternOptionBuilderFileValue() {
        String str = "test.txt";
        File file = TypeHandler.createValue(str, PatternOptionBuilder.FILE_VALUE);
        assertSame(new File(str), file);
    }

    @Test
    public void testCreateValueWithStringPatternOptionBuilderExistingFileValue() {
        String str = "test.txt";
        File file = new File(str);
        if (file.exists()) {
            File value = TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE);
            assertSame(file, value);
        } else {
            assertNull(TypeHandler.createValue(str, PatternOptionBuilder.EXISTING_FILE_VALUE));
        }
    }

    @Test
    public void testCreateValueWithStringPatternOptionBuilderFilesValue() {
        String str = "test1.txt,test2.txt";
        File[] values = TypeHandler.createValue(str, PatternOptionBuilder.FILES_VALUE);
        File[] expectedValues = { new File("test1.txt"), new File("test2.txt") };
        assertArrayEquals(expectedValues, values);
    }

    @Test
    public void testCreateValueWithStringPatternOptionBuilderURLValue() {
        String str = "http://example.com";
        URL url = TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
        assertSame(new URL("http://example.com"), url);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValueWithNullStr() {
        Object obj = new Object() {
        };
        TypeHandler.createValue(null, obj.getClass());
    }

    @Test(expected = NullPointerException.class)
    public void testCreateValueWithNullObj() {
        String str = "test";
        TypeHandler.createValue(str, null);
    }

    @Test(expected = ClassNotFoundException.class)
    public void testCreateValueWithInvalidClassName() {
        String str = "Xyzxyz";
        TypeHandler.createValue(str, PatternOptionBuilder.CLASS_VALUE);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateValueWithInvalidURLString() {
        String str = "invalid://example.com";
        TypeHandler.createValue(str, PatternOptionBuilder.URL_VALUE);
    }
}