import org.jsoup.DataUtil;
import org.jsoup.helper.DataUtilTest;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(PowerMockRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File testFile = new File("testFile.html");
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testFile, expectedCharset, baseUri);
        assertNotNull(document);
        assertEquals(expectedCharset, document.outputSettings().charset().name());
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File testFile = new File("testFile.html");
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testFile, null, baseUri);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(testDataBytes);
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testInputStream, expectedCharset, baseUri);
        assertNotNull(document);
        assertEquals(expectedCharset, document.outputSettings().charset().name());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(testDataBytes);
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testInputStream, null, baseUri);
        assertNotNull(document);
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testData = "<html><head><meta charset=\"invalidCharset\"></meta></head><body></body></html>";
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(testDataBytes);
        String baseUri = "http://example.com";
        assertThrows(IOException.class, () -> DataUtil.load(testInputStream, "invalidCharset", baseUri));
    }

    @Test
    public void testCrossStreams() throws IOException {
        File testFile = new File("testFile.html");
        File outputFile = new File("outputFile.html");
        String testCharset = "UTF-8";
        String outputCharset = "UTF-16";
        String baseUri = "http://example.com";

        try (InputStream testInputStream = new FileInputStream(testFile);
             OutputStream outputStream = new FileOutputStream(outputFile)) {
            DataUtil.parseInputStream(testInputStream, testCharset, baseUri, DataUtil.Parser.htmlParser());
            DataUtil.crossStreams(testInputStream, outputStream);
        }

        DataUtil.load(outputFile, outputCharset, baseUri);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String validCharset = "UTF-8";
        String invalidCharset = "invalidCharset";
        String charset = DataUtil.validateCharset(validCharset);
        assertEquals(validCharset, charset);

        charset = DataUtil.validateCharset(invalidCharset);
        assertNull(charset);
    }

    @Test
    public void testMimeBoundary() {
        String expectedResult = new String(new Random().generateSeed(32), 0, 32);
        String result = DataUtil.mimeBoundary();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(testDataBytes);
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.parseInputStream(testInputStream, charset, baseUri, DataUtil.Parser.htmlParser());
        assertNotNull(document);
        assertEquals(charset, document.outputSettings().charset().name());
    }

    @Test
    public void testParseInputStreamWithoutCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(testDataBytes);
        String baseUri = "http://example.com";
        Document document = PowerMockito.spy(DataUtil.parseInputStream(testInputStream, null, baseUri, DataUtil.Parser.htmlParser()));
        PowerMockito.doReturn("UTF-8").when(document).outputSettings().charset();
        assertEquals("UTF-8", document.outputSettings().charset().name());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testData = "<html><head><meta charset=\"invalidCharset\"></meta></head><body></body></html>";
        byte[] testDataBytes = testData.getBytes(StandardCharsets.UTF_8);
        InputStream testInputStream = new ByteArrayInputStream(testDataBytes);
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        assertThrows(IOException.class, () -> DataUtil.parseInputStream(testInputStream, charset, baseUri, DataUtil.Parser.htmlParser()));
    }
}