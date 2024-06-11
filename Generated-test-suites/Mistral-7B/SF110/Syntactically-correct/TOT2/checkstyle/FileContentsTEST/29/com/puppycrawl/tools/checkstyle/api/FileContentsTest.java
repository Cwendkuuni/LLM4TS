import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.FileContents;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorWithFilenameAndLines() {
        String filename = "TestFile.java";
        String[] lines = {"public class TestClass {", "  // This is a comment", "}"};
        FileContents fileContents = new FileContents(filename, lines);
        assertEquals(filename, fileContents.getFileName());
        assertEquals(lines.length, fileContents.getLines().length);
    }
}