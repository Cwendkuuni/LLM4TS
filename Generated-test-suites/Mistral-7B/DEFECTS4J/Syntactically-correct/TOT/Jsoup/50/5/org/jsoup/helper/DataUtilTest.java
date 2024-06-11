package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
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
import java.util.Locale;
import java.util.Random;

public class DataUtilTest {

    @org.junit.Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html"); // replace with a valid file path
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.title());
        Assert.assertEquals("Example Domain", doc.title().text());

        // edge case: file not found
        File nonExistentFile = new File("nonExistentFile.html");
        try {
            DataUtil.load(nonExistentFile, charsetName, baseUri);
            Assert.fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            // expected
        }

        // error case: invalid charset
        String invalidCharset = "ABC123";
        try {
            DataUtil.load(testFile, invalidCharset, baseUri);
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = Parser.htmlParser();

        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.title());
        Assert.assertEquals("Test", doc.title().text());

        // edge case: input stream empty
        InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        try {
            DataUtil.load(emptyInputStream, charsetName, baseUri, parser);
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // error case: input stream contains invalid charset
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write("<html><head><meta charset=\"INVALID\"></head><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        InputStream invalidCharsetInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        try {
            DataUtil.load(invalidCharsetInputStream, charsetName, baseUri, parser);
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testParseByteData() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body><h1>Test</h1></body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(htmlContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertNotNull(doc.title());
        Assert.assertEquals("Test", doc.title().text());

        // edge case: null charsetName
        Document nullCharsetNameDoc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        Assert.assertNotNull(nullCharsetNameDoc);
        Assert.assertEquals(DataUtil.defaultCharset, nullCharsetNameDoc.outputSettings().charset());

        // edge case: empty byteData
        ByteBuffer emptyByteData = DataUtil.emptyByteBuffer();
        Document emptyByteDataDoc = DataUtil.parseByteData(emptyByteData, "UTF-8", null, Parser.htmlParser());
        Assert.assertNull(emptyByteDataDoc);

        // error case: invalid charsetName
        ByteBuffer invalidCharsetByteData = ByteBuffer.wrap("InvalidCharset".getBytes(StandardCharsets.UTF_8));
        try {
            Document invalidCharsetDoc = DataUtil.parseByteData(invalidCharsetByteData, "InvalidCharset", null, Parser.htmlParser());
            Assert.fail("Expected IllegalArgumentException was not thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("input.html");
        File outputFile = new File("output.html");

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        // edge case: check if data was copied correctly
        ByteArrayOutputStream expectedOutput = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[bufferSize];
        while ((len = inputStream.read(buffer, 0, bufferSize)) != -1) {
            expectedOutput.write(buffer, 0, len);
        }
        byte[] output = outputFile.getBytes();
        Assert.assertArrayEquals(expectedOutput.toByteArray(), output);

        // error case: check if streams are not closed
        inputStream.close();
        outputStream.close();

        try {
            DataUtil.crossStreams(inputStream, outputStream);
            Assert.fail("Expected IOException was not thrown.");
        } catch (IOException e) {
            // expected
        }
    }
}