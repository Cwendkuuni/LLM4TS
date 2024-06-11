package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("test.txt");
        FSPathResult fsPathResult = new FSPathResult(file);

        File resultFile = fsPathResult.getFile();
        assertEquals("test.txt", resultFile.getAbsolutePath());
        assertEquals(file, resultFile);

        String resultString = fsPathResult.toString();
        assertEquals("test.txt", resultString);

        assertNull(fsPathResult.getBoolean());
        assertNull(fsPathResult.getDate());
        assertNull(fsPathResult.getDouble());
        assertNull(fsPathResult.getString());
        assertNull(fsPathResult.getURI());
    }

    @Test
    public void testFSPathResultWithString() {
        FSPathResult fsPathResult = new FSPathResult("Hello World!");

        String resultString = fsPathResult.getString();
        assertEquals("Hello World!", resultString);

        File resultFile = fsPathResult.getFile();
        assertNull(resultFile);

        Double resultDouble = fsPathResult.getDouble();
        assertNull(resultDouble);

        Date resultDate = fsPathResult.getDate();
        assertNull(resultDate);

        URI resultURI = fsPathResult.getURI();
        assertNull(resultURI);

        Boolean resultBoolean = fsPathResult.getBoolean();
        assertNull(resultBoolean);

        String resultToString = fsPathResult.toString();
        assertEquals("Hello World!", resultToString);
    }

    @Test
    public void testFSPathResultWithDouble() {
        FSPathResult fsPathResult = new FSPathResult(3.14);

        Double resultDouble = fsPathResult.getDouble();
        assertEquals(3.14, resultDouble, 0.0);

        File resultFile = fsPathResult.getFile();
        assertNull(resultFile);

        String resultString = fsPathResult.getString();
        assertNull(resultString);

        Date resultDate = fsPathResult.getDate();
        assertNull(resultDate);

        URI resultURI = fsPathResult.getURI();
        assertNull(resultURI);

        Boolean resultBoolean = fsPathResult.getBoolean();
        assertNull(resultBoolean);

        String resultToString = fsPathResult.toString();
        assertEquals("3.14", resultToString);
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);

        Date resultDate = fsPathResult.getDate();
        assertEquals(date, resultDate);

        File resultFile = fsPathResult.getFile();
        assertNull(resultFile);

        String resultString = fsPathResult.getString();
        assertNull(resultString);

        Double resultDouble = fsPathResult.getDouble();
        assertNull(resultDouble);

        URI resultURI = fsPathResult.getURI();
        assertNull(resultURI);

        Boolean resultBoolean = fsPathResult.getBoolean();
        assertNull(resultBoolean);

        String resultToString = fsPathResult.toString();
        assertEquals(date.toString(), resultToString);
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);

        URI resultURI = fsPathResult.getURI();
        assertEquals(uri, resultURI);

        File resultFile = fsPathResult.getFile();
        assertNull(resultFile);

        String resultString = fsPathResult.getString();
        assertNull(resultString);

        Double resultDouble = fsPathResult.getDouble();
        assertNull(resultDouble);

        Date resultDate = fsPathResult.getDate();
        assertNull(resultDate);

        Boolean resultBoolean = fsPathResult.getBoolean();
        assertNull(resultBoolean);

        String resultToString = fsPathResult.toString();
        assertEquals(uri.toString(), resultToString);
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = Boolean.TRUE;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);

        Boolean resultBoolean = fsPathResult.getBoolean();
        assertEquals(booleanValue, resultBoolean);

        File resultFile = fsPathResult.getFile();
        assertNull(resultFile);

        String resultString = fsPathResult.getString();
        assertNull(resultString);

        Double resultDouble = fsPathResult.getDouble();
        assertNull(resultDouble);

        Date resultDate = fsPathResult.getDate();
        assertNull(resultDate);

        URI resultURI = fsPathResult.getURI();
        assertNull(resultURI);

        String resultToString = fsPathResult.toString();
        assertEquals(booleanValue.toString(), resultToString);
    }

    @Test
    public void testToStringForNullFSPathResult() {
        FSPathResult fsPathResult = null;
        String resultToString = fsPathResult.toString();
        assertEquals("FSPathResult : empty", resultToString);
    }

    @Test
    public void testToStringForEmptyFSPathResult() {
        FSPathResult fsPathResult = new FSPathResult();
        String resultToString = fsPathResult.toString();
        assertEquals("FSPathResult : empty", resultToString);
    }
}