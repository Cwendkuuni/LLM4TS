import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        File inputFile = new File("path/to/file.html");
        Document expectedDocument = new Document(); // Initialize an expected Document for comparison
        Document actualDocument = DataUtil.load(inputFile, charsetName, baseUri);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        String charsetName = "UTF-8";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document expectedDocument = new Document(); // Initialize an expected Document for comparison
        Document actualDocument = DataUtil.load(inputStream, charsetName, baseUri);
        assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testDetectCharsetFromBom() {
        ByteBuffer byteData = DataUtil.emptyByteBuffer();
        byteData.put((byte) 0xFF).put((byte) 0xFE).put((byte) 0x00).put((byte) 0x00);
        String charsetName = DataUtil.detectCharsetFromBom(byteData, null);
        assertEquals("UTF-16", charsetName);
    }

    @Test
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        String charsetName = " InvalidCharset";
        String baseUri = "http://example.com";
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        assertThrows(IOException.class, () -> DataUtil.load(inputStream, charsetName, baseUri));
    }
}