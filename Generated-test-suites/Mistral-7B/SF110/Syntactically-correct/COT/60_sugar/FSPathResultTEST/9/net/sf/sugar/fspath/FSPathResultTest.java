import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testConstructorWithFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        Assert.assertEquals(file, result.getFile());
    }

    @Test
    public void testConstructorWithString() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        Assert.assertEquals(string, result.getString());
    }

    @Test
    public void testConstructorWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);
        Assert.assertEquals(doubleValue, result.getDouble());
    }

    @Test
    public void testConstructorWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Assert.assertEquals(date, result.getDate());
    }

    @Test
    public void testConstructorWithURI() {
        URI uri = new URI("http://test.com");
        FSPathResult result = new FSPathResult(uri);
        Assert.assertEquals(uri, result.getURI());
    }

    @Test
    public void testConstructorWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        Assert.assertEquals(booleanValue, result.getBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionWhenInvalidObjectIsPassedToConstructor() {
        Object invalidObject = new Object();
        new FSPathResult(invalidObject);
    }

    @Test
    public void testToStringMethodWithFileObject() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        Assert.assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringMethodWithStringObject() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        Assert.assertEquals(string, result.toString());
    }

    @Test
    public void testToStringMethodWithDoubleObject() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);
        Assert.assertEquals(doubleValue.toString(), result.toString());
    }

    @Test
    public void testToStringMethodWithBooleanObject() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        Assert.assertEquals(booleanValue.toString(), result.toString());
    }

    @Test
    public void testToStringMethodWithDateObject() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Assert.assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testToStringMethodWithURIObject() {
        URI uri = new URI("http://test.com");
        FSPathResult result = new FSPathResult(uri);
        Assert.assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testToStringMethodWithEmptyResult() {
        FSPathResult result = new FSPathResult();
        Assert.assertEquals("FSPathResult : empty", result.toString());
    }
}