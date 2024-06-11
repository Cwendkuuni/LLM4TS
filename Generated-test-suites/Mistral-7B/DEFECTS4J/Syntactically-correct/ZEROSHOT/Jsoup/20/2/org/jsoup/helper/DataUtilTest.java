import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
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

import static org.junit.Assert.*;

public class DataUtilTest {

    private static File testFile;
    private static InputStream testInputStream;
    private static ByteArrayOutputStream testOutputStream;
    private static ByteArrayInputStream testInputByteStream;
    private static Parser parser;

    @BeforeClass
    public static void setup() {
        testFile = new File("src/test/resources/test.html");
        testInputStream = new FileInputStream(testFile);
        testOutputStream = new ByteArrayOutputStream();
        testInputByteStream = new ByteArrayInputStream(testOutputStream.toByteArray());
        parser = Mockito.mock(Parser.class);
    }

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        Document document = DataUtil.load(testFile, charsetName, baseUri);

        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullInputStream() {
        DataUtil.load(null, "UTF-8", "http://example.com");
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithNullCharset() {
        File file = new File("src/test/resources/test.html");
        Document document = DataUtil.load(file, null, "http://example.com");
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";

        InputStream inputStream = testInputStream;
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);
        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNotNull(document);
        assertEquals(charsetName, document.outputSettings().charset());
    }

    @Test
    public void testParseByteDataWithNullByteData() {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        ByteBuffer byteData = ByteBuffer.allocate(0);

        Document document = DataUtil.parseByteData(byteData, charsetName, baseUri, parser);

        assertNull(document);
    }

    @Test
    public void testParseByteDataWithNullCharset() {
        ByteBuffer byteData = ByteBuffer.wrap(new byte[]{'H', 'e', 'l', 'l', 'o'});
        String baseUri = "http://example.com";

        Document document = DataUtil.parseByteData(byteData, null, baseUri, parser);

        assertNull(document);
    }

    @Test
    public void testReadToByteBufferWithNullInputStream() throws IOException {
        InputStream inputStream = null;

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertNull(byteData);
    }

    @Test
    public void testReadToByteBufferWithEmptyInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);

        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        assertEquals(0, byteData.limit());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertEquals("UTF-8", charset);
    }

    @Test
    public void testGetCharsetFromContentTypeWithNullContentType() {
        String contentType = null;

        String charset = DataUtil.getCharsetFromContentType(contentType);

        assertNull(charset);
    }
}