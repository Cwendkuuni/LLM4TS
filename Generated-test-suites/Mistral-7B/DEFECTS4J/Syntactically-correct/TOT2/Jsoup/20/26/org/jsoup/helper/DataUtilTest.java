import org.jsoup.helper.DataUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataUtilTest {
    @Test
    public void testLoadFileDocument() throws IOException {
        File testFile = new File("test.html"); // replace with actual test file
        String charsetName = "UTF-8";
        String baseUri = "/";
        Document document = DataUtil.load(testFile, charsetName, baseUri);
        Assertions.assertNotNull(document);
    }
}