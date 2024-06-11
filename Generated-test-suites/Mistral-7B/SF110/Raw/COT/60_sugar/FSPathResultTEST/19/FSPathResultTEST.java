import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testConstructorWithFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        Assert.assertEquals(file, fsPathResult.getFile());
    }

    @Test
    public void testConstructorWithString() {
        String string = "testString";
        FSPathResult fsPathResult = new FSPathResult(string);
        Assert.assertEquals(string, fsPathResult.getString());
    }

    @Test
    public void testConstructorWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        Assert.assertEquals(doubleValue, fsPathResult.getDouble());
    }

    @Test
    public void testConstructorWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        Assert.assertEquals(date, fsPathResult.getDate());
    }

    @Test
    public void testConstructorWithURI() {
        URI uri = new URI("http://test.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        Assert.assertEquals(uri, fsPathResult.getURI());
    }

    @Test
    public void testConstructorWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        Assert.assertEquals(booleanValue, fsPathResult.getBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructor() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test
    public void testToStringWithFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        Assert.assertEquals(file.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testToStringWithString() {
        String string = "testString";
        FSPathResult fsPathResult = new FSPathResult(string);
        Assert.assertEquals(string, fsPathResult.toString());
    }

    @Test
    public void testToStringWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        Assert.assertEquals(doubleValue.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        Assert.assertEquals(booleanValue.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        Assert.assertEquals(date.toString(), fsPathResult.toString());
    }

    @Test
    public void testToStringWithURI() {
        URI uri = new URI("http://test.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        Assert.assertEquals(uri.toString(), fsPathResult.toString());
    }

    @Test
    public void testEmptyToString() {
        FSPathResult fsPathResult = new FSPathResult();
        Assert.assertEquals("FSPathResult : empty", fsPathResult.toString());
    }

}