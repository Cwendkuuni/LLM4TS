import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class DataUtilTest {
    // Test case 1: Testing load method with valid file, default charset, and baseUri
    @Test
    public void testLoadValidFile() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, null, null);
        Assert.assertNotNull(doc);
    }

    // Test case 2: Testing load method with valid file, specified charset, and baseUri
    @Test
    public void testLoadValidFileWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    // Test case 3: Testing load method with valid inputStream, default charset, and baseUri
    @Test
    public void testLoadValidInputStream() throws IOException {
        File file = new File("testFile.html");
        InputStream inputStream = new FileInputStream(file);
        Document doc = DataUtil.load(inputStream, null, null);
        Assert.assertNotNull(doc);
    }

    // Test case 4: Testing load method with valid inputStream, specified charset, and baseUri
    @Test
    public void testLoadValidInputStreamWithSpecifiedCharset() throws IOException {
        File file = new File("testFile.html");
        InputStream inputStream = new FileInputStream(file);
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    // Test case 5: Testing load method with valid inputStream, specified Parser, and baseUri
    @Test
    public void testLoadValidInputStreamWithSpecifiedParser() throws IOException {
        File file = new File("testFile.html");
        InputStream inputStream = new FileInputStream(file);
        Document doc = DataUtil.load(inputStream, null, null, DataUtil.Parser.xmlParser());
        Assert.assertNotNull(doc);
    }

    // Test case 6: Testing crossStreams method with valid inputStream and outputStream
    @Test
    public void testCrossStreams() throws IOException {
        File testFile = new File("testFile.html");
        File resultFile = new File("resultFile.html");
        InputStream inputStream = new FileInputStream(testFile);
        OutputStream outputStream = new FileOutputStream(resultFile);
        DataUtil.crossStreams(inputStream, outputStream);
        Assert.assertTrue(resultFile.length() > 0);
    }
}