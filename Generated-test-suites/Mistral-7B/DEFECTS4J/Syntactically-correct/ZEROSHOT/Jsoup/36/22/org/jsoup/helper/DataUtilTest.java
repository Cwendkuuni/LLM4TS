package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private DataUtil dataUtil;
    private Parser parser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parser = mock(Parser.class);
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        try (FileWriter fileWriter = new FileWriter(testFile)) {
            fileWriter.write(fileContent);
        }

        Document expectedDocument = DataUtil.load(testFile, "UTF-8", "/");
        Document actualDocument = dataUtil.load(testFile, "UTF-8", "/");

        assertEquals(expectedDocument, actualDocument);

        testFile.delete();
    }

    @Test
    public void testLoadFileNoCharset() throws IOException {
        String fileContent = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        try (FileWriter fileWriter = new FileWriter(testFile)) {
            fileWriter.write(fileContent);
        }

        Document expectedDocument = DataUtil.load(testFile, null, "/");
        Document actualDocument = dataUtil.load(testFile, null, "/");

        assertEquals(expectedDocument, actualDocument);

        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFileIOException() throws IOException {
        File testFile = new File("testFile.html");
        testFile.createNewFile();
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read()).thenReturn(-1);

        dataUtil.load(testFile, null, "/", inputStream);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String testString = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes());

        Document expectedDocument = DataUtil.load(null, "UTF-8", "/", testInputStream);
        Document actualDocument = dataUtil.load(null, "UTF-8", "/", testInputStream);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStreamNoCharset() throws IOException {
        String testString = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes());

        Document expectedDocument = DataUtil.load(null, null, "/", testInputStream);
        Document actualDocument = dataUtil.load(null, null, "/", testInputStream);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteData() throws IOException {
        String testString = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes());

        Document expectedDocument = DataUtil.parseByteData(byteBuffer, "UTF-8", "/", parser);
        Document actualDocument = dataUtil.parseByteData(byteBuffer, "UTF-8", "/", parser);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataDetectCharset() throws IOException {
        String testString = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=UTF-8'><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes());

        Document expectedDocument = DataUtil.parseByteData(byteBuffer, null, "/", parser);
        Document actualDocument = dataUtil.parseByteData(byteBuffer, null, "/", parser);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataInvalidCharset() throws IOException {
        String testString = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=EUC-JP'><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes());

        Document expectedDocument = null;
        Document actualDocument = dataUtil.parseByteData(byteBuffer, null, "/", parser);

        assertNull(expectedDocument);
        assertNull(actualDocument);
    }

    @Test
    public void testParseByteDataInvalidMetaCharset() throws IOException {
        String testString = "<html><head><meta charset='EUC-JP'><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(testString.getBytes());

        Document expectedDocument = null;
        Document actualDocument = dataUtil.parseByteData(byteBuffer, null, "/", parser);

        assertNull(expectedDocument);
        assertNull(actualDocument);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String testString = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes());

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(testString.getBytes());
        ByteBuffer actualByteBuffer = dataUtil.readToByteBuffer(testInputStream);

        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testReadToByteBufferMaxSize() throws IOException {
        String testString = "<?xml version='1.0' encoding='UTF-8'?>\n<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        InputStream testInputStream = new ByteArrayInputStream(testString.getBytes());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int read;

        while ((read = testInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, read);
        }

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        ByteBuffer actualByteBuffer = dataUtil.readToByteBuffer(testInputStream, 1024);

        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String expectedCharset = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, charset);
    }
}