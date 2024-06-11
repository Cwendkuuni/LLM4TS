package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals("testFile.txt", fsPathResult.toString());
        assertEquals(file, fsPathResult.getFile());
    }

    @Test
    public void testFSPathResultWithString() {
        FSPathResult fsPathResult = new FSPathResult("testString");
        assertEquals("testString", fsPathResult.toString());
        assertEquals("testString", fsPathResult.getString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        FSPathResult fsPathResult = new FSPathResult(123.45);
        assertEquals("123.45", fsPathResult.toString());
        assertEquals(123.45, fsPathResult.getDouble().doubleValue());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertEquals(date.toString(), fsPathResult.toString());
        assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.example.com/test.html");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertEquals(uri.toString(), fsPathResult.toString());
        assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        FSPathResult fsPathResult = new FSPathResult(true);
        assertEquals("true", fsPathResult.toString());
        assertEquals(true, fsPathResult.getBoolean());
    }

    @Test
    public void testFSPathResultWithNull() {
        FSPathResult fsPathResult = new FSPathResult(null);
        assertNull(fsPathResult.getFile());
        assertNull(fsPathResult.getString());
        assertNull(fsPathResult.getDouble());
        assertNull(fsPathResult.getDate());
        assertNull(fsPathResult.getURI());
        assertNull(fsPathResult.getBoolean());
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }

    @Test
    public void testToStringWithNullResultFile() {
        FSPathResult fsPathResult = new FSPathResult(null);
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }

    @Test
    public void testToStringWithNullResultString() {
        FSPathResult fsPathResult = new FSPathResult(null);
        FSPathResult fsPathResultWithString = new FSPathResult("testString");
        assertNotEquals("FSPathResult : empty", fsPathResultWithString.toString());
    }

    @Test
    public void testToStringWithNullResultDouble() {
        FSPathResult fsPathResult = new FSPathResult(null);
        FSPathResult fsPathResultWithDouble = new FSPathResult(123.45);
        assertNotEquals("FSPathResult : empty", fsPathResultWithDouble.toString());
    }

    @Test
    public void testToStringWithNullResultDate() {
        FSPathResult fsPathResult = new FSPathResult(null);
        FSPathResult fsPathResultWithDate = new FSPathResult(new Date());
        assertNotEquals("FSPathResult : empty", fsPathResultWithDate.toString());
    }

    @Test
    public void testToStringWithNullResultURI() {
        FSPathResult fsPathResult = new FSPathResult(null);
        FSPathResult fsPathResultWithURI = new FSPathResult(new URI("http://www.example.com/test.html"));
        assertNotEquals("FSPathResult : empty", fsPathResultWithURI.toString());
    }

    @Test
    public void testToStringWithNullResultBoolean() {
        FSPathResult fsPathResult = new FSPathResult(null);
        FSPathResult fsPathResultWithBoolean = new FSPathResult(true);
        assertNotEquals("FSPathResult : empty", fsPathResultWithBoolean.toString());
    }
}