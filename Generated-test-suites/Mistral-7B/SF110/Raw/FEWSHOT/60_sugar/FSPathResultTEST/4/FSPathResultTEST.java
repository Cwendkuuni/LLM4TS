package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResult_File() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testFSPathResult_String() {
        String str = "test.txt";
        FSPathResult result = new FSPathResult(str);
        assertEquals(str, result.getString());
    }

    @Test
    public void testFSPathResult_Double() {
        double d = 12.34;
        FSPathResult result = new FSPathResult(d);
        assertEquals(d, result.getDouble().doubleValue(), 0.0);
    }

    @Test
    public void testFSPathResult_Date() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
    }

    @Test
    public void testFSPathResult_URI() {
        URI uri = new URI("http://example.com/test.html");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testFSPathResult_Boolean() {
        boolean b = true;
        FSPathResult result = new FSPathResult(b);
        assertEquals(b, result.getBoolean().booleanValue());
    }

    @Test
    public void testToString_File() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        String expected = file.getAbsolutePath();
        String actual = result.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_String() {
        String str = "test.txt";
        FSPathResult result = new FSPathResult(str);
        String expected = str;
        String actual = result.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_Double() {
        double d = 12.34;
        FSPathResult result = new FSPathResult(d);
        String expected = Double.toString(d);
        String actual = result.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_Date() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        String expected = date.toString();
        String actual = result.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_URI() {
        URI uri = new URI("http://example.com/test.html");
        FSPathResult result = new FSPathResult(uri);
        String expected = uri.toString();
        String actual = result.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_Boolean() {
        boolean b = true;
        FSPathResult result = new FSPathResult(b);
        String expected = Boolean.toString(b);
        String actual = result.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString_Empty() {
        FSPathResult result = new FSPathResult(null);
        String expected = "FSPathResult : empty";
        String actual = result.toString();
        assertEquals(expected, actual);
    }
}