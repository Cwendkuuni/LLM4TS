import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.Assert.*;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // existing file
        Document doc = DataUtil.load(file, "UTF-8", "");
        Assert.assertNotNull(doc);
    }

    @Test(expected = IOException.class)
    public void testLoadFileWithInvalidCharset() throws IOException {
        File file = new File("test.html");
        Document doc = null;
        try {
            doc = DataUtil.load(file, "InvalidCharset", "");
        } catch (IOException e) {
            // expected exception
            assertEquals("IOException message", e.getMessage());
        }
        Assert.assertNull(doc);
    }

    @Test
    public void testLoadFileWithNullBaseUri() throws IOException {
        File file = new File("test.html");
        Document doc = DataUtil.load(file, "UTF-8", null);
        Assert.assertNotNull(doc);
    }

    @Test(expected = NullPointerException.class)
    public void testLoadFileWithNullFile() throws IOException {
        Document doc = null;
        try {
            doc = DataUtil.load(null, "UTF-8", "");
        } catch (NullPointerException e) {
            // expected exception
            assertEquals("NullPointerException message", e.getMessage());
        }
        Assert.assertNull(doc);
    }
}