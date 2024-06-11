import org.junit.Test;
import org.jsoup.helper.DataUtil;
import static org.junit.Assert.assertNotNull;

public class DataUtilTest {

    @Test
    public void testLoadFromFile() throws IOException {
        File testFile = new File("test.html"); // Replace with a valid test file
        Document doc = DataUtil.load(testFile, "UTF-8", null);
        assertNotNull(doc);
    }
}