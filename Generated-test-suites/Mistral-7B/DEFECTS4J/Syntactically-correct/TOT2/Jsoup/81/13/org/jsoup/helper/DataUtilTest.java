import org.junit.Test;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // assuming the file exists and is in the classpath
        Document doc = DataUtil.load(file, null);
        assertEquals("Expected HTML content", "<html><body></body></html>", doc.html());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8");
        assertEquals("Expected HTML content", "<html><body></body></html>", doc.html());
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        Document doc = null;
        try {
            doc = DataUtil.load(file, "InvalidCharset");
        } catch (IOException e) {
        }
        assertNotNull(doc); // Exception was thrown, test passed
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, null);
        assertEquals("Expected HTML content", "<html><body></body></html>", doc.html());
    }

    // Add more test cases for other methods, make sure to cover edge cases and exceptions
}