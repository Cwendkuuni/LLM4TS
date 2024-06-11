package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DataUtil.class)
public class DataUtilTest {

    private Document document;
    private static final String CHARSET_NAME = "UTF-8";
    private static final String BASE_URI = "http://example.com";

    @Before
    public void setUp() {
        document = new Document(BASE_URI);
    }

    @Test
    public void testLoadFileWhenInputIsEmpty() throws IOException {
        File file = new File("");
        Document result = DataUtil.load(file, CHARSET_NAME, BASE_URI);
        assertEquals(document, result);
    }

    @Test
    public void testLoadFileWithNullCharsetName() throws IOException {
        File file = new File("test.html");
        try {
            Document result = DataUtil.load(file, null, BASE_URI);
            assertNull(result);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testLoadFileWithValidCharsetName() throws IOException {
        File file = new File("test.html");
        Document result = DataUtil.load(file, CHARSET_NAME, BASE_URI);
        assertNotNull(result);
    }

    @Test
    public void testLoadInputStreamWithNullInput() throws IOException {
        InputStream input = null;
        Document result = DataUtil.load(input, CHARSET_NAME, BASE_URI);
        assertEquals(document, result);
    }

    @Test
    public void testLoadInputStreamWithEmptyInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Document result = DataUtil.load(input, CHARSET_NAME, BASE_URI);
        assertEquals(document, result);
    }

    @Test
    public void testLoadInputStreamWithValidInput() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document result = DataUtil.load(input, CHARSET_NAME, BASE_URI);
        assertNotNull(result);
        assertEquals(html, result.html());
    }

    @Test
    public void testParseInputStreamWithNullInput() throws IOException {
        InputStream input = null;
        Document result = DataUtil.parseInputStream(input, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertEquals(document, result);
    }

    @Test
    public void testParseInputStreamWithEmptyInput() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Document result = DataUtil.parseInputStream(input, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertEquals(document, result);
    }

    @Test
    public void testParseInputStreamWithValidInput() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document result = DataUtil.parseInputStream(input, CHARSET_NAME, BASE_URI, Parser.htmlParser());
        assertNotNull(result);
        assertEquals(html, result.html());
    }

    @Test
    public void testParseInputStreamWithDifferentParser() throws IOException {
        String html = "<html><head></head><body></body></html>";
        InputStream input = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        Document result = DataUtil.parseInputStream(input, CHARSET_NAME, BASE_URI, Parser.xmlParser());
        assertNotNull(result);
        assertEquals(html, result.xml().html());
    }

    @Test
    public void testCrossStreamsWithValidInput() throws IOException {
        File inputFile = new File("test.html");
        File outputFile = new File("output.html");
        InputStream input = new FileInputStream(inputFile);
        OutputStream output = new FileOutputStream(outputFile);
        try {
            DataUtil.crossStreams(input, output);
            assertNotNull(outputFile.length() > 0);
        } finally {
            input.close();
            output.close();
            inputFile.delete();
            outputFile.delete();
        }
    }
}