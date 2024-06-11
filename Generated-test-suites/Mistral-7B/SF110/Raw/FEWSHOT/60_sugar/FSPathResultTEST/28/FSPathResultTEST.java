package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
        File testFile = new File("testFile.txt");
        FSPathResult result = new FSPathResult(testFile);
        assertEquals(testFile.getAbsolutePath(), result.toString());
    }

    @Test
    public void testFSPathResultString() {
        FSPathResult result = new FSPathResult("Test String");
        assertEquals("Test String", result.toString());
    }

    @Test
    public void testFSPathResultDouble() {
        FSPathResult result = new FSPathResult(3.14);
        assertEquals("3.14", result.toString());
    }

    @Test
    public void testFSPathResultDate() {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        assertEquals(testDate.toString(), result.toString());
    }

    @Test
    public void testFSPathResultURI() {
        URI testURI = new URI("http://www.test.com");
        FSPathResult result = new FSPathResult(testURI);
        assertEquals(testURI.toString(), result.toString());
    }

    @Test
    public void testFSPathResultBoolean() {
        FSPathResult result = new FSPathResult(true);
        assertEquals("true", result.toString());
    }

    @Test
    public void testFSPathResultEmpty() {
        FSPathResult result = new FSPathResult();
        assertEquals("FSPathResult : empty", result.toString());
    }

    @Test
    public void testFSPathResultNull() {
        FSPathResult result = new FSPathResult(null);
        assertNull(result.toString());
    }
}