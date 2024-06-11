package org.jsoup.helper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

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

/**
 * Test class for DataUtil.
 */
public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        assertEquals("Test", doc.title());

        FileUtils.deleteQuietly(testFile);
    }

    @Test(expected = IOException.class)
    public void testLoadFileInvalidCharset() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        File testFile = new File("testFile.html");
        FileUtils.writeStringToFile(testFile, fileContent, "UTF-8");

        Document doc = null;
        try {
            doc = DataUtil.load(testFile, "ISO-8859-1", null);
        } finally {
            FileUtils.deleteQuietly(testFile);
        }

        fail("Expected IOException but no exception was thrown.");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertEquals("Test", doc.title());
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamInvalidCharset() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));

        Document doc = null;
        try {
            doc = DataUtil.load(inputStream, "ISO-8859-1", null);
        } finally {
            inputStream.close();
        }

        fail("Expected IOException but no exception was thrown.");
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteBuffer = ByteBuffer.allocate(fileContent.getBytes("UTF-8").length);
        byteBuffer.put(fileContent.getBytes("UTF-8"));
        byteBuffer.flip();

        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", null, Parser.htmlParser());
        assertEquals("Test", doc.title());
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Test</h1>\n" +
                "</body>\n" +
                "</html>";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"));
        byte[] buffer = new byte[bufferSize];

        ByteBuffer byteBuffer = DataUtil.readToByteBuffer(inputStream, 0);

        assertEquals(fileContent.getBytes("UTF-8").length, byteBuffer.limit());
        assertEquals(fileContent.getBytes("UTF-8"), byteBuffer.array());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType1 = "text/html; charset=UTF-8";
        String contentType2 = "text/html; charset=EUC-JP";
        String contentType3 = "text/html";

        String charset1 = DataUtil.getCharsetFromContentType(contentType1);
        String charset2 = DataUtil.getCharsetFromContentType(contentType2);
        String charset3 = DataUtil.getCharsetFromContentType(contentType3);

        assertEquals("UTF-8", charset1);
        assertEquals("EUC-JP", charset2);
        assertEquals(null, charset3);
    }
}