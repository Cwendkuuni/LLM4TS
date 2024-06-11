package net.sf.sugar.fspath.test;

import org.junit.Assert;
import org.junit.Test;

import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

    @Test
    public void testFSPathResultWithFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        String expected = file.getAbsolutePath();
        String actual = result.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithString() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        String expected = string;
        String actual = result.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithDouble() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);
        String expected = doubleValue.toString();
        String actual = result.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        String expected = date.toString();
        String actual = result.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithURI() {
        URI uri = new URI("http://example.com");
        FSPathResult result = new FSPathResult(uri);
        String expected = uri.toString();
        String actual = result.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFSPathResultWithBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        String expected = booleanValue.toString();
        String actual = result.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullFile() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullString() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullDouble() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultWithNullDate() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testFSPathResultWithInvalidURI() {
        URI uri = new URI("fs:///test");
        FSPathResult result = new FSPathResult(uri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultWithNullURI() {
        FSPathResult result = new FSPathResult(null);
    }
}