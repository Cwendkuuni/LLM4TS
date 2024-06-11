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
 * Created on 08 April 2008, 16:21
 */

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        String expected = file.getAbsolutePath();

        assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "Hello World!";
        FSPathResult result = new FSPathResult(string);
        String expected = string;

        assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 42.0;
        FSPathResult result = new FSPathResult(doubleValue);
        String expected = doubleValue.toString();

        assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        String expected = date.toString();

        assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        String expected = uri.toString();

        assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        String expected = booleanValue.toString();

        assertEquals(expected, result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullFile() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullString() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullDouble() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullDate() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullURI() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullBoolean() {
        FSPathResult result = new FSPathResult(null);
    }
}