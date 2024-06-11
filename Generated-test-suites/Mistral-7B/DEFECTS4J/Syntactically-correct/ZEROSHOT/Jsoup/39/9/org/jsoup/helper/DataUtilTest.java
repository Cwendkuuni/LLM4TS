import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataUtilTest {

    private static final String TEST_FILE_NAME = "testFile.html";
    private static final String TEST_CHARSET = "UTF-8";
    private static final String TEST_BASE_URI = "http://test.com";
    private static final Pattern CHARSET_PATTERN = DataUtil.charsetPattern;
    private static final Parser HTML_PARSER = DataUtil.Parser.htmlParser();

    private File testFile;
    private InputStream testInputStream;
    private Document testDocument;

    @Before
    public void setUp() throws IOException {
        testFile = new File(TEST_FILE_NAME);
        FileOutputStream testFileOutputStream = new FileOutputStream(testFile);
        testFileOutputStream.write("Test file content.".getBytes());
        testFileOutputStream.close();

        testInputStream = new FileInputStream(testFile);
    }

    @Test
    public void testLoadFile() throws IOException {
        Document document = DataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
        assertNotNull(document);
        assertEquals("Test file content.", document.body().text());
    }

    @Test
    public void testLoadInputStream() throws IOException {
        ByteArrayOutputStream testByteArrayOutputStream = new ByteArrayOutputStream();
        testByteArrayOutputStream.write("Test stream content.".getBytes());
        testByteArrayOutputStream.close();
        InputStream testInputStream = new ByteArrayInputStream(testByteArrayOutputStream.toByteArray());

        Document document = DataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI);
        assertNotNull(document);
        assertEquals("Test stream content.", document.body().text());
    }

    @Test
    public void testLoadInputStreamWithCharsetDetectedFromFile() throws IOException {
        File testFileWithMetaTag = new File(TEST_FILE_NAME + "_charset.html");
        FileOutputStream testFileOutputStream = new FileOutputStream(testFileWithMetaTag);
        testFileOutputStream.write("<html lang=\"en\">".getBytes());
        testFileOutputStream.write("<head><meta charset=\"GBK\"><title>Test file</title></head>".getBytes());
        testFileOutputStream.write("<body>Test file content.</body></html>".getBytes());
        testFileOutputStream.close();

        InputStream testInputStream = new FileInputStream(testFileWithMetaTag);
        Document document = DataUtil.load(testInputStream, null, TEST_BASE_URI);
        assertEquals("GBK", document.outputSettings().charset());
        assertEquals("Test file content.", document.body().text());

        testInputStream.close();
        new File(TEST_FILE_NAME + "_charset.html").delete();
    }

    @Test
    public void testLoadInputStreamWithCharsetDetectedFromMetaTag() throws IOException {
        File testFileWithMetaTag = new File(TEST_FILE_NAME + "_charset.html");
        FileOutputStream testFileOutputStream = new FileOutputStream(testFileWithMetaTag);
        testFileOutputStream.write("<html lang=\"en\">".getBytes());
        testFileOutputStream.write("<head><meta charset=\"ISO-8859-1\"><title>Test file</title></head>".getBytes());
        testFileOutputStream.write("<body>Test file content.</body></html>".getBytes());
        testFileOutputStream.close();

        InputStream testInputStream = new FileInputStream(testFileWithMetaTag);
        Document document = DataUtil.load(testInputStream, null, TEST_BASE_URI);
        assertEquals("ISO-8859-1", document.outputSettings().charset());
        assertEquals("Test file content.", document.body().text());

        testInputStream.close();
        new File(TEST_FILE_NAME + "_charset.html").delete();
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithUnsupportedCharset() throws IOException {
        File testFileWithUnsupportedMetaTag = new File(TEST_FILE_NAME + "_charset.html");
        FileOutputStream testFileOutputStream = new FileOutputStream(testFileWithUnsupportedMetaTag);
        testFileOutputStream.write("<html lang=\"en\">".getBytes());
        testFileOutputStream.write("<head><meta charset=\"X-PASCAL\"><title>Test file</title></head>".getBytes());
        testFileOutputStream.write("<body>Test file content.</body></html>".getBytes());
        testFileOutputStream.close();

        InputStream testInputStream = new FileInputStream(testFileWithUnsupportedMetaTag);
        DataUtil.load(testInputStream, null, TEST_BASE_URI);
        new File(TEST_FILE_NAME + "_charset.html").delete();
    }

    @Test
    public void testParseByteData() {
        ByteArrayOutputStream testByteArrayOutputStream = new ByteArrayOutputStream();
        testByteArrayOutputStream.write("Test data content.".getBytes());
        testByteArrayOutputStream.close();
        ByteBuffer testByteBuffer = ByteBuffer.wrap(testByteArrayOutputStream.toByteArray());

        Document document = DataUtil.parseByteData(testByteBuffer, TEST_CHARSET, TEST_BASE_URI, HTML_PARSER);
        assertNotNull(document);
        assertEquals("Test data content.", document.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        ByteArrayOutputStream testByteArrayOutputStream = new ByteArrayOutputStream();
        testByteArrayOutputStream.write("Test buffer content.".getBytes());
        testByteArrayOutputStream.close();
        InputStream testInputStream = new ByteArrayInputStream(testByteArrayOutputStream.toByteArray());

        ByteBuffer testByteBuffer = DataUtil.readToByteBuffer(testInputStream);
        assertEquals(testByteArrayOutputStream.size(), testByteBuffer.limit());

        testByteBuffer.rewind();
        String testString = Charset.forName(TEST_CHARSET).decode(testByteBuffer).toString();
        assertEquals("Test buffer content.", testString);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeWithUnsupportedCharset() {
        String contentType = "text/html; charset=X-PASCAL";
        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }
}

class Validate {
    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new RuntimeException(message);
        }
    }
}