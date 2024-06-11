package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @org.junit.Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String expectedContent = "This is a test file.";
        String filePath = "testFile.html";
        File testFile = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(testFile);

        Document document = DataUtil.load(testFile, null);
        String actualContent = document.text();

        Assert.assertEquals(expectedContent, actualContent);
        fileInputStream.close();
    }

    @org.junit.Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String expectedContent = "Das ist ein Testdatei."; // German text for charset test
        String filePath = "testFile_de.html";
        File testFile = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(testFile);

        Document document = DataUtil.load(testFile, "ISO-8859-1");
        String actualContent = document.text();

        Assert.assertEquals(expectedContent, actualContent);
        fileInputStream.close();
    }

    @org.junit.Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String expectedContent = "This is a test file.";
        String testString = "This is a test file.";

        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null);
        String actualContent = document.text();

        Assert.assertEquals(expectedContent, actualContent);
        inputStream.close();
    }

    @org.junit.Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String expectedContent = "Das ist ein Testdatei."; // German text for charset test
        String testString = "Das ist ein Testdatei.";

        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.ISO_8859_1));

        Document document = DataUtil.load(inputStream, "ISO-8859-1");
        String actualContent = document.text();

        Assert.assertEquals(expectedContent, actualContent);
        inputStream.close();
    }

    @org.junit.Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String testString = "This is a test file.";

        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        try {
            Document document = DataUtil.load(inputStream, "ISO-8859-1");
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
        inputStream.close();
    }

    @org.junit.Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String expectedContent = "This is a test file.";
        String testString = "This is a test file.";

        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.parseInputStream(inputStream, null, null, Parser.htmlParser());
        String actualContent = document.text();

        Assert.assertEquals(expectedContent, actualContent);
        inputStream.close();
    }

    @org.junit.Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String expectedContent = "Das ist ein Testdatei."; // German text for charset test
        String testString = "Das ist ein Testdatei.";

        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.ISO_8859_1));

        Document document = DataUtil.parseInputStream(inputStream, "ISO-8859-1", null, Parser.htmlParser());
        String actualContent = document.text();

        Assert.assertEquals(expectedContent, actualContent);
        inputStream.close();
    }

    @org.junit.Test
    public void testCrossStreams() throws IOException {
        String expectedContent = "This is a test.";
        String testString = "This is a test.";

        InputStream source = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        OutputStream output = new ByteArrayOutputStream();

        DataUtil.crossStreams(source, output);

        byte[] readBytes = output.toByteArray();
        String actualContent = new String(readBytes, StandardCharsets.UTF_8);

        Assert.assertEquals(expectedContent, actualContent);

        source.close();
        output.close();
    }
}