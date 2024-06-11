package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.util.Maps;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class FileContentsTest {
    @Test
    public void testFileContentsConstructionFromFile() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestFile {}",
                          "/**",
                          " * This is a Javadoc comment.",
                          " */",
                          "private int testVariable = 42;",
                          "/**",
                          " * Another Javadoc comment.",
                          " */"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertEquals(fileName, fileContents.getFileName());
        assertSame(lines, fileContents.getLines());
    }

    @Test
    public void testFileContentsConstructionFromFileText() {
        String fileName = "testFile.java";
        FileText fileText = new FileText("testFile.java", new String[]{"public class TestFile ",
                                                                        "/**",
                                                                        " * This is a Javadoc comment.",
                                                                        " */",
                                                                        "private int testVariable = 42;",
                                                                        "/**",
                                                                        " * Another Javadoc comment.",
                                                                        " */"});
        FileContents fileContents = new FileContents(fileText);
        assertEquals(fileName, fileContents.getFileName());
        assertSame(fileText, fileContents.getText());
    }

    @Test
    public void testReportSingleLineComment() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestFile ",
                          "//This is a single line comment",
                          "private int testVariable = 42;"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        fileContents.reportSingleLineComment("", startLineNo, startColNo);
        Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
        TextBlock comment = cppComments.get(startLineNo);
        assertEquals("This is a single line comment", comment.getText());
        assertEquals(startLineNo, comment.getStartLineNo());
        assertEquals(startColNo, comment.getStartColNo());
    }

    @Test
    public void testReportBlockComment() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestFile ",
                          "/**",
                          " * This is a Javadoc comment.",
                          " */",
                          "private int testVariable = 42;",
                          "/**",
                          " * Another Javadoc comment.",
                          " */"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 3;
        int endColNo = 1;
        fileContents.reportBlockComment("", startLineNo, startColNo, endLineNo, endColNo);
        Map<Integer, TextBlock> cComments = fileContents.getCComments();
        List<TextBlock> comments = cComments.get(startLineNo);
        TextBlock comment = comments.get(0);
        assertEquals("This is a Javadoc comment.", comment.getText());
        assertEquals(startLineNo, comment.getStartLineNo());
        assertEquals(startColNo, comment.getStartColNo());
        assertEquals(endLineNo, comment.getEndLineNo());
        assertEquals(endColNo, comment.getEndColNo());
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "testFile.java";
        String[] lines = {"package test.packageName;",
                          "/**",
                          " * This is a Javadoc comment before line 4.",
                          " */",
                          "",
                          "public class TestFile {}",
                          "/**",
                          " * This is a Javadoc comment before line 7.",
                          " */",
                          "private int testVariable = 42;"};
        FileContents fileContents = new FileContents(fileName, lines);
        int lineNoBefore = 6;
        TextBlock javadocBefore = fileContents.getJavadocBefore(lineNoBefore);
        assertEquals("This is a Javadoc comment before line 7.", javadocBefore.getText());
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestFile ",
                          "//This is a single line comment",
                          "private int testVariable = 42;",
                          "/**",
                          " * This is a Javadoc comment.",
                          " */"};
        FileContents fileContents = new FileContents(fileName, lines);
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 1;
        int endColNo = 20;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 1));
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "testFile.java";
        FileContents fileContents = new FileContents(fileName);
        assertFalse(fileContents.inPackageInfo());
        fileName = "testFile-package.java";
        fileContents = new FileContents(fileName);
        assertTrue(fileContents.inPackageInfo());
    }

    @Test
    public void testGetLines() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestFile ",
                          "//This is a single line comment",
                          "private int testVariable = 42;",
                          "/**",
                          " * This is a Javadoc comment.",
                          " */"};
        FileContents fileContents = new FileContents(fileName, lines);
        String[] expectedLines = {"public class TestFile ",
                                "//This is a single line comment",
                                "private int testVariable = 42;",
                                "/**",
                                " * This is a Javadoc comment.",
                                " */"};
        assertArrayEquals(expectedLines, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        String fileName = "testFile.java";
        String[] lines = {"public class TestFile ",
                          "private int testVariable = 42;"};
        FileContents fileContents = new FileContents(fileName, lines);
        String expectedLine = "private int testVariable = 42;";
        String line = fileContents.getLine(1);
        assertEquals(expectedLine, line);
    }

    @Test
    public void testEmptyConstructor() {
        FileContents fileContents = new FileContents();
        assertNull(fileContents.getText());
        assertNull(fileContents.getFileName());
    }
}