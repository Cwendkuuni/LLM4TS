import net.sf.sugar.fspath.FSPathResult;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Date;

/**
 * FSPathResultTest class file for JUnit 4 test methods to test all methods in FSPathResult class.
 */
public class FSPathResultTest {

    @Test
    public void testFSPathResultInstanceWithFile() {
        File file = new File("src/test/resources/testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);

        Assert.assertEquals(file.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultInstanceWithString() {
        String string = "Hello World!";
        FSPathResult fsPathResult = new FSPathResult(string);

        Assert.assertEquals(string, fsPathResult.toString());
    }

    @Test
    public void testFSPathResultInstanceWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);

        Assert.assertEquals(doubleValue.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultInstanceWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);

        Assert.assertEquals(date.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultInstanceWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);

        Assert.assertEquals(uri.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultInstanceWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);

        Assert.assertEquals(booleanValue.toString(), fsPathResult.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultInstanceWithNull() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }
}