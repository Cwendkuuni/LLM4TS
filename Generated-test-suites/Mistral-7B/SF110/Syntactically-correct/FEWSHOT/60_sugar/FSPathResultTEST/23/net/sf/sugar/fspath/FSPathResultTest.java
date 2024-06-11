package net.sf.sugar.fspath;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("path/to/file.txt");
        FSPathResult result = new FSPathResult(file);

        assertEquals("path/to/file.txt", result.toString());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "Hello World!";
        FSPathResult result = new FSPathResult(string);

        assertEquals("Hello World!", result.toString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);

        assertEquals("123.45", result.toString());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);

        assertEquals("Sat Jan 21 13:20:32 EDT 2023", result.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);

        assertEquals("http://example.com", result.toString());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);

        assertEquals("true", result.toString());
    }

    @Test
    public void testFSPathResultWithNullValues() {
        FSPathResult result = new FSPathResult(null);

        assertEquals("FSPathResult : empty", result.toString());
    }
}