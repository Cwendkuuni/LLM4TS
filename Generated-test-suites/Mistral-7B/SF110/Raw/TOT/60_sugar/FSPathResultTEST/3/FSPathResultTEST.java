import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
        File file = new File("path/to/test/file");
        FSPathResult result = new FSPathResult(file);
        Assert.assertEquals("path/to/test/file", result.getFile().getAbsolutePath());
        Assert.assertEquals("path/to/test/file", result.toString());
    }

    @Test
    public void testFSPathResultString() {
        String string = "Test String";
        FSPathResult result = new FSPathResult(string);
        Assert.assertEquals("Test String", result.getString());
        Assert.assertEquals("Test String", result.toString());
    }

    @Test
    public void testFSPathResultDouble() {
        Double result = 3.14;
        FSPathResult resultDouble = new FSPathResult(result);
        Assert.assertEquals(result, resultDouble.getDouble());
        Assert.assertEquals("3.14", resultDouble.toString());
    }

    @Test
    public void testFSPathResultDate() {
        Date date = new Date();
        FSPathResult resultDate = new FSPathResult(date);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String expected = formatter.format(date);
        Assert.assertEquals(expected, resultDate.toString());
    }

    @Test
    public void testFSPathResultURI() {
        URI uri = new URI("http://www.example.com/test");
        FSPathResult resultURI = new FSPathResult(uri);
        Assert.assertEquals("http://www.example.com/test", resultURI.getURI().toString());
        Assert.assertEquals("http://www.example.com/test", resultURI.toString());
    }

    @Test
    public void testFSPathResultBoolean() {
        Boolean resultBoolean = true;
        FSPathResult result = new FSPathResult(resultBoolean);
        Assert.assertEquals(resultBoolean, result.getBoolean());
        Assert.assertEquals("true", result.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNull() {
        FSPathResult result = new FSPathResult(null);
    }
}