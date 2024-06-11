package com.example.junit;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructorWithFilenameAndLines() {
        String fileName = "TestFile.java";
        String[] lines = {"public class TestClass {", " // This is a comment", "}"};
        FileContents fileContents = new FileContents(fileName, lines);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(lines.length, fileContents.getLines().length);
        assertNull(fileContents.getText()); // Text should be null since lines are provided
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        String fileName = "TestFile.java";
        String fileContent = "public class TestClass {\n // This is a comment\n}";
        FileText fileText = new FileText(fileContent);
        FileContents fileContents = new FileContents(fileText);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(1, fileContents.getLines().length);
        assertEquals(fileContent, fileContents.getText().toString());
    }

    @Test
    public void testReportCppComment() {
        String fileName = "TestFile.java";
        String[] lines = {"// This is a comment", "public class TestClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCppComment(1, 0);

        Map<Integer, Comment> expectedCppComments = new HashMap<>();
        Comment cppComment = new Comment(new String[]{"// This is a comment"}, 0, 1, 0);
        expectedCppComments.put(1, cppComment);

        assertEquals(expectedCppComments, fileContents.getCComments());
    }

    @Test
    public void testReportCComment() {
        String fileName = "TestFile.java";
        String[] lines = {
                "/**",
                "* This is a Javadoc comment",
                "*/",
                "// This is a C comment",
                "public class TestClass {",
                "}"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCComment(2, 0, 2, 23);

        List<TextBlock> expectedClangComments = new ArrayList<>();
        Comment clangComment = new Comment(new String[]{"* This is a Javadoc comment"}, 0, 2, 23);
        expectedClangComments.add(clangComment);

        Map<Integer, List<TextBlock>> expectedCComments = new HashMap<>();
        expectedCComments.put(2, expectedClangComments);

        assertEquals(expectedCComments, fileContents.getCComments());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReportCCommentOutOfRange() {
        String fileName = "TestFile.java";
        String[] lines = {"public class TestClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportCComment(1, 0, 0, 0);
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "TestFile.java";
        String[] lines = {
                "/**",
                "* This is a Javadoc comment",
                "*/",
                "public class TestClass {",
                "}"
        };
        FileContents fileContents = new FileContents(fileName, lines);

        Comment expectedJavadoc = new Comment(new String[]{"/**", "* This is a Javadoc comment", "*/"}, 0, 1, 2);
        Comment actualJavadoc = fileContents.getJavadocBefore(2);

        assertEquals(expectedJavadoc, actualJavadoc);
    }

    @Test
    public void testLineIsBlank() {
        String fileName = "TestFile.java";
        String[] lines = {"", "", "public class TestClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);

        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        String fileName = "TestFile.java";
        String[] lines = {"// This is a comment", "public class TestClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);

        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "TestFile.java";
        String[] lines = {"// This is a comment", "public class TestClass {", "}"};
        FileContents fileContents = new FileContents(fileName, lines);

        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 1;
        int endColNo = 10;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        startLineNo = 1;
        startColNo = 5;
        endLineNo = 2;
        endColNo = 5;

        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "TestFile.java";
        FileContents fileContents = new FileContents(fileName, new String[0]);

        assertFalse(fileContents.inPackageInfo());

        fileName = "package-info.java";
        fileContents = new FileContents(fileName, new String[0]);

        assertTrue(fileContents.inPackageInfo());
    }
}