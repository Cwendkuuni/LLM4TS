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

        File actualResultFile = result.getFile();
        String expectedResultFilePath = "test.txt";

        Assert.assertNotNull(actualResultFile);
        Assert.assertEquals(expectedResultFilePath, actualResultFile.getAbsolutePath());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "Hello, World!";
        FSPathResult result = new FSPathResult(string);

        String actualResultString = result.getString();
        String expectedResultString = "Hello, World!";

        Assert.assertEquals(expectedResultString, actualResultString);
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleResult = 3.14;
        FSPathResult result = new FSPathResult(doubleResult);

        Double actualResultDouble = result.getDouble();
        Assert.assertEquals(doubleResult, actualResultDouble);
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);

        Date actualResultDate = result.getDate();
        Assert.assertEquals(date, actualResultDate);
    }

    @Test
    public void testFSPathResultWithURI() {
        String uriString = "http://www.example.com";
        URI uri = new URI(uriString);
        FSPathResult result = new FSPathResult(uri);

        URI actualResultURI = result.getURI();
        Assert.assertEquals(uri, actualResultURI);
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanResult = true;
        FSPathResult result = new FSPathResult(booleanResult);

        Boolean actualResultBoolean = result.getBoolean();
        Assert.assertEquals(booleanResult, actualResultBoolean);
    }

    @Test
    public void testFSPathResultToStringWithFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);

        String actualResultString = result.toString();
        String expectedResultString = "test.txt";

        Assert.assertEquals(expectedResultString, actualResultString);
    }

    @Test
    public void testFSPathResultToStringWithString() {
        String string = "Hello, World!";
        FSPathResult result = new FSPathResult(string);

        String actualResultString = result.toString();
        String expectedResultString = "Hello, World!";

        Assert.assertEquals(expectedResultString, actualResultString);
    }

    @Test
    public void testFSPathResultToStringWithDouble() {
        Double doubleResult = 3.14;
        FSPathResult result = new FSPathResult(doubleResult);

        String actualResultString = result.toString();
        String expectedResultString = "3.14";

        Assert.assertEquals(expectedResultString, actualResultString);
    }

    @Test
    public void testFSPathResultToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);

        String actualResultString = result.toString();
        String expectedResultString = date.toString();

        Assert.assertEquals(expectedResultString, actualResultString);
    }

    @Test
    public void testFSPathResultToStringWithURI() {
        String uriString = "http://www.example.com";
        URI uri = new URI(uriString);
        FSPathResult result = new FSPathResult(uri);

        String actualResultString = result.toString();
        String expectedResultString = uriString;

        Assert.assertEquals(expectedResultString, actualResultString);
    }

    @Test
    public void testFSPathResultToStringWithBoolean() {
        Boolean booleanResult = true;
        FSPathResult result = new FSPathResult(booleanResult);

        String actualResultString = result.toString();
        String expectedResultString = "true";

        Assert.assertEquals(expectedResultString, actualResultString);
    }
}