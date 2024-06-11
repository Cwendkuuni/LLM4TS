package org.jsoup.helper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DataUtil.class})
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<html><head></head><body></body></html>";
        File file = new File("testFile.html");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(fileContent.getBytes());
        PowerMockito.mockStatic(Charset.class);
        PowerMockito.when(Charset.forName(DataUtil.defaultCharset)).thenReturn(Charset.forInputBytes(fileContent.getBytes()));
        PowerMockito.when(Charset.forInputBytes(fileContent.getBytes())).thenReturn(Charset.forName(DataUtil.defaultCharset));
        PowerMockito.when(Charset.isSupported(DataUtil.defaultCharset)).thenReturn(true);

        Document document = DataUtil.load(file, DataUtil.defaultCharset, null);

        assertNotNull(document);
        assertEquals("<html><head></head><body></body></html>", document.body().html());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String inputStreamContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        Document document = DataUtil.load(inputStream, null, null);

        assertNotNull(document);
        assertEquals("<html><head></head><body></body></html>", document.body().html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String inputStreamContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        PowerMockito.mockStatic(Charset.class);
        PowerMockito.when(Charset.forName("UTF-8")).thenReturn(Charset.forInputBytes(inputStreamContent.getBytes()));

        Document document = DataUtil.load(inputStream, "UTF-8", null);

        assertNotNull(document);
        assertEquals("<html><head></head><body></body></html>", document.body().html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedParser() throws IOException {
        String inputStreamContent = "<html><head></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        Parser parser = Mockito.mock(Parser.class);

        Document document = DataUtil.load(inputStream, null, null, parser);

        assertNotNull(document);
        Mockito.verify(parser).parseInput(inputStreamContent, null);
    }

    @Test
    public void testCrossStreams() throws IOException {
        String inputStreamContent = "testContent";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        DataUtil.crossStreams(inputStream, outputStream);

        assertNotNull(outputStream.toString());
        assertEquals(inputStreamContent, outputStream.toString());
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String byteDataContent = "<html><head></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteDataContent.getBytes());

        Document document = DataUtil.parseByteData(byteBuffer, null, null, Parser.htmlParser());

        assertNotNull(document);
        assertEquals("<html><head></head><body></body></html>", document.body().html());
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        String byteDataContent = "<html><head></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteDataContent.getBytes());
        String charsetName = "UTF-8";

        Document document = DataUtil.parseByteData(byteBuffer, charsetName, null, Parser.htmlParser());

        assertNotNull(document);
        assertEquals("<html><head></head><body></body></html>", document.body().html());
    }

    @Test
    public void testParseByteDataWithSpecifiedParser() throws IOException {
        String byteDataContent = "<html><head></head><body></body></html>";
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteDataContent.getBytes());
        Parser parser = Mockito.mock(Parser.class);

        Document document = DataUtil.parseByteData(byteBuffer, null, null, parser);

        assertNotNull(document);
        Mockito.verify(parser).parseInput(byteDataContent, null);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "testContent";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byteBuffer.flip();
        byteBuffer.get(outputStream.toByteArray(), 0, (int) byteBuffer.remaining());

        assertEquals(inputStreamContent, outputStream.toString());
    }

    @Test
    public void testReadToByteBufferWithMaxSize() throws IOException {
        String inputStreamContent = "testContent";
        InputStream inputStream = new ByteArrayInputStream(inputStreamContent.getBytes());
        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 10);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byteBuffer.flip();
        byteBuffer.get(outputStream.toByteArray(), 0, (int) byteBuffer.remaining());

        assertEquals(inputStreamContent.substring(0, 10), outputStream.toString());
    }

    @Test
    public void testReadFileToByteBuffer() throws IOException {
        String fileContent = "<html><head></head><body></body></html>";
        File file = new File("testFile.html");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(fileContent.getBytes());
        file.deleteOnExit(); // Automatically deletes the file after test execution

        ByteBuffer byteBuffer = DataUtil.readFileToByteBuffer(file);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byteBuffer.flip();
        byteBuffer.get(outputStream.toByteArray(), 0, (int) byteBuffer.remaining());

        assertEquals(fileContent, outputStream.toString());
    }

    @Test
    public void testEmptyByteBuffer() {
        ByteBuffer byteBuffer = DataUtil.emptyByteBuffer();

        assertEquals(ByteBuffer.allocate(0).position(), byteBuffer.position());
        assertEquals(ByteBuffer.allocate(0).limit(), byteBuffer.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=EUC-JP";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("EUC-JP", charset);
    }

    @Test
    public void testGetCharsetFromContentType_InvalidCharset() {
        String contentType = "text/html; charset=InvalidCharset";

        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testGetCharsetFromContentType_NoCharset() {
        String contentType = "text/html";

        assertNull(DataUtil.getCharsetFromContentType(contentType));
    }

    @Test
    public void testValidateCharset() {
        String charset = "UTF-8";

        assertEquals(charset, DataUtil.validateCharset(charset));
    }

    @Test
    public void testValidateCharset_NullCharset() {
        assertNull(DataUtil.validateCharset(null));
    }

    @Test
    public void testMimeBoundary() {
        String mimeBoundary = DataUtil.mimeBoundary();

        assertNotNull(mimeBoundary);
    }
}