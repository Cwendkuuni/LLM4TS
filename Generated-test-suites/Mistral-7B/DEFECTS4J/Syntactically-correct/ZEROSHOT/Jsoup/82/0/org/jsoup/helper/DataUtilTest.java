package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.HelperException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DataUtilTest {

    private Document document;

    @Before
    public void setUp() throws IOException {
        String html = "<html><head>" +
                "<meta charset=\"UTF-8\">" +
                "</head><body>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=gb2312\">" +
                "</body></html>";
        document = Jsoup.parse(html, Parser.htmlParser());
    }

    @After
    public void tearDown() {
        if (document != null) {
            document.close();
        }
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertEquals(document.html(), doc.html());
        doc.close();
        file.delete(); // clean up
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String html = "<html><head>" +
                "<meta charset=\"UTF-8\">" +
                "</head><body>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=gb2312\">" +
                "</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals(document.html(), doc.html());
        doc.close();

        Element metaElement = document.select("meta[http-equiv=content-type], meta[charset]").first();
        assertEquals("text/html;charset=gb2312", metaElement.attr("content"));

        doc = DataUtil.load(inputStream, null, null);
        assertEquals(document.html(), doc.html());
        doc.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head>" +
                "<meta charset=\"UTF-8\">" +
                "</head><body>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=gb2312\">" +
                "</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertEquals(document.html(), doc.html());
        doc.close();

        Element metaElement = document.select("meta[http-equiv=content-type], meta[charset]").first();
        assertEquals("text/html;charset=gb2312", metaElement.attr("content"));

        doc = DataUtil.parseInputStream(inputStream, null, null, Parser.xmlParser());
        assertEquals(document.html(), doc.html()); // xml parser is not used for parsing HTML files
        doc.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        inputStream.close();
        byte[] expected = "<html><head><meta charset=\"UTF-8\"></head><body><meta http-equiv=\"Content-Type\" content=\"text/html;charset=gb2312\">".getBytes(StandardCharsets.UTF_8);
        byte[] actual = outputStream.toByteArray();
        assertArrayEquals(expected, actual);
        outputStream.close();
    }

    @Test(expected = HelperException.class)
    public void testInvalidCharset() throws IOException {
        DataUtil.load(new File("test.html"), " invalid_charset ", null);
    }

    @Test
    public void testDetectCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";
        String detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("EUC-JP", detectedCharset);

        contentType = "text/html";
        detectedCharset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(detectedCharset);
    }

    @Test(expected = HelperException.class)
    public void testInvalidCharsetFromContentType() {
        String html = "<html><head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=invalid_charset\">" +
                "</head><body></body></html>";
        Document doc = Jsoup.parse(html, Parser.htmlParser());
        assertEquals(0, doc.select("meta[http-equiv=content-type], meta[charset]").size());
        doc.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String input = "Hello World!";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);
        assertEquals(input.length(), byteBuffer.limit());
        byteBuffer.flip();
        byte[] expected = input.getBytes(StandardCharsets.UTF_8);
        byte[] actual = new byte[expected.length];
        byteBuffer.get(actual);
        assertArrayEquals(expected, actual);
        byteBuffer.clear();
        inputStream.close();
    }

    @Test(expected = HelperException.class)
    public void testReadToByteBufferWithNegativeSize() throws IOException {
        DataUtil.readToByteBuffer(null, -1);
    }

    @Test(expected = HelperException.class)
    public void testReadToByteBufferWithSizeLessThanZero() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Hello World!".getBytes(StandardCharsets.UTF_8));
        DataUtil.readToByteBuffer(inputStream, -1);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        String expected = new String(new Random().getInts(boundaryLength, new Random().nextInt(mimeBoundaryChars.length) + mimeBoundaryChars.length)
                .mapToObj(i -> String.valueOf(mimeBoundaryChars[i % mimeBoundaryChars.length])).collect(Collectors.joining()));
        assertEquals(expected, boundary);
    }
}