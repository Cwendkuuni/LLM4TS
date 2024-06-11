package org.jsoup.helper.test;

import org.jsoup.DataUtil;
import org.jsoup.helper.DataUtilTest;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class DataUtilTest {

    private static final String HTML_UTF8 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n" +
            "<head><meta charset=\"UTF-8\"/></head>\n" +
            "<body>\n" +
            "<p>Test text</p>\n" +
            "</body>\n" +
            "</html>";

    private static final String HTML_ISO_8859_1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
            "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n" +
            "<head><meta charset=\"ISO-8859-1\"/></head>\n" +
            "<body>\n" +
            "<p>Test text</p>\n" +
            "</body>\n" +
            "</html>";

    private static final String HTML_INVALID_CHARSET = "<?xml version=\"1.0\" encoding=\"InvalidCharset\"?>\n" +
            "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n" +
            "<head><meta charset=\"InvalidCharset\"/></head>\n" +
            "<body>\n" +
            "<p>Test text</p>\n" +
            "</body>\n" +
            "</html>";

    private static final String HTML_NO_META_CHARSET = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n" +
            "<head></head>\n" +
            "<body>\n" +
            "<p>Test text</p>\n" +
            "</body>\n" +
            "</html>";

    private static final String HTML_NO_BOM = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<html xmlns:html=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n" +
            "<head><meta charset=\"UTF-8\"/></head>\n" +
            "<body>\n" +
            "<p>Test text</p>\n" +
            "</body>\n" +
            "</html>";

    private Document utf8Document;
    private Document iso88591Document;
    private Document invalidCharsetDocument;
    private Document noMetaCharsetDocument;
    private Document noBomDocument;

    @Before
    public void setup() throws IOException {
        utf8Document = DataUtil.load(new ByteArrayInputStream(HTML_UTF8.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8.name(), null);
        iso88591Document = DataUtil.load(new ByteArrayInputStream(HTML_ISO_8859_1.getBytes(StandardCharsets.ISO_8859_1)), StandardCharsets.ISO_8859_1.name(), null);
        invalidCharsetDocument = DataUtil.load(new ByteArrayInputStream(HTML_INVALID_CHARSET.getBytes(StandardCharsets.UTF_8)), null, null);
        noMetaCharsetDocument = DataUtil.load(new ByteArrayInputStream(HTML_NO_META_CHARSET.getBytes(StandardCharsets.UTF_8)), null, null);
        noBomDocument = DataUtil.load(new ByteArrayInputStream(HTML_NO_BOM.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8.name(), null);
    }

    @Test
    public void testLoadWithUtf8Charset() throws IOException {
        Document document = DataUtil.load(new ByteArrayInputStream(HTML_UTF8.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8.name(), null);
        assertNotNull(document);
        assertEquals(utf8Document.body().text(), document.body().text());
    }

    @Test
    public void testLoadWithIso88591Charset() throws IOException {
        Document document = DataUtil.load(new ByteArrayInputStream(HTML_ISO_8859_1.getBytes(StandardCharsets.ISO_8859_1)), StandardCharsets.ISO_8859_1.name(), null);
        assertNotNull(document);
        assertEquals(iso88591Document.body().text(), document.body().text());
    }

    @Test(expected = IOException.class)
    public void testLoadWithInvalidCharset() throws IOException {
        Document document = DataUtil.load(new ByteArrayInputStream(HTML_INVALID_CHARSET.getBytes(StandardCharsets.UTF_8)), null, null);
        fail("Expected IOException");
    }

    @Test
    public void testLoadWithNoMetaCharset() throws IOException {
        Document document = DataUtil.load(new ByteArrayInputStream(HTML_NO_META_CHARSET.getBytes(StandardCharsets.UTF_8)), null, null);
        assertNotNull(document);
        assertEquals(noMetaCharsetDocument.body().text(), document.body().text());
    }

    @Test
    public void testLoadWithNoBom() throws IOException {
        Document document = DataUtil.load(new ByteArrayInputStream(HTML_NO_BOM.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8.name(), null);
        assertNotNull(document);
        assertEquals(noBomDocument.body().text(), document.body().text());
    }

    @Test
    public void testParseInputStreamWithUtf8Charset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(HTML_UTF8.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, StandardCharsets.UTF_8.name(), null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(utf8Document.body().text(), document.body().text());
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithIso88591Charset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(HTML_ISO_8859_1.getBytes(StandardCharsets.ISO_8859_1));
        Document document = DataUtil.parseInputStream(inputStream, StandardCharsets.ISO_8859_1.name(), null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(iso88591Document.body().text(), document.body().text());
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(HTML_INVALID_CHARSET.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        fail("Expected IOException");
        inputStream.close();
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(HTML_NO_BOM.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(noBomDocument.body().text(), document.body().text());
        inputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test data".getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        assertEquals("Test data", outputStream.toString(StandardCharsets.UTF_8.name()));
        inputStream.close();
        outputStream.close();
    }
}