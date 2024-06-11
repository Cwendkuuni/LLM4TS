package org.jsoup.helper.test;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String filePath = "testFiles/validFileWithCharset.html";
        String charsetName = "ISO-8859-1";
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(new File(filePath), charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String filePath = "testFiles/validFileWithCharset.html";
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";

        try {
            Document doc = DataUtil.load(new File(filePath), charsetName, baseUri);
            Assert.fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testLoadFileWithNoCharset() throws IOException {
        String filePath = "testFiles/validFileWithNoCharset.html";
        String charsetName = null;
        String baseUri = "http://example.com";

        Document doc = DataUtil.load(new File(filePath), charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String filePath = "testFiles/validFileWithCharset.html";
        String charsetName = "ISO-8859-1";
        String baseUri = "http://example.com";

        File file = new File(filePath);
        byte[] fileContent = FileUtils.readByteArray(file);

        InputStream inputStream = new ByteArrayInputStream(fileContent);

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(charsetName, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        String filePath = "testFiles/validFileWithNoCharset.html";
        String charsetName = null;
        String baseUri = "http://example.com";

        File file = new File(filePath);
        byte[] fileContent = FileUtils.readByteArray(file);

        InputStream inputStream = new ByteArrayInputStream(fileContent);

        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        Assert.assertNotNull(doc);
        Assert.assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String filePath = "testFiles/validFileWithCharset.html";
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";

        File file = new File(filePath);
        byte[] fileContent = FileUtils.readByteArray(file);

        InputStream inputStream = new ByteArrayInputStream(fileContent);

        try {
            Document doc = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        byte[] testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element>Hello World</element>\n</root>".getBytes(StandardCharsets.UTF_8);

        InputStream inputStream = new ByteArrayInputStream(testData);

        Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(charsetName, doc.outputSettings().charset());

        XmlDeclaration xmlDeclaration = doc.child(0);
        Assert.assertEquals("UTF-8", xmlDeclaration.attr("encoding"));
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String baseUri = "http://example.com";

        byte[] testData = "<root><element>Hello World</element></root>".getBytes();

        InputStream inputStream = new ByteArrayInputStream(testData);

        Document doc = DataUtil.parseInputStream(inputStream, null, baseUri, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(DataUtil.defaultCharset, doc.outputSettings().charset());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";

        byte[] testData = "<root><element>Hello World</element></root>".getBytes();

        InputStream inputStream = new ByteArrayInputStream(testData);

        try {
            Document doc = DataUtil.parseInputStream(inputStream, charsetName, baseUri, DataUtil.Parser.htmlParser());
            Assert.fail("Expected IllegalArgumentException, but no exception was thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFilePath = "testFiles/input.html";
        String outputFilePath = "testFiles/output.html";

        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        String expectedContent = FileUtils.readFileToString(inputFile, StandardCharsets.UTF_8);
        String actualContent = FileUtils.readFileToString(outputFile, StandardCharsets.UTF_8);

        Assert.assertEquals(expectedContent, actualContent);

        inputStream.close();
        outputStream.close();
    }

    @Test
    public void testMimeBoundary() {
        String expectedMimeBoundary = StringUtils.join(DataUtil.mimeBoundaryChars, null, 0, DataUtil.boundaryLength);

        String mimeBoundary = DataUtil.mimeBoundary();

        Assert.assertEquals(expectedMimeBoundary, mimeBoundary);
    }
}