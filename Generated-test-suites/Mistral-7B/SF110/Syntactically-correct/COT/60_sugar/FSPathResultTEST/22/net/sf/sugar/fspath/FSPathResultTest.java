import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("path/to/file.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        Assert.assertEquals("path/to/file.txt", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "Hello World!";
        FSPathResult fsPathResult = new FSPathResult(string);
        Assert.assertEquals(string, fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        Assert.assertEquals(doubleValue.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        Assert.assertEquals(date.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        Assert.assertEquals(uri.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        Assert.assertEquals(booleanValue.toString(), fsPathResult.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullInput() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test
    public void testToStringWhenResultIsEmpty() {
        FSPathResult fsPathResult = new FSPathResult(null);
        Assert.assertEquals("FSPathResult : empty", fsPathResult.toString());
    }

    @Test
    public void testGetFile() {
        File file = new File("path/to/file.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        Assert.assertEquals(file, fsPathResult.getFile());
    }

    @Test
    public void testGetString() {
        String string = "Hello World!";
        FSPathResult fsPathResult = new FSPathResult(string);
        Assert.assertEquals(string, fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        Double doubleValue = 3.14;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        Assert.assertEquals(doubleValue, fsPathResult.getDouble());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        Assert.assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        URI uri = new URI("http://example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        Assert.assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        Assert.assertEquals(booleanValue, fsPathResult.getBoolean());
    }
}