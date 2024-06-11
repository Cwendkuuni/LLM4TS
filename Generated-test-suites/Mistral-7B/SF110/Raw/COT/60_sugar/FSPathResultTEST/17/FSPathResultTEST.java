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
        FSPathResult result = new FSPathResult(file);
        Assert.assertEquals("testFile.txt", result.toString());
    }

    @Test
    public void testFSPathResultWithString() {
        FSPathResult result = new FSPathResult("Hello World!");
        Assert.assertEquals("Hello World!", result.toString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        FSPathResult result = new FSPathResult(3.14);
        Assert.assertEquals("3.14", result.toString());
    }

    @Test
    public void testFSPathResultWithDate() throws ParseException {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Assert.assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        Assert.assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithBooleanTrue() {
        FSPathResult result = new FSPathResult(true);
        Assert.assertEquals("true", result.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullFile() {
        FSPathResult result = new FSPathResult(null);
        // expected NullPointerException
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullString() {
        FSPathResult result = new FSPathResult(null);
        // expected NullPointerException
    }

    @Test(expected = NumberFormatException.class)
    public void testFSPathResultWithInvalidDouble() {
        FSPathResult result = new FSPathResult("abc");
        // expected NumberFormatException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullDate() {
        FSPathResult result = new FSPathResult(null);
        // expected IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullURI() {
        FSPathResult result = new FSPathResult(null);
        // expected IllegalArgumentException
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullBoolean() {
        FSPathResult result = new FSPathResult(null);
        // expected IllegalArgumentException
    }

    @Test
    public void testFSPathResultToStringMethodForEmptyInstance() {
        FSPathResult result = new FSPathResult();
        Assert.assertEquals("FSPathResult : empty", result.toString());
    }
}