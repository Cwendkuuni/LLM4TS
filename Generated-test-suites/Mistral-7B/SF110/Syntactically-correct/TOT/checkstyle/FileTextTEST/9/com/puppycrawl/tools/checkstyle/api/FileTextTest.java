import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.FileText;

public class FileTextTest {
    @Test
    public void testFileTextConstruction() throws IOException {
        File file = new File("testFiles/sample.txt");
        FileText fileText = new FileText(file, "UTF-8");
        String expectedContent = new String(Files.readAllBytes(file.toPath()), Charsets.UTF_8);

        assertEquals(expectedContent, fileText.getFullText());
        assertEquals(10, fileText.size());
        // Test specific lines by checking their content
        assertEquals("Line 1\n", fileText.get(0));
        assertEquals("Line 2\nLine 2 continuation.\n", fileText.get(1));
        // ...
    }
}