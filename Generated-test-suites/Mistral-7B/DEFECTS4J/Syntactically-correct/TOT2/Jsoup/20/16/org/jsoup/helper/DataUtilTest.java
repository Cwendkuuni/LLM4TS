import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/valid/file.html"); // replace with valid file path
        Document document = DataUtil.load(file, "UTF-8", "http://baseurl.com");
        assertNotNull(document);
    }
}