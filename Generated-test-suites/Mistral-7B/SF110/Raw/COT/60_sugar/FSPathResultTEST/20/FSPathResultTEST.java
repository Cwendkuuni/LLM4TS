import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.Date;

import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFileConstructor() {
        File testFile = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(testFile);
        assertEquals(testFile.getAbsolutePath(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultStringConstructor() {
        FSPathResult fsPathResult = new FSPathResult("testString");
        assertEquals("testString", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultDoubleConstructor() {
        FSPathResult fsPathResult = new FSPathResult(123.45);
        assertEquals("123.45", fsPathResult.toString());
    }

    @Test
    public void testFSPathResultDateConstructor() {
        Date testDate = new Date();
        FSPathResult fsPathResult = new FSPathResult(testDate);
        assertEquals(testDate.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultURIConstructor() {
        URI testURI = new URI("http://www.example.com/test");
        FSPathResult fsPathResult = new FSPathResult(testURI);
        assertEquals(testURI.toString(), fsPathResult.toString());
    }

    @Test
    public void testFSPathResultBooleanConstructor() {
        FSPathResult fsPathResult = new FSPathResult(true);
        assertEquals("true", fsPathResult.toString());
    }

    @Test
    public void testGetFile() {
        File testFile = new File("testFile.txt");
        FSPathResult fsPathResult = new FSPathResult(testFile);
        assertSame(testFile, fsPathResult.getFile());
    }

    @Test
    public void testGetString() {
        FSPathResult fsPathResult = new FSPathResult("testString");
        assertEquals("testString", fsPathResult.getString());
    }

    @Test
    public void testGetDouble() {
        FSPathResult fsPathResult = new FSPathResult(123.45);
        assertEquals(123.45, fsPathResult.getDouble().doubleValue(), 0.001);
    }

    @Test
    public void testGetDate() {
        Date testDate = new Date();
        FSPathResult fsPathResult = new FSPathResult(testDate);
        assertSame(testDate, fsPathResult.getDate());
    }

    @Test
    public void testGetURI() {
        URI testURI = new URI("http://www.example.com/test");
        FSPathResult fsPathResult = new FSPathResult(testURI);
        assertSame(testURI, fsPathResult.getURI());
    }

    @Test
    public void testGetBoolean() {
        FSPathResult fsPathResult = new FSPathResult(true);
        assertEquals(true, fsPathResult.getBoolean());
    }

    @Test(expected = NullPointerException.class)
    public void testNullConstructorFile() {
        new FSPathResult(null);
    }

    @Test
    public void testToStringEmpty() {
        FSPathResult fsPathResult = new FSPathResult();
        assertEquals("FSPathResult : empty", fsPathResult.toString());
    }
}