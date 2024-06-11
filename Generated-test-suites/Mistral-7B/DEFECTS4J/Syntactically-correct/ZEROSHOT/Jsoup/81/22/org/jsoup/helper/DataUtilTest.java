package org.jsoup.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataUtil.class)
public class DataUtilTest {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String HTML_DOCUMENT = "<html></html>";
    private static final String XML_DOCUMENT = "<xml></xml>";
    private static final String META_CHARSET_TAG = "meta charset=\"UTF-8\"";
    private static final String META_CONTENT_TYPE_TAG = "meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"";

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, "UTF-8");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(HTML_DOCUMENT.getBytes());
        Document document = DataUtil.load(inputStream, null);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(HTML_DOCUMENT.getBytes());
        Document document = DataUtil.load(inputStream, "UTF-8");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(XML_DOCUMENT.getBytes());
        Document document = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(HTML_DOCUMENT.getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertNotNull(outputStream.toByteArray());
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String testData = "Test Data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, null);
        assertNotNull(document);
        assertTrue(document.text().equals(testData));
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String testData = "Test Data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8");
        assertNotNull(document);
        assertTrue(document.text().equals(testData));
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String testData = "Test Data";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes());
        Document document = DataUtil.parseInputStream(inputStream, null, null, DataUtil.Parser.xmlParser());
        assertNotNull(document);
        assertTrue(document.text().equals(testData));
    }

    @Test
    public void testDetectCharsetFromBOM() throws IOException {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{0x00, 0x00, (byte) 0xFE, (byte) 0xFF});
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertTrue(bomCharset.charset.equals("UTF-32"));
        assertFalse(bomCharset.offset);

        byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xFE, 0x00, 0x00});
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertTrue(bomCharset.charset.equals("UTF-16"));
        assertFalse(bomCharset.offset);

        byteData = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF});
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertTrue(bomCharset.charset.equals("UTF-8"));
        assertTrue(bomCharset.offset);

        byteData = ByteBuffer.wrap(new byte[]{0xFE, 0xFF});
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);

        byteData = ByteBuffer.wrap(new byte[]{0xFF, 0xFE});
        bomCharset = DataUtil.detectCharsetFromBom(byteData);
        assertNull(bomCharset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertTrue(charset.equals("UTF-8"));

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);
        assertTrue(validatedCharset.equals(charset));

        charset = "UTF-16";
        validatedCharset = DataUtil.validateCharset(charset);
        assertTrue(validatedCharset.equals(charset));

        charset = "UTF-16BE";
        validatedCharset = DataUtil.validateCharset(charset);
        assertTrue(validatedCharset.equals(charset));

        charset = "UTF-32";
        validatedCharset = DataUtil.validateCharset(charset);
        assertTrue(validatedCharset.equals(charset));

        charset = "ISO-8859-1";
        validatedCharset = DataUtil.validateCharset(charset);
        assertTrue(validatedCharset.equals(charset));

        charset = null;
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);

        charset = "";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);

        charset = " invalid charset ";
        validatedCharset = DataUtil.validateCharset(charset);
        assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        assertTrue(mimeBoundary.length() >= 32);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        PowerMockito.mockStatic(Charset.class);
        when(Charset.isSupported("UTF-16BE")).thenReturn(false);

        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, "UTF-16BE");

        assertNotNull(document);
        assertTrue(document.outputSettings().charset().equals(DEFAULT_CHARSET));
    }
}