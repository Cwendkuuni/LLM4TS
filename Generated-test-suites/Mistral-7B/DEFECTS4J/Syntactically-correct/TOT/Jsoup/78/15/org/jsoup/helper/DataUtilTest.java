package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFile() throws IOException {
        String filePath = "path/to/your/test/file.html";
        File file = new File(filePath);
        Document document = DataUtil.load(file, "UTF-8", "");
        Assert.assertNotNull(document);

        // Test with empty file
        File emptyFile = new File("path/to/empty/file.html");
        Document emptyDocument = DataUtil.load(emptyFile, "UTF-8", "");
        Assert.assertNotNull(emptyDocument);
        Assert.assertTrue(emptyDocument.content().isEmpty());
    }

    @Test
    public void testLoadDocumentFromInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset='UTF-8'></head><body>Test</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.body().text());

        // Test with invalid charset
        ByteArrayInputStream inputStreamWithWrongCharset = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.ISO_8859_1));
        Document documentWithWrongCharset = null;
        try {
            documentWithWrongCharset = DataUtil.load(inputStreamWithWrongCharset, "ISO-8859-1", "");
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(documentWithWrongCharset);
    }

    @Test
    public void testParseInputStreamWithContentType() throws IOException {
        String htmlContent = "<html><head><meta charset='UTF-8'></head><body>Test</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "text/html; charset=UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.body().text());

        // Test with invalid content type
        ByteArrayInputStream inputStreamWithWrongContentType = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document documentWithWrongContentType = null;
        try {
            documentWithWrongContentType = DataUtil.parseInputStream(inputStreamWithWrongContentType, "text/plain", "", Parser.htmlParser());
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(documentWithWrongContentType);
    }

    @Test
    public void testParseInputStreamWithMetaTag() throws IOException {
        String htmlContent = "<html><head><meta charset='gb2312'></head><body>Test</body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(inputStream, "", "", Parser.htmlParser());
        Assert.assertNotNull(document);
        Assert.assertEquals("Test", document.body().text());

        // Test with missing meta tag
        String htmlContentWithoutMetaTag = "<html><body>Test</body></html>";
        ByteArrayInputStream inputStreamWithoutMetaTag = new ByteArrayInputStream(htmlContentWithoutMetaTag.getBytes(StandardCharsets.UTF_8));
        Document documentWithoutMetaTag = DataUtil.parseInputStream(inputStreamWithoutMetaTag, "", "", Parser.htmlParser());
        Assert.assertNotNull(documentWithoutMetaTag);
        Assert.assertEquals("Test", documentWithoutMetaTag.body().text());
    }
}