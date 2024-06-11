import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFileConstructor() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        Assert.assertEquals(file, fsPathResult.getFile());
    }

    @Test
    public void testFSPathResultStringConstructor() {
        String string = "Test String";
        FSPathResult fsPathResult = new FSPathResult(string);
        Assert.assertEquals(string, fsPathResult.getString());
    }

    @Test
    public void testFSPathResultDoubleConstructor() {
        Double doubleValue = 123.45;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        Assert.assertEquals(doubleValue, fsPathResult.getDouble());
    }

    @Test
    public void testFSPathResultDateConstructor() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        Assert.assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testFSPathResultURIConstructor() {
        URI uri = new URI("http://test.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        Assert.assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testFSPathResultBooleanConstructor() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        Assert.assertEquals(booleanValue, fsPathResult.getBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultInvalidFileConstructor() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test
    public void testFSPathResultGetFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        Assert.assertEquals(file, fsPathResult.getFile());
    }

    @Test
    public void testFSPathResultGetString() {
        String string = "Test String";
        FSPathResult fsPathResult = new FSPathResult(string);
        Assert.assertEquals(string, fsPathResult.getString());
    }

    @Test
    public void testFSPathResultGetDouble() {
        Double doubleValue = 123.45;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        Assert.assertEquals(doubleValue, fsPathResult.getDouble());
    }

    @Test
    public void testFSPathResultGetDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        Assert.assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testFSPathResultGetURI() {
        URI uri = new URI("http://test.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        Assert.assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testFSPathResultGetBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        Assert.assertEquals(booleanValue, fsPathResult.getBoolean());
    }

    @Test
    public void testFSPathResultToStringFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        Assert.assertEquals(file.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultToStringString() {
        String string = "Test String";
        FSPathResult fsPathResult = new FSPathResult(string);
        Assert.assertEquals(string, fsPathResult.toString());
    }

    @Test
    public void testFSPathResultToStringDouble() {
        Double doubleValue = 123.45;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        Assert.assertEquals(doubleValue.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultToStringDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        Assert.assertEquals(date.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultToStringURI() {
        URI uri = new URI("http://test.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        Assert.assertEquals(uri.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultToStringBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        Assert.assertEquals(booleanValue.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultToStringEmpty() {
        FSPathResult fsPathResult = new FSPathResult();
        Assert.assertEquals("FSPathResult : empty", fsPathResult.toString());
    }
}