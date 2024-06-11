import net.sf.sugar.fspath.FSPathResult;
import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {

    @Test
    public void testFSPathResultFileConstruction() {
        File inputFile = new File("testFile.txt");
        FSPathResult result = new FSPathResult(inputFile);
        Assert.assertNotNull(result.getFile());
        Assert.assertEquals("testFile.txt", result.toString());
    }

    @Test
    public void testFSPathResultStringConstruction() {
        String inputString = "This is a test string";
        FSPathResult result = new FSPathResult(inputString);
        Assert.assertNull(result.getFile());
        Assert.assertEquals(inputString, result.getString());
        Assert.assertEquals(inputString, result.toString());
    }

    @Test
    public void testFSPathResultDoubleConstruction() {
        Double inputDouble = new Double(42.0);
        FSPathResult result = new FSPathResult(inputDouble);
        Assert.assertNull(result.getFile());
        Assert.assertEquals(inputDouble.toString(), result.getDouble().toString());
        Assert.assertEquals(inputDouble.toString(), result.toString());
    }

    @Test
    public void testFSPathResultDateConstruction() {
        Date inputDate = new Date();
        FSPathResult result = new FSPathResult(inputDate);
        Assert.assertNull(result.getFile());
        Assert.assertEquals(inputDate.toString(), result.getDate().toString());
        Assert.assertEquals(inputDate.toString(), result.toString());
    }

    @Test
    public void testFSPathResultURIConstruction() {
        URI inputURI = new URI("http://example.com/test.html");
        FSPathResult result = new FSPathResult(inputURI);
        Assert.assertNull(result.getFile());
        Assert.assertEquals(inputURI.toString(), result.getURI().toString());
        Assert.assertEquals(inputURI.toString(), result.toString());
    }

    @Test
    public void testFSPathResultBooleanConstruction() {
        Boolean inputBoolean = true;
        FSPathResult result = new FSPathResult(inputBoolean);
        Assert.assertNull(result.getFile());
        Assert.assertEquals(inputBoolean.toString(), result.getBoolean().toString());
        Assert.assertEquals(inputBoolean.toString(), result.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullConstructionFile() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullConstructionString() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullConstructionDouble() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = NullPointerException.class)
    public void testFSPathResultNullConstructionDate() {
        FSPathResult result = new FSPathResult(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFSPathResultInvalidURIConstruction() {
        FSPathResult result = new FSPathResult(new URI(""));
    }

    @Test
    public void testToStringWhenResultFileIsNotNull() {
        File inputFile = new File("testFile.txt");
        FSPathResult result = new FSPathResult(inputFile);
        Assert.assertNotNull(result.getFile());
        Assert.assertEquals(inputFile.getAbsolutePath(), result.toString());
    }

    @Test
    public void testToStringWhenResultStringIsNotNull() {
        String inputString = "This is a test string";
        FSPathResult result = new FSPathResult(inputString);
        Assert.assertNull(result.getFile());
        Assert.assertEquals(inputString, result.toString());
    }

    @Test
    public void testToStringWhenResultDoubleIsNotNull() {
        Double inputDouble = new Double(42.0);
        FSPathResult result = new FSPathResult(inputDouble);
        Assert.assertNull(result.getFile());
        Assert.assertEquals(inputDouble.toString(), result.toString());
    }

    @Test
    public void testToStringWhenResultDateIsNotNull() {
        Date inputDate = new Date();
        FSPathResult result = new FSPathResult(inputDate);
        Assert.assertNull(result.getFile());
        Assert.assertEquals(inputDate.toString(), result.toString());
    }

    @Test
    public void testToStringWhenResultURIIsNotNull() {
        URI inputURI = new URI("http://example.com/test.html");
        FSPathResult result = new FSPathResult(inputURI);
        Assert.assertNull(result.getFile());
        Assert.assertEquals(inputURI.toString(), result.toString());
    }

    @Test
    public void testToStringWhenResultBooleanIsNotNull() {
        Boolean inputBoolean = true;
        FSPathResult result = new FSPathResult(inputBoolean);
        Assert.assertNull(result.getFile());
        Assert.assertEquals(inputBoolean.toString(), result.toString());
    }

    @Test
    public void testToStringWhenResultIsEmpty() {
        FSPathResult result = new FSPathResult();
        Assert.assertNull(result.getFile());
        Assert.assertEquals("FSPathResult : empty", result.toString());
    }

}