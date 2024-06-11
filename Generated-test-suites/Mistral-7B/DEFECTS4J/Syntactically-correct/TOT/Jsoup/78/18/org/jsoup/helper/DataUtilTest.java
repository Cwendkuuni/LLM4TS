import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "file:///path/to/test.html";

        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test document title", "Test Title", doc.title());

        // Test edge case: file not found
        File nonExistentFile = new File("nonExistentFile.html");
        doc = DataUtil.load(nonExistentFile, charsetName, baseUri);
        Assert.assertNull(doc);

        // Test error case: invalid charset name
        String invalidCharsetName = "!@#$%^&*";
        doc = DataUtil.load(testFile, invalidCharsetName, baseUri);
        Assert.assertNotNull(doc); // Default charset should be used
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Test title</h1></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://localhost/";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.load(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test document title", "Test title", doc.title());

        // Test edge case: invalid charset name
        Charset invalidCharset = Charset.forName("!@#$%^&*");
        doc = DataUtil.load(inputStream, invalidCharset.toString(), baseUri, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc); // Default charset should be used
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><body><h1>Test title</h1></body></html>";
        String charsetName = "UTF-8";
        String baseUri = "http://localhost/";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals("Test document title", "Test title", doc.title());

        // Test edge case: empty input stream
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        doc = DataUtil.parseInputStream(emptyInputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
        Assert.assertNull(doc);

        // Test error case: invalid charset name
        Charset invalidCharset = Charset.forName("!@#$%^&*");
        doc = DataUtil.parseInputStream(inputStream, invalidCharset.toString(), baseUri, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc); // Default charset should be used
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        // Test edge case: empty input file
        inputFile = new File("emptyInput.txt");
        inputStream = new FileInputStream(inputFile);
        outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertEquals("", new String(Files.readAllBytes(outputFile.toPath())));

        // Test error case: input and output streams the same
        inputFile = new File("input.txt");
        outputFile = new File("input.txt");
        inputStream = new FileInputStream(inputFile);
        outputStream = new FileOutputStream(outputFile);

        try {
            DataUtil.crossStreams(inputStream, outputStream);
            Assert.fail("IOException expected");
        } catch (IOException e) {
            // Expected
        }
    }
}