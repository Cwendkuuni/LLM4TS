package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultInstanceWithFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals(file, fsPathResult.getFile());
    }

    @Test
    public void testFSPathResultInstanceWithString() {
        String str = "testString";
        FSPathResult fsPathResult = new FSPathResult(str);
        assertEquals(str, fsPathResult.getString());
    }

    @Test
    public void testFSPathResultInstanceWithDouble() {
        Double doubleValue = 1.2;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        assertEquals(doubleValue, fsPathResult.getDouble());
    }

    @Test
    public void testFSPathResultInstanceWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testFSPathResultInstanceWithURI() {
        URI uri = new URI("https://www.example.com/test");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testFSPathResultInstanceWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        assertEquals(booleanValue, fsPathResult.getBoolean());
    }

    @Test
    public void testToStringMethodWithFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        String result = fsPathResult.toString();
        assertTrue(result.endsWith(file.getAbsolutePath()));
    }

    @Test
    public void testToStringMethodWithString() {
        String str = "testString";
        FSPathResult fsPathResult = new FSPathResult(str);
        String result = fsPathResult.toString();
        assertEquals(str, result);
    }

    @Test
    public void testToStringMethodWithDouble() {
        Double doubleValue = 1.2;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        String result = fsPathResult.toString();
        assertEquals(doubleValue.toString(), result);
    }

    @Test
    public void testToStringMethodWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        String result = fsPathResult.toString();
        assertEquals(date.toString(), result);
    }

    @Test
    public void testToStringMethodWithURI() {
        URI uri = new URI("https://www.example.com/test");
        FSPathResult fsPathResult = new FSPathResult(uri);
        String result = fsPathResult.toString();
        assertEquals(uri.toString(), result);
    }

    @Test
    public void testToStringMethodWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        String result = fsPathResult.toString();
        assertEquals(booleanValue.toString(), result);
    }

    @Test
    public void testToStringMethodWithEmptyFSPathResult() {
        FSPathResult fsPathResult = new FSPathResult();
        String result = fsPathResult.toString();
        assertEquals("FSPathResult : empty", result);
    }

    @Test
    public void testGetters() {
        File file = new File("testFile.txt");
        String string = "testString";
        Double doubleValue = 1.2;
        Date date = new Date();
        URI uri = new URI("https://www.example.com/test");
        Boolean booleanValue = true;

        FSPathResult fsPathResultFile = new FSPathResult(file);
        FSPathResult fsPathResultString = new FSPathResult(string);
        FSPathResult fsPathResultDouble = new FSPathResult(doubleValue);
        FSPathResult fsPathResultDate = new FSPathResult(date);
        FSPathResult fsPathResultURI = new FSPathResult(uri);
        FSPathResult fsPathResultBoolean = new FSPathResult(booleanValue);

        assertSame(file, fsPathResultFile.getFile());
        assertSame(string, fsPathResultString.getString());
        assertEquals(doubleValue, fsPathResultDouble.getDouble());
        assertSame(date, fsPathResultDate.getDate());
        assertSame(uri, fsPathResultURI.getURI());
        assertEquals(booleanValue, fsPathResultBoolean.getBoolean());
    }

    @Test
    public void testNullGetters() {
        FSPathResult fsPathResult = new FSPathResult();
        assertNull(fsPathResult.getFile());
        assertNull(fsPathResult.getString());
        assertNull(fsPathResult.getDouble());
        assertNull(fsPathResult.getDate());
        assertNull(fsPathResult.getURI());
        assertNull(fsPathResult.getBoolean());
    }
}