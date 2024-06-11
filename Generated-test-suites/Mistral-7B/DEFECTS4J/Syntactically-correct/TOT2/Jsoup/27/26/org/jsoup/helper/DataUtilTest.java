import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File testFile = new File("path/to/test/file.html"); // Replace with the path to your test file
        Document document = DataUtil.load(testFile, "UTF-8", "http://test.com");
        assertNotNull(document);
    }
}