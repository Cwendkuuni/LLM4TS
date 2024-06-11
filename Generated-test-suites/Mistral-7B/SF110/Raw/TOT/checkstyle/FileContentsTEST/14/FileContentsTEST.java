import static org.junit.Assert.*;
import com.google.common.collect.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.*;

public class FileContentsTest {
    private static final String VALID_FILE_PATH = "src/test/resources/validFile.java";
    private static final String INVALID_FILE_PATH = "src/test/resources/invalidFile.java";

    @Test
    public void testFileContentsConstructionWithFileNameAndLines() {
        FileContents fileContents = new FileContents(VALID_FILE_PATH, "valid line 1", "valid line 2", "valid line 3");
        assertEquals(VALID_FILE_PATH, fileContents.getFileName());
        ImmutableMap<Integer, TextBlock> expectedCppComments = ImmutableMap.of(1, new Comment("// This is a comment", 1, 1, 1));
        ImmutableMap<Integer, TextBlock> actualCppComments = fileContents.getCppComments();
        assertEquals(expectedCppComments, actualCppComments);
    }

    @Test
    public void testGetCppComments() {
        FileContents fileContents = new FileContents(new FileText("src/test/resources/validFile.java"));
        ImmutableMap<Integer, List<TextBlock>> expectedCComments = ImmutableMap.of(1, Lists.newArrayList(new Comment("/** This is a Javadoc comment */", 1, 1, 1)));
        ImmutableMap<Integer, List<TextBlock>> actualCComments = fileContents.getCComments();
        assertEquals(expectedCComments, actualCComments);
    }

    @Test
    public void testHasIntersectionWithComment() {
        FileContents fileContents = new FileContents(new FileText("src/test/resources/validFile.java"));
        FileContents invalidFileContents = new FileContents(new FileText("src/test/resources/invalidFile.java"));

        // Valid intersection
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 1;
        int endColNo = 1;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        // Invalid intersection
        assertFalse(fileContents.hasIntersectionWithComment(2, 2, 1, 1));
        assertFalse(invalidFileContents.hasIntersectionWithComment(1, 1, 1, 1));
    }

    @Test
    public void testFileContentsConstructionWithFileText() {
        FileContents fileContents = new FileContents(new FileText("src/test/resources/validFile.java"));
        assertEquals(VALID_FILE_PATH, fileContents.getFileName());

        // Test error cases
        try {
            new FileContents(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        try {
            new FileContents(new FileText(""));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}