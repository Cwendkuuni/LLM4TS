package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals("testFile.txt", result.toString());
        assertEquals(file, result.getFile());
    }

    @Test
    public void testFSPathResultString() {
        FSPathResult result = new FSPathResult("testString");
        assertEquals("testString", result.toString());
        assertEquals("testString", result.getString());
    }

    @Test
    public void testFSPathResultDouble() {
        FSPathResult result = new FSPathResult(3.14);
        assertEquals("3.14", result.toString());
        assertEquals(3.14, result.getDouble().doubleValue());
    }

    @Test
    public void testFSPathResultDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals("Mon Jan 23 17:18:25 EDT 2023", result.toString());
        assertEquals(date, result.getDate());
    }

    @Test
    public void testFSPathResultURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals("http://www.example.com", result.toString());
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testFSPathResultBoolean() {
        FSPathResult result = new FSPathResult(Boolean.TRUE);
        assertEquals("true", result.toString());
        assertEquals(Boolean.TRUE, result.getBoolean());
    }

    @Test
    public void testFSPathResultEmpty() {
        FSPathResult result = new FSPathResult();
        assertEquals("FSPathResult : empty", result.toString());
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }
}