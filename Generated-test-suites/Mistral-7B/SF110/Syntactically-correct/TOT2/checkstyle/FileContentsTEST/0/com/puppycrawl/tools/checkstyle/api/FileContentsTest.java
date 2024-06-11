import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;

public class FileContentsTest {
    @Test
    public void testFileContentsConstructionWithStringAndLines() {
        String filename = "testFile.java";
        String[] lines = {"public class TestClass {", "// This is a comment", "}"};
        FileContents fileContents = new FileContents(filename, lines);
        assertEquals(filename, fileContents.getFileName());
        assertEquals(5, fileContents.getText().size()); // 5 lines including empty line before comments
        // Test getJavadocBefore and getLine methods
        TextBlock javadocBefore = fileContents.getJavadocBefore(3);
        assertEquals("// This is a comment", javadocBefore.getText());
        assertEquals(3, javadocBefore.getStartLine());
        assertEquals(41, javadocBefore.getLength());
        assertEquals("TestClass", fileContents.getLine(1).trim());
    }
}