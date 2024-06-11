
import static org.junit.Assert.*;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.*;
import java.io.*;
import org.junit.*;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructor() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass", "{}"};
        FileContents fileContents = new FileContents(fileName, lines);
        Map<Integer, TextBlock> expectedCppComments = Maps.newHashMap();
        Map<Integer, Map<Integer, TextBlock>> expectedClangComments = Maps.newHashMap();
        Map<Integer, TextBlock> expectedJavadocComments = Maps.newHashMap();
        assertEquals(expectedCppComments, fileContents.getCppComments());
        assertEquals(expectedClangComments, fileContents.getCComments());
        assertEquals(expectedJavadocComments, fileContents.getJavadocComments());
        assertEquals(fileName, fileContents.getFileName());
    }

    @Test
    public void testFileContentsConstructorWithFileNotFoundException() {
        String fileName = "does_not_exist.java";
        new FileContents(fileName, new String[0]);
    }

    @Test
    public void testFileContentsConstructorWithIOException() {
        String fileName = "testFile.java";
        byte[] input = new byte[1024];
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            fileInputStream.read(input);
        } catch (IOException e) {
            // expected exception
        }
        new FileContents(fileName, new String(input));
    }

    @Test
    public void testReportSingleLineComment() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "// This is a single line comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        TextBlock actualCppComment = fileContents.cppComments.get(2);
        TextBlock expectedCppComment = new TextBlock("// This is a single line comment");
        assertEquals(expectedCppComment, actualCppComment);
    }

    @Test
    public void testReportBlockComment() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "/**", " * This is a block comment", " * with multiple lines", " */", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        TextBlock actualCComment = fileContents.clangComments.get(2).get(0);
        TextBlock expectedCComment = new TextBlock("/**", " * This is a block comment", " * with multiple lines", " */");
        assertEquals(expectedCComment, actualCComment);
    }

    @Test
    public void testExtractCComment() {
        String fileName = "testFile.java";
        String[] lines = {"/** This is a multi-line comment", " public class TestClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        String[] actualCComment = fileContents.extractCComment(1, 1, 1, lines[1].length());
        String[] expectedCComment = {"/** This is a multi-line comment"};
        assertArrayEquals(expectedCComment, actualCComment);
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "testFile.java";
        String[] lines = {"/** This is a Javadoc comment before the class", "public class TestClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        TextBlock actualJavadocComment = fileContents.getJavadocBefore(2);
        TextBlock expectedJavadocComment = new TextBlock("/** This is a Javadoc comment before the class");
        assertEquals(expectedJavadocComment, actualJavadocComment);
    }

    @Test
    public void testHasIntersectionWithCommentWithOverlappingComments() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "/** This is a test comment", " * that overlaps with another comment*/", "}"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.hasIntersectionWithComment(2, 3, 3, 25));
    }
}
