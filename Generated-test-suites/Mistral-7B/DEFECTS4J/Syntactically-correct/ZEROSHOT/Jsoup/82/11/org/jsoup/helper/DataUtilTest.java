package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for org.jsoup.helper.DataUtil.
 */
public class DataUtilTest {

    /**
     * Test method for {@link DataUtil#parseInputStream(InputStream, String, String, Parser)}.
     * @throws IOException on IO error
     */
    @Test
    public void testParseInputStream() throws IOException {
        String htmlContent = "<html lang=\"en\">" +
                "<head><meta charset=\"UTF-8\"></head>" +
                "<body>" +
                "<h1>Hello World</h1>" +
                "</body>" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        Document document = DataUtil.load(inputStream, "UTF-8", null);

        assertEquals("UTF-8", document.outputSettings().charset().name());
        assertEquals("html", document.tagName());
        assertEquals("html", document.tagName());
        assertEquals(1, document.childNodeSize());
        Element htmlElement = document.child(0);
        assertEquals("head", htmlElement.tagName());
        assertEquals(1, htmlElement.childNodeSize());
        Element metaElement = htmlElement.child(0);
        assertEquals("meta", metaElement.tagName());
        assertEquals("charset", metaElement.attr("http-equiv"));
        assertEquals("UTF-8", metaElement.attr("content"));

        Element bodyElement = document.body();
        assertEquals("body", bodyElement.tagName());
        assertEquals(1, bodyElement.childNodeSize());
        Element h1Element = bodyElement.child(0);
        assertEquals("h1", h1Element.tagName());
        assertEquals("Hello World", h1Element.text());

        // Test loading an HTML document with an XML declaration
        htmlContent = "<?xml version='1.0' encoding='UTF-8'?>" +
                "<html lang=\"en\">" +
                "<head><meta charset=\"UTF-8\"></head>" +
                "<body>" +
                "<h1>Hello World</h1>" +
                "</body>" +
                "</html>";

        inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        document = DataUtil.load(inputStream, "UTF-8", null);

        assertEquals("UTF-8", document.outputSettings().charset().name());
        assertEquals("html", document.tagName());
        assertEquals("html", document.tagName());
        assertEquals(1, document.childNodeSize());
        XmlDeclaration xmlDeclaration = document.child(0);
        assertEquals("xml", xmlDeclaration.tagName());
        assertEquals("version", xmlDeclaration.attribute(0).tagName());
        assertEquals("1.0", xmlDeclaration.attribute(0).attr("value"));
        assertEquals("encoding", xmlDeclaration.attribute(1).tagName());
        assertEquals("UTF-8", xmlDeclaration.attribute(1).attr("value"));

        htmlElement = document.child(1);
        assertEquals("head", htmlElement.tagName());
        assertEquals(1, htmlElement.childNodeSize());
        metaElement = htmlElement.child(0);
        assertEquals("meta", metaElement.tagName());
        assertEquals("charset", metaElement.attr("http-equiv"));
        assertEquals("UTF-8", metaElement.attr("content"));

        bodyElement = document.body();
        assertEquals("body", bodyElement.tagName());
        assertEquals(1, bodyElement.childNodeSize());
        h1Element = bodyElement.child(0);
        assertEquals("h1", h1Element.tagName());
        assertEquals("Hello World", h1Element.text());

        // Test loading an invalid HTML document
        String invalidHtmlContent = "<html lang=\"en\"><body></body>";
        inputStream = new ByteArrayInputStream(invalidHtmlContent.getBytes(Charset.forName("UTF-8")));
        try {
            document = DataUtil.load(inputStream, "UTF-8", null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        // Test loading an empty HTML document
        String emptyHtmlContent = "";
        inputStream = new ByteArrayInputStream(emptyHtmlContent.getBytes(Charset.forName("UTF-8")));
        document = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals(0, document.childNodeSize());

        // Test loading a non-HTML document
        String xmlContent = "<rss version='2.0'><channel><title>RSS Feed</title></channel></rss>";
        inputStream = new ByteArrayInputStream(xmlContent.getBytes(Charset.forName("UTF-8")));
        try {
            document = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        // Test loading a non-existent file
        String filePath = "non-existent-file.html";
        try {
            document = DataUtil.load(new File(filePath), null, null);
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        // Test loading a file with an invalid charset
        String invalidCharsetHtmlContent = "<html lang=\"en\">" +
                "<head><meta charset=\"invalid-charset\"></head>" +
                "<body>" +
                "<h1>Hello World</h1>" +
                "</body>" +
                "</html>";
        File file = new File("test.html");
        try {
            Files.write(file.toPath(), invalidCharsetHtmlContent.getBytes("UTF-8"));
            document = DataUtil.load(file, null, null);
            Files.delete(file.toPath());
            fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }

        // Test loading a file with a BOM
        String bomHtmlContent = "\ufeff<html lang=\"en\">" +
                "<head><meta charset=\"UTF-8\"></head>" +
                "<body>" +
                "<h1>Hello World</h1>" +
                "</body>" +
                "</html>";
        file = new File("test.html");
        try {
            Files.write(file.toPath(), bomHtmlContent.getBytes(Charset.forName("UTF-8")));
            document = DataUtil.load(file, null, null);
            assertEquals("UTF-8", document.outputSettings().charset().name());
            Files.delete(file.toPath());
        } catch (IOException e) {
            // expected
        }

        // Test writing to an output stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String outputString = "<html lang=\"en\">" +
                "<head><meta charset=\"UTF-8\"></head>" +
                "<body>" +
                "<h1>Hello World</h1>" +
                "</body>" +
                "</html>";
        DataUtil.crossStreams(new ByteArrayInputStream(outputString.getBytes(Charset.forName("UTF-8"))), outputStream);
        byte[] outputBytes = outputStream.toByteArray();
        assertEquals(outputString, new String(outputBytes, Charset.forName("UTF-8")));
        assertEquals("UTF-8", new String(outputBytes, 0, 5).trim().equals("UTF-8") ? null : "not UTF-8");
    }
}