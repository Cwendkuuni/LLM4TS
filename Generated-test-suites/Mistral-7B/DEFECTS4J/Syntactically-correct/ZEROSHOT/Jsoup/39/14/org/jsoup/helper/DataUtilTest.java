package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileNoSuchFile() throws IOException {
        File inputFile = new File("nonexistentfile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        DataUtil.load(inputFile, charsetName, baseUri);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testLoadInputStreamDefaultCharset() throws IOException {
        String html = "<html><head></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("ISO-8859-1")));
        String charsetName = null;
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.body().text());
    }

    @Test
    public void testLoadInputStreamDetectCharset() throws IOException {
        String html = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(Charset.forName("UTF-8")));
        String charsetName = null;
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        assertEquals("Test", doc.body().text());
        assertEquals("ISO-8859-1", doc.outputSettings().charset());
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        String html1 = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><body>Test 1</body></html>";
        ByteBuffer byteData1 = ByteBuffer.wrap(html1.getBytes(Charset.forName("UTF-8")));
        Document doc1 = DataUtil.parseByteData(byteData1, charsetName, baseUri, parser);
        assertNotNull(doc1);
        assertEquals("Test 1", doc1.body().text());

        String charsetNameNull = null;
        String html2 = "<html><head></head><body>Test 2</body></html>";
        ByteBuffer byteData2 = ByteBuffer.wrap(html2.getBytes(Charset.forName("UTF-8")));
        Document doc2 = DataUtil.parseByteData(byteData2, charsetNameNull, baseUri, parser);
        assertNotNull(doc2);
        assertEquals("Test 2", doc2.body().text());
        assertEquals("UTF-8", doc2.outputSettings().charset());

        String html3 = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'></head><body>Test 3</body></html>";
        ByteBuffer byteData3 = ByteBuffer.wrap(html3.getBytes(Charset.forName("UTF-8")));
        Document doc3 = DataUtil.parseByteData(byteData3, charsetNameNull, baseUri, parser);
        assertNotNull(doc3);
        assertEquals("Test 3", doc3.body().text());
        assertEquals("ISO-8859-1", doc3.outputSettings().charset());

        String html4 = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'></head><body>Test 4\uD800\uDC00</body></html>";
        ByteBuffer byteData4 = ByteBuffer.wrap(html4.getBytes(Charset.forName("UTF-8")));
        Document doc4 = DataUtil.parseByteData(byteData4, charsetName, baseUri, parser);
        assertNotNull(doc4);
        assertEquals("Test 4\uD800\uDC00", doc4.body().text());
        assertEquals("UTF-8", doc4.outputSettings().charset());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String charsetName = "UTF-8";
        InputStream inputStream = new ByteArrayInputStream("Test".getBytes(Charset.forName("UTF-8")));
        int maxSize = 10;

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertEquals(10, byteData.limit());
        assertEquals("Test", Charset.forName(charsetName).decode(byteData).toString());

        maxSize = -1;
        byteData = DataUtil.readToByteBuffer(inputStream, maxSize);
        assertEquals(-1, byteData.limit());
        assertEquals("Test", Charset.forName(charsetName).decode(byteData).toString());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        assertEquals("UTF-8", charset1);

        String contentType2 = "text/html";
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        assertNull(charset2);

        String contentType3 = "text/html; charset=EUC-JP";
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);
        assertEquals("EUC-JP", charset3);

        String contentType4 = "text/html; charset=ISO-8859-1";
        String charset4 = DataUtil.getCharsetFromContentType(contentType4);
        assertEquals("ISO-8859-1", charset4);
    }
}