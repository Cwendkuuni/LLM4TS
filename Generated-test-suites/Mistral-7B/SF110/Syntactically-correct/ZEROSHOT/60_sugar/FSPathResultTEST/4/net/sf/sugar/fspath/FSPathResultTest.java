import net.sf.sugar.fspath.FSPathResult;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        String expected = file.getAbsolutePath();

        Assert.assertEquals(expected, result.toString());
        Assert.assertNotNull(result.getFile());
        Assert.assertNull(result.getString());
        Assert.assertNull(result.getDouble());
        Assert.assertNull(result.getDate());
        Assert.assertNull(result.getURI());
        Assert.assertNull(result.getBoolean());
    }

    @Test
    public void testFSPathResultWithString() {
        FSPathResult result = new FSPathResult("Hello World!");
        String expected = "Hello World!";

        Assert.assertEquals(expected, result.toString());
        Assert.assertNull(result.getFile());
        Assert.assertEquals(expected, result.getString());
        Assert.assertNull(result.getDouble());
        Assert.assertNull(result.getDate());
        Assert.assertNull(result.getURI());
        Assert.assertNull(result.getBoolean());
    }

    @Test
    public void testFSPathResultWithDouble() {
        FSPathResult result = new FSPathResult(3.14);
        String expected = "3.14";

        Assert.assertEquals(expected, result.toString());
        Assert.assertNull(result.getFile());
        Assert.assertNull(result.getString());
        Assert.assertEquals(3.14, result.getDouble().doubleValue(), 0.0);
        Assert.assertNull(result.getDate());
        Assert.assertNull(result.getURI());
        Assert.assertNull(result.getBoolean());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        String expected = date.toString();

        Assert.assertEquals(expected, result.toString());
        Assert.assertNull(result.getFile());
        Assert.assertNull(result.getString());
        Assert.assertNull(result.getDouble());
        Assert.assertEquals(date, result.getDate());
        Assert.assertNull(result.getURI());
        Assert.assertNull(result.getBoolean());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        String expected = uri.toString();

        Assert.assertEquals(expected, result.toString());
        Assert.assertNull(result.getFile());
        Assert.assertNull(result.getString());
        Assert.assertNull(result.getDouble());
        Assert.assertNull(result.getDate());
        Assert.assertEquals(uri, result.getURI());
        Assert.assertNull(result.getBoolean());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        String expected = bool.toString();

        Assert.assertEquals(expected, result.toString());
        Assert.assertNull(result.getFile());
        Assert.assertNull(result.getString());
        Assert.assertNull(result.getDouble());
        Assert.assertNull(result.getDate());
        Assert.assertNull(result.getURI());
        Assert.assertEquals(bool, result.getBoolean());
    }
}