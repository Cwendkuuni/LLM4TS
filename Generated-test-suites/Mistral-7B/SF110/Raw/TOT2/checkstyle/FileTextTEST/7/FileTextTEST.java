import org.junit.Test;
import static org.junit.Assert.*;

public class FileTextTest {

    @Test
    public void testFileTextConstruction() throws IOException {
        File file = new File("test.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertEquals(file, fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertEquals(5, fileText.size()); // Assuming test.txt contains 5 lines
    }
}