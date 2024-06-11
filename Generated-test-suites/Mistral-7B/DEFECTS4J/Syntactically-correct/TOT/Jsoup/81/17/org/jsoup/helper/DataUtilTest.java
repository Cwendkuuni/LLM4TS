package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, "UTF-8", "");
        // Test if the document is not null
        Assert.assertNotNull(document);
        // Test if the document content is correct
        String expectedContent = "Expected content of the file";
        Assert.assertEquals(expectedContent, document.body().text());
        // Test if the charset is correctly detected from the file
        String detectedCharset = document.outputSettings().charset();
        Assert.assertEquals("UTF-8", detectedCharset);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Expected content of the input stream</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        // Test if the document is not null
        Assert.assertNotNull(document);
        // Test if the document content is correct
        Assert.assertEquals("Expected content of the input stream", document.body().text());
        // Test if the charset is correctly detected from the input stream meta tag
        Assert.assertEquals("UTF-8", document.outputSettings().charset());

        // Test if the document content is correct when the input stream does not contain a charset meta tag
        String htmlContentWithoutMetaTag = "<html><body><p>Expected content of the input stream without meta tag</p></body></html>";
        InputStream inputStreamWithoutMetaTag = new ByteArrayInputStream(htmlContentWithoutMetaTag.getBytes("UTF-8"));
        Document documentWithoutMetaTag = DataUtil.load(inputStreamWithoutMetaTag, null, "");
        // Test if the document content is correct
        Assert.assertEquals("Expected content of the input stream without meta tag", documentWithoutMetaTag.body().text());
        // Test if the default charset is used when no charset is provided
        Assert.assertEquals("UTF-8", documentWithoutMetaTag.outputSettings().charset());
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Expected content of the input stream</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        // Test if the document is not null
        Assert.assertNotNull(document);
        // Test if the document content is correct
        Assert.assertEquals("Expected content of the input stream", document.body().text());
        // Test if the charset is correctly determined from the input stream
        Assert.assertEquals("UTF-8", document.outputSettings().charset());

        // Test if the document content is correct when the input stream contains a BOM
        String bomHtmlContent = "\uFEFF<html><head><meta charset=\"UTF-8\"></head><body><p>Expected content of the input stream with BOM</p></body></html>";
        InputStream bomInputStream = new ByteArrayInputStream(bomHtmlContent.getBytes("UTF-8"));
        Document bomDocument = DataUtil.parseInputStream(bomInputStream, null, "", Parser.htmlParser());
        // Test if the document content is correct
        Assert.assertEquals("Expected content of the input stream with BOM", bomDocument.body().text());
        // Test if the UTF-8 charset is used when a BOM is detected
        Assert.assertEquals("UTF-8", bomDocument.outputSettings().charset());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceContent = "Source content";
        String targetContent = "Target content";
        InputStream sourceInputStream = new ByteArrayInputStream(sourceContent.getBytes("UTF-8"));
        OutputStream targetOutputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(sourceInputStream, targetOutputStream);
        byte[] targetBytes = targetOutputStream.toByteArray();
        // Test if the target content is equal to the source content
        Assert.assertEquals(sourceContent, new String(targetBytes, "UTF-8"));
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharsetName() throws IOException {
        File inputFile = new File("test.html");
        Document document = DataUtil.load(inputFile, "InvalidCharsetName", "");
        // The test should fail with an IOException due to an invalid charset name
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidCharsetName() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><p>Expected content of the input stream</p></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = null;
        try {
            document = DataUtil.parseInputStream(inputStream, "InvalidCharsetName", "", Parser.htmlParser());
        } catch (IOException e) {
            // The test should succeed and catch the IOException
        } finally {
            // Test if the document is null
            Assert.assertNull(document);
        }
    }
}