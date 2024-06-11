package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Test for DataUtil class.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "test.html";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "/";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        // Check if the document is correctly loaded
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInvalidFile() {
        String filePath = "nonExistentFile.html";
        String charsetName = "UTF-8";
        String baseUri = "/";
        try {
            Document doc = DataUtil.load(new File(filePath), charsetName, baseUri);
            Assert.fail("Expected an IOException but got a Document instead");
        } catch (IOException e) {
            // Pass
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/";
        String htmlContent = "<html><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName(charsetName)));
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        // Check if the document is correctly loaded
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadInvalidInputStream() {
        String charsetName = "UTF-8";
        String baseUri = "/";
        try {
            Document doc = DataUtil.load(new ByteArrayInputStream(new byte[0]), charsetName, baseUri);
            Assert.fail("Expected an IOException but got a Document instead");
        } catch (IOException e) {
            // Pass
        }
    }

    @Test
    public void testLoadWithInvalidCharset() throws IOException {
        String filePath = "test.html";
        File file = new File(filePath);
        String charsetName = "INVALID_CHARSET";
        String baseUri = "/";
        try {
            Document doc = DataUtil.load(file, charsetName, baseUri);
            Assert.fail("Expected an IOException but got a Document instead");
        } catch (IOException e) {
            // Pass
        }
    }

    @Test
    public void testLoadWithInvalidBaseUri() throws IOException {
        String filePath = "test.html";
        File file = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "invalidBaseUri";
        Document doc = DataUtil.load(file, charsetName, baseUri);
        // Check if an IOException is thrown
        try {
            Assert.assertNotNull(doc);
            Assert.fail("Expected an IOException but got a Document instead");
        } catch (IOException e) {
            // Pass
        }
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String htmlContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><body></body></html>";
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/testDataUtilParseByteDataWithDefaultCharset.html"));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        // Check if the document is correctly parsed
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String htmlContent = "<?xml version='1.0' encoding='gb2312'?>\n<html><body></body></html>";
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/testDataUtilParseByteDataWithSpecifiedCharset.html"));
        Document doc = DataUtil.parseByteData(byteData, "gb2312", null, Parser.htmlParser());
        // Check if the document is correctly parsed
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithBOM() throws IOException {
        String htmlContent = "<?xml version='1.0' encoding='UTF-8'?>\n\uFEFF<html><body></body></html>";
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/testDataUtilParseByteDataWithBOM.html"));
        Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
        // Check if the document is correctly parsed
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String htmlContent = "<?xml version='1.0' encoding='INVALID_CHARSET'?>\n<html><body></body></html>";
        ByteBuffer byteData = DataUtil.readFileToByteBuffer(new File("src/test/resources/testDataUtilParseByteDataWithInvalidCharset.html"));
        try {
            Document doc = DataUtil.parseByteData(byteData, null, null, Parser.htmlParser());
            Assert.fail("Expected an IOException but got a Document instead");
        } catch (IOException e) {
            // Pass
        }
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputFileName = "src/test/resources/testDataUtilCrossStreamsInput.txt";
        String outputFileName = "src/test/resources/testDataUtilCrossStreamsOutput.txt";

        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        byte[] expectedOutput = new byte[]{(byte) 0x41, (byte) 0x42, (byte) 0x43};
        byte[] actualOutput = new byte[outputFile.length()];
        FileInputStream outputFileInputStream = new FileInputStream(outputFile);
        outputFileInputStream.read(actualOutput, 0, outputFile.length());

        Assert.assertArrayEquals(expectedOutput, actualOutput);
        outputFile.delete();
    }

    @Test
    public void testCrossStreamsWithException() throws IOException {
        String inputFileName = "src/test/resources/testDataUtilCrossStreamsInput.txt";
        String outputFileName = "src/test/resources/testDataUtilCrossStreamsOutput.txt";

        File inputFile = new File(inputFileName);
        File outputFile = new File(outputFileName);

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        try {
            DataUtil.crossStreams(inputStream, null);
            Assert.fail("Expected an IOException but got no exception");
        } catch (IOException e) {
            // Pass
        } finally {
            inputStream.close();
            outputStream.close();
        }

        Assert.assertTrue(outputFile.exists());
        outputFile.delete();
    }
}