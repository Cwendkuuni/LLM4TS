package org.jsoup.helper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.helper.DataUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {

    private static final String TEST_FILE_PATH = "test.html";
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "https://example.com";

    private File testFile;

    @Before
    public void setUp() throws Exception {
        testFile = new File(TEST_FILE_PATH);
        testFile.createNewFile();
    }

    @After
    public void tearDown() throws Exception {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body></body></html>";
        writeFileContent(testFile, testHtml);

        Document document = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
        assertNotNull(document);
        assertEquals(TEST_BASE_URI, document.baseUri());
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        String testHtml = "<html><head><meta charset=\"GBK\"><title>Test</title></head><body></body></html>";
        writeFileContent(testFile, testHtml);

        Document document = null;
        try {
            document = DataUtil.load(testFile, null, TEST_BASE_URI);
        } catch (IOException e) {
            // Expect an IOException
        }
        assertNotNull(document);
        assertEquals(TEST_BASE_URI, document.baseUri());
    }

    @Test
    public void testLoadStreamWithValidCharset() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testHtml.getBytes(TEST_CHARSET));

        Document document = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI);
        assertNotNull(document);
        assertEquals(TEST_BASE_URI, document.baseUri());
    }

    @Test
    public void testLoadStreamWithInvalidCharset() throws IOException {
        String testHtml = "<html><head><meta charset=\"GBK\"><title>Test</title></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testHtml.getBytes());

        Document document = null;
        try {
            document = DataUtil.load(testInputStream, null, TEST_BASE_URI);
        } catch (IOException e) {
            // Expect an IOException
        }
        assertNotNull(document);
        assertEquals(TEST_BASE_URI, document.baseUri());
    }

    @Test
    public void testLoadStreamWithParser() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testHtml.getBytes(TEST_CHARSET));

        Document document = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());
        assertNotNull(document);
        assertEquals(TEST_BASE_URI, document.baseUri());
    }

    @Test
    public void testParseByteDataWithValidCharset() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body></body></html>";
        String documentData = Charset.forName(TEST_CHARSET).decode(testHtml.getBytes(TEST_CHARSET)).toString();

        ByteBuffer byteBuffer = ByteBuffer.wrap(testHtml.getBytes(TEST_CHARSET));
        Document document = DataUtil.parseByteData(byteBuffer, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(TEST_BASE_URI, document.baseUri());
    }

    @Test
    public void testParseByteDataWithInvalidCharsetDetected() throws IOException {
        String testHtml = "<html><head><meta charset=\"GBK\"><title>Test</title></head><body></body></html>";
        String documentData = Charset.forName(TEST_CHARSET).decode(testHtml.getBytes(TEST_CHARSET)).toString();

        ByteBuffer byteBuffer = ByteBuffer.wrap(testHtml.getBytes());
        Document document = null;
        try {
            document = DataUtil.parseByteData(byteBuffer, null, TEST_BASE_URI, Parser.htmlParser());
        } catch (IOException e) {
            // Expect an IOException
        }
        assertNotNull(document);
        assertEquals(TEST_BASE_URI, document.baseUri());
    }

    @Test
    public void testParseByteDataWithValidCharsetDetected() throws IOException {
        String testHtml = "<html><head><meta charset=\"UTF-8\"><title>Test</title></head><body></body></html>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">";
        String documentData = Charset.forName(TEST_CHARSET).decode(testHtml.getBytes(TEST_CHARSET)).toString();

        ByteBuffer byteBuffer = ByteBuffer.wrap(testHtml.getBytes());
        Document document = DataUtil.parseByteData(byteBuffer, null, TEST_BASE_URI, Parser.htmlParser());
        assertNotNull(document);
        assertEquals(TEST_BASE_URI, document.baseUri());
    }

    private void writeFileContent(File file, String content) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(content);
        }
    }

}