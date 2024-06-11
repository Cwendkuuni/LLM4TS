import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    @Test
    public void testLoadFileWithDefaultCharset() throws Exception {
        File file = new File("test.html"); // replace with actual file path
        Document doc = DataUtil.load(file, null);
        Assert.assertNotNull(doc);
        // other assertions for the document as needed
    }

    @Test
    public void testLoadFileWithSpecifiedCharset() throws Exception {
        File file = new File("test.html"); // replace with actual file path
        Document doc = DataUtil.load(file, "UTF-8");
        Assert.assertNotNull(doc);
        // other assertions for the document as needed
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws Exception {
        byte[] fileContent = getFileContent("test.html"); // replace with actual file content
        InputStream inputStream = new ByteArrayInputStream(fileContent);
        Document doc = DataUtil.load(inputStream, null);
        Assert.assertNotNull(doc);
        // other assertions for the document as needed
    }

    @Test
    public void testLoadInputStreamWithSpecifiedCharset() throws Exception {
        byte[] fileContent = getFileContent("test.html"); // replace with actual file content
        InputStream inputStream = new ByteArrayInputStream(fileContent);
        Document doc = DataUtil.load(inputStream, "UTF-8");
        Assert.assertNotNull(doc);
        // other assertions for the document as needed
    }

    // Add test methods for other public methods, such as parseInputStream, crossStreams, parseToInput, and validateCharset
}