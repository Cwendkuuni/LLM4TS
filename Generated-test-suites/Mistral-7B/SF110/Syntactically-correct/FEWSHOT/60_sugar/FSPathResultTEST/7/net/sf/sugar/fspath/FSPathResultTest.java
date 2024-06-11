package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("/tmp/test.txt");
        FSPathResult result = new FSPathResult(file);
        assertNotNull(result.getFile());
        assertEquals("/tmp/test.txt", result.toString());
    }

    @Test
    public void testFSPathResultWithString() {
        FSPathResult result = new FSPathResult("Hello World");
        assertNotNull(result.getString());
        assertEquals("Hello World", result.toString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        FSPathResult result = new FSPathResult(3.14159265358979323846);
        assertNotNull(result.getDouble());
        assertEquals("3.14159265358979323846", result.toString());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertNotNull(result.getDate());
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.example.com/");
        FSPathResult result = new FSPathResult(uri);
        assertNotNull(result.getURI());
        assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        FSPathResult result = new FSPathResult(true);
        assertNotNull(result.getBoolean());
        assertEquals("true", result.toString());
    }

    @Test
    public void testFSPathResultEmpty() {
        FSPathResult result = new FSPathResult(null);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
        assertEquals("FSPathResult : empty", result.toString());
    }
}