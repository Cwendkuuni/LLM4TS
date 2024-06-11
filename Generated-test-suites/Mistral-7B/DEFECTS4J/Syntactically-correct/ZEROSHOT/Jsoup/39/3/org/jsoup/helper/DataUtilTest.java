package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        String filePath = "testData/sample.html"; // Replace with the path to your test file
        File inputFile = new File(filePath);

        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String content = "HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        String charsetName = "UTF-8";
        String baseUri = null;
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String content = "HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("ISO-8859-1"));

        String charsetName = "UTF-8";
        String baseUri = null;
        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String content = "HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        String charsetName = null;
        String baseUri = null;
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithNullBaseUri() throws IOException {
        String content = "HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        String charsetName = "UTF-8";
        String baseUri = null;
        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String content = "HTML content here...";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes("UTF-8"));

        String charsetName = null;
        String baseUri = null;
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String content = "HTML content here...";
        String charsetName = "UTF-8";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes("UTF-8"));

        String baseUri = null;
        Parser parser = Parser.htmlParser();
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String content = "HTML content here...";
        String charsetName = "ISO-8859-1";
        ByteBuffer byteData = ByteBuffer.wrap(content.getBytes("UTF-8"));

        String baseUri = null;
        Parser parser = Parser.htmlParser();
        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(byteData, charsetName, baseUri, parser));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String content = "HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String content = "HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, 100);
        assertNotNull(byteData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadToByteBufferWithNegativeMaxSize() throws IOException {
        String content = "HTML content here...";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream, -100);
    }
}