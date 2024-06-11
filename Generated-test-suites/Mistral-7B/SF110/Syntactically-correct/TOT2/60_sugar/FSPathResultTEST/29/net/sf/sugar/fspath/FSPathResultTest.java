import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;

public class FSPathResultTest {

    // Test constructors

    // Test constructor with File argument
    @Test
    public void testFSPathResultWithFile() {
        File file = new File("path/to/file");
        FSPathResult result = new FSPathResult(file);
        assertNotNull(result.getFile());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullFile() {
        File file = null;
        FSPathResult result = new FSPathResult(file);
        fail("Expected NullPointerException but got: " + result);
    }

    // Test constructor with String argument
    @Test
    public void testFSPathResultWithString() {
        String string = "Hello World";
        FSPathResult result = new FSPathResult(string);
        assertEquals(string, result.getString());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullString() {
        String string = null;
        FSPathResult result = new FSPathResult(string);
        fail("Expected NullPointerException but got: " + result);
    }

    // Test constructor with Double argument
    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        assertEquals(doubleValue, result.getDouble());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNaN() {
        FSPathResult result = new FSPathResult(Double.NaN);
        fail("Expected IllegalArgumentException but got: " + result);
    }

    // Test constructor with Date argument
    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        assertEquals(date, result.getDate());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullDate() {
        Date date = null;
        FSPathResult result = new FSPathResult(date);
        fail("Expected NullPointerException but got: " + result);
    }

    // Test constructor with URI argument
    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        assertEquals(uri, result.getURI());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullURI() {
        URI uri = null;
        FSPathResult result = new FSPathResult(uri);
        fail("Expected NullPointerException but got: " + result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithEmptyURI() {
        FSPathResult result = new FSPathResult(new URI(""));
        fail("Expected IllegalArgumentException but got: " + result);
    }

    // Test constructor with Boolean argument
    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        assertEquals(booleanValue, result.getBoolean());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullBoolean() {
        Boolean booleanValue = null;
        FSPathResult result = new FSPathResult(booleanValue);
        fail("Expected NullPointerException but got: " + result);
    }

    // Getters

    // Test getFile()
    @Test
    public void testGetFile() {
        File file = new File("path/to/file");
        FSPathResult result = new FSPathResult(file);
        File actualFile = result.getFile();
        assertEquals(file, actualFile);
    }

    @Test
    public void testGetFileNullResult() {
        FSPathResult result = new FSPathResult(null);
        assertNull(result.getFile());
    }

    // Test getString()
    @Test
    public void testGetString() {
        String string = "Hello World";
        FSPathResult result = new FSPathResult(string);
        String actualString = result.getString();
        assertEquals(string, actualString);
    }

    @Test
    public void testGetStringNullResult() {
        FSPathResult result = new FSPathResult(null);
        assertNull(result.getString());
    }

    @Test
    public void testGetStringEmptyString() {
        FSPathResult result = new FSPathResult("");
        assertEquals("", result.getString());
    }

    // Test getDouble()
    @Test
    public void testGetDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        Double actualDouble = result.getDouble();
        assertEquals(doubleValue, actualDouble);
    }

    @Test
    public void testGetDoubleNullResult() {
        FSPathResult result = new FSPathResult(null);
        assertNull(result.getDouble());
    }

    @Test
    public void testGetDoubleNaN() {
        FSPathResult result = new FSPathResult(Double.NaN);
        assertNotNull(result.getDouble());
        assertEquals(Double.NaN, result.getDouble());
    }

    // Test getDate()
    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Date actualDate = result.getDate();
        assertEquals(date, actualDate);
    }

    @Test
    public void testGetDateNullResult() {
        FSPathResult result = new FSPathResult(null);
        assertNull(result.getDate());
    }

    @Test(expected = ParseException.class)
    public void testGetDateInvalidFormat() {
        String dateString = "invalidDateFormat";
        FSPathResult result = new FSPathResult(dateString);
        fail("Expected ParseException but got: " + result.getDate());
    }

    // Test getURI()
    @Test
    public void testGetURI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        URI actualURI = result.getURI();
        assertEquals(uri, actualURI);
    }

    @Test
    public void testGetURINullResult() {
        FSPathResult result = new FSPathResult(null);
        assertNull(result.getURI());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetURIEmptyURI() {
        FSPathResult result = new FSPathResult(new URI(""));
        fail("Expected IllegalArgumentException but got: " + result.getURI());
    }

    // Test getBoolean()
    @Test
    public void testGetBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        Boolean actualBoolean = result.getBoolean();
        assertEquals(booleanValue, actualBoolean);
    }

    @Test
    public void testGetBooleanNullResult() {
        FSPathResult result = new FSPathResult(null);
        assertNull(result.getBoolean());
    }

    // Test toString()
    @Test
    public void testToStringFile() {
        File file = new File("path/to/file");
        FSPathResult result = new FSPathResult(file);
        String expected = file.getAbsolutePath();
        String actual = result.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringString() {
        String string = "Hello World";
        FSPathResult result = new FSPathResult(string);
        String expected = string;
        String actual = result.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        String expected = doubleValue.toString();
        String actual = result.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        String expected = date.toString();
        String actual = result.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringURI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        String expected = uri.toString();
        String actual = result.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        String expected = booleanValue.toString();
        String actual = result.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringEmpty() {
        FSPathResult result = new FSPathResult(null);
        String expected = "FSPathResult : empty";
        String actual = result.toString();
        assertEquals(expected, actual);
    }
}