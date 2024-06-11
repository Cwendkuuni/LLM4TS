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
        assertEquals(string, result.getString());
        assertEquals(string, result.toString());
    }

    @Test
    public void testFSPathResultDouble() {
        Double doubleValue = 3.14;
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
        URI uri = new URI("http://example.com");
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
    public void testGetters() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertSame(file, result.getFile());

        String string = "test";
        FSPathResult stringResult = new FSPathResult(string);
        assertSame(string, stringResult.getString());

        Double doubleValue = 3.14;
        FSPathResult doubleResult = new FSPathResult(doubleValue);
        assertSame(doubleValue, doubleResult.getDouble());

        Date date = new Date();
        FSPathResult dateResult = new FSPathResult(date);
        assertSame(date, dateResult.getDate());

        URI uri = new URI("http://example.com");
        FSPathResult uriResult = new FSPathResult(uri);
        assertSame(uri, uriResult.getURI());

        Boolean booleanValue = true;
        FSPathResult booleanResult = new FSPathResult(booleanValue);
        assertSame(booleanValue, booleanResult.getBoolean());

        assertNull(result.getFile(), result.getString());
        assertNull(result.getFile(), result.getDouble());
        assertNull(result.getFile(), result.getDate());
        assertNull(result.getFile(), result.getURI());
        assertNull(result.getBoolean(), result.getString());
    }
}