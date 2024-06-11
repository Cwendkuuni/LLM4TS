package net.sf.sugar.fspath;

import org.junit.Assert;
import org.junit.Test;

public class FSPathResultTest {

    @Test
    public void testGetFile() {
        File testFile = new File("test.txt");
        FSPathResult fsPathResult = new FSPathResult(testFile);
        File actualFile = fsPathResult.getFile();
        Assert.assertEquals(testFile.getAbsolutePath(), actualFile.getAbsolutePath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorsWithNullValues() {
        FSPathResult testFSPathResult = new FSPathResult(null); // Test all constructors
    }

    @Test
    public void testGetFileWithNullFile() {
        FSPathResult fsPathResult = new FSPathResult(null);
        Assert.assertNull(fsPathResult.getFile());
    }

    @Test
    public void testGetStringWithEmptyString() {
        FSPathResult fsPathResult = new FSPathResult("");
        Assert.assertEquals("FSPathResult : empty", fsPathResult.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDateWithInvalidDate() {
        Date invalidDate = new Date(-1);
        FSPathResult fsPathResult = new FSPathResult(invalidDate);
    }

    @Test
    public void testGetURIWithNullURI() {
        URI nullURI = null;
        FSPathResult fsPathResult = new FSPathResult(nullURI);
        Assert.assertNull(fsPathResult.getURI());
    }

    @Test
    public void testGetBooleanWithInvalidBoolean() {
        FSPathResult fsPathResult = new FSPathResult((Object)1.5); // Integer should not be convertible to Boolean
        Assert.assertFalse(fsPathResult.getBoolean());
    }
}