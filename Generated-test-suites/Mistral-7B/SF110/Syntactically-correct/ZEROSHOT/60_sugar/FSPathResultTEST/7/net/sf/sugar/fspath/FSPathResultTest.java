package net.sf.sugar.fspath;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * FSPathResultTest.java
 *
 * Created on 08 April 2023, 16:21
 *
 */

public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        File getFile = result.getFile();
        assertEquals(file.getAbsolutePath(), getFile.getAbsolutePath());
    }

    @Test
    public void testFSPathResultString() {
        String string = "Test String";
        FSPathResult result = new FSPathResult(string);
        String getString = result.getString();
        assertEquals(string, getString);
    }

    @Test
    public void testFSPathResultDouble() {
        double doubleValue = 12.34;
        FSPathResult result = new FSPathResult(doubleValue);
        Double getDouble = result.getDouble();
        assertEquals(doubleValue, getDouble.doubleValue(), 0.0);
    }

    @Test
    public void testFSPathResultDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Date getDate = result.getDate();
        assertEquals(date, getDate);
    }

    @Test
    public void testFSPathResultURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        URI getURI = result.getURI();
        assertEquals(uri, getURI);
    }

    @Test
    public void testFSPathResultBoolean() {
        boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        Boolean getBoolean = result.getBoolean();
        assertEquals(booleanValue, getBoolean.booleanValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testGettingWrongType() {
        FSPathResult result = new FSPathResult(new Date());
        result.getFile();
    }

    @Test
    public void testToStringFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        String toString = result.toString();
        assertTrue(toString.contains(file.getAbsolutePath()));
    }

    @Test
    public void testToStringString() {
        String string = "Test String";
        FSPathResult result = new FSPathResult(string);
        String toString = result.toString();
        assertEquals(string, toString);
    }

    @Test
    public void testToStringDouble() {
        double doubleValue = 12.34;
        FSPathResult result = new FSPathResult(doubleValue);
        String toString = result.toString();
        assertTrue(toString.contains(Double.toString(doubleValue)));
    }

    @Test
    public void testToStringDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        String toString = result.toString();
        assertTrue(toString.contains(date.toString()));
    }

    @Test
    public void testToStringURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        String toString = result.toString();
        assertTrue(toString.contains(uri.toString()));
    }

    @Test
    public void testToStringBoolean() {
        boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        String toString = result.toString();
        assertTrue(toString.contains(Boolean.toString(booleanValue)));
    }

    @Test
    public void testToStringEmpty() {
        FSPathResult result = new FSPathResult();
        String toString = result.toString();
        assertEquals("FSPathResult : empty", toString);
    }
}