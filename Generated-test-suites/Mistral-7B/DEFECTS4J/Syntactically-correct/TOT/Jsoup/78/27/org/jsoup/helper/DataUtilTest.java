package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private DataUtil dataUtil;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataUtil = new DataUtil();
    }

    @After
    public void cleanup() throws Exception {
        // Close any open streams, etc.
    }

    @Test
    public void testLoadFileWithValidCharset() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        Document expectedDocument = new Document(baseUri);

        InputStream expectedInputStream = getClass().getResourceAsStream("/test.html");
        Document actualDocument = dataUtil.load(inputFile, charsetName, baseUri);
        assertEquals(expectedDocument, actualDocument);

        assertEquals(expectedInputStream.readAllBytes(), actualDocument.body().bytes());
    }

    @Test
    public void testLoadFileWithoutCharset() throws IOException {
        File inputFile = new File("test.html");
        String baseUri = "http://example.com/";

        Document actualDocument = dataUtil.load(inputFile, null, baseUri);
        assertNull(actualDocument);
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws IOException {
        File inputFile = new File("test.html");
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com/";

        Document actualDocument = dataUtil.load(inputFile, charsetName, baseUri);
        assertNull(actualDocument);
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws IOException {
        String inputData = "Test data.";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        Document expectedDocument = new Document(baseUri);

        Document actualDocument = dataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(expectedDocument, actualDocument);

        assertEquals(inputData, actualDocument.body().text());
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String inputData = "Test data.";
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com/";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        Document actualDocument = dataUtil.load(inputStream, charsetName, baseUri);
        assertNull(actualDocument);
    }

    @Test
    public void testLoadInputStreamWithNoCharset() throws IOException {
        String inputData = "Test data.";
        String baseUri = "http://example.com/";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        Document actualDocument = dataUtil.load(inputStream, null, baseUri);
        assertNull(actualDocument);
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String inputData = "Test data.";
        String charsetName = "UTF-8";
        String baseUri = "http://example.com/";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        Parser parser = Parser.htmlParser();
        Document expectedDocument = parser.parseInput(inputData, baseUri);

        Document actualDocument = dataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertEquals(expectedDocument, actualDocument);

        assertEquals(inputData, actualDocument.body().text());
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String inputData = "Test data.";
        String charsetName = "invalidCharset";
        String baseUri = "http://example.com/";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        Parser parser = Parser.htmlParser();
        Document actualDocument = dataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertNull(actualDocument);
    }

    @Test
    public void testParseInputStreamWithNoCharset() throws IOException {
        String inputData = "Test data.";
        String baseUri = "http://example.com/";
        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));

        Parser parser = Parser.htmlParser();
        Document actualDocument = dataUtil.parseInputStream(inputStream, null, baseUri, parser);
        assertNull(actualDocument);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputData = "Test data.";
        String outputData = "Test data copied.";

        InputStream inputStream = new ByteArrayInputStream(inputData.getBytes(StandardCharsets.UTF_8));
        OutputStream outputStream = new ByteArrayOutputStream();

        dataUtil.crossStreams(inputStream, outputStream);
        assertEquals(inputData, outputStream.toString());
    }
}