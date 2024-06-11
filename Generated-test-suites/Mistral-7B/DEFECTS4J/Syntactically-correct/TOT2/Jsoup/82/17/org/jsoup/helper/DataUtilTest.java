import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtilTest; // Assuming you created this test class
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the path to your test file
        String expectedCharset = "UTF-8"; // Replace with the expected charset of the test file
        String baseUri = "http://www.example.com"; // Replace with a base URI

        Document doc = DataUtil.load(file, expectedCharset, baseUri);

        // Check the loaded document is not null
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        String baseUri = "http://www.example.com";

        Document doc = DataUtil.load(inputStream, null, baseUri);

        // Check the loaded document is not null
        Assert.assertNotNull(doc);

        // Check the document content is as expected
        Assert.assertEquals(htmlContent, doc.body().html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><head><meta charset=\"ISO-8859-1\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        String baseUri = "http://www.example.com";
        String charset = "ISO-8859-1";

        Document doc = DataUtil.load(inputStream, charset, baseUri);

        // Check the loaded document is not null
        Assert.assertNotNull(doc);

        // Check the document content is as expected, after being decoded using the specified charset
        Assert.assertEquals(htmlContent.replaceAll("ISO-8859-1", charset), doc.body().html());
    }

    @Test
    public void testLoadInputStreamWithBOM() throws IOException {
        String htmlContent = "\uFEFF<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        String baseUri = "http://www.example.com";

        Document doc = DataUtil.load(inputStream, null, baseUri);

        // Check the loaded document is not null
        Assert.assertNotNull(doc);

        // Check the document content is as expected, after being decoded using the default charset (UTF-8, since the BOM indicates UTF-8)
        Assert.assertEquals(htmlContent, doc.body().html());
    }

    @Test
    public void testLoadInputStreamWithNoBOMAndInvalidCharset() throws IOException {
        String htmlContent = "Invalid content"; // This content should not be valid UTF-8
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        String baseUri = "http://www.example.com";
        String charset = "InvalidCharset";

        try {
            Document doc = DataUtil.load(inputStream, charset, baseUri);
            Assert.fail();
        } catch (IllegalArgumentException expected) {
            // The method should throw an IllegalArgumentException when an invalid charset is provided
        }
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        String baseUri = "http://www.example.com";

        Document doc = DataUtil.parseInputStream(inputStream, "UTF-8", baseUri, Parser.htmlParser());

        // Check the loaded document is not null
        Assert.assertNotNull(doc);

        // Check the document content is as expected
        Assert.assertEquals(htmlContent, doc.body().html());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceContent = "Source content";
        String targetContent = "Target content";
        InputStream sourceStream = new ByteArrayInputStream(sourceContent.getBytes(Charset.forName("UTF-8")));
        OutputStream targetStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(sourceStream, targetStream);

        // Check the target stream content is as expected
        Assert.assertEquals(sourceContent, targetStream.toString(Charset.forName("UTF-8")));
    }
}