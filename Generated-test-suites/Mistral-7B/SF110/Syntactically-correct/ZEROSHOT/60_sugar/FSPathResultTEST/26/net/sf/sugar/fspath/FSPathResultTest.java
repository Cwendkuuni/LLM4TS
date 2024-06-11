package net.sf.sugar.fspath.test;

import org.junit.Assert;
import org.junit.Test;

import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
        File file = new File("test.txt");
        FSPathResult result = new FSPathResult(file);
        String expected = "test.txt";
        String actual = result.toString();
        Assert.assertEquals(expected, actual);
        File actualFile = result.getFile();
        Assert.assertEquals(file, actualFile);
    }

    @Test
    public void testFSPathResultString() {
        String string = "Hello World";
        FSPathResult result = new FSPathResult(string);
        String expected = "Hello World";
        String actual = result.toString();
        Assert.assertEquals(expected, actual);
        String actualString = result.getString();
        Assert.assertEquals(string, actualString);
    }

    @Test
    public void testFSPathResultDouble() {
        Double doubleValue = 123.45;
        FSPathResult result = new FSPathResult(doubleValue);
        String expected = "123.45";
        String actual = result.toString();
        Assert.assertEquals(expected, actual);
        Double actualDouble = result.getDouble();
        Assert.assertEquals(doubleValue, actualDouble);
    }

    @Test
    public void testFSPathResultDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        String expected = "Sat Aug 27 12:34:56 CEST 2023";
        String actual = result.toString();
        Assert.assertEquals(expected, actual);
        Date actualDate = result.getDate();
        Assert.assertEquals(date, actualDate);
    }

    @Test
    public void testFSPathResultURI() {
        URI uri = new URI("http://www.example.com/test.html");
        FSPathResult result = new FSPathResult(uri);
        String expected = "http://www.example.com/test.html";
        String actual = result.toString();
        Assert.assertEquals(expected, actual);
        URI actualURI = result.getURI();
        Assert.assertEquals(uri, actualURI);
    }

    @Test
    public void testFSPathResultBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        String expected = "true";
        String actual = result.toString();
        Assert.assertEquals(expected, actual);
        Boolean actualBoolean = result.getBoolean();
        Assert.assertEquals(booleanValue, actualBoolean);
    }
}