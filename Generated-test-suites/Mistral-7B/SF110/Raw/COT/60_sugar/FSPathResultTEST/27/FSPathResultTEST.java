import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        FSPathResult fsPathResult = new FSPathResult(new File("testFilePath"));
        File expectedFile = new File("testFilePath");
        FSPathResult expectedFSPathResult = new FSPathResult(expectedFile);

        Assert.assertEquals(expectedFSPathResult, fsPathResult);
        Assert.assertEquals(expectedFile.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithString() {
        FSPathResult fsPathResult = new FSPathResult("testString");
        String expectedString = "testString";
        FSPathResult expectedFSPathResult = new FSPathResult(expectedString);

        Assert.assertEquals(expectedFSPathResult, fsPathResult);
        Assert.assertEquals(expectedString, fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        FSPathResult fsPathResult = new FSPathResult(3.14);
        Double expectedDouble = 3.14;
        FSPathResult expectedFSPathResult = new FSPathResult(expectedDouble);

        Assert.assertEquals(expectedFSPathResult, fsPathResult);
        Assert.assertEquals(expectedDouble.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        String expectedDateString = String.format("%tF %<tT", date);
        FSPathResult expectedFSPathResult = new FSPathResult(expectedDateString);

        Assert.assertEquals(expectedFSPathResult, fsPathResult);
        Assert.assertEquals(expectedDateString, fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        String expectedURIString = uri.toString();
        FSPathResult expectedFSPathResult = new FSPathResult(expectedURIString);

        Assert.assertEquals(expectedFSPathResult, fsPathResult);
        Assert.assertEquals(expectedURIString, fsPathResult.toString());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean bool = true;
        FSPathResult fsPathResult = new FSPathResult(bool);
        String expectedBooleanString = bool.toString();
        FSPathResult expectedFSPathResult = new FSPathResult(expectedBooleanString);

        Assert.assertEquals(expectedFSPathResult, fsPathResult);
        Assert.assertEquals(expectedBooleanString, fsPathResult.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullArgument() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test
    public void testFSPathResultToStringWithNullResult() {
        FSPathResult fsPathResult = new FSPathResult(null);
        Assert.assertEquals("FSPathResult : empty", fsPathResult.toString());
    }
}