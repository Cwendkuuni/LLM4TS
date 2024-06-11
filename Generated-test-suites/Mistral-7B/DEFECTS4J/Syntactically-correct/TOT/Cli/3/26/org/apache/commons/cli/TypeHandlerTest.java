import org.apache.commons.cli.TypeHandler;
import org.apache.commons.cli.PatternOptionBuilder;
import org.junit.Test;
import org.junit.Assert;

import java.io.File;
import java.net.URL;
import java.util.Date;

public class TypeHandlerTest {

    @Test
    public void testCreateValueStringString() {
        String str = "hello";
        Object obj = new String("world");
        Object result = TypeHandler.createValue(str, obj.getClass());
        Assert.assertEquals(obj, result);
    }

    @Test
    public void testCreateValueStringPatternOptionStringValue() {
        String str = "hello";
        Object obj = PatternOptionBuilder.STRING_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.assertEquals(str, result);
    }

    @Test(expected = RuntimeException.class)
    public void testCreateValueStringClassNotFoundException() {
        String str = "com.example.MyClass";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        Object result = TypeHandler.createValue(str, obj);
        Assert.fail("Expected RuntimeException, but got " + result);
    }

    @Test
    public void testCreateValueStringClassNewInstanceException() {
        String str = "com.example.MyClass";
        Object obj = PatternOptionBuilder.CLASS_VALUE;
        try {
            Object result = TypeHandler.createValue(str, obj);
            Assert.fail("Expected NewInstanceException, but got " + result);
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testCreateNumberLongValue() {
        String str = "123";
        Number number = TypeHandler.createNumber(str);
        Assert.assertEquals(123L, number.longValue());
    }

    @Test(expected = NumberFormatException.class)
    public void testCreateNumberInvalidNumberFormat() {
        String str = "123a";
        Number number = TypeHandler.createNumber(str);
        Assert.fail("Expected NumberFormatException, but got " + number);
    }

    @Test
    public void testCreateDateValidDateString() {
        String str = "2022-03-01";
        Date date = TypeHandler.createDate(str);
        Assert.assertNotNull(date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDateInvalidDateFormat() {
        String str = "2022-03-32";
        Date date = TypeHandler.createDate(str);
        Assert.fail("Expected IllegalArgumentException, but got " + date);
    }

    @Test
    public void testCreateURLValidURLString() {
        String str = "http://example.com";
        URL url = TypeHandler.createURL(str);
        Assert.assertNotNull(url);
    }

    @Test(expected = MalformedURLException.class)
    public void testCreateURLInvalidURLString() {
        String str = "http://example.com/does%20not%20exist";
        URL url = TypeHandler.createURL(str);
        Assert.fail("Expected MalformedURLException, but got " + url);
    }

    @Test
    public void testCreateFileValidFileString() {
        String str = "path/to/file.txt";
        File file = TypeHandler.createFile(str);
        Assert.assertNotNull(file);
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateFileInvalidFileString() {
        String str = "path/to/nonexistentfile.txt";
        File file = TypeHandler.createFile(str);
        Assert.fail("Expected FileNotFoundException, but got " + file);
    }
}