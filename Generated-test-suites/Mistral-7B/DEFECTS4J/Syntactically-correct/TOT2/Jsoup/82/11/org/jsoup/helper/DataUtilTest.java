import org.jsoup.helper.DataUtil;
import org.jsoup.helper.ConverterUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import static org.mockito.Mockito.mock;

public class DataUtilTest {

    private static final String HTML_WITH_UTF8_META_CHARSET = "<?xml version='1.0' encoding='UTF-8'?>\n" +
            "<html><head><meta charset='UTF-8' /></head><body><h1>Hello World</h1></body></html>";
    private static final String HTML_WITH_GBK_META_CHARSET = "<?xml version='1.0' encoding='gbk'?>\n" +
            "<html><head><meta charset='gbk' /></head><body><h1>你好世界</h1></body></html>";
    private static final String HTML_WITH_NO_META_CHARSET = "<html><head></head><body><h1>Hello World</h1></body></html>";
    private static final String HTML_WITH_INVALID_META_CHARSET = "<html><head><meta charset='invalid' /></head><body><h1>Hello World</h1></body></html>";
    private static final String HTML_WITH_XML_DECLARATION = "<?xml version='1.0' encoding='UTF-8'?>\n" +
            "<html><head></head><body><h1>Hello World</h1></body></html>";
    private static final String HTML_WITH_XML_DECLARATION_INVALID_ENCODING = "<?xml version='1.0' encoding='invalid'?>\n" +
            "<html><head></head><body><h1>Hello World</h1></body></html>";
    private static final String HTML_WITH_MIME_BOUNDARY = "--_boundary_1234567890--\nContent-Type: text/html\nContent-Disposition: attachment; filename=\"test.html\"\n\n<html><head></head><body><h1>Hello World</h1></body></html>\n--_boundary_1234567890--";

    private Document htmlWithUtf8MetaCharset;
    private Document htmlWithGbkMetaCharset;
    private Document htmlWithNoMetaCharset;
    private Document htmlWithInvalidMetaCharset;
    private Document htmlWithXmlDeclaration;
    private Document htmlWithXmlDeclarationInvalidEncoding;
    private Document htmlWithMimeBoundary;
    private InputStream inputStreamWithUtf8;
    private InputStream inputStreamWithGbk;
    private InputStream inputStreamWithInvalidCharset;
    private InputStream inputStreamWithMimeBoundary;
    private File fileWithUtf8;
    private File fileWithGbk;
    private File fileWithInvalidCharset;
    private File fileWithMimeBoundary;

    @Before
    public void setUp() throws IOException {
        htmlWithUtf8MetaCharset = DataUtil.parseString(HTML_WITH_UTF8_META_CHARSET);
        htmlWithGbkMetaCharset = DataUtil.parseString(HTML_WITH_GBK_META_CHARSET);
        htmlWithNoMetaCharset = DataUtil.parseString(HTML_WITH_NO_META_CHARSET);
        htmlWithInvalidMetaCharset = DataUtil.parseString(HTML_WITH_INVALID_META_CHARSET);
        htmlWithXmlDeclaration = DataUtil.parseString(HTML_WITH_XML_DECLARATION);
        htmlWithXmlDeclarationInvalidEncoding = DataUtil.parseString(HTML_WITH_XML_DECLARATION_INVALID_ENCODING);
        htmlWithMimeBoundary = DataUtil.parseString(HTML_WITH_MIME_BOUNDARY);

        inputStreamWithUtf8 = new ByteArrayInputStream(HTML_WITH_UTF8_META_CHARSET.getBytes(StandardCharsets.UTF_8));
        inputStreamWithGbk = new ByteArrayInputStream(HTML_WITH_GBK_META_CHARSET.getBytes(StandardCharsets.GBK));
        inputStreamWithInvalidCharset = new ByteArrayInputStream(HTML_WITH_INVALID_META_CHARSET.getBytes(StandardCharsets.UTF_8));
        inputStreamWithMimeBoundary = new ByteArrayInputStream(HTML_WITH_MIME_BOUNDARY.getBytes(StandardCharsets.UTF_8));

        fileWithUtf8 = new File("fileWithUtf8.html");
        fileWithGbk = new File("fileWithGbk.html");
        fileWithInvalidCharset = new File("fileWithInvalidCharset.html");
        fileWithMimeBoundary = new File("fileWithMimeBoundary.html");

        ConverterUtil.saveDocument(htmlWithUtf8MetaCharset, fileWithUtf8, StandardCharsets.UTF_8.toString());
        ConverterUtil.saveDocument(htmlWithGbkMetaCharset, fileWithGbk, StandardCharsets.GBK.toString());
        ConverterUtil.saveDocument(htmlWithInvalidMetaCharset, fileWithInvalidCharset, StandardCharsets.UTF_8.toString());
        ConverterUtil.saveDocument(htmlWithMimeBoundary, fileWithMimeBoundary, StandardCharsets.UTF_8.toString());
    }

    @Test
    public void testLoadFileWithUtf8MetaCharset() throws IOException {
        Document actual = DataUtil.load(fileWithUtf8, null, null);
        Document expected = htmlWithUtf8MetaCharset;

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.title(), actual.title());
        Assert.assertEquals(expected.htmlText(), actual.htmlText());
    }

    @Test
    public void testLoadFileWithGbkMetaCharset() throws IOException {
        Document actual = DataUtil.load(fileWithGbk, null, null);
        Document expected = htmlWithGbkMetaCharset;

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.title(), actual.title());
        Assert.assertEquals(expected.htmlText(), actual.htmlText());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidMetaCharset() throws IOException {
        DataUtil.load(fileWithInvalidCharset, null, null);
    }

    @Test
    public void testLoadInputStreamWithUtf8() throws IOException {
        InputStream inputStream = inputStreamWithUtf8;
        Document actual = DataUtil.load(inputStream, null, null);
        Document expected = htmlWithUtf8MetaCharset;

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.title(), actual.title());
        Assert.assertEquals(expected.htmlText(), actual.htmlText());
        inputStream.close();
    }

    @Test
    public void testLoadInputStreamWithGbk() throws IOException {
        InputStream inputStream = inputStreamWithGbk;
        Document actual = DataUtil.load(inputStream, null, null);
        Document expected = htmlWithGbkMetaCharset;

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.title(), actual.title());
        Assert.assertEquals(expected.htmlText(), actual.htmlText());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        Document actual = DataUtil.load(inputStreamWithInvalidCharset, null, null);
        Assert.fail("Expected IOException");
    }

    @Test
    public void testLoadInputStreamWithMimeBoundary() throws IOException {
        InputStream inputStream = inputStreamWithMimeBoundary;
        Document actual = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        Document expected = htmlWithMimeBoundary;

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.title(), actual.title());
        Assert.assertEquals(expected.htmlText(), actual.htmlText());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithUtf8() throws IOException {
        InputStream inputStream = inputStreamWithUtf8;
        Document actual = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Document expected = htmlWithUtf8MetaCharset;

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.title(), actual.title());
        Assert.assertEquals(expected.htmlText(), actual.htmlText());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithGbk() throws IOException {
        InputStream inputStream = inputStreamWithGbk;
        Document actual = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Document expected = htmlWithGbkMetaCharset;

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.title(), actual.title());
        Assert.assertEquals(expected.htmlText(), actual.htmlText());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        Document actual = DataUtil.parseInputStream(inputStreamWithInvalidCharset, null, null, Parser.htmlParser());
        Assert.fail("Expected IOException");
    }

    @Test
    public void testParseInputStreamWithMimeBoundary() throws IOException {
        InputStream inputStream = inputStreamWithMimeBoundary;
        Document actual = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        Document expected = htmlWithMimeBoundary;

        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.title(), actual.title());
        Assert.assertEquals(expected.htmlText(), actual.htmlText());
        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBomUtf32() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x00, 0x00, 0xFE, 0xFF});
        String actual = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 4));
        String expected = "UTF-32";

        Assert.assertEquals(expected, actual);
        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBomUtf16() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0xFE, 0xFF, 0x00, 0x00});
        String actual = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 4));
        String expected = "UTF-16";

        Assert.assertEquals(expected, actual);
        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBomUtf8() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0xEF, 0xBB, 0xBF});
        String actual = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 4));
        String expected = "UTF-8";

        Assert.assertEquals(expected, actual);
        inputStream.close();
    }

    @Test
    public void testDetectCharsetFromBomInvalid() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0xFF, 0xEE, 0xA1, 0xAB});
        String actual = DataUtil.detectCharsetFromBom(DataUtil.readToByteBuffer(inputStream, 4));
        Assert.assertNull(actual);
        inputStream.close();
    }

    @Test
    public void testMimeBoundary() {
        String actual = DataUtil.mimeBoundary();
        String expected = "--_boundary_1234567890";

        Assert.assertEquals(expected, actual);
    }
}