package org.jsoup.helper;

import static org.junit.Assert.*;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(fileContent);
        }

        Document document = DataUtil.load(testFile, null);
        assertEquals("This is a test file.", document.body().text());
        testFile.delete();
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        String fileContent = "This is a test file.";
        String charsetName = "UTF-16";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(fileContent);
        }

        Document document = DataUtil.load(testFile, charsetName);
        assertEquals("This is a test file.", document.body().text(), "Unexpected document content");
        testFile.delete();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null);
        assertEquals("This is a test input stream.", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        String charsetName = "UTF-16";

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] utf16Bytes = inputStreamContent.getBytes(StandardCharsets.UTF_16LE);
        outputStream.write(utf16Bytes);
        InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Document document = DataUtil.load(testInputStream, charsetName);
        assertEquals("This is a test input stream.", document.body().text(), "Unexpected document content");
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><body><h1>This is a test input stream.</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        assertEquals("This is a test input stream.", document.body().select("h1").text(), "Unexpected document content");
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputContent = "This is a test input file.";
        File inputFile = new File("inputFile.html");
        inputFile.createNewFile();
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write(inputContent);
        }

        File outputFile = new File("outputFile.html");
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            Document document = DataUtil.load(new File("inputFile.html"), null);
            DataUtil.crossStreams(document.outputStream(), outputStream);
            String outputContent = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
            assertEquals(inputContent, outputContent, "Unexpected output file content");
        }

        testFile.delete();
        outputFile.delete();
    }

    @Test
    public void testParseInputStream() throws IOException {
        String inputStreamContent = "This is a test input stream.";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";

        Document document = DataUtil.parseInputStream(inputStream, charsetName, null, DataUtil.Parser.htmlParser());
        assertEquals("This is a test input stream.", document.body().text(), "Unexpected document content");
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String inputStreamContent = "<html><body><h1>This is a test input stream.</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";

        Document document = DataUtil.parseInputStream(inputStream, charsetName, null, DataUtil.Parser.xmlParser());
        assertEquals("This is a test input stream.", document.body().select("h1").text(), "Unexpected document content");
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String inputStreamContent = "<html><body><h1>This is a test input stream.</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "invalidCharset";

        Document document = null;
        try {
            document = DataUtil.parseInputStream(inputStream, charsetName, null, DataUtil.Parser.htmlParser());
            fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }

        assertEquals(null, document);
    }

    @Test
    public void testParseInputStreamWithInvalidParser() throws IOException {
        String inputStreamContent = "<html><body><h1>This is a test input stream.</h1></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";

        Document document = null;
        try {
            document = DataUtil.parseInputStream(inputStream, charsetName, null, DataUtil.Parser.textParser());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        assertEquals(null, document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputContent = "This is a test file.";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(inputContent);
        }

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(testFile));
        String contentFromByteBuffer = new String(byteBuffer.array(), StandardCharsets.UTF_8);
        assertEquals(inputContent, contentFromByteBuffer, "Unexpected file content");

        testFile.delete();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputContent = "This is a test file.";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(inputContent);
        }

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(testFile), 10);
        assertEquals(10, byteBuffer.limit(), "Unexpected byte buffer size");

        testFile.delete();
    }

    @Test
    public void testReadToByteBufferWithInvalidMaxSize() throws IOException {
        String inputContent = "This is a test file.";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(inputContent);
        }

        ByteBuffer byteBuffer = null;
        try {
            byteBuffer = DataUtil.readToByteBuffer(new FileInputStream(testFile), -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        assertEquals(null, byteBuffer);

        testFile.delete();
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "This is a test file.";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(fileContent);
        }

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);
        String contentFromByteBuffer = new String(byteBuffer.array(), StandardCharsets.UTF_8);
        assertEquals(fileContent, contentFromByteBuffer, "Unexpected file content");

        testFile.delete();
    }

    @Test
    public void testEmptyByteBuffer() throws IOException {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit(), "Unexpected byte buffer size");
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset, "Unexpected charset");

        contentType = "text/html";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(null, charset, "Unexpected charset");

        contentType = "application/xml; charset= shifts_jis";
        charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("shifts_jis", charset, "Unexpected charset");
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals("UTF-8", DataUtil.validateCharset(charset), "Unexpected charset");

        charset = null;
        assertEquals(null, DataUtil.validateCharset(charset), "Unexpected charset");

        charset = "UTF-16";
        assertEquals("UTF-16", DataUtil.validateCharset(charset), "Unexpected charset");

        charset = "INVALID_CHARSET";
        assertEquals(null, DataUtil.validateCharset(charset), "Unexpected charset");
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        String expectedBoundary = new String(new char[32]).replace("\0", "-");
        assertEquals(expectedBoundary, boundary, "Unexpected mime boundary");
    }

    @Test
    public void testDetectCharsetFromBom() throws IOException {
        String bomUtf8 = "\uFEFFThis is a test BOM.";
        ByteBuffer byteBufferUtf8 = ByteBuffer.wrap(bomUtf8.getBytes(StandardCharsets.UTF_8));
        BomCharset bomUtf8Result = DataUtil.detectCharsetFromBom(byteBufferUtf8);
        assertEquals("UTF-8", bomUtf8Result.charset, "Unexpected charset");
        assertEquals(0, bomUtf8Result.offset, "Unexpected offset");

        String bomUtf16 = "\uFFFE\uFEFFThis is a test BOM.";
        ByteBuffer byteBufferUtf16 = ByteBuffer.wrap(bomUtf16.getBytes(StandardCharsets.UTF_16LE));
        BomCharset bomUtf16Result = DataUtil.detectCharsetFromBom(byteBufferUtf16);
        assertEquals(null, bomUtf16Result, "Unexpected result");
    }
}

class FileWriter {
    FileWriter(File file) throws IOException {
        this.file = file;
        this.outputStream = new FileOutputStream(file);
    }

    void write(String content) throws IOException {
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        outputStream.write(contentBytes);
    }

    void close() throws IOException {
        outputStream.close();
        file.delete();
    }

    private File file;
    private FileOutputStream outputStream;
}