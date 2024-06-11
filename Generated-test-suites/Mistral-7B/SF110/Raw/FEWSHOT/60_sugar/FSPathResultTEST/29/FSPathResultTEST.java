package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testNewInstanceWithFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testNewInstanceWithString() {
        String string = "Test String";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
    }

    @Test
    public void testNewInstanceWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue, result.getDouble());
    }

    @Test
    public void testNewInstanceWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
    }

    @Test
    public void testNewInstanceWithURI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testNewInstanceWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue, result.getBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNewInstanceWithNull() {
        new FSPathResult(null);
    }

    @Test
    public void testGetters() {
        File file = new File("testFile.txt");
        String string = "Test String";
        Double doubleValue = 3.14;
        Date date = new Date();
        URI uri = new URI("http://example.com");
        Boolean booleanValue = true;

        FSPathResult resultFile = new FSPathResult(file);
        FSPathResult resultString = new FSPathResult(string);
        FSPathResult resultDouble = new FSPathResult(doubleValue);
        FSPathResult resultDate = new FSPathResult(date);
        FSPathResult resultURI = new FSPathResult(uri);
        FSPathResult resultBoolean = new FSPathResult(booleanValue);

        assertEquals(file, resultFile.getFile());
        assertEquals(string, resultString.getString());
        assertEquals(doubleValue, resultDouble.getDouble());
        assertEquals(date, resultDate.getDate());
        assertEquals(uri, resultURI.getURI());
        assertEquals(booleanValue, resultBoolean.getBoolean());
    }

    @Test
    public void testToStringWithFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringWithString() {
        String string = "Test String";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.toString());
    }

    @Test
    public void testToStringWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue.toString(), result.toString());
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue.toString(), result.toString());
    }

    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToStringWithURI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testToStringWithEmptyResult() {
        FSPathResult result = new FSPathResult();
        assertEquals("FSPathResult : empty", result.toString());
    }

    @Test
    public void testEqualsWithSameInstance() {
        File file = new File("testFile.txt");
        FSPathResult resultFile1 = new FSPathResult(file);
        FSPathResult resultFile2 = new FSPathResult(file);
        assertTrue(resultFile1.equals(resultFile2));
    }

    @Test
    public void testEqualsWithDifferentTypes() {
        File file = new File("testFile.txt");
        String string = "Test String";
        FSPathResult resultFile = new FSPathResult(file);
        FSPathResult resultString = new FSPathResult(string);
        assertFalse(resultFile.equals(resultString));
    }

    @Test
    public void testEqualsWithSameValues() {
        File file1 = new File("testFile.txt");
        File file2 = new File("testFile.txt");
        FSPathResult resultFile1 = new FSPathResult(file1);
        FSPathResult resultFile2 = new FSPathResult(file2);
        assertTrue(resultFile1.equals(resultFile2));

        String string1 = "Test String";
        String string2 = "Test String";
        FSPathResult resultString1 = new FSPathResult(string1);
        FSPathResult resultString2 = new FSPathResult(string2);
        assertTrue(resultString1.equals(resultString2));

        Double doubleValue1 = 3.14;
        Double doubleValue2 = 3.14;
        FSPathResult resultDouble1 = new FSPathResult(doubleValue1);
        FSPathResult resultDouble2 = new FSPathResult(doubleValue2);
        assertTrue(resultDouble1.equals(resultDouble2));

        Date date1 = new Date();
        Date date2 = new Date();
        FSPathResult resultDate1 = new FSPathResult(date1);
        FSPathResult resultDate2 = new FSPathResult(date2);
        assertTrue(resultDate1.equals(resultDate2));

        URI uri1 = new URI("http://example.com");
        URI uri2 = new URI("http://example.com");
        FSPathResult resultURI1 = new FSPathResult(uri1);
        FSPathResult resultURI2 = new FSPathResult(uri2);
        assertTrue(resultURI1.equals(resultURI2));

        Boolean booleanValue1 = true;
        Boolean booleanValue2 = true;
        FSPathResult resultBoolean1 = new FSPathResult(booleanValue1);
        FSPathResult resultBoolean2 = new FSPathResult(booleanValue2);
        assertTrue(resultBoolean1.equals(resultBoolean2));
    }

    @Test
    public void testEqualsWithNull() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        assertFalse(result.equals(null));
    }

    @Test
    public void testHashCodeWithSameInstance() {
        File file = new File("testFile.txt");
        FSPathResult resultFile1 = new FSPathResult(file);
        FSPathResult resultFile2 = new FSPathResult(file);
        assertEquals(resultFile1.hashCode(), resultFile2.hashCode());
    }

    @Test
    public void testHashCodeWithDifferentInstances() {
        File file1 = new File("testFile1.txt");
        File file2 = new File("testFile2.txt");
        FSPathResult resultFile1 = new FSPathResult(file1);
        FSPathResult resultFile2 = new FSPathResult(file2);
        assertNotEquals(resultFile1.hashCode(), resultFile2.hashCode());
    }
}