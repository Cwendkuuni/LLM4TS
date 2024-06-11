package org.jsoup.helper;

import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public final class DataUtilTest {

    private static final String TEST_FILE = "test.html";
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://example.com/";
    private static final String TEST_MIME_BOUNDARY = DataUtil.mimeBoundary();

    @BeforeClass
    public static void setup() {
        // Prepare test HTML file content as a String
        String testHtml = "<html lang=\"en\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">" +
                "</head>" +
                "<body>" +
                "<p>Test content</p>" +
                "</body>" +
                "</html>";

        // Save test HTML file content to a ByteArrayOutputStream
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write(testHtml.getBytes(TEST_CHARSET));

        // Create a File object for the ByteArrayOutputStream
        File testFile = new File(TEST_FILE);

        // Write the ByteArrayOutputStream to the File
        FileWriter fileWriter = new FileWriter(testFile);
        fileWriter.write(testHtml);
        fileWriter.close();
    }

    @Test
    public void testLoadFile() throws IOException {
        // Given
        File testFile = new File(TEST_FILE);

        // When
        Document document = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);

        // Then
        assertNotNull(document);
        assertEquals(TEST_CHARSET, document.outputSettings().charset());
        assertEquals(TEST_BASE_URI, document.baseUri());
        assertEquals("<html lang=\"en\"><head><meta charset=\"UTF-8\"><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\"><body><p>Test content</p></body></html>", document.html());

        // Cleanup
        testFile.delete();
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        // Given
        String testContent = "Test content";
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write(testContent.getBytes(TEST_CHARSET));
        InputStream testInputStream = new ByteArrayInputStream(testOutputStream.toByteArray());

        // When
        Document document = DataUtil.load(testInputStream, null, TEST_BASE_URI);

        // Then
        assertNotNull(document);
        assertEquals(TEST_CHARSET, document.outputSettings().charset());
        assertEquals(TEST_BASE_URI, document.baseUri());
        assertEquals(testContent, document.text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        // Given
        String testContent = "Test content";
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write(testContent.getBytes(TEST_CHARSET));
        InputStream testInputStream = new ByteArrayInputStream(testOutputStream.toByteArray());

        // When
        Document document = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI);

        // Then
        assertNotNull(document);
        assertEquals(TEST_CHARSET, document.outputSettings().charset());
        assertEquals(TEST_BASE_URI, document.baseUri());
        assertEquals(testContent, document.text());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        // Given
        String testContent = "Test content";
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write(testContent.getBytes(TEST_CHARSET));
        InputStream testInputStream = new ByteArrayInputStream(testOutputStream.toByteArray());

        // When
        Document document = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());

        // Then
        assertNotNull(document);
        assertEquals(TEST_CHARSET, document.outputSettings().charset());
        assertEquals(TEST_BASE_URI, document.baseUri());
        assertEquals(testContent, document.xml().asXml());
    }

    @Test
    public void testCrossStreams() throws IOException {
        // Given
        File testInputFile = new File("input.html");
        File testOutputFile = new File("output.html");
        String testInputContent = "<html><body><p>Test input</p></body></html>";
        String testOutputContent = "<html><body><p>Test output</p></p></body></html>";
        ByteArrayOutputStream testInputOutputStream = new ByteArrayOutputStream();
        testInputOutputStream.write(testInputContent.getBytes(TEST_CHARSET));
        FileWriter testInputFileWriter = new FileWriter(testInputFile);
        testInputFileWriter.write(testInputContent);
        testInputFileWriter.close();
        InputStream testInputStream = new FileInputStream(testInputFile);
        ByteArrayOutputStream testOutputOutputStream = new ByteArrayOutputStream();

        // When
        DataUtil.crossStreams(testInputStream, testOutputStream);

        // Then
        InputStream testOutputStream = new FileInputStream(testOutputFile);
        String testOutputContentRead = new String(testOutputStream.readAllBytes(), TEST_CHARSET);
        assertTrue(testOutputContentRead.equals(testOutputContent));
        testOutputStream.close();
        testOutputFile.delete();

        // Cleanup
        testInputFile.delete();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        // Given
        String testContent = "Test content";
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write(testContent.getBytes(TEST_CHARSET));
        ByteBuffer testByteBuffer = ByteBuffer.wrap(testOutputStream.toByteArray());

        // When
        Document document = DataUtil.parseByteData(testByteBuffer, null, TEST_BASE_URI, Parser.htmlParser());

        // Then
        assertNotNull(document);
        assertEquals(TEST_CHARSET, document.outputSettings().charset());
        assertEquals(TEST_BASE_URI, document.baseUri());
        assertEquals(testContent, document.html());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        // Given
        String testContent = "Test content";
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write(testContent.getBytes(TEST_CHARSET));
        ByteBuffer testByteBuffer = ByteBuffer.wrap(testOutputStream.toByteArray());

        // When
        Document document = DataUtil.parseByteData(testByteBuffer, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());

        // Then
        assertNotNull(document);
        assertEquals(TEST_CHARSET, document.outputSettings().charset());
        assertEquals(TEST_BASE_URI, document.baseUri());
        assertEquals(testContent, document.html());
    }

    @Test
    public void testParseByteDataWithSpecifiedParser() throws IOException {
        // Given
        String testContent = "Test content";
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write(testContent.getBytes(TEST_CHARSET));
        ByteBuffer testByteBuffer = ByteBuffer.wrap(testOutputStream.toByteArray());

        // When
        Document document = DataUtil.parseByteData(testByteBuffer, null, TEST_BASE_URI, Parser.xmlParser());

        // Then
        assertNotNull(document);
        assertEquals(TEST_CHARSET, document.outputSettings().charset());
        assertEquals(TEST_BASE_URI, document.baseUri());
        assertEquals(testContent, document.xml().asXml());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Given
        String testContent = "Test content";
        ByteArrayOutputStream testOutputStream = new ByteArrayOutputStream();
        testOutputStream.write(testContent.getBytes(TEST_CHARSET));
        InputStream testInputStream = new ByteArrayInputStream(testOutputStream.toByteArray());

        // When
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(testInputStream);

        // Then
        assertEquals(testOutputStream.size(), byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        // Given
        String testContent = "Test content";
        File testFile = new File(TEST_FILE);
        FileWriter testFileWriter = new FileWriter(testFile);
        testFileWriter.write(testContent);
        testFileWriter.close();

        // When
        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(testFile);

        // Then
        assertEquals(testFile.length(), byteBuffer.limit());

        // Cleanup
        testFile.delete();
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Given
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "application/json; charset=gb2312";
        String contentType3 = "text/plain";

        // When
        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);

        // Then
        assertEquals(TEST_CHARSET, charset1);
        assertEquals("gb2312", charset2);
        assertNull(charset3);
    }

    @Test
    public void testMimeBoundary() {
        // Given

        // When
        String boundary = DataUtil.mimeBoundary();

        // Then
        assertEquals(boundary.length(), 32);
    }
}