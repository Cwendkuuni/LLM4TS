package org.jsoup.helper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("test.html");
        Document doc = DataUtil.load(testFile, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"/></head><body>Test</body></html>";
        InputStream testStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(testStream, "UTF-8", null);
        assertNotNull(doc);
        assertTrue(doc.body().text().equals("Test"));
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"/></head><body>Test</body></html>";
        InputStream testStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(testStream, null, null);
        assertNotNull(doc);
        assertTrue(doc.body().text().equals("Test"));
    }

    @Test
    public void testLoadInputStreamWithBaseUri() throws IOException {
        String baseUri = "http://test.com";
        String testData = "<html><head></head><body><a href=\"test.html\">Link</a></body></html>";
        InputStream testStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.load(testStream, null, baseUri);
        assertNotNull(doc);
        assertTrue(doc.select("a[href]").text().equals("Link"));
        assertTrue(doc.select("a[href]").attr("abs:href").startsWith(baseUri));
    }

    @Test
    public void testParseInputStream() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"/></head><body>Test</body></html>";
        InputStream testStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.parseInputStream(testStream, "UTF-8", null, Parser.htmlParser());
        assertNotNull(doc);
        assertTrue(doc.body().text().equals("Test"));
    }

    @Test
    public void testParseInputStreamWithParser() throws IOException {
        String testData = "<html><head><meta charset=\"UTF-8\"/></head><body>Test</body></html>";
        InputStream testStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = DataUtil.parseInputStream(testStream, "UTF-8", null, Parser.xmlParser());
        assertNotNull(doc);
        assertTrue(doc.body().text().isEmpty());
    }

    @Test
    public void testCrossStreams() throws IOException {
        String testData = "Test";
        byte[] testInput = testData.getBytes();
        ByteArrayInputStream testInputStream = new ByteArrayInputStream(testInput);
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(testInputStream, testOutputStream);
        byte[] testOutput = testOutputStream.toByteArray();
        String testOutputString = new String(testOutput);

        assertTrue(testOutputString.equals(testData));
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String testData = "<html><head><meta charset=\"Invalid Charset\"/></head><body>Test</body></html>";
        InputStream testStream = new ByteArrayInputStream(testData.getBytes());
        Document doc = null;
        try {
            doc = DataUtil.parseInputStream(testStream, "Invalid Charset", null, Parser.htmlParser());
        } catch (IOException e) {
            // Expected
        }
        assertNull(doc);
    }

    @Test
    public void testDetectCharsetFromBom() {
        byte[] utf32Bom = {0x00, 0x00, (byte) 0xFE, (byte) 0xFF};
        byte[] utf16BomBE = {(byte) 0xFF, (byte) 0xFE, 0x00, 0x00};
        byte[] utf16BomLE = {0x00, 0x00, (byte) 0xFE, (byte) 0xFF};
        byte[] utf8Bom = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

        BomCharset utf32 = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(utf32Bom));
        BomCharset utf16BE = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(utf16BomBE));
        BomCharset utf16LE = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(utf16BomLE));
        BomCharset utf8 = DataUtil.detectCharsetFromBom(ByteBuffer.wrap(utf8Bom));

        assertTrue(utf32 != null);
        assertTrue(utf32.charset.equals("UTF-32"));
        assertTrue(utf32.offset);

        assertTrue(utf16BE != null);
        assertTrue(utf16BE.charset.equals("UTF-16"));
        assertTrue(utf16BE.offset);

        assertTrue(utf16LE != null);
        assertTrue(utf16LE.charset.equals("UTF-16"));
        assertFalse(utf16LE.offset);

        assertTrue(utf8 != null);
        assertTrue(utf8.charset.equals("UTF-8"));
        assertFalse(utf8.offset);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String testData = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(testData);
        assertTrue(charset != null);
        assertTrue(charset.equals("UTF-8"));

        testData = "text/html";
        charset = DataUtil.getCharsetFromContentType(testData);
        assertNull(charset);

        testData = "text/html; charset=Invalid Charset";
        charset = DataUtil.getCharsetFromContentType(testData);
        assertNull(charset);

        testData = "text/html; charset=GBK";
        charset = DataUtil.getCharsetFromContentType(testData);
        assertTrue(charset != null);
        assertTrue(charset.equalsIgnoreCase("GBK"));
    }

    @Test
    public void testValidateCharset() {
        String utf8 = "UTF-8";
        String gbk = "GBK";
        String invalidCharset = "Invalid Charset";

        String validatedUtf8 = DataUtil.validateCharset(utf8);
        String validatedGbk = DataUtil.validateCharset(gbk);
        String validatedInvalidCharset = DataUtil.validateCharset(invalidCharset);

        assertTrue(validatedUtf8 != null);
        assertTrue(validatedUtf8.equalsIgnoreCase("UTF-8"));

        assertTrue(validatedGbk != null);
        assertTrue(validatedGbk.equalsIgnoreCase("GBK"));

        assertNull(validatedInvalidCharset);
    }

    @Test
    public void testMimeBoundary() {
        String boundary = DataUtil.mimeBoundary();
        Random random = new Random();
        StringBuilder testBoundary = new StringBuilder();

        for (int i = 0; i < 32; i++) {
            testBoundary.append(mimeBoundaryChars[random.nextInt(mimeBoundaryChars.length)]);
        }

        assertTrue(boundary.length() == testBoundary.length());
        for (int i = 0; i < boundary.length(); i++) {
            assertTrue(boundary.charAt(i) == testBoundary.charAt(i));
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testData = "Test";
        InputStream testStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(testData.getBytes());
        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(testStream, 0);

        assertTrue(actualByteBuffer.remaining() == 0);
        assertTrue(expectedByteBuffer.equals(actualByteBuffer.asReadOnlyBuffer()));
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String testData = "Test";
        InputStream testStream = new ByteArrayInputStream(testData.getBytes());
        ByteBuffer actualByteBuffer = DataUtil.readToByteBuffer(testStream, testData.length());

        assertTrue(actualByteBuffer.remaining() == 0);
        assertTrue(expectedByteBuffer.equals(actualByteBuffer.asReadOnlyBuffer()));
    }
}