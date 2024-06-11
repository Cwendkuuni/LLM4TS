import org.junit.Test;
import org.junit.Assert.*;
import java.io.File;
import java.net.URI;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullString() {
        String string = null;
        FSPathResult result = new FSPathResult(string);
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);

        Double expectedDouble = 3.14;
        Double actualDouble = result.getDouble();

        assertEquals(expectedDouble, actualDouble);
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);

        Date expectedDate = new Date();
        Date actualDate = result.getDate();

        assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);

        URI expectedURI = new URI("http://example.com");
        URI actualURI = result.getURI();

        assertEquals(expectedURI, actualURI);
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);

        Boolean expectedBoolean = true;
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
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);

        String expectedString = doubleValue.toString();
        String actualString = result.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);

        String expectedString = date.toString();
        String actualString = result.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithURI() {
        URI uri = new URI("http://example.com");
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
    public void testToStringEmptyFSPathResult() {
        FSPathResult result = new FSPathResult();

        String expectedString = "FSPathResult : empty";
        String actualString = result.toString();

        assertEquals(expectedString, actualString);
    }
}