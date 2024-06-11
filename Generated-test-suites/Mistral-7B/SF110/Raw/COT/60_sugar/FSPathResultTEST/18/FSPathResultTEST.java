import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        FSPathResult fsPathResult = new FSPathResult(new File("path/to/file"));
        File expectedFile = new File("path/to/file");
        File actualFile = fsPathResult.getFile();
        Assert.assertEquals("Should return correct File", expectedFile, actualFile);
    }

    @Test
    public void testFSPathResultWithString() {
        FSPathResult fsPathResult = new FSPathResult("test string");
        String expectedString = "test string";
        String actualString = fsPathResult.getString();
        Assert.assertEquals("Should return correct String", expectedString, actualString);
    }

    @Test
    public void testFSPathResultWithDouble() {
        FSPathResult fsPathResult = new FSPathResult(123.45);
        Double expectedDouble = 123.45;
        Double actualDouble = fsPathResult.getDouble();
        Assert.assertEquals("Should return correct Double", expectedDouble, actualDouble);
    }

    @Test
    public void testFSPathResultWithDate() {
        Date expectedDate = new Date();
        FSPathResult fsPathResult = new FSPathResult(expectedDate);
        Date actualDate = fsPathResult.getDate();
        Assert.assertEquals("Should return correct Date", expectedDate, actualDate);
    }

    @Test
    public void testFSPathResultWithURI() {
        URI expectedURI = new URI("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(expectedURI);
        URI actualURI = fsPathResult.getURI();
        Assert.assertEquals("Should return correct URI", expectedURI, actualURI);
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean expectedBoolean = true;
        FSPathResult fsPathResult = new FSPathResult(expectedBoolean);
        Boolean actualBoolean = fsPathResult.getBoolean();
        Assert.assertEquals("Should return correct Boolean", expectedBoolean, actualBoolean);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullInput() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test
    public void testFSPathResultToStringWithFile() {
        FSPathResult fsPathResult = new FSPathResult(new File("path/to/file"));
        String expectedString = "path/to/file";
        String actualString = fsPathResult.toString();
        Assert.assertEquals("toString should return correct String", expectedString, actualString);
    }

    @Test
    public void testFSPathResultToStringWithString() {
        FSPathResult fsPathResult = new FSPathResult("test string");
        String expectedString = "test string";
        String actualString = fsPathResult.toString();
        Assert.assertEquals("toString should return correct String", expectedString, actualString);
    }

    @Test
    public void testFSPathResultToStringWithDouble() {
        FSPathResult fsPathResult = new FSPathResult(123.45);
        String expectedString = "123.45";
        String actualString = fsPathResult.toString();
        Assert.assertEquals("toString should return correct String", expectedString, actualString);
    }

    @Test
    public void testFSPathResultToStringWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        String expectedString = date.toString();
        String actualString = fsPathResult.toString();
        Assert.assertEquals("toString should return correct String", expectedString, actualString);
    }

    @Test
    public void testFSPathResultToStringWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        String expectedString = uri.toString();
        String actualString = fsPathResult.toString();
        Assert.assertEquals("toString should return correct String", expectedString, actualString);
    }

    @Test
    public void testFSPathResultToStringWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        String expectedString = booleanValue.toString();
        String actualString = fsPathResult.toString();
        Assert.assertEquals("toString should return correct String", expectedString, actualString);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultToStringWithNullInput() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test
    public void testGettersForNullInput() {
        FSPathResult fsPathResult = new FSPathResult(null);
        Assert.assertNull("getFile should return null for null input", fsPathResult.getFile());
        Assert.assertNull("getString should return null for null input", fsPathResult.getString());
        Assert.assertNull("getDouble should return null for null input", fsPathResult.getDouble());
        Assert.assertNull("getDate should return null for null input", fsPathResult.getDate());
        Assert.assertNull("getURI should return null for null input", fsPathResult.getURI());
        Assert.assertNull("getBoolean should return null for null input", fsPathResult.getBoolean());
    }
}