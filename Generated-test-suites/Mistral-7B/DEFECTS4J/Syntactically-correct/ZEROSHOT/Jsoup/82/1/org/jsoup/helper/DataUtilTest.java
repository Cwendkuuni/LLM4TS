package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Test for DataUtil class
 */
public class DataUtilTest {

    @Test
    public void testLoadDocument() throws IOException {
        String htmlContent = "<html><head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "UTF-8";
        Document document = DataUtil.load(inputStream, expectedCharset, null);
        assertEquals("Document should be loaded correctly.", document, assertDocumentContent("ValidDocument.html", expectedCharset));
    }

    @Test
    public void testLoadDocumentWithInvalidCharset() throws IOException {
        String htmlContent = "<html><head>\n" +
                "<meta charset=\"ISO-8859-1\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "ISO-8859-1";
        Document document = DataUtil.load(inputStream, expectedCharset, null);
        assertEquals("Document should be loaded correctly with the specified charset.", document, assertDocumentContent("ValidDocumentWithInvalidCharset.html", expectedCharset));
    }

    @Test
    public void testLoadDocumentWithNoCharsetSpecified() throws IOException {
        String htmlContent = "<html><head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null);
        assertEquals("Document should be loaded correctly with the default charset.", document, assertDocumentContent("ValidDocument.html", "UTF-8"));
    }

    @Test
    public void testLoadDocumentWithNoMetaTag() throws IOException {
        String htmlContent = "<html><body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null);
        assertEquals("Document should be loaded correctly with the default charset.", document, assertDocumentContent("ValidDocumentNoMetaTag.html", "UTF-8"));
    }

    @Test
    public void testLoadDocumentWithXmlDeclaration() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><\n" +
                "html>\n" +
                "<head>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "UTF-8";
        Document document = DataUtil.load(inputStream, expectedCharset, null);
        assertEquals("Document should be loaded correctly.", document, assertDocumentContent("ValidDocumentWithXmlDeclaration.html", expectedCharset));
    }

    @Test
    public void testLoadDocumentWithBomUtf16() throws IOException {
        String htmlContent = "\uFEFF<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_16LE));
        String expectedCharset = "UTF-8";
        Document document = DataUtil.load(inputStream, expectedCharset, null);
        assertEquals("Document should be loaded correctly.", document, assertDocumentContent("ValidDocumentWithBomUtf16.html", expectedCharset));
    }

    @Test
    public void testLoadDocumentWithBomUtf32() throws IOException {
        String htmlContent = "\u0000\u0000\u00FE\u00FF<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_32BE));
        String expectedCharset = "UTF-8";
        Document document = DataUtil.load(inputStream, expectedCharset, null);
        assertEquals("Document should be loaded correctly.", document, assertDocumentContent("ValidDocumentWithBomUtf32.html", expectedCharset));
    }

    @Test(expected = IOException.class)
    public void testLoadDocumentWithInvalidBom() throws IOException {
        String htmlContent = "\u0000\u0000\u00FB\u00FF<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_32BE));
        String expectedCharset = "UTF-8";
        DataUtil.load(inputStream, expectedCharset, null);
    }

    @Test
    public void testLoadDocumentWithInvalidXmlDeclaration() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"invalid-charset\"?><\n" +
                "html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(inputStream, null, null);
        assertEquals("Document should be loaded correctly with the default charset.", document, assertDocumentContent("ValidDocumentWithInvalidXmlDeclaration.html", "UTF-8"));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html><head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser(), outputStream);
        String expectedContent = "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        assertEquals("Output content should match the expected content.", expectedContent, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    public void testParseInputStreamWithInvalidXmlDeclaration() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"invalid-charset\"?><\n" +
                "html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser(), outputStream);
        String expectedContent = "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        assertEquals("Output content should match the expected content.", expectedContent, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    public void testParseInputStreamWithXmlDeclarationAndNoMetaTag() throws IOException {
        String htmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser(), outputStream);
        String expectedContent = "<html>\n" +
                "<head>\n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        assertEquals("Output content should match the expected content.", expectedContent, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    public void testParseInputStreamWithBomUtf16() throws IOException {
        String htmlContent = "\uFEFF<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_16LE));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser(), outputStream);
        String expectedContent = "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        assertEquals("Output content should match the expected content.", expectedContent, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test
    public void testParseInputStreamWithBomUtf32() throws IOException {
        String htmlContent = "\u0000\u0000\u00FE\u00FF<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_32BE));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser(), outputStream);
        String expectedContent = "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        assertEquals("Output content should match the expected content.", expectedContent, new String(outputStream.toByteArray(), StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void testParseInputStreamWithInvalidBom() throws IOException {
        String htmlContent = "\u0000\u0000\u00FB\u00FF<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_32BE));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser(), outputStream);
    }

    private Document assertDocumentContent(String fileName, String charset) throws IOException {
        String expectedContent = new String(getClass().getResourceAsStream(fileName), charset);
        Document document = new Parser().parseInput(expectedContent, null);
        return document;
    }
}