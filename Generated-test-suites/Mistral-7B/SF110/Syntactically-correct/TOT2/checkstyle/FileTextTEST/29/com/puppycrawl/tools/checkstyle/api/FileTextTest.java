import org.junit.Test;
import static org.junit.Assert.*;

public class FileTextTest {
    @Test
    public void testFileTextConstructor() throws IOException {
        File file = new File("testFile.txt");
        FileText fileText = new FileText(file, "UTF-8");
        assertNotNull(fileText);
        assertNotNull(fileText.getFile());
        assertNotNull(fileText.getCharset());
        assertNotNull(fileText.getFullText());
        assertNotNull(fileText.toLinesArray());
        assertEquals(3, fileText.size());
    }
}