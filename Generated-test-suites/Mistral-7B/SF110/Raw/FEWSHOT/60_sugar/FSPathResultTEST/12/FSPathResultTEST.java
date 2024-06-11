package net.sf.sugar.fspath;

import static org.junit.Assert.*;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
        File file = new File("test.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals("test.txt", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultString() {
        String string = "Hello World";
        FSPathResult fsPathResult = new FSPathResult(string);
        assertEquals("Hello World", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultDouble() {
        Double doubleValue = 123.45;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        assertEquals("123.45", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertEquals(date.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultURI() {
        URI uri = new URI("http://www.example.com/test.html");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertEquals(uri.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        assertEquals(booleanValue.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultEmpty() {
        FSPathResult fsPathResult = new FSPathResult();
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNull() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }
}