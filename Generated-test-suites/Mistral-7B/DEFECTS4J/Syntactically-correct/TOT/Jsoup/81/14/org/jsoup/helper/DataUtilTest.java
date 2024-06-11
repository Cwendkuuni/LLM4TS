package org.jsoup.helper.test;

import org.junit.Assert;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @org.junit.Test
    public void testLoadFromFileWithValidCharset() throws IOException {
        String expectedCharset = "UTF-8";
        String filePath = "testFiles/validCharset.html";
        Document doc = DataUtil.load(new File(filePath), expectedCharset, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(expectedCharset, doc.outputSettings().charset());
    }

    @org.junit.Test
    public void testLoadFromFileWithInvalidCharset() throws IOException {
        String filePath = "testFiles/invalidCharset.html";
        Document doc = null;
        try {
            doc = DataUtil.load(new File(filePath), null, null);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(doc);
    }

    @org.junit.Test
    public void testLoadFromInputStreamWithValidCharset() throws IOException {
        String expectedCharset = "UTF-8";
        String inputStreamData = "<html><head><meta charset=\"" + expectedCharset + "\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, expectedCharset, null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(expectedCharset, doc.outputSettings().charset());
    }

    @org.junit.Test
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        String inputStreamData = "<html><head><meta charset=\"GBK\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(StandardCharsets.UTF_8));
        try {
            Document doc = DataUtil.load(inputStream, null, null);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
        Assert.assertNull(doc);
    }

    @org.junit.Test
    public void testLoadFromInputStreamWithParser() throws IOException {
        String expectedCharset = "UTF-8";
        String inputStreamData = "<html><head><meta charset=\"" + expectedCharset + "\"></head><body><h1>Test</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamData.getBytes(StandardCharsets.UTF_8));
        Document doc = DataUtil.load(inputStream, expectedCharset, null, Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(expectedCharset, doc.outputSettings().charset());
    }

    @org.junit.Test
    public void testCrossStreams() throws IOException {
        String inputFilePath = "testFiles/input.html";
        String outputFilePath = "testFiles/output.html";
        String inputFileContent = "Test input content.";
        String outputFileContent = "Test output content.";

        File inputFile = new File(inputFilePath);
        File outputFile = new File(outputFilePath);

        Document inputDoc = DataUtil.load(inputFile, null, null);
        Document outputDoc = null;

        // Ensure that the input file is loaded
        Assert.assertNotNull(inputDoc);

        // Write the input content to a temporary file
        File tempFile = new File("temp.html");
        DataUtil.save(tempFile, inputDoc, null);

        InputStream inputStream = new FileInputStream(tempFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        inputStream.close();
        outputStream.close();

        // Ensure that the output file is created and contains the expected content
        Document newOutputDoc = DataUtil.load(outputFile, null, null);
        Assert.assertNotNull(newOutputDoc);
        Assert.assertEquals(outputFileContent, newOutputDoc.body().text());

        // Clean up
        tempFile.delete();
    }
}