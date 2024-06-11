package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFileWithValidCharset() throws Exception {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, charsetName, baseUri);
        assertEquals("Expected document to be loaded correctly", document, DataUtil.parseInputStream(getInputStreamFromFile(file), charsetName, baseUri, Parser.htmlParser()));
    }

    @Test
    public void testLoadFileWithInvalidCharset() throws Exception {
        File file = new File("test.html");
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        assertThrows(IOException.class, () -> DataUtil.load(file, charsetName, baseUri));
    }

    @Test
    public void testLoadFileWithNullCharset() throws Exception {
        File file = new File("test.html");
        String baseUri = "http://example.com";

        Document document = DataUtil.load(file, null, baseUri);
        assertEquals("Expected document to be loaded correctly with default charset", document, DataUtil.parseInputStream(getInputStreamFromFile(file), DataUtil.defaultCharset, baseUri, Parser.htmlParser()));
    }

    @Test
    public void testLoadInputStreamWithValidCharset() throws Exception {
        String inputString = "<html><head><meta charset='UTF-8'><title>Test</title></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(DataUtil.defaultCharset));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals("Expected document to be loaded correctly", document, DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser()));
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws Exception {
        String inputString = "<html><head><meta charset='UTF-8'><title>Test</title></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(DataUtil.defaultCharset));
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStreamWithNullCharset() throws Exception {
        String inputString = "<html><head><meta charset='UTF-8'><title>Test</title></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(DataUtil.defaultCharset));
        String baseUri = "http://example.com";

        Document document = DataUtil.load(inputStream, null, baseUri);
        assertEquals("Expected document to be loaded correctly with default charset", document, DataUtil.parseInputStream(inputStream, DataUtil.defaultCharset, baseUri, Parser.htmlParser()));
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws Exception {
        String inputString = "<html><head><meta charset='UTF-8'><title>Test</title></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(DataUtil.defaultCharset));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);

        when(parser.parseInput(anyString(), eq(baseUri))).thenReturn(new Document(baseUri));

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertEquals("Expected document to be loaded correctly with custom parser", document, DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser));
    }

    @Test
    public void testCrossStreams() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test Data".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertEquals("Expected 'Test Data' to
be written to output stream", "Test Data", outputStream.toString());
    }

    @Test
    public void testParseInputStreamWithValidCharset() throws IOException {
        String inputString = "<html><head><meta charset='UTF-8'><title>Test</title></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(DataUtil.defaultCharset));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser());
        assertEquals("Expected document to be parsed correctly", document, DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testParseInputStreamWithInvalidCharset() throws IOException {
        String inputString = "<html><head><meta charset='UTF-8'><title>Test</title></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(DataUtil.defaultCharset));
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";

        assertThrows(IOException.class, () -> DataUtil.parseInputStream(inputStream, charsetName, baseUri, Parser.htmlParser()));
    }

    @Test
    public void testParseInputStreamWithNullCharset() throws IOException {
        String inputString = "<html><head><meta charset='UTF-8'><title>Test</title></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(DataUtil.defaultCharset));
        String baseUri = "http://example.com";

        Document document = DataUtil.parseInputStream(inputStream, null, baseUri, Parser.htmlParser());
        assertEquals("Expected document to be parsed correctly with default charset", document, DataUtil.load(inputStream, DataUtil.defaultCharset, baseUri));
    }

    @Test
    public void testParseInputStreamWithCustomParser() throws IOException {
        String inputString = "<html><head><meta charset='UTF-8'><title>Test</title></head><body><div>Hello World</div></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputString.getBytes(DataUtil.defaultCharset));
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = mock(Parser.class);

        when(parser.parseInput(anyString(), eq(baseUri))).thenReturn(new Document(baseUri));

        Document document = DataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        assertEquals("Expected document to be parsed correctly with custom parser", document, DataUtil.load(inputStream, charsetName, baseUri));
    }

    private InputStream getInputStreamFromFile(File file) throws IOException {
        return new FileInputStream(file);
    }
}