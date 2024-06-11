package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File inputFile = new File("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        String html = "<html><head><meta charset=\"InvalidCharset\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "InvalidCharset"; // Not a valid charset
        String baseUri = "http://example.com";
        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, charsetName, baseUri);
        } catch (IOException e) {
            // Expected
        }
        assertNull(doc);
    }

    @Test
    public void testParseInputStream() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>Test</body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("testFile.html");
        String outputFilePath = "testOutput.html";
        String charsetName = "UTF-8";

        // Load input file
        Document doc = DataUtil.load(inputFile, charsetName, null);

        // Write to output file
        File outputFile = new File(outputFilePath);
        OutputStream outputStream = new FileOutputStream(outputFile);
        DataUtil.crossStreams(doc.inputStream(), outputStream);
        outputStream.close();

        // Verify output file
        Document outputDoc = DataUtil.load(outputFile, charsetName, null);
        assertEquals(doc.outerHtml(), outputDoc.outerHtml());
        outputFile.deleteOnExit(); // Clean up
    }
}