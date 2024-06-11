package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(PowerMockRunner.class)
public class DataUtilTest {

    private static final String TEST_CHARSET = "US-ASCII";
    private static final String TEST_FILE_NAME = "testFile.html";
    private static final String TEST_BASE_URI = "http://test.com";
    private static final String TEST_INPUT_STREAM_CONTENT = "<html>" +
            "<head><meta charset='" + TEST_CHARSET + "'/></head>" +
            "<body></body>" +
            "</html>";

    private DataUtil dataUtil;
    private Document testDocument;
    private InputStream testInputStream;
    private ByteArrayInputStream testByteArrayInputStream;
    private ByteArrayOutputStream testByteArrayOutputStream;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dataUtil = new DataUtil();
        testDocument = PowerMockito.mock(Document.class);
        testInputStream = PowerMockito.mock(InputStream.class);
        testByteArrayInputStream = new ByteArrayInputStream(TEST_INPUT_STREAM_CONTENT.getBytes(StandardCharsets.UTF_8));
        testByteArrayOutputStream = new ByteArrayOutputStream();
    }

    @After
    public void tearDown() {
        PowerMockito.resetAll();
    }

    @Test
    public void testLoadFile() throws IOException {
        File testFile = PowerMockito.mock(File.class);
        PowerMockito.when(testFile.exists()).thenReturn(true);
        PowerMockito.when(testFile.length()).thenReturn(TEST_INPUT_STREAM_CONTENT.length());
        PowerMockito.when(testFile.getAbsolutePath()).thenReturn(TEST_FILE_NAME);

        Document result = dataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
        Document expected = PowerMockito.mock(Document.class);
        PowerMockito.when(Parser.htmlParser().parseInput(TEST_INPUT_STREAM_CONTENT, TEST_BASE_URI)).thenReturn(expected);
        PowerMockito.when(testFile.openInputStream()).thenReturn(testInputStream);

        PowerMockito.doAnswer(invocation -> dataUtil.load(testInputStream, TEST_CHARSET, TEST_BASE_URI)).when(dataUtil).load(testInputStream, TEST_CHARSET, TEST_BASE_URI);

        result = dataUtil.load(testFile, TEST_CHARSET, TEST_BASE_URI);
        assertSame(expected, result);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        Document result = dataUtil.load(testByteArrayInputStream, TEST_CHARSET, TEST_BASE_URI);
        Document expected = PowerMockito.mock(Document.class);
        PowerMockito.when(Parser.htmlParser().parseInput(TEST_INPUT_STREAM_CONTENT, TEST_BASE_URI)).thenReturn(expected);

        PowerMockito.doAnswer(invocation -> dataUtil.parseInputStream(testByteArrayInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser())).when(dataUtil).load(testByteArrayInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser());

        result = dataUtil.load(testByteArrayInputStream, TEST_CHARSET, TEST_BASE_URI);
        assertSame(expected, result);
    }

    @Test
    public void testParseInputStreamWithDefaultParser() throws IOException {
        Document result = dataUtil.load(testByteArrayInputStream, TEST_CHARSET, TEST_BASE_URI);
        Document expected = PowerMockito.mock(Document.class);
        PowerMockito.when(Parser.htmlParser().parseInput(TEST_INPUT_STREAM_CONTENT, TEST_BASE_URI)).thenReturn(expected);

        PowerMockito.doAnswer(invocation -> dataUtil.parseInputStream(testByteArrayInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.htmlParser())).when(dataUtil).load(testByteArrayInputStream, TEST_CHARSET, TEST_BASE_URI);

        result = dataUtil.load(testByteArrayInputStream, TEST_CHARSET, TEST_BASE_URI);
        assertSame(expected, result);
    }

    @Test
    public void testParseInputStreamWithCustomParser() throws IOException {
        Document result = dataUtil.load(testByteArrayInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());
        Document expected = PowerMockito.mock(Document.class);
        PowerMockito.when(Parser.xmlParser().parseInput(TEST_INPUT_STREAM_CONTENT, TEST_BASE_URI)).thenReturn(expected);

        PowerMockito.doAnswer(invocation -> dataUtil.parseInputStream(testByteArrayInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser())).when(dataUtil).load(testByteArrayInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());

        result = dataUtil.load(testByteArrayInputStream, TEST_CHARSET, TEST_BASE_URI, Parser.xmlParser());
        assertSame(expected, result);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = PowerMockito.mock(InputStream.class);
        OutputStream outputStream = PowerMockito.mock(OutputStream.class);

        ByteArrayOutputStream outputStreamSpy = PowerMockito.spy(testByteArrayOutputStream);

        dataUtil.crossStreams(testByteArrayInputStream, outputStreamSpy);

        PowerMockito.verifyNew(ByteArrayOutputStream.class).withCapacity(32 * 1024);
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = testByteArrayInputStream.read(buffer, 0, bufferSize)) != -1) {
            outputStreamSpy.write(buffer, 0, len);
        }
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = PowerMockito.mock(InputStream.class);
        ByteBuffer result = dataUtil.readToByteBuffer(inputStream, 1024);
        ByteBuffer expected = ByteBuffer.wrap(TestDataUtil.getTestDataBytes(1024));

        PowerMockito.doAnswer(invocation -> inputStream.read(TestDataUtil.getTestDataByteArray(1024))).when(inputStream).read(TestDataUtil.getTestDataByteArray(1024));

        assertEquals(expected, result);
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = PowerMockito.mock(File.class);
        ByteBuffer result = dataUtil.readFileToByteBuffer(file);
        ByteBuffer expected = ByteBuffer.wrap(TestDataUtil.getTestDataBytes(TEST_INPUT_STREAM_CONTENT.length()));

        PowerMockito.when(file.length()).thenReturn(TEST_INPUT_STREAM_CONTENT.length());
        PowerMockito.when(file.getAbsolutePath()).thenReturn(TEST_FILE_NAME);

        PowerMockito.doAnswer(invocation -> TestDataUtil.getTestDataBytes(TEST_INPUT_STREAM_CONTENT.length())).when(file).getBytes(StandardCharsets.UTF_8);

        assertEquals(expected, result);
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer result = dataUtil.emptyByteBuffer();
        ByteBuffer expected = ByteBuffer.allocate(0);

        assertEquals(expected, result);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String result = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        String expected = "UTF-8";

        assertEquals(expected, result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidCharset() {
        String result = DataUtil.getCharsetFromContentType("text/html; charset=invalidCharset");
        String expected = null;

        assertNull(result);
    }

    @Test
    public void testGetCharsetFromContentTypeWithoutCharset() {
        String result = DataUtil.getCharsetFromContentType("text/html");
        String expected = null;

        assertNull(result);
    }

    @Test
    public void testValidateCharset() {
        String result = DataUtil.validateCharset("UTF-8");
        String expected = "UTF-8";

        assertEquals(expected, result);
    }

    @Test
    public void testValidateCharsetWithInvalidCharset() {
        String result = DataUtil.validateCharset("invalidCharset");
        String expected = null;

        assertNull(result);
    }

    @Test
    public void testMimeBoundary() {
        String result = DataUtil.mimeBoundary();
        String expected = new String(new Random(new Random().nextLong()).nextBytes(32), StandardCharsets.UTF_8);

        assertEquals(expected, result);
    }
}

class TestDataUtil {
    public static byte[] getTestDataBytes(int length) {
        byte[] bytes = new byte[length];
        new Random().nextBytes(bytes);
        return bytes;
    }

    public static byte[] getTestDataByteArray(int length) {
        return getTestDataBytes(length);
    }
}