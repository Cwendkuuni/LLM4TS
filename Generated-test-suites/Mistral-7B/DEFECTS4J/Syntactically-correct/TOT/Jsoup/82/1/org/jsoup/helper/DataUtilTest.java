package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFileWithValidCharset() throws IOException {
        File file = new File("test.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(file, charset, baseUri);
        // perform some checks on the document
        Assert.assertNotNull(document);
        // more checks based on the structure and content of the document
    }

    @Test
    public void testLoadDocumentFromFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        String charset = "ISO-8859-1"; // invalid charset for the file
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(file, charset, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected exception, check message is as expected
            Assert.assertEquals("Error parsing input from file", e.getMessage());
        }
    }

    @Test
    public void testLoadDocumentFromInputStreamWithValidCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, charset, baseUri);
        // perform some checks on the document
        Assert.assertNotNull(document);
        // more checks based on the structure and content of the document
    }

    @Test
    public void testLoadDocumentFromInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = "ISO-8859-1"; // invalid charset for the file
        String baseUri = "http://example.com";
        try {
            Document document = DataUtil.load(inputStream, charset, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected exception, check message is as expected
            Assert.assertEquals("Error parsing input from input stream", e.getMessage());
        }
    }

    @Test
    public void testLoadDocumentFromInputStreamWithNoCharsetSpecified() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String baseUri = "http://example.com";
        Document document = DataUtil.load(inputStream, null, baseUri);
        // perform some checks on the document
        Assert.assertNotNull(document);
        // more checks based on the structure and content of the document
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseInputStream(inputStream, charset, baseUri, parser);
        // perform some checks on the document
        Assert.assertNotNull(document);
        // more checks based on the structure and content of the document
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = "ISO-8859-1"; // invalid charset for the file
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        try {
            Document document = DataUtil.parseInputStream(inputStream, charset, baseUri, parser);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected exception, check message is as expected
            Assert.assertEquals("Error parsing input from input stream", e.getMessage());
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        OutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.crossStreams(inputStream, outputStream);
        // check that the data was correctly copied from input stream to output stream
        byte[] expectedData = "Test data".getBytes();
        byte[] actualData = outputStream.toByteArray();
        Assert.assertArrayEquals(expectedData, actualData);
    }
}