package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static final String TEST_FILE = "test.html";
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://localhost/test";

    private Document doc;
    private InputStream inputStream;
    private File file;

    @Before
    public void setUp() throws IOException {
        file = new File(getClass().getResource(TEST_FILE).getFile());
        inputStream = new FileInputStream(file);
        doc = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI);
    }

    @Test
    public void testLoadFile() throws IOException {
        Document docFromFile = DataUtil.load(new File("test.html"), TEST_CHARSET, TEST_BASE_URI);
        assertEquals(doc, docFromFile);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = new FileInputStream(file);
        Document docFromInputStream = DataUtil.load(inputStream, TEST_CHARSET, TEST_BASE_URI);
        assertEquals(doc, docFromInputStream);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        try {
            Document docFromInputStream = DataUtil.load(inputStream, "invalid_charset", TEST_BASE_URI);
            assertNull(docFromInputStream);
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        Document docFromInputStream = DataUtil.load(inputStream, null, TEST_BASE_URI);
        assertEquals(doc, docFromInputStream);
    }

    @Test
    public void testLoadInputStreamWithEmptyCharset() throws IOException {
        Document docFromInputStream = DataUtil.load(inputStream, "", TEST_BASE_URI);
        assertEquals(doc, docFromInputStream);
    }

    @Test
    public void testParseInputStream() throws IOException {
        Document docFromParsedInputStream = DataUtil.parseInputStream(inputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
        assertEquals(doc, docFromParsedInputStream);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        try {
            Document docFromParsedInputStream = DataUtil.parseInputStream(inputStream, "invalid_charset", TEST_BASE_URI, Parser.htmlParser());
            assertNull(docFromParsedInputStream);
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testParseInputStreamWithNullCharset() throws IOException {
        Document docFromParsedInputStream = DataUtil.parseInputStream(inputStream, null, TEST_BASE_URI, Parser.htmlParser());
        assertEquals(doc, docFromParsedInputStream);
    }

    @Test
    public void testParseInputStreamWithEmptyCharset() throws IOException {
        Document docFromParsedInputStream = DataUtil.parseInputStream(inputStream, "", TEST_BASE_URI, Parser.htmlParser());
        assertEquals(doc, docFromParsedInputStream);
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals(new String(outputStream.toByteArray(), TEST_CHARSET), doc.body().html());
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer bomBuffer = DataUtil.readToByteBuffer(inputStream);
        BomCharset bom = DataUtil.detectCharsetFromBom(bomBuffer);
        assertEquals("UTF-8", bom.charset);
        assertEquals(false, bom.offset);
    }

    @Test
    public void testDetectCharsetFromBom_InvalidBom() {
        ByteBuffer bomBuffer = DataUtil.readToByteBuffer(new FileInputStream(new File("invalid_bom.txt")));
        assertNull(DataUtil.detectCharsetFromBom(bomBuffer));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        String contentType = "text/html";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("utf-8"));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8 "));
        assertEquals("UTF-8", DataUtil.validateCharset("UTF-8\n"));
        assertNull(DataUtil.validateCharset("invalid-charset"));
        assertNull(DataUtil.validateCharset("INVALID-CHARSET"));
        assertNull(DataUtil.validateCharset("UTF-8\ninvalid-charset"));
        assertNull(DataUtil.validateCharset("UTF-8 invalid-charset"));
        assertNull(DataUtil.validateCharset("UTF-8\ninvalid-charset "));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        int length = boundary.length();
        assertEquals(boundary.charAt(0), mimeBoundaryChars[new Random().nextInt(mimeBoundaryChars.length)]);
        assertEquals(boundary.charAt(length - 1), mimeBoundaryChars[new Random().nextInt(mimeBoundaryChars.length)]);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer emptyByteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, emptyByteBuffer.limit());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        byte[] testBytes = new byte[1024];
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        for (int i = 0; i < testBytes.length && i < byteBuffer.limit(); i++) {
            assertEquals(testBytes[i], byteBuffer.get());
            byteBuffer.position(byteBuffer.position() + 1);
        }
    }

    @Test
    public void testReadToByteBuffer_ExceedMaxSize() throws IOException {
        byte[] testBytes = new byte[1025];
        InputStream inputStream = new ByteArrayInputStream(testBytes);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 1024);
        assertEquals(1024, byteBuffer.limit());
        for (int i = 0; i < 1024 && i < testBytes.length; i++) {
            assertEquals(testBytes[i], byteBuffer.get());
            byteBuffer.position(byteBuffer.position() + 1);
        }
    }

    @Test
    public void testReadToByteBuffer_Interrupted() {
        RandomAccessFile randomAccessFile = new RandomAccessFile(new File("test.html"), "r");
        randomAccessFile.getchannel().position(randomAccessFile.length() - 1);
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(randomAccessFile.inputStream(), 1024);
        randomAccessFile.close();

        assertEquals(1023, byteBuffer.limit());
        for (int i = 0; i < 1023; i++) {
            assertEquals(i + 1, byteBuffer.get());
            byteBuffer.position(byteBuffer.position() + 1);
        }
    }

    @Test
    public void testGetCharsetFromMetaTag() throws IOException {
        String metaTag = "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>";
        Document document = Jsoup.parse(metaTag);
        String charset = DataUtil.getCharsetFromMetaTag(document);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromMetaTag_NoCharset() throws IOException {
        String metaTag = "<meta http-equiv='Content-Type' content='text/html'>";
        Document document = Jsoup.parse(metaTag);
        assertNull(DataUtil.getCharsetFromMetaTag(document));
    }

    @Test
    public void testGetCharsetFromMetaTag_InvalidCharset() throws IOException {
        String metaTag = "<meta http-equiv='Content-Type' content='text/html; charset=invalid'>";
        Document document = Jsoup.parse(metaTag);
        assertNull(DataUtil.getCharsetFromMetaTag(document));
    }

    @Test
    public void testGetCharsetFromMetaTag_CharsetInLowerCase() throws IOException {
        String metaTag = "<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>";
        Document document = Jsoup.parse(metaTag);
        assertEquals("UTF-8", DataUtil.getCharsetFromMetaTag(document));
    }

    @Test
    public void testGetCharsetFromMetaTag_CharsetInQuotes() throws IOException {
        String metaTag = "<meta http-equiv='Content-Type' content='text/html; charset=\"UTF-8\">";
        Document document = Jsoup.parse(metaTag);
        assertEquals("UTF-8", DataUtil.getCharsetFromMetaTag(document));
    }

    @Test
    public void testGetCharsetFromMetaTag_NoMetaTag() throws IOException {
        String html = "<html><body>";
        Document document = Jsoup.parse(html);
        assertNull(DataUtil.getCharsetFromMetaTag(document));
    }

    @Test
    public void testGetCharsetFromMetaTag_MultipleMetaTags() throws IOException {
        String html = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'><meta http-equiv='Content-Type' content='text/html; charset=UTF-16'></head><body>";
        Document document = Jsoup.parse(html);
        assertEquals("UTF-8", DataUtil.getCharsetFromMetaTag(document));
    }

    @Test
    public void testGetCharsetFromMetaTag_MetaTagWithInvalidCharset() throws IOException {
        String html = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=invalid'></head><body>";
        Document document = Jsoup.parse(html);
        assertNull(DataUtil.getCharsetFromMetaTag(document));
    }

    @Test
    public void testGetCharsetFromMetaTag_MetaTagWithMissingCharset() throws IOException {
        String html = "<html><head><meta http-equiv='Content-Type' content='text/html'></head><body>";
        Document document = Jsoup.parse(html);
        assertNull(DataUtil.getCharsetFromMetaTag(document));
    }

    @Test(expected = NullPointerException.class)
    public void testGetCharsetFromMetaTag_NullDocument() {
        String charset = DataUtil.getCharsetFromMetaTag(null);
    }
}