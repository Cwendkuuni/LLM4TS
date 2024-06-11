import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import static org.junit.Assert.*;

public class DataUtilTest {

    private File testFile;

    @Before
    public void setUp() throws Exception {
        testFile = new File("path/to/your/test/file.html"); // Replace this with the path to your test file
    }

    @After
    public void tearDown() throws Exception {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testLoadFromFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(testFile, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFromFileWithInvalidCharset() throws IOException {
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        DataUtil.load(testFile, charsetName, baseUri);
    }

    @Test
    public void testLoadFromInputStream() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/your/test/file.html"); // Replace this with the path to your test file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
        assertNotNull(doc);
        inputStream.close();
    }

    @Test(expected = IOException.class)
    public void testLoadFromInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/your/test/file.html"); // Replace this with the path to your test file
        String charsetName = "InvalidCharset";
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(inputStream, charsetName, baseUri);
    }

    @Test
    public void testLoadFromInputStreamWithCustomParser() throws IOException {
        InputStream inputStream = new FileInputStream("path/to/your/test/file.xml"); // Replace this with the path to your test file
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Parser parser = DataUtil.Parser.xmlParser();
        Document doc = DataUtil.load(inputStream, charsetName, baseUri, parser);
        assertNotNull(doc);
        inputStream.close();
    }

    @Test
    public void testParseByteData() {
        File testFile = new File("path/to/your/test/file.html"); // Replace this with the path to your test file
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        String charsetName = null;
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertNotNull(doc);
        inputStream.close();

        testFile.delete();
    }

    @Test
    public void testParseByteDataWithDetectedCharset() {
        String testData = "Data to be tested\n<meta http-equiv='Content-Type' content='text/html; charset=UTF-16'>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData.getBytes("UTF-8"));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        String charsetName = null;
        String baseUri = "http://example.com";
        Document doc = DataUtil.parseByteData(byteData, charsetName, baseUri, Parser.htmlParser());
        assertEquals("UTF-16", doc.outputSettings().charset());
        inputStream.close();
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        File testFile = new File("path/to/your/test/file.html"); // Replace this with the path to your test file
        InputStream inputStream = new FileInputStream(testFile);
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        assertNotNull(byteData);
        inputStream.close();
        testFile.delete();
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeNull() {
        String contentType = null;
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertNull(charset);
    }
}