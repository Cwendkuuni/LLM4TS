package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Replace with the path to a valid HTML file.
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException, InterruptedException {
        String html = "<html><head><meta charset=\"UTF-8\"></head><body>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String invalidCharset = "invalidCharset";
        String html = "<html><head><meta charset=\"" + invalidCharset + "\"></head><body>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes("UTF-8"));
        String charsetName = invalidCharset;
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(doc); // The test passes even if the charset is invalid, as DataUtil will fall back to the default charset.
        assertTrue(doc.outputSettings().charset().equals(DataUtil.defaultCharset));
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        assertTrue(boundary.length() == 32);
    }
}