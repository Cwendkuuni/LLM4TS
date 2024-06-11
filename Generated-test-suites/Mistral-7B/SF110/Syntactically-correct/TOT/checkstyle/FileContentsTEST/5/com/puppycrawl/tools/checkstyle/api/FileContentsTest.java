import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import static org.junit.Assert.*;

public class FileContentsTest {
    @Test
    public void testFileContentsWithSingleLineComment() {
        // Given
        String filename = "TestFile.java";
        String[] lines = {"public class TestFile {", "// This is a single line comment", "}"};
        FileContents fileContents = new FileContents(filename, lines);

        // When
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

        // Then
        assertEquals(1, cppComments.size());
        TextBlock comment = cppComments.values().iterator().next();
        assertEquals("// This is a single line comment", comment.getText());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(1, comment.getStartColNo());
        assertEquals(1, comment.getEndLineNo());
        assertEquals(1, comment.getEndColNo());
    }
}