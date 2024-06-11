package org.jsoup.helper;

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
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private static final String HTML_FILE_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<html lang=\"en\">" +
            "<head>" +
            "<meta charset=\"UTF-8\">" +
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">" +
            "</head>" +
            "<body>" +
            "<div>Test content</div>" +
            "</body>" +
            "</html>";

    private DataUtil dataUtil;
    private Parser parserMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parserMock = mock(Parser.class);
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoad_FileWithCharset_ReturnsDocument() throws IOException {
        // Given
        File inputFile = new File("input.html");
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document document = dataUtil.load(inputFile, charset, baseUri);

        // Then
        assertNotNull(document);
        assertEquals(HTML_FILE_CONTENT, document.body().text());
        verify(parserMock, times(1)).parseInput(eq(HTML_FILE_CONTENT), eq(baseUri));
    }

    @Test
    public void testLoad_FileWithoutCharset_DetectsCharsetFromContentTypeAndMetaTag() throws IOException {
        // Given
        File inputFile = new File("input.html");
        String baseUri = "http://example.com";

        // When
        Document document = dataUtil.load(inputFile, null, baseUri);

        // Then
        assertEquals(HTML_FILE_CONTENT, document.body().text());
        verify(parserMock, times(1))
                .parseInput(eq(HTML_FILE_CONTENT.substring(1)), eq(baseUri)); // exclude UTF-8 BOM
        verify(parserMock, times(1)).parseInput(eq(HTML_FILE_CONTENT), eq(baseUri)); // for the actual parsed document
    }

    @Test
    public void testLoad_InputStreamWithCharset_ReturnsDocument() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream(HTML_FILE_CONTENT.getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document document = dataUtil.load(inputStream, charset, baseUri);

        // Then
        assertNotNull(document);
        assertEquals(HTML_FILE_CONTENT, document.body().text());
    }

    @Test
    public void testLoad_InputStreamWithoutCharset_DetectsCharsetFromContentTypeAndMetaTag() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream(HTML_FILE_CONTENT.getBytes());
        String baseUri = "http://example.com";

        // When
        Document document = dataUtil.load(inputStream, null, baseUri);

        // Then
        assertEquals(HTML_FILE_CONTENT, document.body().text());
        verify(parserMock, times(1))
                .parseInput(eq(HTML_FILE_CONTENT.substring(1).getBytes()), eq(baseUri)); // exclude UTF-8 BOM
        verify(parserMock, times(1)).parseInput(eq(HTML_FILE_CONTENT.getBytes()), eq(baseUri)); // for the actual parsed document
    }

    @Test
    public void testLoad_InputStreamWithParser_ReturnsDocument() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream(HTML_FILE_CONTENT.getBytes());
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document document = dataUtil.load(inputStream, charset, baseUri, parserMock);

        // Then
        assertNotNull(document);
        assertEquals(HTML_FILE_CONTENT, document.body().text());
        verify(parserMock, times(1)).parseInput(eq(HTML_FILE_CONTENT.substring(1).getBytes()), eq(baseUri)); // exclude UTF-8 BOM
        verify(parserMock, times(1)).parseInput(eq(HTML_FILE_CONTENT.getBytes()), eq(baseUri)); // for the actual parsed document
    }

    @Test
    public void testParseByteData() throws IOException {
        // Given
        ByteBuffer byteData = ByteBuffer.wrap(HTML_FILE_CONTENT.getBytes());
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        // When
        Document document = dataUtil.parseByteData(byteData, charsetName, baseUri, parserMock);

        // Then
        assertNotNull(document);
        assertEquals(HTML_FILE_CONTENT, document.body().text());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // Given
        InputStream inputStream = new ByteArrayInputStream(HTML_FILE_CONTENT.getBytes());
        int maxSize = 1024;

        // When
        ByteBuffer byteData = dataUtil.readToByteBuffer(inputStream, maxSize);

        // Then
        assertEquals(HTML_FILE_CONTENT.length(), byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        // Given
        String contentType = "text/html; charset=UTF-8";

        // When
        String charset = dataUtil.getCharsetFromContentType(contentType);

        // Then
        assertEquals("UTF-8", charset);
    }
}