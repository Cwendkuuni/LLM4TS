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

        File expectedFile = new File("test.txt");
        File actualFile = result.getFile();

        Assert.assertEquals(expectedFile.getAbsolutePath(), actualFile.getAbsolutePath());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "Hello World!";
        FSPathResult result = new FSPathResult(string);

        String expectedString = "Hello World!";
        String actualString = result.getString();

        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);

        Double expectedDouble = 3.14;
        Double actualDouble = result.getDouble();

        Assert.assertEquals(expectedDouble, actualDouble);
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);

        Date expectedDate = date;
        Date actualDate = result.getDate();

        Assert.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("https://www.example.com");
        FSPathResult result = new FSPathResult(uri);

        URI expectedURI = uri;
        URI actualURI = result.getURI();

        Assert.assertEquals(expectedURI, actualURI);
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);

        Boolean expectedBoolean = true;
        Boolean actualBoolean = result.getBoolean();

        Assert.assertEquals(expectedBoolean, actualBoolean);
    }

    @Test
    public void testFSPathResultToStringWithFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);

        String expected = file.getAbsolutePath();
        String actual = result.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultToStringWithString() {
        String string = "Hello World!";
        FSPathResult result = new FSPathResult(string);

        String expected = string;
        String actual = result.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultToStringWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);

        String expected = doubleValue.toString();
        String actual = result.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);

        String expected = date.toString();
        String actual = result.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultToStringWithURI() {
        URI uri = new URI("https://www.example.com");
        FSPathResult result = new FSPathResult(uri);

        String expected = uri.toString();
        String actual = result.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultToStringWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);

        String expected = booleanValue.toString();
        String actual = result.toString();

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testFSPathResultWithNull() {
        FSPathResult result = new FSPathResult(null);
    }
}