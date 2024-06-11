import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;

public class DataUtilTest {

    @Test
    public void testLoadFile() throws IOException {
        File file = new File("test.html"); // replace with a valid file path
        String baseUri = "http://localhost:8080/";
        Document document = DataUtil.load(file, "UTF-8", baseUri);
        assertNotNull(document);
    }
}