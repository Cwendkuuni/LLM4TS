package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("/test/somefile.txt");
        FSPathResult result = new FSPathResult(file);

        assertEquals(file, result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());

        assertEquals("/test/somefile.txt", result.toString());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "test string";
        FSPathResult result = new FSPathResult(string);

        assertEquals(string, result.getString());
        assertNull(result.getFile());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());

        assertEquals("test string", result.toString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);

        assertEquals(doubleValue, result.getDouble());
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());

        assertEquals("123.45", result.toString());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);

        assertEquals(date, result.getDate());
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getURI());
        assertNull(result.getBoolean());

        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);

        assertEquals(uri, result.getURI());
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getBoolean());

        assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);

        assertEquals(booleanValue, result.getBoolean());
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());

        assertEquals(booleanValue.toString(), result.toString());
    }

    @Test
    public void testToString_File() {
        File file = new File("/test/somefile.txt");
        FSPathResult result = new FSPathResult(file);

        assertEquals("/test/somefile.txt", result.toString());
    }

    @Test
    public void testToString_String() {
        String string = "test string";
        FSPathResult result = new FSPathResult(string);

        assertEquals("test string", result.toString());
    }

    @Test
    public void testToString_Double() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);

        assertEquals("123.45", result.toString());
    }

    @Test
    public void testToString_Date() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);

        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToString_URI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);

        assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testToString_Boolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);

        assertEquals(booleanValue.toString(), result.toString());
    }

    @Test
    public void testEquals_SameType_SameValue() {
        File file1 = new File("/test/somefile.txt");
        File file2 = new File("/test/somefile.txt");

        FSPathResult result1 = new FSPathResult(file1);
        FSPathResult result2 = new FSPathResult(file2);

        assertTrue(result1.equals(result2));
    }

    @Test
    public void testEquals_DifferentType_SameValue() {
        File file = new File("/test/somefile.txt");
        String string = "/test/somefile.txt";

        FSPathResult result1 = new FSPathResult(file);
        FSPathResult result2 = new FSPathResult(string);

        assertTrue(result1.equals(result2));
    }

    @Test
    public void testEquals_SameType_DifferentValue() {
        File file1 = new File("/test/somefile1.txt");
        File file2 = new File("/test/somefile2.txt");

        FSPathResult result1 = new FSPathResult(file1);
        FSPathResult result2 = new FSPathResult(file2);

        assertFalse(result1.equals(result2));
    }

    @Test
    public void testEquals_DifferentType_DifferentValue() {
        File file1 = new File("/test/somefile1.txt");
        String string2 = "/test/somefile2.txt";

        FSPathResult result1 = new FSPathResult(file1);
        FSPathResult result2 = new FSPathResult(string2);

        assertFalse(result1.equals(result2));
    }

    @Test
    public void testEquals_SameType_SameInstance() {
        File file = new File("/test/somefile.txt");

        FSPathResult result1 = new FSPathResult(file);
        FSPathResult result2 = result1;

        assertTrue(result1.equals(result2));
    }

    @Test
    public void testEquals_DifferentType_SameInstance() {
        File file = new File("/test/somefile.txt");

        FSPathResult result1 = new FSPathResult(file);
        String stringResult2 = result1;

        assertFalse(result1.equals(stringResult2));
    }

    @Test
    public void testHashCode_SameType_SameValue() {
        File file1 = new File("/test/somefile.txt");
        File file2 = new File("/test/somefile.txt");

        FSPathResult result1 = new FSPathResult(file1);
        FSPathResult result2 = new FSPathResult(file2);

        assertEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    public void testHashCode_DifferentType_SameValue() {
        File file = new File("/test/somefile.txt");
        String string = "/test/somefile.txt";

        FSPathResult result1 = new FSPathResult(file);
        FSPathResult result2 = new FSPathResult(string);

        assertEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    public void testHashCode_SameType_DifferentValue() {
        File file1 = new File("/test/somefile1.txt");
        File file2 = new File("/test/somefile2.txt");

        FSPathResult result1 = new FSPathResult(file1);
        FSPathResult result2 = new FSPathResult(file2);

        assertNotEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    public void testHashCode_DifferentType_DifferentValue() {
        File file1 = new File("/test/somefile1.txt");
        String string2 = "/test/somefile2.txt";

        FSPathResult result1 = new FSPathResult(file1);
        FSPathResult result2 = new FSPathResult(string2);

        assertNotEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    public void testHashCode_SameInstance() {
        File file = new File("/test/somefile.txt");

        FSPathResult result1 = new FSPathResult(file);
        FSPathResult result2 = result1;

        assertEquals(result1.hashCode(), result2.hashCode());
    }

    @Test
    public void testHashCode_DifferentType_SameInstance() {
        File file = new File("/test/somefile.txt");

        FSPathResult result1 = new FSPathResult(file);
        String stringResult2 = result1;

        assertNotEquals(result1.hashCode(), result2.hashCode());
    }
}