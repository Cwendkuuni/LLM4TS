package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File inputFile = new File("test.html"); // Replace with an actual file path
        Document document = DataUtil.load(inputFile, "UTF-8", "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStream() throws IOException, HelperException {
        String htmlContent = "<html><head><meta charset='UTF-8'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = DataUtil.load(inputStream, "UTF-8", "");
        Assert.assertNotNull(document);
    }

    @Test
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException, HelperException {
        String htmlContent = "<html><head><meta charset='ISO-8859-1'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = null;
        try {
            document = DataUtil.load(inputStream, "ISO-8859-1", "");
        } catch (IOException e) {
            // Expected exception, as the charset is not specified correctly in the input
        }
        Assert.assertNull(document);
    }

    @Test
    public void testParseInputStreamWithUTF8Charset() throws IOException, HelperException {
        String htmlContent = "<html><head><meta charset='UTF-8'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = DataUtil.parseInputStream(inputStream, "UTF-8", "", Parser.htmlParser());
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException, HelperException {
        String htmlContent = "<html><head><meta charset='ISO-8859-1'></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        Document document = null;
        try {
            document = DataUtil.parseInputStream(inputStream, "ISO-8859-1", "", Parser.htmlParser());
        } catch (IOException e) {
            // Expected exception, as the charset is not specified correctly in the input
        }
        Assert.assertNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String sourceContent = "Source content";
        String targetContent = "Target content";
        InputStream inputStream = new ByteArrayInputStream(sourceContent.getBytes("UTF-8"));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);
        String resultContent = outputStream.toString("UTF-8");

        Assert.assertEquals(targetContent, resultContent);
    }
}