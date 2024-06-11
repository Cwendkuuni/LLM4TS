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

        assertSame(file, result.getFile());
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testFSPathResultString() {
        String str = "testString";
        FSPathResult result = new FSPathResult(str);

        assertNull(result.getFile());
        assertEquals(str, result.getString());
        assertEquals(str, result.toString());
    }

    @Test
    public void testFSPathResultDouble() {
        Double doubleValue = 1.0;
        FSPathResult result = new FSPathResult(doubleValue);

        assertNull(result.getFile());
        assertNull(result.getString());
        assertEquals(doubleValue.toString(), result.toString());
    }

    @Test
    public void testFSPathResultDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);

        assertNull(result.getFile());
        assertNull(result.getString());
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testFSPathResultURI() {
        URI uri = new URI("http://test.com");
        FSPathResult result = new FSPathResult(uri);

        assertNull(result.getFile());
        assertNull(result.getString());
        assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testFSPathResultBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);

        assertNull(result.getFile());
        assertNull(result.getString());
        assertEquals(booleanValue.toString(), result.toString());
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