package net.sf.sugar.fspath;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("/test/example.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file, result.getFile());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "Test string";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 4.5;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue, result.getDouble());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue, result.getBoolean());
    }

    @Test
    public void testToStringWithFile() {
        File file = new File("/test/example.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals("/test/example.txt", result.toString());
    }

    @Test
    public void testToStringWithString() {
        String string = "Test string";
        FSPathResult result = new FSPathResult(string);
        assertEquals("Test string", result.toString());
    }

    @Test
    public void testToStringWithDouble() {
        Double doubleValue = 4.5;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals("4.5", result.toString());
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
    public void testToStringWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue.toString(), result.toString());
    }

    @Test
    public void testToStringWithEmptyResult() {
        FSPathResult result = new FSPathResult(null);
        assertEquals("FSPathResult : empty", result.toString());
    }

    @Test
    public void testEqualsWithSameInstance() {
        FSPathResult result1 = new FSPathResult(new File("/test/example.txt"));
        FSPathResult result2 = result1;
        assertTrue(result1.equals(result2));
    }

    @Test
    public void testEqualsWithDifferentInstancesButSameValues() {
        FSPathResult result1 = new FSPathResult(new File("/test/example.txt"));
        FSPathResult result2 = new FSPathResult(new File("/test/example.txt"));
        assertTrue(result1.equals(result2));
    }

    @Test
    public void testEqualsWithDifferentInstancesButDifferentValues() {
        FSPathResult result1 = new FSPathResult(new File("/test/example.txt"));
        FSPathResult result2 = new FSPathResult(new File("/test/different.txt"));
        assertFalse(result1.equals(result2));
    }

    @Test
    public void testHashCodeWithSameInstances() {
        FSPathResult result1 = new FSPathResult(new File("/test/example.txt"));
        FSPathResult result2 = result1;
        int hashCode1 = result1.hashCode();
        int hashCode2 = result2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeWithDifferentInstancesButSameValues() {
        FSPathResult result1 = new FSPathResult(new File("/test/example.txt"));
        FSPathResult result2 = new FSPathResult(new File("/test/example.txt"));
        int hashCode1 = result1.hashCode();
        int hashCode2 = result2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeWithDifferentInstancesButDifferentValues() {
        FSPathResult result1 = new FSPathResult(new File("/test/example.txt"));
        FSPathResult result2 = new FSPathResult(new File("/test/different.txt"));
        int hashCode1 = result1.hashCode();
        int hashCode2 = result2.hashCode();
        assertNotSame(hashCode1, hashCode2);
    }
}