import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File testFile = new File("testFile.txt");
        FSPathResult result = new FSPathResult(testFile);
        Assert.assertEquals("testFile.txt", result.toString());
    }

    @Test
    public void testFSPathResultWithString() {
        String testString = "Hello World";
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
    public void testFSPathResultWithDate() throws ParseException {
        Date testDate = new Date();
        FSPathResult result = new FSPathResult(testDate);
        Assert.assertEquals(testDate.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI testURI = new URI("http://www.example.com/test.html");
        FSPathResult result = new FSPathResult(testURI);
        Assert.assertEquals(testURI.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithBooleanTrue() {
        Boolean testBoolean = true;
        FSPathResult result = new FSPathResult(testBoolean);
        Assert.assertEquals(testBoolean.toString(), result.toString());
    }

    @Test
    public void testFSPathResultWithBooleanFalse() {
        Boolean testBoolean = false;
        FSPathResult result = new FSPathResult(testBoolean);
        Assert.assertEquals(testBoolean.toString(), result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullFile() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullString() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = NumberFormatException.class)
    public void testFSPathResultWithInvalidDouble() {
        FSPathResult result = new FSPathResult("Invalid Double");
    }

    @Test(expected = ParseException.class)
    public void testFSPathResultWithInvalidDate() {
        String invalidDateString = "2022-02-30";
        FSPathResult result = new FSPathResult(invalidDateString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullURI() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test
    public void testFSPathResultWithEmptyObject() {
        FSPathResult result = new FSPathResult();
        Assert.assertEquals("FSPathResult : empty", result.toString());
    }
}