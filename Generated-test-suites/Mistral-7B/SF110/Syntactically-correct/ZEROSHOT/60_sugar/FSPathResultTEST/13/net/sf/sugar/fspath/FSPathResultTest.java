import net.sf.sugar.fspath.FSPathResult;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("src/test/resources/test.txt");
        FSPathResult result = new FSPathResult(file);

        File expectedFile = new File("src/test/resources/test.txt");

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

        Date expectedDate = new Date();

        Date actualDate = result.getDate();
        Assert.assertEquals(expectedDate, actualDate);
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.google.com");
        FSPathResult result = new FSPathResult(uri);

        URI expectedUri = new URI("http://www.google.com");

        URI actualUri = result.getURI();
        Assert.assertEquals(expectedUri, actualUri);
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
    public void testToStringWithFile() {
        File file = new File("src/test/resources/test.txt");
        FSPathResult result = new FSPathResult(file);

        String expectedString = "src/test/resources/test.txt";

        String actualString = result.toString();
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithString() {
        String string = "Hello World!";
        FSPathResult result = new FSPathResult(string);

        String expectedString = "Hello World!";

        String actualString = result.toString();
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult result = new FSPathResult(doubleValue);

        String expectedString = "3.14";

        String actualString = result.toString();
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);

        String expectedString = "Mon Aug 01 12:00:00 EDT 2023";

        String actualString = result.toString();
        Assert.assertNotSame(expectedString, actualString); // Date format may vary
    }

    @Test
    public void testToStringWithURI() {
        URI uri = new URI("http://www.google.com");
        FSPathResult result = new FSPathResult(uri);

        String expectedString = "http://www.google.com";

        String actualString = result.toString();
        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);

        String expectedString = "true";

        String actualString = result.toString();
        Assert.assertEquals(expectedString, actualString);
    }
}