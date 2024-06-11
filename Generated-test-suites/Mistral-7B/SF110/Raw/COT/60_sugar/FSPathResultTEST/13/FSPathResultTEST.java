import org.junit.Assert;
import org.junit.Test;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File testFile = new File("testFile.txt");
        FSPathResult result = new FSPathResult(testFile);
        Assert.assertEquals("testFile.txt", result.toString());
    }

    @Test
    public void testFSPathResultWithString() {
        String testString = "testString";
        FSPathResult result = new FSPathResult(testString);
        Assert.assertEquals(testString, result.toString());
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double testDouble = 123.45;
        FSPathResult result = new FSPathResult(testDouble);
        Assert.assertEquals(testDouble.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        Assert.assertEquals(testDate.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI testURI = new URI("http://test.com");
        FSPathResult result = new FSPathResult(testURI);
        Assert.assertEquals(testURI.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean testBoolean = true;
        FSPathResult result = new FSPathResult(testBoolean);
        Assert.assertEquals(testBoolean.toString(), result.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullFSPathResult() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithInvalidFile() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test
    public void testToStringWithEmptyFSPathResult() {
        FSPathResult result = new FSPathResult(null);
        Assert.assertEquals("FSPathResult : empty", result.toString());
    }
}