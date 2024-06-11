package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String fileContent = "This is a test file with UTF-8 encoding.";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, fileContent, StandardCharsets.UTF_8);

        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.body().text().equals(fileContent));

        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String fileContent = "This is a test file with ISO-8859-1 encoding.";
        File file = new File("testFile.html");
        FileUtils.writeStringToFile(file, fileContent, StandardCharsets.ISO_8859_1);

        Document doc = null;
        try {
            doc = DataUtil.load(file, "UTF-8", null);
        } catch (IOException e) {
            // Expected
        }

        Assert.assertNull(doc);
        FileUtils.deleteQuietly(file);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String inputStreamContent = "This is a test input stream with UTF-8 encoding.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.body().text().equals(inputStreamContent));
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "This is a test input stream with ISO-8859-1 encoding.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.ISO_8859_1));

        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, "UTF-8", null);
        } catch (IOException e) {
            // Expected
        }

        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithValidatorParser() throws IOException {
        String inputStreamContent = "<?xml version='1.0' encoding='UTF-8'?> <root/>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.rootElement().tagName().equals("root"));
    }

    @Test
    public void testCrossStreams() throws IOException {
        File inputFile = new File("testInputFile.html");
        File outputFile = new File("testOutputFile.html");

        String inputFileContent = "This is a test input file.";
        FileUtils.writeStringToFile(inputFile, inputFileContent, StandardCharsets.UTF_8);

        InputStream inputStream = new FileInputStream(inputFile);
        OutputStream outputStream = new FileOutputStream(outputFile);

        DataUtil.crossStreams(inputStream, outputStream);

        String outputFileContent = FileUtils.readFileToString(outputFile, StandardCharsets.UTF_8);
        Assert.assertTrue(outputFileContent.equals(inputFileContent));

        FileUtils.deleteQuietly(inputFile);
        FileUtils.deleteQuietly(outputFile);
    }

    @Test
    public void testParseByteDataWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String docData = "<html><body>This is a test document.</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(byteData, charsetName, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.body().text().equals("This is a test document."));
    }

    @Test
    public void testParseByteDataWithInvalidCharset() throws IOException {
        String charsetName = "ISO-8859-1";
        String docData = "<html><body>This is a test document.</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));

        Document doc = null;
        try {
            doc = DataUtil.parseByteData(byteData, charsetName, null, DataUtil.Parser.htmlParser());
        } catch (IOException e) {
            // Expected
        }

        Assert.assertNull(doc);
    }

    @Test
    public void testParseByteDataWithDetectedCharset() throws IOException {
        String charsetName = null;
        String docData = "<html><head><meta charset='UTF-8'/><body>This is a test document.</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(byteData, charsetName, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.body().text().equals("This is a test document."));
    }

    @Test
    public void testParseByteDataWithDetectedCharsetAndDefault() throws IOException {
        String charsetName = null;
        String docData = "<html><head><meta charset='ISO-8859-1'/><body>This is a test document.</body></html>";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(byteData, charsetName, null, DataUtil.Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertTrue(doc.body().text().equals("This is a test document."));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertNotNull(mimeBoundary);
        Assert.assertEquals(mimeBoundary.length(), 32);
    }
}