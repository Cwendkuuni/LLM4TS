import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("path/to/existing/file.txt");
        FSPathResult result = new FSPathResult(file);
        String expected = file.getAbsolutePath();

        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "Hello World";
        FSPathResult result = new FSPathResult(string);
        String expected = string;

        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);
        String expected = doubleValue.toString();

        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResultWithDate() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateFormat.parse("2022-12-31 12:00:00");
        FSPathResult result = new FSPathResult(date);
        String expected = dateFormat.format(date);

        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.example.com/path/to/file.txt");
        FSPathResult result = new FSPathResult(uri);
        String expected = uri.toString();

        Assert.assertEquals(expected, result.toString());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        String expected = booleanValue.toString();

        Assert.assertEquals(expected, result.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testFSPathResultWithNullInput() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test
    public void testFSPathResultToStringEmpty() {
        FSPathResult result = new FSPathResult(null);
        String expected = "FSPathResult : empty";

        Assert.assertEquals(expected, result.toString());
    }
}