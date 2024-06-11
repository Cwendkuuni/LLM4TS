import org.junit.Test;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() {
        File file = new File("test.html"); // Assuming test.html exists in the project directory
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(file, null, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() {
        File file = new File("test.html"); // Assuming test.html exists in the project directory
        String baseUri = "http://example.com";
        String charsetName = "UTF-16";
        Document doc = null;
        try {
            doc = DataUtil.load(file, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown when loading file with a valid charset.");
        }
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() {
        File file = new File("test.html"); // Assuming test.html exists in the project directory
        String baseUri = "http://example.com";
        String charsetName = "invalidCharset";
        Document doc = null;
        try {
            doc = DataUtil.load(file, charsetName, baseUri);
        } catch (IOException e) {
            // Expected exception
        } finally {
            assertNull(doc);
        }
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() {
        String inputData = "Test data";
        InputStream input = new ByteArrayInputStream(inputData.getBytes());
        String baseUri = "http://example.com";
        Document doc = DataUtil.load(input, null, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() {
        String inputData = "Test data";
        String charsetName = "UTF-16";
        InputStream input = new ByteArrayInputStream(inputData.getBytes());
        String baseUri = "http://example.com";
        Document doc = null;
        try {
            doc = DataUtil.load(input, charsetName, baseUri);
            assertNotNull(doc);
        } catch (IOException e) {
            fail("IOException should not be thrown when loading input stream with a valid charset.");
        }
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() {
        String inputData = "Test data";
        String charsetName = "invalidCharset";
        InputStream input = new ByteArrayInputStream(inputData.getBytes());
        String baseUri = "http://example.com";
        Document doc = null;
        try {
            doc = DataUtil.load(input, charsetName, baseUri);
        } catch (IOException e) {
            // Expected exception
        } finally {
            assertNull(doc);
        }
    }

    // Add tests for other methods: parseInputStream, crossStreams, emptyByteBuffer, readToByteBuffer, readFileToByteBuffer, getCharsetFromContentType, validateCharset, mimeBoundary, and detectCharsetFromBom
}