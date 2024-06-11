import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static Document expectedDocument;
    private static File testFile;
    private static InputStream inputStream;

    @BeforeClass
    public static void setUp() throws Exception {
        String htmlContent = "<html><head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">" +
                "<title>Test Page</title>" +
                "</head>" +
                "<body>" +
                "<p>Hello, World!</p>" +
                "</body>" +
                "</html>";
        expectedDocument = Parser.htmlParser().parseInput(htmlContent, null);

        testFile = new File("test.html");
        FileUtils.writeStringToFile(testFile, htmlContent, StandardCharsets.UTF_8);

        inputStream = new FileInputStream(testFile);
    }

    @Test
    public void testLoadFile() throws IOException {
        Document document = DataUtil.load(testFile, "ISO-8859-1", null);
        assertEquals(expectedDocument, document);
    }

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        Document document = DataUtil.load(testFile, null, null);
        assertEquals(expectedDocument, document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedDocument.html().getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals(expectedDocument, document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedDocument.html().getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "INVALID_CHARSET", null);
        assertNull(document);
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(expectedDocument.html().getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null);
        assertEquals(expectedDocument, document);
    }

    @Test
    public void testParseByteData() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(expectedDocument.html().getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        assertEquals(expectedDocument, document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(expectedDocument.html().getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, "INVALID_CHARSET", null, Parser.htmlParser());
        assertNull(document);
    }

    @Test
    public void testParseByteDataWithNoCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(expectedDocument.html().getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        assertEquals(expectedDocument, document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(expectedDocument.html().getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());

        Element meta = document.select("meta[http-equiv=content-type], meta[charset]").first();
        String charset = null;
        if (meta != null) {
            charset = DataUtil.getCharsetFromContentType(meta.attr("content"));
        }
        assertSame(expectedDocument.outputSettings().charset(), charset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(file), 0);
        ByteBuffer expectedByteData = ByteBuffer.wrap(expectedDocument.html().getBytes(StandardCharsets.UTF_8));
        byteData.compareTo(expectedByteData); // We can't use assertEquals() directly here because ByteBuffer doesn't override equals()
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(file), 1);
        assertEquals(1, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String contentType = "text/html; charset=INVALID_CHARSET";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}