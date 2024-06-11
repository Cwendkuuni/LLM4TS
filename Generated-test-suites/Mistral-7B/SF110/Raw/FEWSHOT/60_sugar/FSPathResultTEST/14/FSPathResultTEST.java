package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
        File file = new File("test.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals("test.txt", fsPathResult.toString());
        assertSame(file, fsPathResult.getFile());
    }

    @Test
    public void testFSPathResultString() {
        String string = "Hello World!";
        FSPathResult fsPathResult = new FSPathResult(string);
        assertEquals("Hello World!", fsPathResult.toString());
        assertEquals(string, fsPathResult.getString());
    }

    @Test
    public void testFSPathResultDouble() {
        Double doubleValue = 3.141592653589793;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        assertEquals("3.141592653589793", fsPathResult.toString());
        assertEquals(doubleValue, fsPathResult.getDouble());
    }

    @Test
    public void testFSPathResultDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertEquals("Sat Jan 21 15:23:45 EST 2023", fsPathResult.toString());
        assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testFSPathResultURI() {
        URI uri = new URI("http://example.com/test");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertEquals("http://example.com/test", fsPathResult.toString());
        assertSame(uri, fsPathResult.getURI());
    }

    @Test
    public void testFSPathResultBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        assertEquals("true", fsPathResult.toString());
        assertEquals(booleanValue, fsPathResult.getBoolean());
    }

    @Test
    public void testFSPathResultEmpty() {
        FSPathResult fsPathResult = new FSPathResult();
        assertEquals("FSPathResult : empty", fsPathResult.toString());
        assertNull(fsPathResult.getFile());
        assertNull(fsPathResult.getString());
        assertNull(fsPathResult.getDouble());
        assertNull(fsPathResult.getDate());
        assertNull(fsPathResult.getURI());
        assertNull(fsPathResult.getBoolean());
    }
}