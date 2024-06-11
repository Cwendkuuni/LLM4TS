package net.sf.sugar.fspath;

import static org.junit.Assert.*;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResult_File() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        String expected = "test.txt";
        assertEquals(expected, result.getFile().getAbsolutePath());
        assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResult_String() {
        String string = "test string";
        FSPathResult result = new FSPathResult(string);
        String expected = "test string";
        assertEquals(expected, result.getString());
        assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResult_Double() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        String expected = "3.14";
        assertEquals(expected, result.getDouble().toString());
        assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResult_Date() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        String expected = "Sat Aug 27 13:53:36 EDT 2023";
        assertEquals(expected, result.getDate().toString());
        assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResult_URI() {
        URI uri = new URI("http://www.example.com/test.html");
        FSPathResult result = new FSPathResult(uri);
        String expected = "http://www.example.com/test.html";
        assertEquals(expected, result.getURI().toString());
        assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResult_Boolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        String expected = "true";
        assertEquals(expected, result.getBoolean().toString());
        assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResult_Empty() {
        FSPathResult result = new FSPathResult();
        assertEquals("FSPathResult : empty", result.toString());
    }
}