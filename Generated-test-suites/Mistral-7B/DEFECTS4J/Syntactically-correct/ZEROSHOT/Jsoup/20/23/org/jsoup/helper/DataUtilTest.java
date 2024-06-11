package org.jsoup.helper;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

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

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DataUtilTest {

    private DataUtil dataUtil;
    private Parser parser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        dataUtil = new DataUtil();
        parser = Parser.htmlParser();
    }

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "Hello World";
        File file = new File("test.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(fileContent);
        fileWriter.close();

        Document expectedDocument = parser.parseInput(fileContent, "");

        Document actualDocument = dataUtil.load(file, null, "");

        assertEquals(expectedDocument, actualDocument);

        file.delete();
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidFile() throws IOException {
        File invalidFile = new File("non-existent-file.html");
        dataUtil.load(invalidFile, null, "");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String inputStreamContent = "Hello World";
        byte[] inputStreamContentBytes = inputStreamContent.getBytes();

        InputStream inputStream = new ByteArrayInputStream(inputStreamContentBytes);

        Document expectedDocument = parser.parseInput(inputStreamContent, "");

        Document actualDocument = dataUtil.load(inputStream, null, "");

        assertEquals(expectedDocument, actualDocument);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidInput() throws IOException {
        InputStream invalidInput = new ByteArrayInputStream(new byte[0]);
        dataUtil.load(invalidInput, null, "");
    }

    @Test
    public void testParseByteData() throws IOException {
        String charset = "UTF-8";
        String documentContent = "Hello World";
        byte[] documentContentBytes = documentContent.getBytes(Charset.forName(charset));
        ByteBuffer byteBuffer = ByteBuffer.wrap(documentContentBytes);

        Document expectedDocument = parser.parseInput(documentContent, "");

        Document actualDocument = dataUtil.parseByteData(byteBuffer, charset, "", parser);

        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        String documentContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=EUC-JP\">\n</head>\n<body>\nHello World\n</body>\n</html>";
        byte[] documentContentBytes = documentContent.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.wrap(documentContentBytes);

        Document expectedDocument = parser.parseInput(documentContent, "");
        String expectedCharset = "EUC-JP";

        Document actualDocument = dataUtil.parseByteData(byteBuffer, null, "", parser);
        String actualCharset = actualDocument.outputSettings().charset();

        assertEquals(expectedDocument, actualDocument);
        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String inputStreamContent = "Hello World";
        byte[] inputStreamContentBytes = inputStreamContent.getBytes();
        InputStream inputStream = new ByteArrayInputStream(inputStreamContentBytes);

        ByteBuffer expectedByteBuffer = ByteBuffer.wrap(inputStreamContentBytes);
        ByteBuffer actualByteBuffer = dataUtil.readToByteBuffer(inputStream);

        assertEquals(expectedByteBuffer, actualByteBuffer);
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String expectedCharset = "UTF-8";

        String actualCharset = dataUtil.getCharsetFromContentType(contentType);

        assertEquals(expectedCharset, actualCharset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;
        assertNull(dataUtil.getCharsetFromContentType(contentType));
    }
}