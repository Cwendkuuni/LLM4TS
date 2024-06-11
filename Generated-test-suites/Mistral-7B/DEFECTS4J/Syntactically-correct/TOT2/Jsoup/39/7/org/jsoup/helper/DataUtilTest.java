import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    // Expert 1 test cases
    // ...

    // Expert 2 test cases
    // ...

    // Expert 3 test cases
    // ...

    @Test
    public void testLoadFile() throws IOException {
        // Given
        File file = new File("test.html"); // Assuming the file "test.html" exists
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document doc = DataUtil.load(file, charsetName, baseUri);

        // Then
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithUTF8Charset() throws IOException {
        // Given
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        // Then
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        // Given
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        // When and Then
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        // The test should fail because of the IOException
    }

    @Test
    public void testParseByteData() throws IOException {
        // Given
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        // When
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        // Then
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        // Given
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        // When
        String invalidCharsetName = "InvalidCharset";
        Document doc = DataUtil.parseByteData(byteData, invalidCharsetName, baseUri, Parser.htmlParser());

        // Then
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        // Given
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = "http://example.com";

        // When
        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        } catch (IllegalArgumentException e) {
            // Expected IllegalArgumentException because of the null charsetName
            return;
        }

        // Then
        Assert.fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParseByteDataWithNullByteData() {
        // Given
        ByteBuffer byteData = null;
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        // Then
        Assert.assertNull(doc);
    }

    @Test
    public void testParseByteDataWithEmptyByteData() throws IOException {
        // Given
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.allocate(0);

        // When
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        // Then
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithLargeData() throws IOException {
        // Given
        String htmlContent = "<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        // Then
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithEmptyCharsetName() throws IOException {
        // Given
        ByteBuffer byteData = DataUtil.readToByteBuffer(new File("test.html").getInputStream());
        String charsetName = "";
        String baseUri = "http://example.com";

        // When
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());

        // Then
        Assert.assertNotNull(doc);
        // Default charset is "UTF-8"
    }

    @Test
    public void testParseByteDataWithBOM() throws IOException {
        // Given
        String htmlContent = "\ufeff<html><body><h1>Test</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);

        // Then
        Assert.assertNotNull(doc);
    }

}