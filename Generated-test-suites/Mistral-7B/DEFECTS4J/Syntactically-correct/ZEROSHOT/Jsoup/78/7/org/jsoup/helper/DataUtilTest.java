package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Mock
    private Parser parser;

    private DataUtil util;

    @Before
    public void setUp() {
        util = new DataUtil();
    }

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String fileContent = "This is a test";
        String charset = "UTF-8";
        File file = new File("test.html");

        when(parser.parseInput(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(new Document(""));

        Document document = util.load(file, charset, "");

        assertEquals("This is a test", document.body().text());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "This is a test";
        String charset = "invalidCharset";
        File file = new File("test.html");

        ByteArrayInputStream inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
        when(parser.parseInput(ArgumentMatchers.eq(inputStream), ArgumentMatchers.anyString()))
            .thenReturn(new Document(""));

        Document document = util.load(file, charset, "");

        assertNull(document.body().text());
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String inputStreamContent = "This is a test";
        String charset = "UTF-8";

        when(parser.parseInput(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(new Document(""));

        Document document = util.load(new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8)), charset, "");

        assertEquals("This is a test", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "This is a test";
        String charset = "invalidCharset";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = util.load(inputStream, charset, "");

        assertNull(document.body().text());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "This is a test";
        String charset = "UTF-8";
        Parser parser = Mockito.mock(Parser.class);

        Document document = Mockito.mock(Document.class);

        when(parser.parseInput(ArgumentMatchers.anyString(), ArgumentMatchers.anyString()))
            .thenReturn(document);

        when(document.body().text()).thenReturn("This is a test");

        Document result = util.load(new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8)), charset, parser);

        assertEquals(document, result);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test input".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        util.crossStreams(inputStream, outputStream);

        assertEquals("Test input", outputStream.toString());
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "This is a test";
        String charset = null;

        Document document = util.parseInputStream(
            new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8)),
            charset, "",
            parser
        );

        assertEquals("This is a test", document.body().text());
    }

    @Test
    public void testParseInputStreamWithExplicitCharset() throws IOException {
        String inputStreamContent = "This is a test";
        String charset = "UTF-8";

        Document document = util.parseInputStream(
            new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8)),
            charset, "",
            parser
        );

        assertEquals("This is a test", document.body().text());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "This is a test";
        String charset = "invalidCharset";

        Document document = util.parseInputStream(
            new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8)),
            charset, "",
            parser
        );

        assertNull(document.body().text());
    }

    @Test
    public void testDetectionOfExplicitCharsetFromContentType() throws IOException {
        String inputStreamContent = "Content-Type: text/html; charset=UTF-8\r\n\r\n<html><body>This is a test</body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        util.crossStreams(inputStream, outputStream);

        Document document = util.parseInputStream(outputStream.toByteArray(), "UTF-8", "", parser);

        assertEquals("This is a test", document.body().text());
    }

    @Test
    public void testDetectionOfInvalidCharsetFromContentType() throws IOException {
        String inputStreamContent = "Content-Type: text/html; charset=invalidCharset\r\n\r\n<html><body>This is a test</body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        util.crossStreams(inputStream, outputStream);

        Document document = util.parseInputStream(outputStream.toByteArray(), null, "", parser);

        assertNull(document.body().text());
    }

    @Test
    public void testDetectionOfExplicitCharsetFromXmlDeclaration() throws IOException {
        String inputStreamContent = "<?xml encoding=\"UTF-8\"?>\r\n<html><body>This is a test</body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        util.crossStreams(inputStream, outputStream);

        Document document = util.parseInputStream(outputStream.toByteArray(), "UTF-8", "", parser);

        assertEquals("This is a test", document.body().text());
    }

    @Test
    public void testDetectionOfInvalidCharsetFromXmlDeclaration() throws IOException {
        String inputStreamContent = "<?xml encoding=\"invalidCharset\"?>\r\n<html><body>This is a test</body></html>";

        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        util.crossStreams(inputStream, outputStream);

        Document document = util.parseInputStream(outputStream.toByteArray(), null, "", parser);

        assertNull(document.body().text());
    }

    @Test
    public void testReadToByteBufferWithMaxSizeZero() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);

        ByteBuffer result = util.readToByteBuffer(inputStream);

        Mockito.verify(inputStream, Mockito.times(1)).mark(ArgumentMatchers.anyInt());
        Mockito.verify(inputStream, Mockito.times(1)).reset();
    }

    @Test
    public void testReadToByteBufferWithMaxSizeGreaterThanZero() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);

        ByteBuffer expectedResult = ByteBuffer.wrap(new byte[]{'A', 'B', 'C'});
        Mockito.when(inputStream.read(ArgumentMatchers.anyByteArray(), ArgumentMatchers.eq(3)))
            .thenReturn(3);

        ByteBuffer result = util.readToByteBuffer(inputStream);

        Mockito.verify(inputStream, Mockito.times(1)).mark(ArgumentMatchers.anyInt());
        Mockito.verify(inputStream, Mockito.times(1)).reset();
        Mockito.verify(inputStream, Mockito.times(1)).read(ArgumentMatchers.anyByteArray(), ArgumentMatchers.eq(3));

        assertEquals(expectedResult, result);
    }

    @Test(expected = IOException.class)
    public void testReadToByteBufferWithInterruptedStream() throws IOException, InterruptedException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ByteBuffer buffer = Mockito.mock(ByteBuffer.class);

        Mockito.when(buffer.remaining()).thenReturn(10);
        Mockito.when(inputStream.read(ArgumentMatchers.anyByteArray(), ArgumentMatchers.anyInt())).thenThrow(InterruptedIOException.class);

        util.readToByteBuffer(inputStream);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        String fileContent = "This is a test";

        ByteBuffer result = util.readFileToByteBuffer(file);

        assertEquals(fileContent.getBytes(StandardCharsets.UTF_8), result.array());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer result = util.emptyByteBuffer();

        assertEquals(0, result.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String result = util.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=invalidCharset";

        String result = util.getCharsetFromContentType(contentType);

        assertNull(result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";

        String result = util.getCharsetFromContentType(contentType);

        assertNull(result);
    }

    @Test
    public void testValidateCharsetWithValidCharset() {
        String charset = "UTF-8";

        String result = util.validateCharset(charset);

        assertEquals("UTF-8", result);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String charset = "invalidCharset";

        String result = util.validateCharset(charset);

        assertNull(result);
    }

    @Test
    public void testMimeBoundary() {
        String result = util.mimeBoundary();

        assertNotNull(result);
        assertEquals(32, result.length());
    }
}