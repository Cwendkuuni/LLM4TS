import org.junit.Test;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.assertEquals;

public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); //Replace with the actual path of your test file
        Document doc = DataUtil.load(file, null);
        assertEquals("Expected HTML content", "Actual HTML content", doc.body().html());
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html"); //Replace with the actual path of your test file
        Document doc = DataUtil.load(file, "UTF-8");
        assertEquals("Expected HTML content", "Actual HTML content", doc.body().html());
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "Expected HTML content";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document doc = DataUtil.load(inputStream, null);
        assertEquals("Expected HTML content", "Actual HTML content", doc.body().html());
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "Expected HTML content";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        Document doc = DataUtil.load(inputStream, "UTF-8");
        assertEquals("Expected HTML content", "Actual HTML content", doc.body().html());
    }

    @Test
    public void testParseInputStreamWithDefaultCharset() throws IOException {
        String htmlContent = "Expected HTML content";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        InputStream in = new ConstrainableInputStream(inputStream, bufferSize, 0);
        Document doc = DataUtil.parseInputStream(in, null, null, Parser.htmlParser());
        assertEquals("Expected HTML content", "Actual HTML content", doc.body().html());
    }

    @Test
    public void testParseInputStreamWithSpecifiedCharset() throws IOException {
        String htmlContent = "Expected HTML content";
        InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
        InputStream in = new ConstrainableInputStream(inputStream, bufferSize, 0);
        Document doc = DataUtil.parseInputStream(in, "UTF-8", null, Parser.htmlParser());
        assertEquals("Expected HTML content", "Actual HTML content", doc.body().html());
    }

    //Add test cases for other methods: crossStreams(), parseInputStream(), readToByteBuffer(), readFileToByteBuffer(), getCharsetFromContentType(), validateCharset(), mimeBoundary()
}