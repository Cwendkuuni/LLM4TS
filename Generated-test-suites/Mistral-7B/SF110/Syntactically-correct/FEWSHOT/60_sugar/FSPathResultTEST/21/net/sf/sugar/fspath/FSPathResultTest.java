package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testFSPathResultString() {
        String string = "test";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.toString());
    }

    @Test
    public void testFSPathResultDouble() {
        Double doubleValue = 12.5;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue.toString(), result.toString());
    }

    @Test
    public void testFSPathResultDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testFSPathResultURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testFSPathResultBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue.toString(), result.toString());
    }

    @Test
    public void testFSPathResultEmpty() {
        FSPathResult result = new FSPathResult();
        assertEquals("FSPathResult : empty", result.toString());
    }

    @Test
    public void testFSPathResultNull() {
        FSPathResult result = new FSPathResult(null);
        assertNull(result.toString());
    }

    @Test
    public void testGetters() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);

        assertSame(file, result.getFile());
        assertEquals("", result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }
}