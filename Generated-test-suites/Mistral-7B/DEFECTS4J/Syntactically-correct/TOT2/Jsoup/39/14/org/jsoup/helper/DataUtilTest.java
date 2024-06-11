import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("testFile.html");
        Document doc = DataUtil.load(testFile, "UTF-8", null);
        Assert.assertNotNull(doc);

        // Test edge cases: file not found
        File nonExistentFile = new File("nonExistentFile.html");
        Document docNull = DataUtil.load(nonExistentFile, "UTF-8", null);
        Assert.assertNull(docNull);

        // Test edge cases: unsupported charset
        File unsupportedCharsetFile = new File("unsupportedCharsetFile.html");
        Document docUnsupported = DataUtil.load(unsupportedCharsetFile, "GBK", null);
        Assert.assertNotNull(docUnsupported);
        // Add assertions to check the document's content or other properties to further validate the test
    }

    @Test
    public void testLoadInputStream() throws IOException {
        // ...
    }

    @Test
    public void testLoadInputStreamCharsetBaseUriParser() throws IOException {
        // ...
    }

    @Test
    public void testParseByteData() {
        // ...
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        // ...
    }

    @Test
    public void testReadToByteBufferLargeInput() throws IOException {
        // ...
    }

    @Test
    public void testGetCharsetFromContentType() {
        // ...
    }

    @Test
    public void testGetCharsetFromContentTypeNoCharset() {
        // ...
    }
}