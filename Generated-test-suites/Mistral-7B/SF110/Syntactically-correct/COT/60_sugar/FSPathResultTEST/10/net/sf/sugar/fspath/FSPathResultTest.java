import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("testFile.txt");
        FSPathResult expectedResult = new FSPathResult(file);
        FSPathResult actualResult = new FSPathResult(file);
        Assert.assertEquals(expectedResult.getFile(), actualResult.getFile());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "testString";
        FSPathResult expectedResult = new FSPathResult(string);
        FSPathResult actualResult = new FSPathResult(string);
        Assert.assertEquals(expectedResult.getString(), actualResult.getString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult expectedResult = new FSPathResult(doubleValue);
        FSPathResult actualResult = new FSPathResult(doubleValue);
        Assert.assertEquals(expectedResult.getDouble(), actualResult.getDouble());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult expectedResult = new FSPathResult(date);
        FSPathResult actualResult = new FSPathResult(date);
        Assert.assertEquals(expectedResult.getDate(), actualResult.getDate());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult expectedResult = new FSPathResult(uri);
        FSPathResult actualResult = new FSPathResult(uri);
        Assert.assertEquals(expectedResult.getURI(), actualResult.getURI());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult expectedResult = new FSPathResult(booleanValue);
        FSPathResult actualResult = new FSPathResult(booleanValue);
        Assert.assertEquals(expectedResult.getBoolean(), actualResult.getBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullArgument() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test
    public void testToStringWithFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        String expectedResult = file.getAbsolutePath();
        String actualResult = result.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToStringWithString() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        String expectedResult = string;
        String actualResult = result.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToStringWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);
        String expectedResult = doubleValue.toString();
        String actualResult = result.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        String expectedResult = booleanValue.toString();
        String actualResult = result.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        String expectedResult = date.toString();
        String actualResult = result.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToStringWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        String expectedResult = uri.toString();
        String actualResult = result.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testToStringWithEmptyResult() {
        FSPathResult result = new FSPathResult();
        String expectedResult = "FSPathResult : empty";
        String actualResult = result.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }
}