import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        FSPathResult fsPathResult = new FSPathResult(new File("testFilePath"));
        File actualFile = fsPathResult.getFile();
        Assert.assertNotNull(actualFile);
        Assert.assertEquals("testFilePath", actualFile.getAbsolutePath());
    }

    @Test
    public void testFSPathResultWithString() {
        FSPathResult fsPathResult = new FSPathResult("testString");
        String actualString = fsPathResult.getString();
        Assert.assertNotNull(actualString);
        Assert.assertEquals("testString", actualString);
    }

    @Test
    public void testFSPathResultWithDouble() {
        FSPathResult fsPathResult = new FSPathResult(3.14);
        Double actualDouble = fsPathResult.getDouble();
        Assert.assertNotNull(actualDouble);
        Assert.assertEquals(3.14, actualDouble.doubleValue(), 0.01);
    }

    @Test
    public void testFSPathResultWithDate() {
        Date testDate = new Date();
        FSPathResult fsPathResult = new FSPathResult(testDate);
        Date actualDate = fsPathResult.getDate();
        Assert.assertNotNull(actualDate);
        Assert.assertEquals(testDate.toString(), actualDate.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI testURI = new URI("http://test.com");
        FSPathResult fsPathResult = new FSPathResult(testURI);
        URI actualURI = fsPathResult.getURI();
        Assert.assertNotNull(actualURI);
        Assert.assertEquals(testURI.toString(), actualURI.toString());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        FSPathResult fsPathResult = new FSPathResult(true);
        Boolean actualBoolean = fsPathResult.getBoolean();
        Assert.assertNotNull(actualBoolean);
        Assert.assertEquals(true, actualBoolean);
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullFSPathResult() {
        FSPathResult fsPathResult = null;
        fsPathResult.getFile();
    }

    @Test(expected = NullPointerException.class)
    public void testGetNullStringFSPathResult() {
        FSPathResult fsPathResult = new FSPathResult(null);
        String testString = fsPathResult.getString();
        Assert.assertNull(testString);
    }

    @Test(expected = ClassCastException.class)
    public void testGetFileFromStringFSPathResult() {
        FSPathResult fsPathResult = new FSPathResult("testString");
        File testFile = fsPathResult.getFile();
        Assert.assertNull(testFile);
    }

    @Test(expected = ClassCastException.class)
    public void testGetFileFromDoubleFSPathResult() {
        FSPathResult fsPathResult = new FSPathResult(3.14);
        File testFile = fsPathResult.getFile();
        Assert.assertNull(testFile);
    }

    @Test(expected = ClassCastException.class)
    public void testGetFileFromDateFSPathResult() {
        Date testDate = new Date();
        FSPathResult fsPathResult = new FSPathResult(testDate);
        File testFile = fsPathResult.getFile();
        Assert.assertNull(testFile);
    }

    @Test(expected = ClassCastException.class)
    public void testGetFileFromURIFSPathResult() {
        URI testURI = new URI("http://test.com");
        FSPathResult fsPathResult = new FSPathResult(testURI);
        File testFile = fsPathResult.getFile();
        Assert.assertNull(testFile);
    }

    @Test(expected = ClassCastException.class)
    public void testGetFileFromBooleanFSPathResult() {
        FSPathResult fsPathResult = new FSPathResult(true);
        File testFile = fsPathResult.getFile();
        Assert.assertNull(testFile);
    }
}