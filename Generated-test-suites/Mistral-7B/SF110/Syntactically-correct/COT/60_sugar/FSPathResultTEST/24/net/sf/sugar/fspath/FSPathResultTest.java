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
        Assert.assertSame(file, fsPathResult.getFile());
    }

    @Test
    public void testFSPathResultStringConstructor() {
        String string = "testString";
        FSPathResult fsPathResult = new FSPathResult(string);
        Assert.assertEquals(string, fsPathResult.getString());
    }

    @Test
    public void testFSPathResultDoubleConstructor() {
        Double doubleValue = 123.45;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        Assert.assertEquals(doubleValue, fsPathResult.getDouble(), 0.0);
    }

    @Test
    public void testFSPathResultDateConstructor() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        Assert.assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testFSPathResultURIConstructor() {
        String uriString = "http://example.com/path";
        URI uri = new URI(uriString);
        FSPathResult fsPathResult = new FSPathResult(uri);
        Assert.assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testFSPathResultBooleanConstructor() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        Assert.assertEquals(booleanValue, fsPathResult.getBoolean());
    }

    @Test(expected = IllegalStateException.class)
    public void testFSPathResultEmptyConstructor() {
        FSPathResult fsPathResult = new FSPathResult();
    }

    @Test
    public void testFSPathResultToStringFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        String expected = file.getAbsolutePath();
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultToStringString() {
        String string = "testString";
        FSPathResult fsPathResult = new FSPathResult(string);
        String expected = string;
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultToStringDouble() {
        Double doubleValue = 123.45;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        String expected = doubleValue.toString();
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultToStringBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        String expected = booleanValue.toString();
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultToStringDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        String expected = date.toString();
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultToStringURI() {
        String uriString = "http://example.com/path";
        URI uri = new URI(uriString);
        FSPathResult fsPathResult = new FSPathResult(uri);
        String expected = uri.toString();
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultToStringEmpty() {
        FSPathResult fsPathResult = new FSPathResult();
        String expected = "FSPathResult : empty";
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }
}