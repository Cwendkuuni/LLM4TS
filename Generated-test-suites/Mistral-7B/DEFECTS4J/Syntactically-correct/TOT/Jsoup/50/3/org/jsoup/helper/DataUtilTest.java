package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File testFile = new File("test.html");
        String expectedCharset = "UTF-8";
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(testFile, expectedCharset, baseUri);
        // typical use case: check if the document is loaded correctly
        Assert.assertNotNull(doc);
        // edge case: check if an incorrect charset is detected
        File incorrectTestFile = new File("test.html");
        String incorrectCharset = "GBK";
        Document incorrectDoc = DataUtil.load(incorrectTestFile, incorrectCharset, baseUri);
        Assert.assertEquals(DataUtil.defaultCharset, incorrectDoc.outputSettings().charset());
        // error scenario: check if an IOException is thrown when the file is not found
        File nonExistentFile = new File("non_existent.html");
        try {
            DataUtil.load(nonExistentFile, expectedCharset, baseUri);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        String htmlData = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlData.getBytes(StandardCharsets.UTF_8));
        String expectedCharset = "UTF-8";
        String baseUri = "http://test.com";
        Document doc = DataUtil.load(inputStream, expectedCharset, baseUri, DataUtil.Parser.htmlParser());
        // typical use case: check if the document is loaded correctly
        Assert.assertNotNull(doc);
        // edge case: check if an incorrect charset is detected
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write("Content-Type: text/html; charset=GBK\r\n".getBytes(StandardCharsets.UTF_8));
        outputStream.write(htmlData.getBytes(StandardCharsets.UTF_8));
        InputStream charsetInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Document charsetDoc = DataUtil.load(charsetInputStream, null, baseUri, DataUtil.Parser.htmlParser());
        Assert.assertEquals(DataUtil.defaultCharset, charsetDoc.outputSettings().charset());
        // error scenario: check if an IOException is thrown when the input stream is empty
        ByteArrayOutputStream emptyOutputStream = new ByteArrayOutputStream();
        InputStream emptyInputStream = new ByteArrayInputStream(emptyOutputStream.toByteArray());
        try {
            DataUtil.load(emptyInputStream, expectedCharset, baseUri, DataUtil.Parser.htmlParser());
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.html");
        File outputFile = new File("output.html");
        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);
        DataUtil.crossStreams(inputStream, outputStream);
        // typical use case: check if the files are copied correctly
        String expectedHtml = "Expected HTML content";
        String inputContent = new String(readAllBytes(inputFile), StandardCharsets.UTF_8);
        Assert.assertEquals(expectedHtml, inputContent);
        String outputContent = new String(readAllBytes(outputFile), StandardCharsets.UTF_8);
        Assert.assertEquals(expectedHtml, outputContent);
        // edge case: check if an IOException is thrown when the input file is not readable
        File notReadableFile = new File("not_readable.html");
        try {
            DataUtil.crossStreams(new FileInputStream(notReadableFile), outputStream);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // expected
        }
    }

    private static byte[] readAllBytes(File file) throws IOException {
        InputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[file.length()];
        int read = 0;
        while (read < buffer.length) {
            read += fis.read(buffer, read, buffer.length - read);
        }
        fis.close();
        return buffer;
    }
}