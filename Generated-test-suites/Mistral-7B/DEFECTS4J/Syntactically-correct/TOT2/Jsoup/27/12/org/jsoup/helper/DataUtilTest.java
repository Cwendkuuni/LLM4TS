import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFile() {
        File inputFile = new File("test.html"); // replace with a valid file path
        Document doc = DataUtil.load(inputFile, "UTF-8", "");
        assertNotNull(doc);
    }
}