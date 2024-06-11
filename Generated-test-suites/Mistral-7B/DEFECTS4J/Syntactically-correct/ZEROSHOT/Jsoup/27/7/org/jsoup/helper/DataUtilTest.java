package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import org.junit.Test;

/**
 * DataUtilTest class to test all methods in DataUtil class.
 *
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String filePath = "test.html";
        File inputFile = new File(filePath);
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080/";

        Document document = DataUtil.load(inputFile, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadFileWithInvalidCharsetName() throws IOException {
        String filePath = "test.html";
        File inputFile = new File(filePath);
        String charsetName = "";
        String baseUri = "http://localhost:8080/";

        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputFile, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080/";

        Document document = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharsetName() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        String charsetName = "";
        String baseUri = "http://localhost:8080/";

        assertThrows(IllegalArgumentException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes(Charset.forName("UTF-8")));
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080/";
        Parser parser = mock(Parser.class);

        Document document = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(document);

        when(parser.parseInput(htmlContent, baseUri)).thenReturn(document);
    }

    @Test
    public void testParseByteDataWithValidCharsetName() {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        byte[] byteData = htmlContent.getBytes(Charset.forName("UTF-8"));
        String charsetName = "UTF-8";
        String baseUri = "http://localhost:8080/";
        Parser parser = mock(Parser.class);

        Document document = DataUtil.parseByteData(ByteBuffer.wrap(byteData), charsetName, baseUri, parser);
        assertNotNull(document);

        when(parser.parseInput(htmlContent, baseUri)).thenReturn(document);
    }

    @Test
    public void testParseByteDataWithInvalidCharsetName() {
        String htmlContent = "<html><head><meta charset=\"UTF-8\"></head><body></body></html>";
        byte[] byteData = htmlContent.getBytes(Charset.forName("UTF-8"));
        String charsetName = "";
        String baseUri = "http://localhost:8080/";
        Parser parser = mock(Parser.class);

        assertThrows(IllegalArgumentException.class, () -> DataUtil.parseByteData(ByteBuffer.wrap(byteData), charsetName, baseUri, parser));
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputContent = "Test Content";
        InputStream inputStream = new ByteArrayInputStream(inputContent.getBytes(Charset.forName("UTF-8")));

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        byte[] expectedByteData = inputContent.getBytes(Charset.forName("UTF-8"));
        byte[] actualByteData = byteData.array();
        assertArrayEquals(expectedByteData, actualByteData);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals(charset, "UTF-8");
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/html";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}