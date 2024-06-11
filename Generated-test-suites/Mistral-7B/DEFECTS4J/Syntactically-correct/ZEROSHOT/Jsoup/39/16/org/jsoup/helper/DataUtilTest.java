package org.jsoup.helper;

import org.jsoup.HelperException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Mock
    private Parser parser;

    private DataUtil util;

    @Before
    public void setup() {
        util = new DataUtil();
        lenient().when(parser.parseInput(any(String.class), ArgumentMatchers.anyString())).thenReturn(createMockDocument());
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        InputStream inputStream = new FileInputStream(file);
        ByteBuffer byteData = util.readToByteBuffer(inputStream);
        Document document = util.load(byteData, charsetName, baseUri, parser);

        assertEquals("Expected document to be loaded", "Test Document", document.title());
        assertNotNull("Expected document to not be null", document);

        inputStream.close();
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";

        InputStream inputStream = new ByteArrayInputStream("Test Document".getBytes());
        Document document = util.load(inputStream, charsetName, baseUri, parser);

        assertEquals("Expected document to be loaded", "Test Document", document.title());
        assertNotNull("Expected document to not be null", document);
    }

    @Test(expected = HelperException.class)
    public void testLoadInputStreamWithNullCharsetName() throws IOException {
        String baseUri = "http://test.com";

        InputStream inputStream = new ByteArrayInputStream("Test Document".getBytes());
        util.load(inputStream, null, baseUri, parser);
    }

    @Test
    public void testParseByteData() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://test.com";
        String docData = "<html><head><meta charset=\"UTF-16\"></head><body><h1>Test Document</h1></body></html>";

        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());
        Document document = util.parseByteData(byteData, charsetName, baseUri, parser);

        assertEquals("Expected document to be loaded", "Test Document", document.title());
        assertNotNull("Expected document to not be null", document);

        ByteBuffer byteDataWithCharset = ByteBuffer.wrap(docData.getBytes());
        byteDataWithCharset.rewind();
        String newCharsetName = "UTF-16";
        document = util.parseByteData(byteDataWithCharset, newCharsetName, baseUri, parser);

        assertEquals("Expected document to be loaded with new charset", "Test Document", document.title());
        assertNotNull("Expected document to not be null", document);
    }

    @Test(expected = HelperException.class)
    public void testParseByteDataWithNullCharsetName() throws IOException {
        String baseUri = "http://test.com";
        ByteBuffer byteData = ByteBuffer.wrap("Test Document".getBytes());
        util.parseByteData(byteData, null, baseUri, parser);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test Document".getBytes());

        ByteBuffer byteData = util.readToByteBuffer(inputStream);
        byte[] expectedByteArray = "Test Document".getBytes();
        byte[] actualByteArray = new byte[byteData.limit()];
        byteData.get(actualByteArray);

        assertArrayEquals("Expected byte arrays to be equal", expectedByteArray, actualByteArray);

        inputStream.close();
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("Test Document".getBytes());
        int maxSize = 10;

        ByteBuffer byteData = util.readToByteBuffer(inputStream, maxSize);
        byte[] expectedByteArray = "Test Docu".getBytes();
        byte[] actualByteArray = new byte[byteData.limit()];
        byteData.get(actualByteArray);

        assertArrayEquals("Expected byte arrays to be equal (truncated)", expectedByteArray, actualByteArray);

        inputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        assertEquals("Expected charset to be UTF-8", "UTF-8", util.getCharsetFromContentType(contentType));

        contentType = "text/html";

        assertNull("Expected charset to be null", util.getCharsetFromContentType(contentType));
    }

    private Document createMockDocument() {
        Document document = Mockito.mock(Document.class);
        Element title = Mockito.mock(Element.class);
        Mockito.when(title.text()).thenReturn("Test Document");
        Mockito.when(document.title()).thenReturn(title);
        return document;
    }
}