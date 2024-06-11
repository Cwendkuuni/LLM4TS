package com.example.junit;

import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.api.FileContents.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorWithFilenameAndLines() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "    // This is a comment", "}"};

        FileContents fileContents = new FileContents(fileName, lines);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(lines.length, fileContents.getText().size());
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
        assertNull(fileContents.getJavadocBefore(0));
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        String fileName = "testFile.java";
        FileText fileText = new FileText(new File(fileName), Arrays.asList("public class TestClass {", "    // This is a comment", "}"));

        FileContents fileContents = new FileContents(fileText);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(fileText.size(), fileContents.getText().size());
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
        assertNull(fileContents.getJavadocBefore(0));
    }

    @Test
    public void testReportSingleLineComment() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "// This is a comment", "}"};

        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCppComment(2, 0);

        TextBlock comment = fileContents.cppComments.get(2);

        assertEquals("This is a comment", comment.getText());
        assertEquals(2, comment.getStartLineNo());
        assertEquals(0, comment.getStartColNo());
        assertEquals(2, comment.getEndLineNo());
        assertEquals(0, comment.getEndColNo());
    }

    @Test
    public void testReportBlockComment() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "/**", " * This is a Javadoc comment", " */", "}"};

        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCComment(2, 0, 2, 30);

        TextBlock comment = fileContents.clangComments.get(2).get(0);

        assertEquals("This is a Javadoc comment", comment.getText());
        assertEquals(2, comment.getStartLineNo());
        assertEquals(0, comment.getStartColNo());
        assertEquals(2, comment.getEndLineNo());
        assertEquals(30, comment.getEndColNo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReportSingleLineCommentInvalidLineNumber() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "// This is a comment", "}"};

        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCppComment(-1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReportSingleLineCommentInvalidColumnNumber() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "// This is a comment", "}"};

        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCppComment(1, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReportBlockCommentInvalidStartLineNumber() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "/**", " * This is a Javadoc comment", " */", "}"};

        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCComment(0, 0, 1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReportBlockCommentInvalidStartColumnNumber() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "/**", " * This is a Javadoc comment", " */", "}"};

        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCComment(1, -1, 1, 30);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReportBlockCommentInvalidEndLineNumber() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "/**", " * This is a Javadoc comment", " */", "}"};

        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCComment(1, 0, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReportBlockCommentInvalidEndColumnNumber() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "/**", " * This is a Javadoc comment", " */", "}"};

        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCComment(1, 0, 1, -1);
    }

    @Test
    public void testGetCppComments() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "// This is a comment", "// Another comment", "}"};

        FileContents fileContents = new FileContents(fileName, lines);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

        List<Map.Entry<Integer, TextBlock>> entries = Collections.list(cppComments.entrySet());

        assertEquals(2, entries.size());
        assertEquals(1, entries.get(0).getKey());
        assertEquals("This is a comment", entries.get(0).getValue().getText());
        assertEquals(2, entries.get(1).getKey());
        assertEquals("Another comment", entries.get(1).getValue().getText());
    }

    @Test
    public void testGetCComments() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestClass {", "/**", " * This is a Javadoc comment", " */", "/**", " * This is another Javadoc comment", " */", "}"};

        FileContents fileContents = new FileContents(fileName, lines);

        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();

        List<Map.Entry<Integer, List<TextBlock>>> entries = Collections.list(cComments.entrySet());

        assertEquals(1, entries.size());
        assertEquals(1, entries.get(0).getKey());
        assertEquals(2, entries.get(0).getValue().size());
        assertEquals("This is a Javadoc comment", entries.get(0).getValue().get(0).getText());
        assertEquals("This is another Javadoc comment", entries.get(0).getValue().get(1).getText());
    }
}