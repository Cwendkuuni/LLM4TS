import static org.junit.Assert.assertEquals;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import org.junit.Test;

public class FileContentsTest {
    @Test
    public void testFileContentsConstructorWithFileNameAndLines() {
        FileContents fileContents = new FileContents("test.java", "line1", "line2", "line3");
        assertEquals("test.java", fileContents.getFileName());
    }
}