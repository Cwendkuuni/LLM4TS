import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Locale;

public final class DataUtilTest {

    @TempDir
    private static File tempDir;

    @BeforeAll
    public static void setup() {
        // Initialize any static variables or configurations here
    }

    // Test case for load(File, String, String) method
    @Test
    public void testLoadFile() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";
        File file = new File(tempDir, "test.html");
        String expectedContent = "Test content";

        // Write test HTML file to temp directory
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(expectedContent.getBytes(Charset.forName(charset)));
        }

        Document actualDoc = DataUtil.load(file, charset, baseUri);
        Assertions.assertNotNull(actualDoc);
        Assertions.assertEquals(expectedContent, actualDoc.body().text());
    }

    // Test case for load(InputStream, String, String) method
    @Test
    public void testLoadInputStream() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        String expectedContent = "Test content";
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes(Charset.forName(charset)));

        Document actualDoc = DataUtil.load(inputStream, charset, baseUri);
        Assertions.assertNotNull(actualDoc);
        Assertions.assertEquals(expectedContent, actualDoc.body().text());
    }

    // Test case for load(InputStream, String, String, Parser) method
    @Test
    public void testLoadInputStreamWithParser() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        String expectedContent = "Test content";
        InputStream inputStream = new ByteArrayInputStream(expectedContent.getBytes(Charset.forName(charset)));

        Document actualDoc = DataUtil.load(inputStream, charset, baseUri, Parser.xmlParser());
        Assertions.assertNotNull(actualDoc);
        Assertions.assertEquals(expectedContent, actualDoc.body().text());
    }

    // Test case for readToByteBuffer(InputStream) method
    @Test
    public void testReadToByteBuffer() throws IOException {
        String charset = "UTF-8";
        String testData = "Test data";

        InputStream inputStream = new ByteArrayInputStream(testData.getBytes(Charset.forName(charset)));
        ByteBuffer byteData = DataUtil.readToByteBuffer(inputStream);

        Assertions.assertNotNull(byteData);
        Assertions.assertEquals(testData.getBytes(Charset.forName(charset)).length, byteData.limit());
    }

    // Test case for parseByteData(ByteBuffer, String, String, Parser) method
    @Test
    public void testParseByteData() throws IOException {
        String charset = "UTF-8";
        String baseUri = "http://example.com";

        String testData = "Test data";
        ByteBuffer byteData = ByteBuffer.wrap(testData.getBytes(Charset.forName(charset)));

        Document actualDoc = DataUtil.parseByteData(byteData, charset, baseUri, Parser.htmlParser());
        Assertions.assertNotNull(actualDoc);
        Assertions.assertEquals(testData, actualDoc.body().text());
    }

    // Test case for detectCharsetFromBom() method
    @Test
    public void testDetectCharsetFromBom() {
        String charsetName = null;
        ByteBuffer byteData = DataUtil.emptyByteBuffer();

        byte[] bomUtf8 = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        byte[] bomUtf16 = {(byte) 0xFF, (byte) 0xFE, 0, 0};
        byte[] bomUtf32BE = {0, 0, (byte) 0xFE, (byte) 0xFF, 0, 0, 0, 0};
        byte[] bomUtf32LE = {0, 0, 0, 0, (byte) 0xFF, (byte) 0xFE, 0, 0};

        // Test UTF-8 BOM
        byteData.put(bomUtf8);
        byteData.position(0);
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        Assertions.assertEquals("UTF-8", charsetName);

        // Test UTF-16 BOM
        byteData.clear();
        byteData.put(bomUtf16);
        byteData.position(0);
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        Assertions.assertEquals("UTF-16", charsetName);

        // Test UTF-32 BE BOM
        byteData.clear();
        byteData.put(bomUtf32BE);
        byteData.position(0);
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        Assertions.assertEquals("UTF-32 BE", charsetName);

        // Test UTF-32 LE BOM
        byteData.clear();
        byteData.put(bomUtf32LE);
        byteData.position(0);
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        Assertions.assertEquals("UTF-32 LE", charsetName);

        // Test invalid BOM
        byteData.clear();
        byteData.put(new byte[]{0x01, 0x02, 0x03});
        byteData.position(0);
        charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        Assertions.assertNull(charsetName);
    }
}