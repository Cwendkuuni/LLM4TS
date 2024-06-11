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
        FSPathResult fsPathResult = new FSPathResult(file);
        String expectedResult = file.getAbsolutePath();

        Assert.assertEquals(expectedResult, fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "Hello World";
        FSPathResult fsPathResult = new FSPathResult(string);
        String expectedResult = string;

        Assert.assertEquals(expectedResult, fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        String expectedResult = doubleValue.toString();

        Assert.assertEquals(expectedResult, fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        String expectedResult = date.toString();

        Assert.assertEquals(expectedResult, fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.google.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        String expectedResult = uri.toString();

        Assert.assertEquals(expectedResult, fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        String expectedResult = booleanValue.toString();

        Assert.assertEquals(expectedResult, fsPathResult.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNull() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }
}