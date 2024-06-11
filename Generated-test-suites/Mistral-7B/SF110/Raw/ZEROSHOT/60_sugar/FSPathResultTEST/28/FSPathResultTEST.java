import net.sf.sugar.fspath.FSPathResult;
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

        Assert.assertEquals(expectedResult, fsPathResult.toString());
        Assert.assertNotNull(fsPathResult.getFile());
        Assert.assertNull(fsPathResult.getString());
        Assert.assertNull(fsPathResult.getDouble());
        Assert.assertNull(fsPathResult.getDate());
        Assert.assertNull(fsPathResult.getURI());
        Assert.assertNull(fsPathResult.getBoolean());
    }

    @Test
    public void testFSPathResultWithString() {
        String testString = "Hello World!";
        FSPathResult fsPathResult = new FSPathResult(testString);
        String expectedResult = testString;

        Assert.assertEquals(expectedResult, fsPathResult.toString());
        Assert.assertNull(fsPathResult.getFile());
        Assert.assertEquals(expectedResult, fsPathResult.getString());
        Assert.assertNull(fsPathResult.getDouble());
        Assert.assertNull(fsPathResult.getDate());
        Assert.assertNull(fsPathResult.getURI());
        Assert.assertNull(fsPathResult.getBoolean());
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double testDouble = 3.14;
        FSPathResult fsPathResult = new FSPathResult(testDouble);
        String expectedResult = testDouble.toString();

        Assert.assertEquals(expectedResult, fsPathResult.toString());
        Assert.assertNull(fsPathResult.getFile());
        Assert.assertNull(fsPathResult.getString());
        Assert.assertEquals(testDouble, fsPathResult.getDouble());
        Assert.assertNull(fsPathResult.getDate());
        Assert.assertNull(fsPathResult.getURI());
        Assert.assertNull(fsPathResult.getBoolean());
    }

    @Test
    public void testFSPathResultWithDate() {
        Date testDate = new Date();
        FSPathResult fsPathResult = new FSPathResult(testDate);
        String expectedResult = testDate.toString();

        Assert.assertEquals(expectedResult, fsPathResult.toString());
        Assert.assertNull(fsPathResult.getFile());
        Assert.assertNull(fsPathResult.getString());
        Assert.assertNull(fsPathResult.getDouble());
        Assert.assertEquals(testDate, fsPathResult.getDate());
        Assert.assertNull(fsPathResult.getURI());
        Assert.assertNull(fsPathResult.getBoolean());
    }

    @Test
    public void testFSPathResultWithURI() {
        URI testURI = new URI("http://www.example.com/test.html");
        FSPathResult fsPathResult = new FSPathResult(testURI);
        String expectedResult = testURI.toString();

        Assert.assertEquals(expectedResult, fsPathResult.toString());
        Assert.assertNull(fsPathResult.getFile());
        Assert.assertNull(fsPathResult.getString());
        Assert.assertNull(fsPathResult.getDouble());
        Assert.assertNull(fsPathResult.getDate());
        Assert.assertEquals(testURI, fsPathResult.getURI());
        Assert.assertNull(fsPathResult.getBoolean());
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean testBoolean = true;
        FSPathResult fsPathResult = new FSPathResult(testBoolean);
        String expectedResult = testBoolean.toString();

        Assert.assertEquals(expectedResult, fsPathResult.toString());
        Assert.assertNull(fsPathResult.getFile());
        Assert.assertNull(fsPathResult.getString());
        Assert.assertNull(fsPathResult.getDouble());
        Assert.assertNull(fsPathResult.getDate());
        Assert.assertNull(fsPathResult.getURI());
        Assert.assertEquals(testBoolean, fsPathResult.getBoolean());
    }
}