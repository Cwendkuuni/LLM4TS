import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        String expected = file.getAbsolutePath();
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "Hello World!";
        FSPathResult fsPathResult = new FSPathResult(string);
        String expected = string;
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        String expected = doubleValue.toString();
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithDate() throws ParseException {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        String expected = date.toString();
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        String expected = uri.toString();
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        String expected = booleanValue.toString();
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullFile() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullString() {
        String string = null;
        FSPathResult fsPathResult = new FSPathResult(string);
    }

    @Test(expected = NumberFormatException.class)
    public void testFSPathResultWithInvalidDouble() {
        FSPathResult fsPathResult = new FSPathResult("abc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullDate() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithInvalidURI() {
        String invalidURI = "invalid://example.com";
        FSPathResult fsPathResult = new FSPathResult(new URI(invalidURI));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullURI() {
        URI uri = null;
        FSPathResult fsPathResult = new FSPathResult(uri);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullBoolean() {
        Boolean booleanValue = null;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
    }
}