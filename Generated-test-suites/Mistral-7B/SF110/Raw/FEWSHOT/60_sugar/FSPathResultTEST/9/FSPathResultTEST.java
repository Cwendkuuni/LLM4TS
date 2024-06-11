package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.net.URI;
import java.util.Date;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFileInstance() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testFSPathResultStringInstance() {
        String string = "test";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
    }

    @Test
    public void testFSPathResultDoubleInstance() {
        Double doubleValue = 1.0;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue, result.getDouble());
    }

    @Test
    public void testFSPathResultDateInstance() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
    }

    @Test
    public void testFSPathResultURIInstance() {
        URI uri = new URI("http://www.google.com/");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testFSPathResultBooleanInstance() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue, result.getBoolean());
    }

    @Test
    public void testToStringFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringString() {
        String string = "test";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.toString());
    }

    @Test
    public void testToStringDouble() {
        Double doubleValue = 1.0;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue.toString(), result.toString());
    }

    @Test
    public void testToStringBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue.toString(), result.toString());
    }

    @Test
    public void testToStringDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToStringURI() {
        URI uri = new URI("http://www.google.com/");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testToStringEmpty() {
        FSPathResult result = new FSPathResult(null);
        assertEquals("FSPathResult : empty", result.toString());
    }

    @Test
    public void testGetters() {
        File file = new File("test.txt");
        String string = "test";
        Double doubleValue = 1.0;
        Date date = new Date();
        URI uri = new URI("http://www.google.com/");
        Boolean booleanValue = true;

        FSPathResult resultFile = new FSPathResult(file);
        FSPathResult resultString = new FSPathResult(string);
        FSPathResult resultDouble = new FSPathResult(doubleValue);
        FSPathResult resultDate = new FSPathResult(date);
        FSPathResult resultURI = new FSPathResult(uri);
        FSPathResult resultBoolean = new FSPathResult(booleanValue);

        assertSame(file, resultFile.getFile());
        assertSame(string, resultString.getString());
        assertSame(doubleValue, resultDouble.getDouble());
        assertSame(date, resultDate.getDate());
        assertSame(uri, resultURI.getURI());
        assertSame(booleanValue, resultBoolean.getBoolean());
    }

    @Test
    public void testNullGetters() {
        FSPathResult result = new FSPathResult(null);
        assertNull(result.getFile());
        assertNull(result.getString());
        assertNull(result.getDouble());
        assertNull(result.getDate());
        assertNull(result.getURI());
        assertNull(result.getBoolean());
    }

    @Test
    public void testEquals() {
        File file1 = new File("test1.txt");
        File file2 = new File("test2.txt");
        String string1 = "test1";
        String string2 = "test2";
        Double doublevalue1 = 1.0;
        Double doublevalue2 = 2.0;
        Date date1 = new Date();
        Date date2 = new Date();
        URI uri1 = new URI("http://www.google.com/");
        URI uri2 = new URI("http://www.yahoo.com/");
        Boolean booleanValue1 = true;
        Boolean booleanValue2 = false;

        FSPathResult resultFile1 = new FSPathResult(file1);
        FSPathResult resultFile2 = new FSPathResult(file2);
        FSPathResult resultString1 = new FSPathResult(string1);
        FSPathResult resultString2 = new FSPathResult(string2);
        FSPathResult resultDouble1 = new FSPathResult(doublevalue1);
        FSPathResult resultDouble2 = new FSPathResult(doublevalue2);
        FSPathResult resultDate1 = new FSPathResult(date1);
        FSPathResult resultDate2 = new FSPathResult(date2);
        FSPathResult resultURI1 = new FSPathResult(uri1);
        FSPathResult resultURI2 = new FSPathResult(uri2);
        FSPathResult resultBoolean1 = new FSPathResult(booleanValue1);
        FSPathResult resultBoolean2 = new FSPathResult(booleanValue2);

        assertNotEquals(resultFile1, resultFile2);
        assertNotEquals(resultString1, resultString2);
        assertNotEquals(resultDouble1, resultDouble2);
        assertNotEquals(resultDate1, resultDate2);
        assertNotEquals(resultURI1, resultURI2);
        assertNotEquals(resultBoolean1, resultBoolean2);

        assertNotEquals(resultFile1, resultString1);
        assertNotEquals(resultFile1, resultDouble1);
        assertNotEquals(resultFile1, resultDate1);
        assertNotEquals(resultFile1, resultURI1);
        assertNotEquals(resultFile1, resultBoolean1);

        assertNotEquals(resultString1, resultDouble1);
        assertNotEquals(resultString1, resultDate1);
        assertNotEquals(resultString1, resultURI1);
        assertNotEquals(resultString1, resultBoolean1);

        assertNotEquals(resultDouble1, resultDate1);
        assertNotEquals(resultDouble1, resultURI1);
        assertNotEquals(resultDouble1, resultBoolean1);

        assertNotEquals(resultDate1, resultURI1);
        assertNotEquals(resultDate1, resultBoolean1);

        assertNotEquals(resultURI1, resultBoolean1);

        assertEquals(resultFile1, resultFile1);
        assertEquals(resultString1, resultString1);
        assertEquals(resultDouble1, resultDouble1);
        assertEquals(resultDate1, resultDate1);
        assertEquals(resultURI1, resultURI1);
        assertEquals(resultBoolean1, resultBoolean1);
    }

    @Test
    public void testHashCode() {
        File file = new File("test.txt");
        String string = "test";
        Double doubleValue = 1.0;
        Date date = new Date();
        URI uri = new URI("http://www.google.com/");
        Boolean booleanValue = true;

        FSPathResult resultFile = new FSPathResult(file);
        FSPathResult resultString = new FSPathResult(string);
        FSPathResult resultDouble = new FSPathResult(doubleValue);
        FSPathResult resultDate = new FSPathResult(date);
        FSPathResult resultURI = new FSPathResult(uri);
        FSPathResult resultBoolean = new FSPathResult(booleanValue);

        int fileHashcode = file.hashCode();
        int stringHashcode = string.hashCode();
        long doubleHashcode = doubleValue.hashCode();
        long dateHashcode = date.hashCode();
        long uriHashcode = uri.hashCode();
        int booleanHashcode = booleanValue.hashCode();

        int resultFileHashcode = resultFile.hashCode();
        int resultStringHashcode = resultString.hashCode();
        long resultDoubleHashcode = resultDouble.hashCode();
        long resultDateHashcode = resultDate.hashCode();
        long resultURIHashcode = resultURI.hashCode();
        int resultBooleanHashcode = resultBoolean.hashCode();

        assertEquals(fileHashcode, resultFile.hashCode());
        assertEquals(stringHashcode, resultString.hashCode());
        assertEquals(doubleHashcode, resultDouble.hashCode());
        assertEquals(dateHashcode, resultDate.hashCode());
        assertEquals(uriHashcode, resultURI.hashCode());
        assertEquals(booleanHashcode, resultBoolean.hashCode());
    }
}