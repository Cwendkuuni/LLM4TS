package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    private static final String HTML_UTF8 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<html xmlns:html=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
            "</head>\n" +
            "<body>\n" +
            "<h1>Hello, World!</h1>\n" +
            "</body>\n" +
            "</html>";

    private static final String HTML_ISO_8859_1 = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
            "<html xmlns:html=\"http://www.w3.org/1999/xhtml\">\n" +
            "<head>\n" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
            "</head>\n" +
            "<body>\n" +
            "<h1>Hello, World!</h1>\n" +
            "</body>\n" +
            "</html>";

    private Document utf8Document;
    private Document iso88591Document;

    @Before
    public void setUp() throws IOException {
        utf8Document = DataUtil.load(new File("test.html"), StandardCharsets.UTF_8.name(), null);
        iso88591Document = DataUtil.load(new File("test_iso-8859-1.html"), null, null);
    }

    @Test
    public void testLoadDocumentWithSpecifiedCharset() throws IOException {
        byte[] utf8DocumentBytes = utf8Document.body().html().getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(utf8DocumentBytes);
        Document loadedDocument = DataUtil.load(inputStream, StandardCharsets.UTF_8.name(), null);
        Assert.assertEquals(utf8Document, loadedDocument);
    }

    @Test
    public void testLoadDocumentWithoutSpecifiedCharset() throws IOException {
        byte[] iso88591DocumentBytes = iso88591Document.body().html().getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(iso88591DocumentBytes);
        Document loadedDocument = DataUtil.load(inputStream, null, null);
        Assert.assertEquals(iso88591Document, loadedDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadDocumentWithInvalidCharset() throws IOException {
        byte[] utf8DocumentBytes = utf8Document.body().html().getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(utf8DocumentBytes);
        DataUtil.load(inputStream, "Invalid_Charset", null);
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream utf8InputStream = new ByteArrayInputStream(HTML_UTF8.getBytes(StandardCharsets.UTF_8));
        Document loadedDocument = DataUtil.parseInputStream(utf8InputStream, StandardCharsets.UTF_8.name(), null, Parser.htmlParser());
        Assert.assertEquals(utf8Document, loadedDocument);

        InputStream iso88591InputStream = new ByteArrayInputStream(HTML_ISO_8859_1.getBytes(StandardCharsets.UTF_8));
        Document loadedDocumentIso88591 = DataUtil.parseInputStream(iso88591InputStream, null, null, Parser.htmlParser());
        Assert.assertEquals(iso88591Document, loadedDocumentIso88591);

        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        Document loadedDocumentEmpty = DataUtil.parseInputStream(emptyInputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(loadedDocumentEmpty);
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        InputStream utf8InputStream = new ByteArrayInputStream(HTML_UTF8.getBytes(StandardCharsets.UTF_8));
        DataUtil.parseInputStream(utf8InputStream, "Invalid_Charset", null, Parser.htmlParser());
    }
}