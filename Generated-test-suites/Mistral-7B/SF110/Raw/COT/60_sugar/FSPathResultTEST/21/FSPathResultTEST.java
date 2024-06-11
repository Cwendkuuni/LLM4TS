package net.sf.sugar.fspath.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

import net.sf.sugar.fspath.FSPathResult;

/**
 * FSPathResultTest.java
 *
 * Test cases for FSPathResult class.
 *
 * @author your_name
 * @version $Id$
 */

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("path/to/file");
        FSPathResult fsPathResult = new FSPathResult(file);
        String expected = "path/to/file";
        String actual = fsPathResult.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "test string";
        FSPathResult fsPathResult = new FSPathResult(string);
        String expected = string;
        String actual = fsPathResult.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        String expected = doubleValue.toString();
        String actual = fsPathResult.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        String expected = date.toString();
        String actual = fsPathResult.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        String expected = uri.toString();
        String actual = fsPathResult.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        String expected = booleanValue.toString();
        String actual = fsPathResult.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullFile() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullString() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test(expected = NumberFormatException.class)
    public void testFSPathResultWithInvalidDouble() {
        FSPathResult fsPathResult = new FSPathResult("invalid_double");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullDate() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithInvalidURI() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test(expected = IllegaArgumentException.class)
    public void testFSPathResultWithNullBoolean() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }
}