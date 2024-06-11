package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File file = new File("test.html"); // Replace with the path to a valid HTML file
        String charset = "UTF-8";
        String baseUri = "";
        Document document = DataUtil.load(file, charset, baseUri);
        assert document != null;
        // Add assertions to check the document content
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html"); // Replace with the path to a valid HTML file
        String charset = "InvalidCharset";
        String baseUri = "";
        try {
            Document document = DataUtil.load(file, charset, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected IOException, check the message contains "Invalid charset"
        }
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String htmlContent = "<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "";
        Document document = DataUtil.load(inputStream, charset, baseUri);
        assert document != null;
        // Add assertions to check the document content
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String invalidHtmlContent = "<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"InvalidCharset\">\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(invalidHtmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "";
        try {
            Document document = DataUtil.load(inputStream, charset, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected IOException, check the message contains "Invalid charset"
        }
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        String htmlContent = "<!DOCTYPE html>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = null;
        String baseUri = "";
        Document document = DataUtil.load(inputStream, charset, baseUri);
        assert document != null;
        // Add assertions to check the document content
    }

    @Test
    public void testLoadInputStreamWithBomUtf8() throws IOException {
        String htmlContent = "\uFEFF<!DOCTYPE html>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "";
        Document document = DataUtil.load(inputStream, charset, baseUri);
        assert document != null;
        // Add assertions to check the document content
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String htmlContent = "<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"UTF-8\">\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "";
        Document document = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
        assert document != null;
        // Add assertions to check the document content
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String invalidHtmlContent = "<!DOCTYPE html>\n<html>\n<head>\n<meta charset=\"InvalidCharset\">\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(invalidHtmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "";
        try {
            Document document = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected IOException, check the message contains "Invalid charset"
        }
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String htmlContent = "<!DOCTYPE html>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = null;
        String baseUri = "";
        Document document = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
        assert document != null;
        // Add assertions to check the document content
    }

    @Test
    public void testParseInputStreamWithBomUtf8() throws IOException {
        String htmlContent = "\uFEFF<!DOCTYPE html>\n<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<p>Test</p>\n</body>\n</html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charset = "UTF-8";
        String baseUri = "";
        Document document = DataUtil.parseInputStream(inputStream, charset, baseUri, Parser.htmlParser());
        assert document != null;
        // Add assertions to check the document content
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assert boundary.length() == 32;
    }
}