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
        String actualResult = fsPathResult.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "Hello World";
        FSPathResult fsPathResult = new FSPathResult(string);
        String expectedResult = string;
        String actualResult = fsPathResult.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 3.14;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        String expectedResult = doubleValue.toString();
        String actualResult = fsPathResult.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        String expectedResult = date.toString();
        String actualResult = fsPathResult.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        String expectedResult = uri.toString();
        String actualResult = fsPathResult.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        String expectedResult = booleanValue.toString();
        String actualResult = fsPathResult.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputToFSPathResultConstructor() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test
    public void testFSPathResultToStringWhenResultIsNull() {
        FSPathResult fsPathResult = new FSPathResult(null);
        String expectedResult = "FSPathResult : empty";
        String actualResult = fsPathResult.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }
}