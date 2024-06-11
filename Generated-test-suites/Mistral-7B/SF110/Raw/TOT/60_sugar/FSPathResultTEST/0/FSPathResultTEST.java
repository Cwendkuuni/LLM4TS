import org.junit.Test;
import static org.junit.Assert.*;
import net.sf.sugar.fspath.FSPathResult;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFile() {
        File file = new File("path/to/existing/file.txt");
        FSPathResult result = new FSPathResult(file);
        String expected = file.getAbsolutePath();

        assertEquals("Expected file path to be equal to the one returned by FSPathResult", expected, result.toString());
    }

    @Test
    public void testFSPathResultString() {
        String string = "Hello World!";
        FSPathResult result = new FSPathResult(string);
        String expected = string;

        assertEquals("Expected string to be equal to the one returned by FSPathResult", expected, result.toString());
    }

    @Test
    public void testFSPathResultDouble() {
        Double doubleValue = 3.14159265;
        FSPathResult result = new FSPathResult(doubleValue);
        String expected = doubleValue.toString();

        assertEquals("Expected double value to be equal to the one returned by FSPathResult", expected, result.toString());
    }

    @Test
    public void testFSPathResultDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        String expected = date.toString();

        assertEquals("Expected date string to be equal to the one returned by FSPathResult", expected, result.toString());
    }

    @Test
    public void testFSPathResultURI() {
        URI uri = new URI("http://test.com/path/to/file.txt");
        FSPathResult result = new FSPathResult(uri);
        String expected = uri.toString();

        assertEquals("Expected URI string to be equal to the one returned by FSPathResult", expected, result.toString());
    }

    @Test
    public void testFSPathResultBoolean() {
        Boolean booleanValue = true;
        FSPathResult result = new FSPathResult(booleanValue);
        String expected = booleanValue.toString();

        assertEquals("Expected boolean value to be equal to the one returned by FSPathResult", expected, result.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullFile() {
        File file = null;
        FSPathResult result = new FSPathResult(file);

        fail("FSPathResult should not accept null File argument");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullString() {
        String string = null;
        FSPathResult result = new FSPathResult(string);

        fail("FSPathResult should not accept null String argument");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullDouble() {
        Double doubleValue = null;
        FSPathResult result = new FSPathResult(doubleValue);

        fail("FSPathResult should not accept null Double argument");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullDate() {
        Date date = null;
        FSPathResult result = new FSPathResult(date);

        fail("FSPathResult should not accept null Date argument");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullURI() {
        URI uri = null;
        FSPathResult result = new FSPathResult(uri);

        fail("FSPathResult should not accept null URI argument");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultNullBoolean() {
        Boolean booleanValue = null;
        FSPathResult result = new FSPathResult(booleanValue);

        fail("FSPathResult should not accept null Boolean argument");
    }
}