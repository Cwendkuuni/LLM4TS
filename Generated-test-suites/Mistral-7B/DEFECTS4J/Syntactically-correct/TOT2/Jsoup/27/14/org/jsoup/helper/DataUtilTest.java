import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;

public class DataUtilTest {

    @Test
    public void testLoadFileDocument() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080";
        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        // Assertions
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadMissingFile() throws IOException {
        File inputFile = new File("missingfile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080";
        DataUtil.load(inputFile, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadWithNullCharsetName() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = null;
        String baseUri = "http://localhost:8080";
        DataUtil.load(inputFile, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadWithEmptyCharsetName() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "";
        String baseUri = "http://localhost:8080";
        DataUtil.load(inputFile, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadWithInvalidCharsetName() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "InvalidCharset";
        String baseUri = "http://localhost:8080";
        DataUtil.load(inputFile, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadWithNullBaseUri() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = null;
        DataUtil.load(inputFile, charsetName, baseUri);
    }

    @Test(expected = IOException.class)
    public void testLoadWithEmptyBaseUri() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "";
        DataUtil.load(inputFile, charsetName, baseUri);
    }
}