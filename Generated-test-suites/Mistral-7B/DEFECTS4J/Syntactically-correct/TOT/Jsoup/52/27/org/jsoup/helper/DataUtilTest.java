package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Parser;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File inputFile = new File("input.html"); // replace with a valid file path
        String expectedCharset = "UTF-8"; // or any valid charset
        String baseUri = "http://example.com"; // or any valid base URI

        Document document = DataUtil.load(inputFile, expectedCharset, baseUri);

        // Check that the Document is not null
        Assert.assertNotNull(document);

        // Check that the Document has been parsed correctly
        Assert.assertEquals("Expected HTML content", "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>", document.html());

        // Check that the Charset has been detected correctly
        Assert.assertEquals("Expected charset to be 'UTF-8'", expectedCharset, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String expectedCharset = "UTF-8"; // or any valid charset
        String baseUri = "http://example.com"; // or any valid base URI

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(expectedCharset));

        Document document = DataUtil.load(inputStream, expectedCharset, baseUri);

        // Check that the Document is not null
        Assert.assertNotNull(document);

        // Check that the Document has been parsed correctly
        Assert.assertEquals("Expected HTML content", "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>", document.html());

        // Check that the Charset has been detected correctly
        Assert.assertEquals("Expected charset to be 'UTF-8'", expectedCharset, document.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String expectedCharset = "ISO-8859-1"; // or any invalid charset
        String baseUri = "http://example.com"; // or any valid base URI

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));

        try {
            Document document = DataUtil.load(inputStream, expectedCharset, baseUri);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String expectedCharset = "UTF-8"; // or any valid charset
        String baseUri = "http://example.com"; // or any valid base URI

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.parseByteData(Charset.forName(expectedCharset).encode(Charset.forName("UTF-8").decode(htmlContent.getBytes())), expectedCharset, baseUri, Parser.htmlParser());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.load(inputStream, expectedCharset, baseUri);

        // Check that the Document is not null
        Assert.assertNotNull(document);

        // Check that the Document has been parsed correctly
        Assert.assertEquals("Expected HTML content", "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>", document.html());

        // Check that the Charset has been detected correctly
        Assert.assertEquals("Expected charset to be 'UTF-8'", expectedCharset, document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head><title>Test</title></head><body><p>Hello, World!</p></body></html>";
        String expectedCharset = "ISO-8859-1"; // or any invalid charset
        String baseUri = "http://example.com"; // or any valid base URI

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.parseByteData(Charset.forName("UTF-8").encode(Charset.forName("UTF-8").decode(htmlContent.getBytes())), null, baseUri, Parser.htmlParser());
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        try {
            Document document = DataUtil.load(inputStream, expectedCharset, baseUri);
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "Hello, World!";
        String outputContent = "Hello, World!";

        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        // Check that the output content is the same as the input content
        Assert.assertEquals("Expected output content to be 'Hello, World!'\n", "Hello, World!\n", outputStream.toString());
    }
}