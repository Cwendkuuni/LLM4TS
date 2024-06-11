package org.jsoup.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import org.junit.Test;

/**
 * Test class for {@link DataUtil}.
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
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        File testFile = new File("testFile.html");
        Writer testFileWriter = new BufferedWriter(new FileWriter(testFile));
        testFileWriter.write(fileContent);
        testFileWriter.close();

        Document doc = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(doc);

        testFile.delete(); // cleanup
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.load(testInputStream, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='ISO-8859-1'?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("ISO-8859-1")));

        Document doc = DataUtil.load(testInputStream, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xhtml='http://www.w3.org/1999/xhtml'>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "</head>\n" +
                "<body xmlns='http://www.w3.org/1999/xhtml'>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        InputStream testInputStream = new ByteArrayInputStream(fileContent.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.load(testInputStream, "UTF-8", null, DataUtil.xmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testParseByteData() throws IOException {
        String fileContent = "<?xml version='1.0' encoding='UTF-8'?>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='UTF-8'>\n" +
                "</head>\n" +
                "<body>\n" +
                "<p>Hello World!</p>\n" +
                "</body>\n" +
                "</html>";

        ByteBuffer byteBuffer = ByteBuffer.wrap(fileContent.getBytes(Charset.forName("UTF-8")));

        Document doc = DataUtil.parseByteData(byteBuffer, "UTF-8", null, DataUtil.htmlParser());
        assertNotNull(doc);
    }

    @Test
    public void testDetermineCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertTrue(charset != null && charset.equals("UTF-8"));
    }

    @Test
    public void testDetermineCharsetFromContentTypeInvalidCharset() {
        String contentType = "text/html; charset=foo";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertTrue(charset == null);
    }

    @Test
    public void testDetermineCharsetFromContentTypeNoCharset() {
        String contentType = "text/html";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertTrue(charset == null);
    }
}