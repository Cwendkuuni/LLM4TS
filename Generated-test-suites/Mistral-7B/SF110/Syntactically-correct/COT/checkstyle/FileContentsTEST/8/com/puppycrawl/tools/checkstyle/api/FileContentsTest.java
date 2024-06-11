package com.example.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;

public class FileContentsTest {

    @Test
    public void testConstructorWithFileNameAndLines() {
        String fileName = "TestFile.java";
        String[] lines = {"public class TestClass {", "// This is a comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(lines.length, fileContents.getLines().length);
        assertEquals(lines[0], fileContents.getLines()[0]);
        assertNotNull(fileContents.getText());
    }

    @Test
    public void testConstructorWithFileText() {
        String fileName = "TestFile.java";
        FileText fileText = new FileText(new File(fileName), Arrays.asList("public class TestClass {", "}"));
        FileContents fileContents = new FileContents(fileText);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(fileText.size(), fileContents.getLines().length);
        assertEquals(fileText.get(0), fileContents.getLines()[0]);
        assertNotNull(fileContents.getText());
    }

    @Test
    public void testReportCppComment() {
        String fileName = "TestFile.java";
        String[] lines = {"// This is a CPP comment at line 1", "public class TestClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);

        int startLineNo = 1;
        int startColNo = 1;
        fileContents.reportCppComment(startLineNo, startColNo);

        Comment cppComment = fileContents.cppComments.get(startLineNo);
        assertNotNull(cppComment);
        assertEquals(lines[startLineNo - 1], cppComment.getText());
        assertEquals(startLineNo, cppComment.getStartLineNo());
        assertEquals(startColNo, cppComment.getStartColNo());
    }

    @Test
    public void testGetCppComments() {
        String fileName = "TestFile.java";
        String[] lines = {"// This is a CPP comment at line 1", "// This is a CPP comment at line 2", "public class TestClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(2, cppComments.size());

        TextBlock cppComment1 = cppComments.get(1);
        TextBlock cppComment2 = cppComments.get(2);

        assertEquals(lines[0], cppComment1.getText());
        assertEquals(1, cppComment1.getStartLineNo());
        assertEquals(1, cppComment1.getStartColNo());

        assertEquals(lines[2], cppComment2.getText());
        assertEquals(3, cppComment2.getStartLineNo());
        assertEquals(1, cppComment2.getStartColNo());
    }

    @Test
    public void testExtractCComment() {
        String fileName = "TestFile.java";
        String[] lines = {"/* This is a multi-line C comment starting at line 1", " * This is a continuation of the comment", " */ public class TestClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);

        String[] expectedCComment = {"/* This is a multi-line C comment starting at line 1", " * This is a continuation of the comment"};
        String[] actualCComment = fileContents.extractCComment(1, 1, 1, lines[0].length());

        assertArrayEquals(expectedCComment, actualCComment);
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "TestFile.java";
        String[] lines = {"/** This is a Javadoc comment before line 4 */", "public class TestClass {", "}", "/** This is a Javadoc comment before line 6 */"};
        FileContents fileContents = new FileContents(fileName, lines);

        int lineNoBefore = 5;
        TextBlock expectedJavadoc = new Comment(new String[]{"/** This is a Javadoc comment before line 6 */"}, 0, 0, lines.length);
        TextBlock actualJavadoc = fileContents.getJavadocBefore(lineNoBefore);

        assertEquals(expectedJavadoc.getText(), actualJavadoc.getText());
        assertEquals(expectedJavadoc.getStartLineNo(), actualJavadoc.getStartLineNo());
        assertEquals(expectedJavadoc.getStartColNo(), actualJavadoc.getStartColNo());
    }

    @Test
    public void testLineIsBlank() {
        String fileName = "TestFile.java";
        String[] lines = {"", "// This is a comment", "public class TestClass {", ""};
        FileContents fileContents = new FileContents(fileName, lines);

        int lineNo1 = 1;
        int lineNo2 = 3;

        assertTrue(fileContents.lineIsBlank(lineNo1));
        assertFalse(fileContents.lineIsBlank(lineNo2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "TestFile.java";
        String[] lines = {"// This is a CPP comment at line 1", "public class TestClass {", "// This is a CPP comment at line 3", "}"};
        FileContents fileContents = new FileContents(fileName, lines);

        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 3;
        int endColNo = 1;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "package-info.java";
        FileContents fileContents = new FileContents(fileName, Collections.emptyList().toArray(new String[0]));

        assertTrue(fileContents.inPackageInfo());

        fileName = "TestFile.java";
        FileContents fileContents2 = new FileContents(fileName, Collections.emptyList().toArray(new String[0]));

        assertFalse(fileContents2.inPackageInfo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReportBlockCommentWithInvalidStartColNo() {
        String fileName = "TestFile.java";
        String[] lines = {"public class TestClass {", "/** This is a Javadoc comment */"};
        FileContents fileContents = new FileContents(fileName, lines);

        int startLineNo = 1;
        int startColNo = -1;
        fileContents.reportBlockComment("", startLineNo, startColNo, 1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetJavadocBeforeWithInvalidLineNo() {
        String fileName = "TestFile.java";
        String[] lines = {"", "public class TestClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);

        int lineNoBefore = -1;
        TextBlock actualJavadoc = fileContents.getJavadocBefore(lineNoBefore);

        assertNull(actualJavadoc);
    }
}