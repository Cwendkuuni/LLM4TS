import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("example.html"); // replace with actual file path
        String charsetName = "UTF-8";
        String baseUri = "/"; // replace with actual base URI
        Document doc = DataUtil.load(file, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "/"; // replace with actual base URI
        InputStream in = getClass().getResourceAsStream("/example.html"); // replace with actual input stream
        Document doc = DataUtil.load(in, charsetName, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = "invalidCharset";
        String baseUri = "/"; // replace with actual base URI
        InputStream in = getClass().getResourceAsStream("/example.html"); // replace with actual input stream
        try {
            Document doc = DataUtil.load(in, charsetName, baseUri);
            fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testParseByteDataWithDefaultCharset() throws IOException {
        byte[] byteData = new byte[]{(byte) 0xFF, (byte) 0xFE, /* ... */};
        String charsetName = null;
        String baseUri = "/"; // replace with actual base URI
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testParseByteDataWithSpecifiedCharset() throws IOException {
        byte[] byteData = new byte[]{(byte) 0xFF, (byte) 0xFE, /* ... */};
        String charsetName = "UTF-8";
        String baseUri = "/"; // replace with actual base URI
        Parser parser = Parser.htmlParser();
        Document doc = DataUtil.parseByteData(ByteBuffer.wrap(byteData), charsetName, baseUri, parser);
        assertNotNull(doc);
    }

    @Test
    public void testReadToByteBuffer() throws IOException {
        InputStream in = getClass().getResourceAsStream("/example.html"); // replace with actual input stream
        ByteBuffer byteData = DataUtil.readToByteBuffer(in);
        assertEquals(byteData.limit(), (int) in.available());
    }

    @Test
    public void testGetCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(charset, "UTF-8");
    }

    @Test
    public void testGetCharsetFromContentTypeWithInvalidContentType() {
        String contentType = "text/plain";
        String charset = DataUtil.getCharsetFromContentType(contentType);
        assertEquals(charset, null);
    }
}