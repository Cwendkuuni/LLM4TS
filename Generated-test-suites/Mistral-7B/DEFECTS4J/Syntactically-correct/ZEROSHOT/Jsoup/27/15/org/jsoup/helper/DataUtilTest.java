package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/your/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithDifferentCharset() throws IOException {
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes("ISO-8859-1"));
        String charsetName = "ISO-8859-1";
        String baseUri = "http://www.example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("<html><body></body></html>".getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(document);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String baseUri = "http://www.example.com";

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("<html><head><meta charset=\"ISO-8859-1\"></head><body></body></html>".getBytes());
        ByteBuffer byteData = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());

        Document document = DataUtil.parseByteData(byteData, null, baseUri, Parser.htmlParser());
        assertNotNull(document);
        assertTrue("Detected charset is not ISO-8859-1".equals(document.outputSettings().charset()));
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() throws IOException {
        File file = new File("path/to/your/invalid/file.html");
        String charsetName = "UTF-8";
        String baseUri = "http://www.example.com";

        DataUtil.load(file, charsetName, baseUri);
    }

}