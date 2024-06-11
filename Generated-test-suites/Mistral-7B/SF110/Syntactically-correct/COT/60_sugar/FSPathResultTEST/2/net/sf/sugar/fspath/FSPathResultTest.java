import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFileConstructor() {
        File expectedFile = new File("expectedPath");
        FSPathResult actualResult = new FSPathResult(expectedFile);
        File resultFile = actualResult.getFile();
        Assert.assertEquals("Expected and actual files should be equal.", expectedFile, resultFile);
    }

    @Test
    public void testFSPathResultStringConstructor() {
        String expectedString = "expectedString";
        FSPathResult actualResult = new FSPathResult(expectedString);
        String resultString = actualResult.getString();
        Assert.assertEquals("Expected and actual strings should be equal.", expectedString, resultString);
    }

    @Test
    public void testFSPathResultDoubleConstructor() {
        double expectedResult = 123.45;
        FSPathResult actualResult = new FSPathResult(expectedResult);
        Double resultDouble = actualResult.getDouble();
        Assert.assertEquals("Expected and actual doubles should be equal.", expectedResult, resultDouble.doubleValue(), 0.0);
    }

    @Test
    public void testFSPathResultDateConstructor() {
        Date expectedDate = new Date();
        FSPathResult actualResult = new FSPathResult(expectedDate);
        Date resultDate = actualResult.getDate();
        Assert.assertEquals("Expected and actual dates should be equal.", expectedDate, resultDate);
    }

    @Test
    public void testFSPathResultURIConstructor() {
        URI expectedURI = new URI("http://www.example.com");
        FSPathResult actualResult = new FSPathResult(expectedURI);
        URI resultURI = actualResult.getURI();
        Assert.assertEquals("Expected and actual URIs should be equal.", expectedURI, resultURI);
    }

    @Test
    public void testFSPathResultBooleanConstructor() {
        boolean expectedResult = true;
        FSPathResult actualResult = new FSPathResult(expectedResult);
        Boolean resultBoolean = actualResult.getBoolean();
        Assert.assertEquals("Expected and actual booleans should be equal.", expectedResult, resultBoolean.booleanValue());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullConstructorFile() {
        new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullConstructorString() {
        new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullConstructorDouble() {
        new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullConstructorDate() {
        new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultInvalidURIConstructor() {
        new FSPathResult(new URI(""));
    }

    @Test
    public void testFSPathResultToStringFile() {
        File expectedFile = new File("expectedPath");
        FSPathResult actualResult = new FSPathResult(expectedFile);
        String resultString = actualResult.toString();
        Assert.assertTrue("Result string should start with file's absolute path.", resultString.startsWith(expectedFile.getAbsolutePath()));
    }

    @Test
    public void testFSPathResultToStringString() {
        String expectedString = "expectedString";
        FSPathResult actualResult = new FSPathResult(expectedString);
        String resultString = actualResult.toString();
        Assert.assertEquals("Expected and actual strings should be equal.", expectedString, resultString);
    }

    @Test
    public void testFSPathResultToStringDouble() {
        double expectedResult = 123.45;
        FSPathResult actualResult = new FSPathResult(expectedResult);
        String resultString = actualResult.toString();
        Assert.assertEquals("Expected and actual strings should be equal.", expectedResult + "", resultString);
    }

    @Test
    public void testFSPathResultToStringDate() {
        Date expectedDate = new Date();
        FSPathResult actualResult = new FSPathResult(expectedDate);
        String resultString = actualResult.toString();
        Assert.assertEquals("Expected and actual strings should be equal.", expectedDate.toString(), resultString);
    }

    @Test
    public void testFSPathResultToStringURI() {
        URI expectedURI = new URI("http://www.example.com");
        FSPathResult actualResult = new FSPathResult(expectedURI);
        String resultString = actualResult.toString();
        Assert.assertEquals("Expected and actual strings should be equal.", expectedURI.toString(), resultString);
    }

    @Test
    public void testFSPathResultToStringBoolean() {
        boolean expectedResult = true;
        FSPathResult actualResult = new FSPathResult(expectedResult);
        String resultString = actualResult.toString();
        Assert.assertEquals("Expected and actual strings should be equal.", expectedResult + "", resultString);
    }

    @Test
    public void testFSPathResultToStringEmpty() {
        FSPathResult result = new FSPathResult();
        String resultString = result.toString();
        Assert.assertEquals("Expected and actual strings should be equal.", "FSPathResult : empty", resultString);
    }
}