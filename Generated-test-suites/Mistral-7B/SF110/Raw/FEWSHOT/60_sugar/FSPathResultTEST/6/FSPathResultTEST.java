package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFileInstance() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        File expectedResult = file;

        File actualResult = result.getFile();

        assertEquals(expectedResult, actualResult);
        assertEquals("testFile.txt", actualResult.getAbsolutePath());
        assertEquals("testFile.txt", result.toString());
    }

    @Test
    public void testFSPathResultStringInstance() {
        String string = "Test String";
        FSPathResult result = new FSPathResult(string);
        String expectedResult = string;

        String actualResult = result.getString();

        assertEquals(expectedResult, actualResult);
        assertEquals("Test String", actualResult);
        assertEquals("Test String", result.toString());
    }

    @Test
    public void testFSPathResultDoubleInstance() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        Double expectedResult = doubleValue;

        Double actualResult = result.getDouble();

        assertEquals(expectedResult, actualResult);
        assertEquals(3.14, actualResult.doubleValue());
        assertEquals("3.14", result.toString());
    }

    @Test
    public void testFSPathResultDateInstance() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Date expectedResult = date;

        Date actualResult = result.getDate();

        assertEquals(expectedResult, actualResult);
        assertEquals(expectedResult.toString(), actualResult.toString());
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testFSPathResultURIInstance() {
        URI uri = new URI("http://test.com");
        FSPathResult result = new FSPathResult(uri);
        URI expectedResult = uri;

        URI actualResult = result.getURI();

        assertSame(expectedResult, actualResult);
        assertEquals(expectedResult.toString(), actualResult.toString());
        assertEquals(expectedResult.toString(), result.toString());
    }

    @Test
    public void testFSPathResultBooleanInstance() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        Boolean expectedResult = booleanValue;

        Boolean actualResult = result.getBoolean();

        assertEquals(expectedResult, actualResult);
        assertEquals(expectedResult.toString(), actualResult.toString());
        assertEquals("true", result.toString());
    }

    @Test
    public void testFSPathResultEmptyInstance() {
        FSPathResult result = new FSPathResult();
        assertEquals("FSPathResult : empty", result.toString());
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }
}