import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.nodes.Document;
import java.io.File;

public class DataUtilTest {
    @Test
    public void testLoadFile() throws IOException {
        File file = new File("path/to/file.html"); // replace with actual file path
        Document document = DataUtil.load(file, "UTF-8", ""); // replace with actual base URI or empty string
        Assertions.assertNotNull(document);
    }
}