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
    public void testFSPathResultInstanceFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals(file, fsPathResult.getFile());
    }

    @Test
    public void testFSPathResultInstanceString() {
        String string = "testString";
        FSPathResult fsPathResult = new FSPathResult(string);
        assertEquals(string, fsPathResult.getString());
    }

    @Test
    public void testFSPathResultInstanceDouble() {
        Double doubleValue = 123.45;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        assertEquals(doubleValue, fsPathResult.getDouble());
    }

    @Test
    public void testFSPathResultInstanceDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testFSPathResultInstanceURI() {
        URI uri = new URI("http://www.test.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testFSPathResultInstanceBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        assertEquals(booleanValue, fsPathResult.getBoolean());
    }

    @Test
    public void testFSPathResultToStringFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultToStringString() {
        String string = "testString";
        FSPathResult fsPathResult = new FSPathResult(string);
        assertEquals(string, fsPathResult.toString());
    }

    @Test
    public void testFSPathResultToStringDouble() {
        Double doubleValue = 123.45;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        assertEquals(doubleValue.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultToStringDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        assertEquals(date.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultToStringURI() {
        URI uri = new URI("http://www.test.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        assertEquals(uri.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultToStringBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        assertEquals(booleanValue.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultToStringEmpty() {
        FSPathResult fsPathResult = new FSPathResult();
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultGetNullResult() {
        FSPathResult fsPathResult = new FSPathResult();
        assertNull(fsPathResult.getFile());
        assertNull(fsPathResult.getString());
        assertNull(fsPathResult.getDouble());
        assertNull(fsPathResult.getDate());
        assertNull(fsPathResult.getURI());
        assertNull(fsPathResult.getBoolean());
    }

    @Test
    public void testFSPathResultGetSameInstance() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult1 = new FSPathResult(file);
        FSPathResult fsPathResult2 = new FSPathResult(file);
        assertSame(fsPathResult1.getFile(), fsPathResult2.getFile());
    }
}