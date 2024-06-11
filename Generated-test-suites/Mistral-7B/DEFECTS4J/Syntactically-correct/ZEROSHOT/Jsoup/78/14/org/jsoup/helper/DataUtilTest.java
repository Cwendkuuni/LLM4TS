package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Mock
    private InputStream inputStreamMock;
    @Mock
    private OutputStream outputStreamMock;
    @Mock
    private Parser parserMock;

    private DataUtil dataUtil;

    @Before
    public void setUp() {
        dataUtil = new DataUtil();
    }

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html");
        Document document = dataUtil.load(file, "UTF-8", "");
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "";
        Document document = dataUtil.load(inputStream, charsetName, baseUri);
        Mockito.verify(inputStream).close();
        assertNotNull(document);
    }

    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = Mockito.mock(Parser.class);
        Document document = dataUtil.load(inputStream, charsetName, baseUri, parser);
        Mockito.verify(inputStream).close();
        Mockito.verify(parser).parseInput(Mockito.anyString(), Mockito.eq(baseUri));
        assertNotNull(document);
    }

    @Test
    public void testCrossStreams() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        OutputStream outputStream = Mockito.mock(OutputStream.class);
        dataUtil.crossStreams(inputStream, outputStream);
        Mockito.verify(inputStream, Mockito.times(1)).read(Mockito.anyByteArray());
        Mockito.verify(outputStream, Mockito.times(1)).write(Mockito.anyByteArray(), Mockito.anyInt(), Mockito.anyInt());
    }

    @Test
    public void testParseInputStream() throws IOException {
        InputStream inputStream = inputStreamMock;
        String charsetName = "UTF-8";
        String baseUri = "";
        Parser parser = parserMock;

        Mockito.when(inputStream.mark(Mockito.anyInt())).thenReturn(inputStream);
        Mockito.when(inputStream.read()).thenReturn(1);
        Mockito.when(inputStream.reset()).thenReturn(inputStream);
        Mockito.when(inputStream.read()).thenReturn(-1);

        Mockito.when(parser.parseInput(Mockito.anyString(), Mockito.eq(baseUri))).thenReturn(new Document("", ""));

        Document document = dataUtil.parseInputStream(inputStream, charsetName, baseUri, parser);
        Mockito.verify(inputStream).close();
        assertNotNull(document);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream);
        Mockito.verify(inputStream).close();
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        InputStream inputStream = Mockito.mock(InputStream.class);
        ByteBuffer byteBuffer = dataUtil.readToByteBuffer(inputStream, 1024 * 32);
        Mockito.verify(inputStream).close();
        assertEquals(1024 * 32, byteBuffer.limit());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        File file = new File("test.html");
        ByteBuffer byteBuffer = dataUtil.readFileToByteBuffer(file);
        assertEquals(file.length(), byteBuffer.limit());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = dataUtil.emptyByteBuffer();
        assertEquals(0, byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";
        assertEquals("UTF-8", dataUtil.validateCharset(charset));
        charset = "UTF-8\t";
        assertNull(dataUtil.validateCharset(charset));
        charset = "UTF-8\"";
        assertNull(dataUtil.validateCharset(charset));
        charset = "UTF-8'";
        assertNull(dataUtil.validateCharset(charset));
        charset = "UTF-16";
        assertEquals("UTF-16", dataUtil.validateCharset(charset));
        charset = "UTF-32";
        assertEquals("UTF-32", dataUtil.validateCharset(charset));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = dataUtil.mimeBoundary();
        assertTrue(mimeBoundary.length() == 32);
        for (int i = 0; i < 32; i++) {
            char c = mimeBoundary.charAt(i);
            assertTrue(Character.isAlphabetic(c) || Character.isDigit(c) || c == '-' || c == '_');
        }
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[]{0xEF, 0xBB, 0xBF});
        BomCharset bomCharset = dataUtil.detectCharsetFromBom(byteBuffer);
        assertEquals("UTF-8", bomCharset.charset);
        assertEquals(3, bomCharset.offset);
    }

    @Test
    public void testDetectCharsetFromBomNull() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[0]);
        assertNull(dataUtil.detectCharsetFromBom(byteBuffer));
    }
}