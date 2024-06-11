package net.sf.sugar.fspath;

import static org.junit.Assert.*;
import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        File expectedFile = file;
        File actualFile = fsPathResult.getFile();
        assertEquals(expectedFile, actualFile);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullFile() {
        File file = null;
        FSPathResult fsPathResult = new FSPathResult(file);
    }

    @Test
    public void testFSPathResultWithEmptyString() {
        FSPathResult fsPathResult = new FSPathResult("");
        String expectedString = "";
        String actualString = fsPathResult.getString();
        assertEquals(expectedString, actualString);
    }

    @Test(expected = NumberFormatException.class)
    public void testFSPathResultWithNonNumericString() {
        FSPathResult fsPathResult = new FSPathResult("abc");
    }

    @Test
    public void testFSPathResultWithNullDate() {
        Date date = null;
        FSPathResult fsPathResult = new FSPathResult(date);
        Date expectedDate = null;
        Date actualDate = fsPathResult.getDate();
        assertEquals(expectedDate, actualDate);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullURI() {
        URI uri = null;
        FSPathResult fsPathResult = new FSPathResult(uri);
    }

    @Test
    public void testFSPathResultWithNullBoolean() {
        Boolean booleanValue = null;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        Boolean expectedBoolean = null;
        Boolean actualBoolean = fsPathResult.getBoolean();
        assertEquals(expectedBoolean, actualBoolean);
    }

    @Test
    public void testGetString() {
        FSPathResult fsPathResult = new FSPathResult("Test String");
        String expectedString = "Test String";
        String actualString = fsPathResult.getString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testGetDouble() {
        FSPathResult fsPathResult = new FSPathResult(3.14159265);
        Double expectedDouble = 3.14159265;
        Double actualDouble = fsPathResult.getDouble();
        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        Date expectedDate = date;
        Date actualDate = fsPathResult.getDate();
        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testGetURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        URI expectedURI = uri;
        URI actualURI = fsPathResult.getURI();
        assertEquals(expectedURI, actualURI);
    }

    @Test
    public void testGetBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        Boolean expectedBoolean = booleanValue;
        Boolean actualBoolean = fsPathResult.getBoolean();
        assertEquals(expectedBoolean, actualBoolean);
    }

    @Test
    public void testToStringWithFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        String expectedString = file.getAbsolutePath();
        String actualString = fsPathResult.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithString() {
        FSPathResult fsPathResult = new FSPathResult("Test String");
        String expectedString = "Test String";
        String actualString = fsPathResult.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithDouble() {
        FSPathResult fsPathResult = new FSPathResult(3.14159265);
        String expectedString = "3.14159265";
        String actualString = fsPathResult.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithBoolean() {
        FSPathResult fsPathResult = new FSPathResult(true);
        String expectedString = "true";
        String actualString = fsPathResult.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        String expectedString = date.toString();
        String actualString = fsPathResult.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testtoStringWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        String expectedString = uri.toString();
        String actualString = fsPathResult.toString();
        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithEmptyObject() {
        FSPathResult fsPathResult = new FSPathResult();
        String expectedString = "FSPathResult : empty";
        String actualString = fsPathResult.toString();
        assertEquals(expectedString, actualString);
    }
}