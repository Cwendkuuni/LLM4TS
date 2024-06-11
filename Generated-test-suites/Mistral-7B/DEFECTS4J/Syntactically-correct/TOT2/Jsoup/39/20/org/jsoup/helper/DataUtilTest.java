package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html"); // Replace with the actual path to your test HTML file
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testFile, expectedCharset, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream testInputStream = getClass().getResourceAsStream("/test.html"); // Replace with the actual path to your test HTML file
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testInputStream, expectedCharset, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream testInputStream = getClass().getResourceAsStream("/test.html"); // Replace with the actual path to your test HTML file
        String expectedCharset = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.xmlParser(); // Replace with the appropriate parser for your test case
        Document document = DataUtil.load(testInputStream, expectedCharset, baseUri, parser);
        Assert.assertNotNull(document);
    }

    // Add test cases for edge cases and exception handling scenarios for all other methods
}