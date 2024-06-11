import net.sf.sugar.fspath.FSPathResult;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFileInstance() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);
        String expected = "testFile.txt";
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultStringInstance() {
        String string = "Hello World!";
        FSPathResult fsPathResult = new FSPathResult(string);
        String expected = "Hello World!";
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultDoubleInstance() {
        Double doubleValue = 3.14;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);
        String expected = "3.14";
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultDateInstance() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);
        String expected = String.format("%1$td/%1$tm/%1$ty %1$tH:%1$tM:%1$tS", date);
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultURIInstance() {
        URI uri = new URI("http://www.test.com");
        FSPathResult fsPathResult = new FSPathResult(uri);
        String expected = "http://www.test.com";
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultBooleanInstance() {
        Boolean booleanValue = Boolean.TRUE;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);
        String expected = "true";
        String actual = fsPathResult.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullFileInstance() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullStringInstance() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullDoubleInstance() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullDateInstance() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullURIInstance() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullBooleanInstance() {
        FSPathResult fsPathResult = new FSPathResult(null);
    }
}