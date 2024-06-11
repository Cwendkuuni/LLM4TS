import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FileTextTest {
    @Test
    public void testGetMethod() {
        FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
        String expectedLine = "This is a test line.";
        String actualLine = fileText.get(0);
        assertEquals(expectedLine, actualLine);
    }
}