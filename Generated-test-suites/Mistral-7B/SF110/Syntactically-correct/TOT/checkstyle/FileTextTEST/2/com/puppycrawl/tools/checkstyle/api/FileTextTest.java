import org.junit.Test;
import static org.junit.Assert.*;// for assertions
import com.puppycrawl.tools.checkstyle.api.FileText;

public class FileTextTest {
    @Test
    public void testFileTextConstruction() throws IOException {
        // Given
        File file = new File("path/to/file.txt");
        String charsetName = "UTF-8";
        // When
        FileText fileText = new FileText(file, charsetName);
        // Then
        assertEquals(file, fileText.getFile());
        assertEquals(charsetName, fileText.getCharset().name());
        // Test the size and lines of the FileText object
        // Add more assertions as needed
    }
}