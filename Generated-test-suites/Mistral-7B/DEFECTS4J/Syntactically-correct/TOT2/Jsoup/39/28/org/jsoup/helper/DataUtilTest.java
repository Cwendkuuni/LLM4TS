import org.junit.Test;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // Assuming the file exists
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, "UTF-8", null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithDefaultCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, null, null);
        assertNotNull(doc);
    }

    @Test
    public void testLoadInputStreamWithCustomParser() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        Document doc = DataUtil.load(inputStream, null, null, Parser.xmlParser());
        assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        DataUtil.load(file, "InvalidCharset", null);
    }

    @Test(expected = IOException.class)
    public void testLoadInputStreamWithInvalidCharset() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/test.html");
        DataUtil.load(inputStream, "InvalidCharset", null);
    }

}