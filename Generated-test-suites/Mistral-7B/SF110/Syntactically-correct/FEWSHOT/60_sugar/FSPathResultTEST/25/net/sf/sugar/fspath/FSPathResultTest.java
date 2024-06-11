package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("/test/file.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "test string";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
        assertEquals(string, result.toString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue, result.getDouble());
        assertEquals(doubleValue.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://test.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
        assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue, result.getBoolean());
        assertEquals(booleanValue.toString(), result.toString());
    }

    @Test
    public void testEmptyFSPathResult() {
        FSPathResult result = new FSPathResult();
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
        assertEquals("FSPathResult : empty", result.toString());
    }

    @Test
    public void testToStringWithFile() {
        File file = new File("/test/file.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringWithString() {
        String string = "test string";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.toString());
    }

    @Test
    public void testToStringWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue.toString(), result.toString());
    }

    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToStringWithURI() {
        URI uri = new URI("http://test.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue.toString(), result.toString());
    }

    @Test
    public void testToStringWithEmptyFSPathResult() {
        FSPathResult result = new FSPathResult();
        assertEquals("FSPathResult : empty", result.toString());
    }
}