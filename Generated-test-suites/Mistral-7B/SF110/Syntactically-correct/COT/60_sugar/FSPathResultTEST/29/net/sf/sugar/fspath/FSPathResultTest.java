package net.sf.sugar.fspath.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import net.sf.sugar.fspath.FSPathResult;

/**
 * FSPathResultTest.java
 */

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);

        File expectedFile = new File("testFile.txt");
        File actualFile = result.getFile();

        assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "Test String";
        FSPathResult result = new FSPathResult(string);

        String expectedString = "Test String";
        String actualString = result.getString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);

        Double expectedDouble = 123.45;
        Double actualDouble = result.getDouble();

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    public void testFSPathResultWithDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2022-12-31 23:59:59");
        FSPathResult result = new FSPathResult(date);

        Date expectedDate = date;
        Date actualDate = result.getDate();

        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);

        URI expectedURI = uri;
        URI actualURI = result.getURI();

        assertEquals(expectedURI, actualURI);
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);

        Boolean expectedBoolean = booleanValue;
        Boolean actualBoolean = result.getBoolean();

        assertEquals(expectedBoolean, actualBoolean);
    }

    @Test
    public void testToStringWithFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);

        String expectedString = file.getAbsolutePath();
        String actualString = result.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithString() {
        String string = "Test String";
        FSPathResult result = new FSPathResult(string);

        String expectedString = string;
        String actualString = result.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);

        String expectedString = doubleValue.toString();
        String actualString = result.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2022-12-31 23:59:59");
        FSPathResult result = new FSPathResult(date);

        String expectedString = dateFormat.format(date);
        String actualString = result.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);

        String expectedString = uri.toString();
        String actualString = result.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);

        String expectedString = booleanValue.toString();
        String actualString = result.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithEmptyFSPathResult() {
        FSPathResult result = new FSPathResult(null);

        String expectedString = "FSPathResult : empty";
        String actualString = result.toString();

        assertEquals(expectedString, actualString);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullFSPathResult() {
        FSPathResult result = null;

        String actualString = result.toString();
        fail();
    }

}