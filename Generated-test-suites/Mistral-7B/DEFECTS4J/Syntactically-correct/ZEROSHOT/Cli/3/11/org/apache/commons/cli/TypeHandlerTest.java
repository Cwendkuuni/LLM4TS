package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.net.URL;

import org.apache.commons.cli.TypeHandler;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import org.junit.Test;

/**
 * Test class for TypeHandler.
 */
public class TypeHandlerTest {

    @Test
    public void testCreateValueStringObject() {
        Object obj = new Object() {
            @Override
            public String toString() {
                return "testObject";
            }
        };
        assertEquals("testObject", TypeHandler.createValue("testObject", obj.getClass()));
    }

    @Test
    public void testCreateValueStringClass() {
        assertEquals("test", TypeHandler.createValue("test", String.class));
        assertEquals(3, TypeHandler.createValue("3", int.class));
        assertEquals(3L, TypeHandler.createValue("3", long.class));
        assertEquals(3.0, TypeHandler.createValue("3.", double.class));
        assertEquals(new Date(), TypeHandler.createValue("2023-03-29 12:34:56", Date.class));
        assertEquals(new File("test.txt"), TypeHandler.createValue("test.txt", File.class));
        assertEquals(new File("test.txt"), TypeHandler.createValue(new File("test.txt").getPath(), File.class));
        assertEquals(new URL("http://example.com"), TypeHandler.createValue("http://example.com", URL.class));
        assertNull(TypeHandler.createValue("invalid", String.class));
        assertNull(TypeHandler.createValue("invalid", int.class));
        assertNull(TypeHandler.createValue("invalid", long.class));
        assertNull(TypeHandler.createValue("invalid", double.class));
        assertNull(TypeHandler.createValue("invalid", Date.class));
        assertNull(TypeHandler.createValue("invalid", File.class));
        assertNull(TypeHandler.createValue("invalid", URL.class));
    }

    @Test
    public void testCreateObject() {
        Class<?> clazz = ClassUtils.forName("java.lang.String");
        Object obj = TypeHandler.createObject("java.lang.String");
        assertSame(String.class, obj.getClass());
        assertEquals("", ((String) obj).trim());
    }

    @Test
    public void testCreateNumber() {
        assertEquals(1, TypeHandler.createNumber("1"));
        assertEquals(1L, TypeHandler.createNumber("1"));
        assertEquals(1.0, TypeHandler.createNumber("1"));
        assertEquals(1.0, TypeHandler.createNumber("1."));
        assertNull(TypeHandler.createNumber("invalid"));
    }

    @Test
    public void testCreateClass() {
        assertEquals(String.class, TypeHandler.createClass("java.lang.String"));
        assertNull(TypeHandler.createClass("invalid"));
    }

    @Test
    public void testCreateDate() {
        assertEquals(DateFormatUtils.parseDate("2023-03-29 12:34:56", "yyyy-MM-dd HH:mm:ss"), TypeHandler.createDate("2023-03-29 12:34:56"));
        assertNull(TypeHandler.createDate("invalid"));
    }

    @Test
    public void testCreateURL() {
        assertEquals(new URL("http://example.com"), TypeHandler.createURL("http://example.com"));
        assertNull(TypeHandler.createURL("invalid"));
    }

    @Test
    public void testCreateFile() {
        File file = new File("test.txt");
        assertEquals(file, TypeHandler.createFile("test.txt"));
        assertEquals(file, TypeHandler.createFile(file.getPath()));
        assertNull(TypeHandler.createFile("invalid"));
    }

    @Test(expected = AssertionError.class)
    public void testCreateFiles() {
        String[] paths = StringUtils.split("test1.txt test2.txt test3.txt", File.pathSeparator);
        File[] files = TypeHandler.createFiles(StringUtils.join(paths, File.pathSeparator));
        assertEquals(3, files.length);
        for (int i = 0; i < files.length; i++) {
            assertEquals(new File(paths[i]), files[i]);
        }
    }
}