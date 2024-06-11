package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document document = DataUtil.load(testFile, "UTF-8", null);
        Assert.assertEquals("This is a test file.", document.text());

        // Clean up
        testFile.delete();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        String charsetName = "UTF-8";

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.load(testInputStream, charsetName, null);
        Assert.assertEquals("This is a test input stream.", document.text());

        testInputStream.close();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "<html><body><p>This is a test input stream.</p></body></html>";
        String charsetName = "UTF-8";

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(testInputStream, charsetName, null, Parser.htmlParser());

        Assert.assertEquals("<html><body><p>This is a test input stream.</p></body></html>", document.html());

        testInputStream.close();
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFileContent = "This is a test file.";
        String outputFileContent = "This is a test output file.";
        File inputFile = new File("testInputFile.html");
        File outputFile = new File("testOutputFile.html");

        FileWriter inputFileWriter = new FileWriter(inputFile);
        inputFileWriter.write(inputFileContent);
        inputFileWriter.close();

        FileReader fileReader = new FileReader(inputFile);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(fileReader, outputStream);
        byte[] outputBytes = outputStream.toByteArray();

        String expectedOutput = inputFileContent;
        String actualOutput = new String(outputBytes, StandardCharsets.UTF_8);
        Assert.assertEquals(expectedOutput, actualOutput);

        // Clean up
        outputFile.delete();
        inputFile.delete();
    }

    @Test
    public void testParseInputStream_WithDefaultParser() throws IOException {
        String inputStreamContent = "<html><body><p>This is a test input stream.</p></body></html>";
        String charsetName = "UTF-8";

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(testInputStream, charsetName, null);

        Assert.assertEquals("<html><body><p>This is a test input stream.</p></body></html>", document.html());

        testInputStream.close();
    }

    @Test
    public void testParseInputStream_WithCustomParser() throws IOException {
        String inputStreamContent = "<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:html xml:lang=\"en\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"><xhtml:head><xhtml:title>Test</xhtml:title></xhtml:head><xhtml:body><p>This is a test input stream.</p></xhtml:body></xhtml:html>";
        String charsetName = "UTF-8";

        ByteArrayInputStream testInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        Document document = DataUtil.parseInputStream(testInputStream, charsetName, null, Parser.xmlParser());

        Assert.assertEquals("<html xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" xmlns:xml=\"http://www.w3.org/XML/1998/namespace\"><head><title>Test</title></head><body><p>This is a test input stream.</p></body></html>", document.xml().asXml());

        testInputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputStream_NullInput() throws IOException {
        InputStream inputStream = null;
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
    }

    @Test(expected = NullPointerException.class)
    public void testParseInputStream_NullCharset() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
        String charsetName = null;
        String baseUri = null;
        Parser parser = Parser.htmlParser();

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputStream_NegativeBufferSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = null;
        Parser parser = Parser.htmlParser();
        int bufferSize = -1;

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser, bufferSize);
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] utf8Bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] utf16Lebom = {(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00};
        byte[] utf32Lebom = {(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};

        BomCharset utf8 = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(utf8Bom));
        BomCharset utf16 = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(utf16Lebom));
        BomCharset utf32 = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(utf32Lebom));

        Assert.assertNotNull(utf8);
        Assert.assertEquals("UTF-8", utf8.charset);
        Assert.assertFalse(utf8.offset);

        Assert.assertNotNull(utf16);
        Assert.assertEquals("UTF-16", utf16.charset);
        Assert.assertFalse(utf16.offset);

        Assert.assertNull(DataUtil.detectCharsetFromBom(ByteBuffer.wrap(new byte[0])));
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(charset);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";
        String charset = DataUtil.getCharsetFromContentType(contentType);

        Assert.assertNull(charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        String validatedCharset = DataUtil.validateCharset(charset);

        Assert.assertEquals("UTF-8", validatedCharset);
    }

    @Test
    public void testValidateCharset_InvalidCharset() {
        String charset = "InvalidCharset";
        String validatedCharset = DataUtil.validateCharset(charset);

        Assert.assertNull(validatedCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        Assert.assertNotNull(boundary);
        Assert.assertFalse(boundary.isEmpty());
        Assert.assertFalse(boundary.equals(""));
    }
}