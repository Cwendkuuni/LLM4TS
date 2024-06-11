import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html");
        Document document = DataUtil.load(file, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        Document document = DataUtil.load(file, charset);
        Assert.assertNotNull(document);
        Assert.assertEquals(charset, document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String invalidCharset = "invalidCharset";
        Document document = DataUtil.load(file, invalidCharset);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        Document document = DataUtil.load(inputStream, charset);
        Assert.assertNotNull(document);
        Assert.assertEquals(charset, document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String invalidCharset = "invalidCharset";
        Document document = DataUtil.load(inputStream, invalidCharset);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Parser parser = DataUtil.parser().xmlParser();
        Document document = DataUtil.load(inputStream, null, null, parser);
        Assert.assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String source = "Source Data";
        String target = "Target Data";
        byte[] buffer = new byte[1024 * 32];

        ByteArrayInputStream inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        String result = outputStream.toString(StandardCharsets.UTF_8.name());
        Assert.assertEquals(source, result);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, null, null, DataUtil.parser().htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);
        Assert.assertNotNull(byteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertEquals("UTF-8", charset);

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertEquals("UTF-8", validatedCharset);

        charset = "invalidCharset";
        validatedCharset = DataUtil.validateCharset(charset);
        Assert.assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), 32);
    }

    @Test
    public void testDetectCharsetFromBom() {
        String bomUtf8 = "\uFEFF";
        ByteBuffer byteDataUtf8 = ByteBuffer.wrap(bomUtf8.getBytes(StandardCharsets.UTF_8));
        BomCharset bomUtf8Charset = DataUtil.detectCharsetFromBom(byteDataUtf8);
        Assert.assertEquals("UTF-8", bomUtf8Charset.charset);
        Assert.assertFalse(bomUtf8Charset.offset);

        String bomUtf16BE = "\uFEFF\uFFFE";
        ByteBuffer byteDataUtf16BE = ByteBuffer.wrap(bomUtf16BE.getBytes(StandardCharsets.UTF_16BE));
        BomCharset bomUtf16BECharset = DataUtil.detectCharsetFromBom(byteDataUtf16BE);
        Assert.assertEquals("UTF-16BE", bomUtf16BECharset.charset);
        Assert.assertTrue(bomUtf16BECharset.offset);

        String bomUtf16LE = "\uFFFE\uFEFF";
        ByteBuffer byteDataUtf16LE = ByteBuffer.wrap(bomUtf16LE.getBytes(StandardCharsets.UTF_16LE));
        BomCharset bomUtf16LECharset = DataUtil.detectCharsetFromBom(byteDataUtf16LE);
        Assert.assertEquals("UTF-16LE", bomUtf16LECharset.charset);
        Assert.assertTrue(bomUtf16LECharset.offset);

        String bomUTF32 = "\uFEFF\uFFFE\u0000\u0000";
        ByteBuffer byteDataUTF32 = ByteBuffer.wrap(bomUTF32.getBytes(StandardCharsets.UTF_32BE));
        BomCharset bomUTF32Charset = DataUtil.detectCharsetFromBom(byteDataUTF32);
        Assert.assertEquals("UTF-32BE", bomUTF32Charset.charset);
        Assert.assertTrue(bomUTF32Charset.offset);

        String empty = "";
        ByteBuffer byteDataEmpty = ByteBuffer.allocate(0);
        BomCharset bomEmptyCharset = DataUtil.detectCharsetFromBom(byteDataEmpty);
        Assert.assertNull(bomEmptyCharset);
    }

    private static Parser parser() {
        return new Parser();
    }
}