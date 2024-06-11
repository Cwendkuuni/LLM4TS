import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() throws IOException {
        File file = new File("test.html"); // Assuming you have a test.html file in your classpath
        Document doc = DataUtil.load(file, null);
        Assert.assertNotNull(doc);
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws IOException {
        File file = new File("test.html"); // Assuming you have a test.html file in your classpath
        Document doc = DataUtil.load(file, "UTF-16");
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html"); // Assuming you have a test.html file in your classpath
        Document doc = null;
        try {
            doc = DataUtil.load(file, "XYZ");
        } catch (IOException e) {
            // Expected exception
        }
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        String html = "<html><head><meta charset=\"UTF-8\"></meta></head><body></body></html>";
        InputStream inputStream = new ByteArrayInputStream(html.getBytes());
        Document doc = DataUtil.load(inputStream, null);
        Assert.assertNotNull(doc);
    }

    // Add test cases for other methods in the DataUtil class
}