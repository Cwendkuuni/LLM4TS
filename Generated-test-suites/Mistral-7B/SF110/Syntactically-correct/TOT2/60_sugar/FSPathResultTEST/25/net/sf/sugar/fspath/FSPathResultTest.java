import org.junit.Test;
import static org.junit.Assert.*;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
        File file = new File("testfile.txt");
        FSPathResult result = new FSPathResult(file);
        assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testFSPathResultString() {
        String string = "Test String";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
    }

    @Test
    public void testFSPathResultDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue.toString(), result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullFile() {
        File file = null;
        FSPathResult result = new FSPathResult(file);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFSPathResultEmptyString() {
        FSPathResult result = new FSPathResult("");
        assertEquals("FSPathResult : empty", result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullString() {
        String string = null;
        FSPathResult result = new FSPathResult(string);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFSPathResultInvalidDouble() {
        FSPathResult result = new FSPathResult(Double.NaN);
        assertEquals("NaN", result.toString());
    }

    // Add test cases for other methods considering edge cases and exceptions

    @Test
    public void testFSPathResultDate() {
        Date date = new Date(0);
        FSPathResult result = new FSPathResult(date);
        assertEquals("Thu Jan 01 00:00:00 GMT 1970", result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullDate() {
        Date date = null;
        FSPathResult result = new FSPathResult(date);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFSPathResultURI() {
        URI uri = new URI("file://testfile.txt");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri.toString(), result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullURI() {
        URI uri = null;
        FSPathResult result = new FSPathResult(uri);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testFSPathResultBoolean() {
        Boolean booleanValue = Boolean.FALSE;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue.toString(), result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullBoolean() {
        Boolean booleanValue = null;
        FSPathResult result = new FSPathResult(booleanValue);
        fail("Expected IllegalArgumentException");
    }

    // Add test cases for empty File object
}