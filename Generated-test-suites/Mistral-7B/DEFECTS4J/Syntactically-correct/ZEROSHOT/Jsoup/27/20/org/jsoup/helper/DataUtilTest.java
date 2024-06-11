package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    private DataUtil dataUtil;
    private Parser parser;
    private Document document;

    @Before
    public void setUp() {
        parser = mock(Parser.class);
        dataUtil = new DataUtil();
        document = mock(Document.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "This is a test.";
        File file = new File("test.html");
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] fileBytes = fileContent.getBytes();
        byteArrayOutputStream.write(fileBytes);
        ByteBuffer byteBuffer = ByteBuffer.allocate(fileBytes.length);
        byteBuffer.put(fileBytes);
        byteBuffer.flip();

        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);

        Document result = dataUtil.load(file, null, null);

        assertEquals(document, result);
        fileInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithIOException() throws IOException {
        File file = new File("test.html");

        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenThrow(new IOException("Test IOException"));

        dataUtil.load(file, null, null);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "This is a test.";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);

        Document result = dataUtil.load(byteArrayInputStream, null, null);

        assertEquals(document, result);
        byteArrayInputStream.close();
    }

    @Test
    public void testLoadInputStreamWithCharset() throws IOException {
        String inputStreamContent = "This is a test.";
        String charset = "UTF-8";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputStreamContent.getBytes(Charset.forName(charset)));

        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);

        Document result = dataUtil.load(byteArrayInputStream, charset, null);

        assertEquals(document, result);
        byteArrayInputStream.close();
    }

    @Test
    public void testParseByteDataWithDefaultCharset() {
        String byteData = "This is a test.";
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteData.getBytes(Charset.forName("UTF-8")));

        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);

        Document result = dataUtil.parseByteData(byteBuffer, null, null, parser);

        assertEquals(document, result);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() {
        String byteData = "This is a test.";
        String charset = "UTF-8";

        when(parser.parseInput(Mockito.anyString(), Mockito.anyString())).thenReturn(document);

        Document result = dataUtil.parseByteData(ByteBuffer.wrap(byteData.getBytes(Charset.forName(charset))), charset, null, parser);

        assertEquals(document, result);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "This is a test.";
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[DataUtil.bufferSize];

        int read = 0;
        while((read = byteArrayInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, read);
        }

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(byteArrayOutputStream.toByteArray());
        ByteBuffer resultByteBuffer = dataUtil.readToByteBuffer(byteArrayInputStream);

        assertEquals(expectedByteBuffer, resultByteBuffer);
        byteArrayInputStream.close();
        byteArrayOutputStream.close();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        assertEquals("UTF-8", dataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        assertNull(dataUtil.getCharsetFromContentType(null));
    }
}