import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.FileContents;

public class FileContentsTest {
    @Test
    public void testFileContents() {
        String filename = "TestFile.java";
        String[] lines = {"public class TestFile {",
                " /**",
                "  * This is a Javadoc comment",
                "  */",
                " int add(int a, int b);",
                "}",
                " /**",
                "  * Another Javadoc comment",
                "  */",
                " int add(int a, int b) {",
                " return a + b;",
                " }"};
        FileContents fileContents = new FileContents(filename, lines);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        TextBlock javadocComment = fileContents.getJavadocBefore(1);
        assertTrue(cppComments.isEmpty());
        assertTrue(cComments.isEmpty());
        assertNotNull(javadocComment);
    }
    
    @Test
    public void testFileContentsWithCppComment() {
        String filename = "TestFile.java";
        String[] lines = {"public class TestFile {",
                " /**",
                "  * This is a Javadoc comment",
                "  */",
                " // This is a C++ comment",
                " int add(int a, int b);",
                "}",
                " /**",
                "  * Another Javadoc comment",
                "  */",
                " int add(int a, int b) {",
                " return a + b;",
                " }"};
        FileContents fileContents = new FileContents(filename, lines);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        TextBlock cppComment = cppComments.get(3);
        List<TextBlock> clangComments = cComments.get(3);
        assertNotNull(cppComment);
        assertNotNull(clangComments);
        assertEquals(1, clangComments.size());
    }
}