import net.sf.sugar.fspath.FSPathResult;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.util.Date;

/**
 * FSPathResultTest.java
 *
 * Created on 08 April 2008, 16:21
 */

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);

        File actualResultFile = fsPathResult.getFile();
        Assert.assertEquals(file.getAbsolutePath(), actualResultFile.getAbsolutePath());
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "Hello World";
        FSPathResult fsPathResult = new FSPathResult(string);

        String actualResultString = fsPathResult.getString();
        Assert.assertEquals(string, actualResultString);
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult fsPathResult = new FSPathResult(doubleValue);

        Double actualResultDouble = fsPathResult.getDouble();
        Assert.assertEquals(doubleValue, actualResultDouble);
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult fsPathResult = new FSPathResult(date);

        Date actualResultDate = fsPathResult.getDate();
        Assert.assertEquals(date, actualResultDate);
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://www.google.com");
        FSPathResult fsPathResult = new FSPathResult(uri);

        URI actualResultURI = fsPathResult.getURI();
        Assert.assertEquals(uri, actualResultURI);
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult fsPathResult = new FSPathResult(booleanValue);

        Boolean actualResultBoolean = fsPathResult.getBoolean();
        Assert.assertEquals(booleanValue, actualResultBoolean);
    }

    @Test
    public void testFSPathResultToString() {
        File file = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(file);

        String actualResult = fsPathResult.toString();
        String expectedResult = file.getAbsolutePath();
        Assert.assertEquals(expectedResult, actualResult);
    }
}