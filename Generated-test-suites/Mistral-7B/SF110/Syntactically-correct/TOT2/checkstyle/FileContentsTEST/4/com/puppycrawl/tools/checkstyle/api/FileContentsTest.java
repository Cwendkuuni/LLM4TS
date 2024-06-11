import static org.junit.Assert.*;
import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import org.junit.Test;

public class FileContentsTest {
    // Test for valid input
    @Test
    public void testValidFileContents() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "// This is a comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertEquals(fileName, fileContents.getFileName());
        // Add more assertions for other attributes and methods as needed
    }

    // Test for empty filename and lines
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFilename() {
        String[] lines = {"public class TestClass {", "// This is a comment", "}"};
        new FileContents(null, lines);
    }

    // Test for empty lines
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLines() {
        String fileName = "testFile.java";
        new FileContents(fileName, new String[0]);
    }
}