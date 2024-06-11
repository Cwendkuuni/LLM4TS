package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
        assertNotNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testFSPathResultString() {
        String str = "test";
        FSPathResult result = new FSPathResult(str);
        assertEquals(str, result.toString());
        assertNull(result.getFile());
        assertEquals(str, result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testFSPathResultDouble() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue.toString(), result.toString());
        assertNull(result.getFile());
        assertNull(result.getString());
        assertEquals(doubleValue, result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testFSPathResultDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertEquals(date, result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testFSPathResultURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertEquals(uri, result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testFSPathResultBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue.toString(), result.toString());
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertEquals(booleanValue, result.getBoolean());
    }
}