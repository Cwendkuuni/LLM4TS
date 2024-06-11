import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {
    @Test
    public void testLoadFromFile() throws IOException {
        File inputFile = new File("test.html"); // assuming the file exists
        Document doc = DataUtil.load(inputFile, null, null);
        assertNotNull(doc);
    }
}