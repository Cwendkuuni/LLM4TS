package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals("test.txt", result.toString());
    }

    @Test
    public void testFSPathResultString() {
        String string = "Test String";
        FSPathResult result = new FSPathResult(string);
        assertEquals("Test String", result.toString());
    }

    @Test
    public void testFSPathResultDouble() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals("123.45", result.toString());
    }

    @Test
    public void testFSPathResultDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testFSPathResultURI() {
        URI uri = new URI("https://www.example.com/test.html");
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
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
        assertEquals("FSPathResult : empty", result.toString());
    }

    @Test
    public void testFSPathResultGetStringFromFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals("test.txt", result.toString());
        assertEquals("test.txt", result.getFile().getAbsolutePath());
    }

    @Test
    public void testFSPathResultBooleanGetValue() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals("true", result.toString());
        assertEquals(booleanValue, result.getBoolean());
    }

    @Test
    public void testFSPathResultDateGetValue() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
        assertEquals(date, result.getDate());
    }

    @Test
    public void testFSPathResultURIGetValue() {
        URI uri = new URI("https://www.example.com/test.html");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testFSPathResultDoubleGetValue() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals("123.45", result.toString());
        assertEquals(doubleValue, result.getDouble());
    }

    @Test
    public void testFSPathResultStringGetValue() {
        String string = "Test String";
        FSPathResult result = new FSPathResult(string);
        assertEquals("Test String", result.toString());
        assertEquals(string, result.getString());
    }

    @Test
    public void testFSPathResultFileGetValue() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals("test.txt", result.toString());
        assertEquals(file, result.getFile());
    }
}