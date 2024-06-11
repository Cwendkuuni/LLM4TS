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
        Assert.assertNotNull(result.getFile());
        Assert.assertEquals("test.txt", result.toString());
    }

    @Test
    public void testFSPathResultWithString() {
        FSPathResult result = new FSPathResult("Test String");
        Assert.assertNull(result.getFile());
        Assert.assertEquals("Test String", result.toString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        FSPathResult result = new FSPathResult(3.14);
        Assert.assertNull(result.getFile());
        Assert.assertEquals("3.14", result.toString());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Assert.assertNull(result.getFile());
        Assert.assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        Assert.assertNull(result.getFile());
        Assert.assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        FSPathResult result = new FSPathResult(true);
        Assert.assertNull(result.getFile());
        Assert.assertEquals("true", result.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullFile() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullString() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullDouble() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullDate() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullURI() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullBoolean() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test
    public void testFSPathResultEquals() {
        File file1 = new File("test.txt");
        File file2 = new File("test.txt");
        FSPathResult result1 = new FSPathResult(file1);
        FSPathResult result2 = new FSPathResult(file2);
        Assert.assertEquals(result1, result2);
    }

    @Test
    public void testFSPathResultNotEquals() {
        File file1 = new File("test.txt");
        File file2 = new File("test2.txt");
        FSPathResult result1 = new FSPathResult(file1);
        FSPathResult result2 = new FSPathResult(file2);
        Assert.assertNotEquals(result1, result2);
    }

    @Test
    public void testFSPathResultHashCode() {
        File file = new File("test.txt");
        FSPathResult result1 = new FSPathResult(file);
        File file2 = new File("test.txt");
        FSPathResult result2 = new FSPathResult(file2);
        Assert.assertEquals(result1.hashCode(), result2.hashCode());
    }
}