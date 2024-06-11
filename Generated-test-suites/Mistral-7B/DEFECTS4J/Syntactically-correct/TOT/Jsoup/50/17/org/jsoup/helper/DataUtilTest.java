package org.jsoup.helper.test;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("testFile.html");
        file.createNewFile();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(fileContent.getBytes(StandardCharsets.UTF_8));
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(fileContent);
                fileWriter.close();
            }
            Document doc = DataUtil.load(file, "UTF-8", null);
            Assert.assertNotNull(doc);
            Assert.assertEquals(fileContent, doc.body().text());
            file.delete();
        }
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] inputStreamBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputStreamBytes);
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testLoadInputStreamWithCharsetDetect() throws IOException {
        String inputStreamContent = "This is a test input stream with a meta charset.";
        String metaCharset = "ISO-8859-1";

        byte[] inputStreamBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputStreamBytes);
        Element metaElement = new Element("meta");
        metaElement.attr("http-equiv", "Content-Type");
        metaElement.attr("content", "text/html; charset=" + metaCharset);
        Document doc = new Document();
        doc.insertBefore(doc.body(), metaElement);

        Document loadedDoc = DataUtil.load(inputStream, null, null, Parser.htmlParser());
        Assert.assertNotNull(loadedDoc);
        Assert.assertEquals(inputStreamContent, loadedDoc.body().text());
        Assert.assertEquals(metaCharset, loadedDoc.outputSettings().charset());
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        byte[] inputStreamBytes = inputStreamContent.getBytes(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(inputStreamBytes);

        Document doc = DataUtil.load(inputStream, "UTF-8", null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(inputStreamContent, doc.body().text());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testString = "Cross streams test.";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        DataUtil.crossStreams(inputStream, outputStream);

        Assert.assertEquals(testString, outputStream.toString());
    }

    @Test
    public void testParseByteData() {
        String byteDataContent = "This is a test byte data.";
        ByteBuffer byteData = ByteBuffer.wrap(byteDataContent.getBytes(StandardCharsets.UTF_8));

        Document doc = DataUtil.parseByteData(byteData, "UTF-8", null, Parser.htmlParser());
        Assert.assertNotNull(doc);
        Assert.assertEquals(byteDataContent, doc.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "Read to byte buffer test.";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        Assert.assertEquals(testString.getBytes(StandardCharsets.UTF_8).length, byteData.limit());
        byte[] expectedBytes = testString.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = new byte[byteData.limit()];
        byteData.get(actualBytes);
        Assert.assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "This is a test file.";
        File file = new File("testFile.html");
        file.createNewFile();
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(fileContent);
            fileWriter.close();
        }

        ByteBuffer byteData = DataUtil.readFileToByteBuffer(file);
        Assert.assertEquals(fileContent.getBytes(StandardCharsets.UTF_8).length, byteData.limit());
        byte[] expectedBytes = fileContent.getBytes(StandardCharsets.UTF_8);
        byte[] actualBytes = new byte[byteData.limit()];
        byteData.get(actualBytes);
        Assert.assertArrayEquals(expectedBytes, actualBytes);

        file.delete();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        Assert.assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));

        contentType = "text/html";
        Assert.assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();
        Assert.assertEquals(mimeBoundary.length(), DataUtil.boundaryLength);
    }
}