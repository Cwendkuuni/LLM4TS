import net.sf.sugar.fspath.FSPathResult;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFileConstruction() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        Assert.assertNotNull(result.getFile());
    }

    @Test
    public void testFSPathResultStringConstruction() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        Assert.assertEquals(string, result.getString());
    }

    @Test
    public void testFSPathResultDoubleConstruction() {
        Double doubleValue = 1.2;
        FSPathResult result = new FSPathResult(doubleValue);
        Assert.assertEquals(doubleValue, result.getDouble());
    }

    @Test
    public void testFSPathResultDateConstruction() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Assert.assertEquals(date, result.getDate());
    }

    @Test
    public void testFSPathResultURIConstruction() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        Assert.assertEquals(uri, result.getURI());
    }

    @Test
    public void testFSPathResultBooleanConstruction() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        Assert.assertEquals(booleanValue, result.getBoolean());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullConstruction() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test
    public void testFSPathResultToStringFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        Assert.assertEquals(file.getAbsolutePath(), result.toString());
    }

    @Test
    public void testFSPathResultToStringString() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        Assert.assertEquals(string, result.toString());
    }

    @Test
    public void testFSPathResultToStringDouble() {
        Double doubleValue = 1.2;
        FSPathResult result = new FSPathResult(doubleValue);
        Assert.assertEquals(doubleValue.toString(), result.toString());
    }

    @Test
    public void testFSPathResultToStringBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        Assert.assertEquals(booleanValue.toString(), result.toString());
    }

    @Test
    public void testFSPathResultToStringDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Assert.assertEquals(date.toString(), result.toString());
    }

    @Test
    public void testFSPathResultToStringURI() {
        URI uri = new URI("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        Assert.assertEquals(uri.toString(), result.toString());
    }

    @Test
    public void testFSPathResultToStringEmpty() {
        FSPathResult result = new FSPathResult();
        Assert.assertEquals("FSPathResult : empty", result.toString());
    }
}