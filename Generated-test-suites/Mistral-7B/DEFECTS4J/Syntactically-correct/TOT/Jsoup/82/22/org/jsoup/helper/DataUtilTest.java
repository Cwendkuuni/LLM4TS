import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtilTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        File inputFile = new File("test.html");
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputFile, expectedCharset, baseUri);

        // Assert document is not null
        Assert.assertNotNull(doc);

        // Assert document content is correct
        String actualContent = doc.body().text();
        String expectedContent = "Test content"; // Replace with the expected content of the test file
        Assert.assertEquals(expectedContent, actualContent);

        // Test loading a file with an incorrect charset
        String incorrectCharset = "ISO-8859-1";
        Document docWithIncorrectCharset = DataUtil.load(inputFile, incorrectCharset, baseUri);
        Assert.assertEquals(expectedContent, docWithIncorrectCharset.body().text()); // The content should be the same as with the correct charset to prove encoding was ignored
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String htmlContent = "<html><body><p>Test content</p></body></html>";
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, expectedCharset, baseUri);

        // Assert document is not null
        Assert.assertNotNull(doc);

        // Assert document content is correct
        String actualContent = doc.body().text();
        Assert.assertEquals("Test content", actualContent);

        // Test loading an input stream with a different charset
        String differentCharset = "ISO-8859-1";
        Document docWithDifferentCharset = DataUtil.load(inputStream, differentCharset, baseUri);
        Assert.assertEquals("Test content", docWithDifferentCharset.body().text()); // The content should be the same as with the correct charset to prove encoding was ignored
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String invalidCharset = "UTF-16BE";
        String inputData = "Invalid charset input";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        try {
            DataUtil.parseInputStream(inputStream, invalidCharset, null, Parser.htmlParser());
            Assert.fail("Expected an IOException for an unsupported charset.");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("test.html");
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream("output.html");

        DataUtil.crossStreams(inputStream, outputStream);

        Document doc = DataUtil.load(outputStream, expectedCharset, baseUri);

        // Assert document is not null
        Assert.assertNotNull(doc);

        // Assert document content is the same as the original file
        String actualContent = doc.body().text();
        String expectedContent = "Test content"; // Replace with the expected content of the test file
        Assert.assertEquals(expectedContent, actualContent);

        outputStream.close();
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithEmptyBOMAndUTF8Charset() throws IOException {
        String emptyBom = "";
        String charset = "UTF-8";
        String inputData = emptyBom;

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseInputStream(inputStream, charset, null, Parser.htmlParser());

        // Assert document is not null
        Assert.assertNotNull(doc);

        // Assert document content is empty
        String actualContent = doc.body().text();
        Assert.assertEquals("", actualContent);

        inputStream.close();
    }
}