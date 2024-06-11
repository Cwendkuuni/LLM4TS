package org.jsoup.helper;

import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
    public void testLoad() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>";
        File tempFile = File.createTempFile("test", null);
        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            fileWriter.write(fileContent);
        }

        Document document = DataUtil.load(tempFile, "UTF-8", "/");
        assertEquals("Hello World", document.title());

        try (InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes("UTF-8"))) {
            document = DataUtil.load(inputStream, "UTF-8", "/");
            assertEquals("Hello World", document.title());
        }

        String charsetFromContentType = DataUtil.getCharsetFromContentType("text/html; charset=UTF-8");
        assertEquals("UTF-8", charsetFromContentType);

        ByteBuffer byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(fileContent.getBytes("UTF-8")));
        Document documentFromByteData = DataUtil.parseByteData(byteData, "UTF-8", "/", Parser.htmlParser());
        assertEquals("Hello World", documentFromByteData.title());

        // Test with empty input
        Document documentFromEmptyInput = DataUtil.parseByteData(ByteBuffer.allocate(0), null, null, Parser.htmlParser());
        assertEquals(null, documentFromEmptyInput);

        // Test with invalid charset
        Document documentFromInvalidCharset = DataUtil.parseByteData(byteData, "INVALID_CHARSET", "/", Parser.htmlParser());
        assertEquals(null, documentFromInvalidCharset);

        tempFile.deleteOnExit();
    }

    @Test(expected = IOException.class)
    public void testLoadFileNotFound() throws IOException {
        Document document = DataUtil.load(new File("non_existent_file.html"), null, "/");
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        String fileContent = "Hello World";
        File tempFile = File.createTempFile("test", null);
        try (FileWriter fileWriter = new FileWriter(tempFile)) {
            fileWriter.write(fileContent);
        }

        ByteBuffer byteData = DataUtil.readToByteBuffer(new FileInputStream(tempFile));
        assertEquals(fileContent, Charset.forName("UTF-8").decode(byteData).toString());

        byteData = DataUtil.readToByteBuffer(new ByteArrayInputStream(fileContent.getBytes("UTF-8")));
        assertEquals(fileContent, Charset.forName("UTF-8").decode(byteData).toString());

        tempFile.deleteOnExit();
    }

    @Test
    public void testGetCharsetFromContentType() {
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=utf-8"));
        assertEquals("UTF-8", DataUtil.getCharsetFromContentType("text/html; charset=UTF-8; foo=bar"));
        assertEquals(null, DataUtil.getCharsetFromContentType("text/html"));
        assertEquals(null, DataUtil.getCharsetFromContentType("application/json"));
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Hello World</h1>\n" +
                "</body>\n" +
                "</html>";
        ByteBuffer byteData = ByteBuffer.wrap(fileContent.getBytes("UTF-8"));
        Document document = DataUtil.parseByteData(byteData, "UTF-8", "/", Parser.htmlParser());
        assertEquals("Hello World", document.title());

        byteData = ByteBuffer.wrap(fileContent.getBytes("ISO-8859-1"));
        document = DataUtil.parseByteData(byteData, "ISO-8859-1", "/", Parser.htmlParser());
        assertEquals("Hello World", document.title());

        byteData = ByteBuffer.wrap(fileContent.getBytes("UTF-16BE"));
        document = DataUtil.parseByteData(byteData, null, "/", Parser.htmlParser());
        assertEquals("Hello World", document.title());

        byteData = ByteBuffer.wrap(fileContent.getBytes("UTF-16LE"));
        document = DataUtil.parseByteData(byteData, null, "/", Parser.htmlParser());
        assertEquals("Hello World", document.title());
    }
}