package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
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
        assertNotNull(result.getFile());
        assertEquals("testFile.txt", result.toString());
    }

    @Test
    public void testFSPathResultString() {
        String str = "testString";
        FSPathResult result = new FSPathResult(str);
        assertNotNull(result.getString());
        assertEquals(str, result.toString());
    }

    @Test
    public void testFSPathResultDouble() {
        Double value = 123.45;
        FSPathResult result = new FSPathResult(value);
        assertNotNull(result.getDouble());
        assertEquals(value.toString(), result.toString());
    }

    @Test
    public void testFSPathResultDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertNotNull(result.getDate());
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testFSPathResultURI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        assertNotNull(result.getURI());
        assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testFSPathResultBoolean() {
        Boolean value = true;
        FSPathResult result = new FSPathResult(value);
        assertNotNull(result.getBoolean());
        assertEquals(value.toString(), result.toString());
    }

    @Test
    public void testFSPathResultEmpty() {
        FSPathResult result = new FSPathResult();
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
        assertEquals("FSPathResult : empty", result.toString());
    }
}