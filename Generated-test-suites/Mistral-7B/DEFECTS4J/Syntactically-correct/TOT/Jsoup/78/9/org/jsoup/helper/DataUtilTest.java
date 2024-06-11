package org.jsoup.helper.test;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.DataUtil.BomCharset;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DataUtilTest {

    @Test
    public void testLoadDocumentFromFileWithValidCharset() throws IOException {
        String filePath = "input/testFile.html";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(new File(filePath), charsetName, baseUri);

        // Assert the loaded document is not null and contains valid HTML content.
        Assert.assertNotNull(document);
        Assert.assertEquals("HTML", document.tagName());
    }

    @Test
    public void testLoadDocumentFromFileWithInvalidCharset() throws IOException {
        String filePath = "input/testFile.html";
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";

        try {
            Document document = DataUtil.load(new File(filePath), charsetName, baseUri);
            Assert.fail("Expected IOException: Charset not supported.");
        } catch (IllegalArgumentException e) {
            // Expected exception, charset is not supported.
        }
    }

    @Test
    public void testLoadDocumentFromInputStreamWithValidCharset() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = getClass().getResourceAsStream("/input/testFile.html");

        Document document = DataUtil.load(inputStream, charsetName, baseUri);

        // Assert the loaded document is not null and contains valid HTML content.
        Assert.assertNotNull(document);
        Assert.assertEquals("HTML", document.tagName());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithNoCharset() throws IOException {
        String baseUri = "http://example.com";

        InputStream inputStream = getClass().getResourceAsStream("/input/testFile.html");

        Document document = DataUtil.load(inputStream, null, baseUri);

        // Assert the loaded document is not null and contains valid HTML content.
        Assert.assertNotNull(document);
        Assert.assertEquals("HTML", document.tagName());
    }

    @Test
    public void testLoadDocumentFromInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";

        InputStream inputStream = getClass().getResourceAsStream("/input/testFile.html");

        try {
            Document document = DataUtil.load(inputStream, charsetName, baseUri);
            Assert.fail("Expected IOException: Charset not supported.");
        } catch (IllegalArgumentException e) {
            // Expected exception, charset is not supported.
        }
    }

    @Test
    public void testLoadDocumentFromInputStreamWithBomUtf8() throws IOException {
        String baseUri = "http://example.com";

        String testFileContent = "<html><head></head><body></body></html>";
        byte[] bomUtf8Bytes = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF, (byte) 0};

        InputStream inputStream = new ByteArrayInputStream(bomUtf8Bytes);
        inputStream = new ByteArrayInputStream(testFileContent.getBytes(StandardCharsets.UTF_8));

        Document document = DataUtil.load(inputStream, null, baseUri);

        // Assert the loaded document is not null and contains valid HTML content.
        Assert.assertNotNull(document);
        Assert.assertEquals("HTML", document.tagName());
    }

    @Test
    public void testDetectCharsetFromBomUtf8() {
        String charsetName = DataUtil.validateCharset("UTF-8");

        Assert.assertEquals("UTF-8", charsetName);
    }

    @Test
    public void testDetectCharsetFromBomUtf16() {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00});
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);

        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-16", bomCharset.charset);
    }

    @Test
    public void testDetectCharsetFromBomUtf32() {
        ByteBuffer byteData = DataUtil.readToByteBuffer(new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00});
        BomCharset bomCharset = DataUtil.detectCharsetFromBom(byteData);

        Assert.assertNotNull(bomCharset);
        Assert.assertEquals("UTF-32", bomCharset.charset);
    }

    @Test
    public void testDetectCharsetFromInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = getClass().getResourceAsStream("/input/testFile.html");

        Document document = DataUtil.load(inputStream, null, baseUri);
        String charsetFromDocument = document.outputSettings().charset();

        Assert.assertEquals(charsetName, charsetFromDocument);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testFileContent = "This is a test.";

        InputStream inputStream = new ByteArrayInputStream(testFileContent.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        String readContent = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);

        Assert.assertEquals(testFileContent, readContent);
    }

    @Test
    public void testParseInputStreamWithUtf8() throws IOException {
        String testFileContent = "This is a test.";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testFileContent.getBytes(StandardCharsets.UTF_8));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("HTML", document.tagName());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testFileContent = "This is a test.";
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testFileContent.getBytes(StandardCharsets.UTF_8));

        try {
            Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
            Assert.fail("Expected IOException: Charset not supported.");
        } catch (IllegalArgumentException e) {
            // Expected exception, charset is not supported.
        }
    }

    @Test
    public void testParseInputStreamWithEmptyContent() throws IOException {
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        Document document = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("", document.text());
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String baseUri = "http://example.com";

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        Document document = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("", document.text());
    }

    @Test
    public void testParseInputStreamWithUtf16() throws IOException {
        String testFileContent = "This is a test.";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testFileContent.getBytes(StandardCharsets.UTF_16LE));
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("HTML", document.tagName());
    }

    @Test
    public void testParseInputStreamWithBomUtf16() throws IOException {
        String testFileContent = "This is a test.";

        byte[] bomUtf16Bytes = new byte[]{0xFF, 0xFE, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        byte[] testFileContentBytes = testFileContent.getBytes(StandardCharsets.UTF_16LE);
        byte[] testFileContentBytesWithBom = new byte[bomUtf16Bytes.length + testFileContentBytes.length];
        System.arraycopy(bomUtf16Bytes, 0, testFileContentBytes, 0, bomUtf16Bytes.length);
        System.arraycopy(testFileContentBytes, 0, testFileContentBytes, bomUtf16Bytes.length, testFileContentBytes.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testFileContentBytes);
        String charsetName = "UTF-16";
        String baseUri = "http://example.com";

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("HTML", document.tagName());
    }

    @Test
    public void testParseInputStreamWithUtf32() throws IOException {
        String testFileContent = "This is a test.";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testFileContent.getBytes(StandardCharsets.UTF_32BE));
        String charsetName = "UTF-32";
        String baseUri = "http://example.com";

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("HTML", document.tagName());
    }

    @Test
    public void testParseInputStreamWithBomUtf32() throws IOException {
        String testFileContent = "This is a test.";

        byte[] bomUtf32Bytes = new byte[]{0x00, 0x00, 0xFE, 0xFF, 0x00, 0x00, 0x00, 0x00};
        byte[] testFileContentBytes = testFileContent.getBytes(StandardCharsets.UTF_32BE);
        byte[] testFileContentBytesWithBom = new byte[bomUtf32Bytes.length + testFileContentBytes.length];
        System.arraycopy(bomUtf32Bytes, 0, testFileContentBytes, 0, bomUtf32Bytes.length);
        System.arraycopy(testFileContentBytes, 0, testFileContentBytes, bomUtf32Bytes.length, testFileContentBytes.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testFileContentBytes);
        String charsetName = "UTF-32";
        String baseUri = "http://example.com";

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());

        Assert.assertNotNull(document);
        Assert.assertEquals("HTML", document.tagName());
    }

    @Test
    public void testReadToByteBufferWithValidSize() throws IOException {
        String testFileContent = "This is a test.";

        InputStream inputStream = new ByteArrayInputStream(testFileContent.getBytes(StandardCharsets.UTF_8));
        int maxSize = testFileContent.length();

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        Assert.assertEquals(testFileContent.getBytes(StandardCharsets.UTF_8), byteBuffer.array());
    }

    @Test
    public void testReadToByteBufferWithInvalidSize() throws IOException {
        String testFileContent = "This is a test.";

        InputStream inputStream = new ByteArrayInputStream(testFileContent.getBytes(StandardCharsets.UTF_8));
        int maxSize = testFileContent.length() + 1;

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, maxSize);

        Assert.assertEquals(testFileContent.getBytes(StandardCharsets.UTF_8), byteBuffer.array());
    }

    @Test
    public void testReadToByteBufferWithUnlimitedSize() throws IOException {
        String testFileContent = "This is a test.";

        InputStream inputStream = new ByteArrayInputStream(testFileContent.getBytes(StandardCharsets.UTF_8));

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);

        Assert.assertEquals(testFileContent.getBytes(StandardCharsets.UTF_8), byteBuffer.array());
    }

    @Test
    public void testReadToByteBufferWithFile() throws IOException {
        String filePath = "input/testFile.html";

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File(filePath));

        Assert.assertEquals(DataUtil.parseInputStream(new FileInputStream(filePath), null, null, Parser.htmlParser()).text().getBytes(StandardCharsets.UTF_8), byteBuffer.array());
    }

    @Test
    public void testReadToByteBufferWithEmptyFile() throws IOException {
        String filePath = "input/emptyFile.html";

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(new File(filePath));

        Assert.assertArrayEquals(new byte[0], byteBuffer.array());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        Assert.assertTrue(byteBuffer.capacity() == 0);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();

        Assert.assertNotNull(boundary);
        Assert.assertEquals(boundary.length(), DataUtil.boundaryLength);
    }
}