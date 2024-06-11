package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Test class for DataUtil.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    @Mock
    private Parser parser;

    @InjectMocks
    private DataUtil dataUtil;

    @Test
    public void testLoadFileDocument() throws IOException {
        File file = new File("testFile.html");
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Document expectedDocument = mock(Document.class);

        when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument);

        FileInputStream inputStream = mock(FileInputStream.class);
        ByteBuffer byteData = ByteBuffer.allocate(1024);

        when(file.length()).thenReturn(1024);
        when(inputStream.read(byteData.array())).thenReturn(1024);

        InputStream in = new FileInputStream(file);
        try {
            Document actualDocument = dataUtil.load(file, charsetName, baseUri);
            assertNotNull(actualDocument);
            assertEquals(expectedDocument, actualDocument);
        } finally {
            in.close();
        }
    }

    @Test
    public void testLoadInputStreamDocument() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Document expectedDocument = mock(Document.class);

        when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument);

        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());

        ByteBuffer byteData = ByteBuffer.allocate(10);
        when(byteData.limit()).thenReturn(10);

        Document actualDocument = dataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(actualDocument);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataDocument() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/test";
        Parser parser = mock(Parser.class);
        Document expectedDocument = mock(Document.class);

        when(parser.parseInput(anyString(), anyString())).thenReturn(expectedDocument);

        String docData = "test data";
        ByteBuffer byteData = ByteBuffer.wrap(docData.getBytes());

        Document actualDocument = dataUtil.parseByteData(byteData, charsetName, baseUri, parser);
        assertNotNull(actualDocument);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test data".getBytes());
        ByteBuffer expectedByteData = ByteBuffer.wrap("test data".getBytes());

        ByteBuffer actualByteData = dataUtil.readToByteBuffer(inputStream);
        assertEquals(expectedByteData, actualByteData);
    }

    @Test
    public void testDetermineCharset() {
        String contentType = "text/html; charset=EUC-JP";
        String expectedCharset = "EUC-JP";

        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }

    @Test
    public void testDetermineCharsetNull() {
        String contentType = "text/html";
        String expectedCharset = null;

        String charset = dataUtil.getCharsetFromContentType(contentType);
        assertEquals(expectedCharset, charset);
    }
}