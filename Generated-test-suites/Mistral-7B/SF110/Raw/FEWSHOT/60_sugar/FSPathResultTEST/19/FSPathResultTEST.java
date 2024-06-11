package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFileConstructor() {
        File file = new File("/test/path/file.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testFSPathResultStringConstructor() {
        String string = "test string";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
    }

    @Test
    public void testFSPathResultDoubleConstructor() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue, result.getDouble());
    }

    @Test
    public void testFSPathResultDateConstructor() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
    }

    @Test
    public void testFSPathResultURIConstructor() {
        URI uri = new URI("http://www.example.com/test");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testFSPathResultBooleanConstructor() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue, result.getBoolean());
    }

    @Test
    public void testToStringFile() {
        File file = new File("/test/path/file.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals("/test/path/file.txt", result.toString());
    }

    @Test
    public void testToStringString() {
        String string = "test string";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.toString());
    }

    @Test
    public void testToStringDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue.toString(), result.toString());
    }

    @Test
    public void testToStringBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue.toString(), result.toString());
    }

    @Test
    public void testToStringDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToStringURI() {
        URI uri = new URI("http://www.example.com/test");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testToStringEmpty() {
        FSPathResult result = new FSPathResult();
        assertEquals("FSPathResult : empty", result.toString());
    }

    @Test
    public void testGetters() {
        File file = new File("/test/path/file.txt");
        FSPathResult result = new FSPathResult(file);
        assertSame(file, result.getFile());

        String string = "test string";
        result = new FSPathResult(string);
        assertSame(string, result.getString());

        Double doubleValue = 3.14;
        result = new FSPathResult(doubleValue);
        assertEquals(doubleValue, result.getDouble());

        Date date = new Date();
        result = new FSPathResult(date);
        assertSame(date, result.getDate());

        URI uri = new URI("http://www.example.com/test");
        result = new FSPathResult(uri);
        assertSame(uri, result.getURI());

        Boolean booleanValue = true;
        result = new FSPathResult(booleanValue);
        assertEquals(booleanValue, result.getBoolean());

        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }
}