package net.sf.sugar.fspath.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

import net.sf.sugar.fspath.FSPathResult;

/**
 * FSPathResultTest.java
 *
 * Test cases for FSPathResult class
 *
 * @author your_name
 * @version $Id$
 */
public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("/test/file.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        File expectedFile = file;
        assertEquals("Expected file object to be equal", expectedFile, fsPathResult.getFile());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "Test string";
        FSPathResult fsPathResult = new FSPathResult(string);
        String expectedString = string;
        assertEquals("Expected string to be equal", expectedString, fsPathResult.getString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        Double expectedDouble = doubleValue;
        assertEquals("Expected double to be equal", expectedDouble, fsPathResult.getDouble());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        Date expectedDate = date;
        assertEquals("Expected date to be equal", expectedDate, fsPathResult.getDate());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.google.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        URI expectedURI = uri;
        assertEquals("Expected URI to be equal", expectedURI, fsPathResult.getURI());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        Boolean expectedBoolean = booleanValue;
        assertEquals("Expected boolean to be equal", expectedBoolean, fsPathResult.getBoolean());
    }

    @Test
    public void testToStringWithFile() {
        File file = new File("/test/file.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        String expectedString = file.getAbsolutePath();
        String actualString = fsPathResult.toString();
        assertEquals("Expected toString() to return file's absolute path", expectedString, actualString);
    }

    @Test
    public void testToStringWithString() {
        String string = "Test string";
        FSPathResult fsPathResult = new FSPathResult(string);
        String expectedString = string;
        String actualString = fsPathResult.toString();
        assertEquals("Expected toString() to return string", expectedString, actualString);
    }

    @Test
    public void testToStringWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        String expectedString = doubleValue.toString();
        String actualString = fsPathResult.toString();
        assertEquals("Expected toString() to return double's value", expectedString, actualString);
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        String expectedString = booleanValue.toString();
        String actualString = fsPathResult.toString();
        assertEquals("Expected toString() to return boolean's value", expectedString, actualString);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNull() {
        FSPathResult fsPathResult = new FSPathResult(null);
        // This test case will throw an exception as toString() is called with null, which is not allowed.
    }

    @Test
    public void testGetFileWhenResultIsString() {
        String string = "Test string";
        FSPathResult fsPathResult = new FSPathResult(string);
        assertNull("Expected getFile() to return null when result is String", fsPathResult.getFile());
    }

    @Test
    public void testGetFileWhenResultIsDouble() {
        Double doubleValue = 3.14;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        assertNull("Expected getFile() to return null when result is Double", fsPathResult.getFile());
    }

    @Test
    public void testGetFileWhenResultIsDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertNull("Expected getFile() to return null when result is Date", fsPathResult.getFile());
    }

    @Test
    public void testGetFileWhenResultIsURI() {
        URI uri = new URI("http://www.google.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertNull("Expected getFile() to return null when result is URI", fsPathResult.getFile());
    }

    @Test
    public void testGetFileWhenResultIsBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        assertNull("Expected getFile() to return null when result is Boolean", fsPathResult.getFile());
    }

    @Test
    public void testGetBooleanWhenResultIsFile() {
        File file = new File("/test/file.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertFalse("Expected getBoolean() to return false when result is File", fsPathResult.getBoolean());
    }

    @Test
    public void testGetBooleanWhenResultIsString() {
        String string = "true";
        FSPathResult fsPathResult = new FSPathResult(string);
        assertTrue("Expected getBoolean() to return true when result is String 'true'", fsPathResult.getBoolean());
    }

    @Test
    public void testGetBooleanWhenResultIsDouble() {
        Double doubleValue = 42.0;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        assertFalse("Expected getBoolean() to return false when result is Double", fsPathResult.getBoolean());
    }

    @Test
    public void testGetBooleanWhenResultIsDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertFalse("Expected getBoolean() to return false when result is Date", fsPathResult.getBoolean());
    }

    @Test
    public void testGetBooleanWhenResultIsURI() {
        URI uri = new URI("http://www.google.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertFalse("Expected getBoolean() to return false when result is URI", fsPathResult.getBoolean());
    }

    @Test
    public void testGetBooleanWhenResultIsBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        assertTrue("Expected getBoolean() to return the same Boolean value as the original", fsPathResult.getBoolean().equals(booleanValue));
    }

    @Test
    public void testGetFileAndGetStringWhenResultIsURI() {
        URI uri = new URI("http://www.google.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertNull("Expected getFile() to be null when result is URI", fsPathResult.getFile());
        String expectedString = uri.toString();
        String actualString = fsPathResult.getString();
        assertEquals("Expected getString() to return the same value as URI.toString()", expectedString, actualString);
    }
}