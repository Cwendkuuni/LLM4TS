import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with the path to a valid HTML file
        Document doc = DataUtil.load(file, "UTF-8", null);
        assertNotNull(doc);
        // add assertions to check the contents of the Document
    }
}