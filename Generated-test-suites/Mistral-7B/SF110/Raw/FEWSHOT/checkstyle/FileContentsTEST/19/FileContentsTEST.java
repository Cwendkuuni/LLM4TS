package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.Comment.TextBlock;

public class FileContentsTest {

    @Test
    public void testEmptyConstruction() {
        FileContents emptyFileContents = new FileContents("");
        assertNull(emptyFileContents.getText());
        assertNull(emptyFileContents.getFileName());
        assertTrue(emptyFileContents.javadocComments.isEmpty());
        assertTrue(emptyFileContents.cppComments.isEmpty());
        assertTrue(emptyFileContents.clangComments.isEmpty());
    }

    @Test
    public void testFileConstruction() {
        String filename = "TestFile.java";
        FileText fileText = new FileText(new File(filename));
        FileContents fileContents = new FileContents(fileText);
        assertEquals(filename, fileContents.getFileName());
        assertEquals(fileText, fileContents.getText());
        assertTrue(fileContents.javadocComments.isEmpty());
        assertTrue(fileContents.cppComments.isEmpty());
        assertTrue(fileContents.clangComments.isEmpty());
    }

    @Test
    public void testFileConstructionWithLines() {
        String filename = "TestFile.java";
        String[] lines = new String[] { "package test;", "public class TestClass {", "    private int privateVar;", "}", "" };
        FileContents fileContents = new FileContents(filename, lines);
        assertEquals(filename, fileContents.getFileName());
        assertEquals(lines, fileContents.getLines());
        assertTrue(fileContents.javadocComments.isEmpty());
        assertTrue(fileContents.cppComments.isEmpty());
        assertTrue(fileContents.clangComments.isEmpty());
    }

    @Test
    public void testReportSingleLineComment() {
        String filename = "TestFile.java";
        String[] lines = new String[] { "// Single line comment", "public class TestClass {", "    private int privateVar;", "}" };
        FileContents fileContents = new FileContents(filename, lines);
        Comment expectedComment = new Comment(new TextBlock[] { new TextBlock("// Single line comment", 0, 0, lines[0].length()) }, 0, 0, lines[0].length());
        List<Comment> comments = fileContents.getCppComments().asList();
        assertEquals(1, comments.size());
        assertEquals(expectedComment, comments.get(0));
    }

    @Test
    public void testReportBlockComment() {
        String filename = "TestFile.java";
        String[] lines = new String[] { "/**", " * Multiple line comment", " */", "public class TestClass {", "    private int privateVar;", "}" };
        FileContents fileContents = new FileContents(filename, lines);
        Comment expectedComment = new Comment(new TextBlock[] { new TextBlock(" * Multiple line comment", 0, 1, lines[1].length() - 2) }, 0, 1, lines[1].length() - 2);
        List<Comment> comments = fileContents.getCComments().get(0).asList();
        assertEquals(1, comments.size());
        assertEquals(expectedComment, comments.get(0));
    }

    @Test
    public void testGetJavadocBefore() {
        String filename = "TestFile.java";
        String[] lines = new String[] { "/**", " * Javadoc before", " */", "public class TestClass {", "    private int privateVar;", "}", "/**", " * Javadoc after", " */" };
        FileContents fileContents = new FileContents(filename, lines);
        TextBlock javadocBefore = fileContents.getJavadocBefore(7);
        TextBlock expectedJavadocBefore = new TextBlock(" * Javadoc before", 5, 6, 18);
        assertEquals(expectedJavadocBefore, javadocBefore);
    }

    @Test
    public void testGetLines() {
        String filename = "TestFile.java";
        String[] lines = new String[] { "package test;", "public class TestClass {", "    private int privateVar;", "}", "" };
        FileContents fileContents = new FileContents(filename, lines);
        String[] expectedLines = lines;
        assertArrayEquals(expectedLines, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        String filename = "TestFile.java";
        String[] lines = new String[] { "package test;", "public class TestClass {", "    private int privateVar;", "}", "" };
        FileContents fileContents = new FileContents(filename, lines);
        String expectedLine = lines[1];
        assertEquals(expectedLine, fileContents.getLine(1));
    }

    @Test
    public void testLineIsBlank() {
        String filename = "TestFile.java";
        String[] lines = new String[] { "", "public class TestClass {", "    private int privateVar;", "}" };
        FileContents fileContents = new FileContents(filename, lines);
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        String filename = "TestFile.java";
        String[] lines = new String[] { "// Single line comment", "public class TestClass {", "    private int privateVar;", "}" };
        FileContents fileContents = new FileContents(filename, lines);
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        String filename = "TestFile.java";
        String[] lines = new String[] { "// Single line comment", "public class TestClass {", "    private int privateVar;", "}", "/**", " * Multiple line comment", " */" };
        FileContents fileContents = new FileContents(filename, lines);
        int startLineNo = 3;
        int startColNo = 0;
        int endLineNo = 5;
        int endColNo = lines[5].length();
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testHasIntersectionWithNoComment() {
        String filename = "TestFile.java";
        String[] lines = new String[] { "package test;", "public class TestClass {", "    private int privateVar;", "}", "public static void main(String[] args) {", "}" };
        FileContents fileContents = new FileContents(filename, lines);
        int startLineNo = 4;
        int startColNo = 0;
        int endLineNo = 4;
        int endColNo = 1;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        String filename = "TestFile.java";
        FileContents fileContents = new FileContents(filename);
        assertFalse(fileContents.inPackageInfo());

        filename = "TestFile-package-info.java";
        fileContents = new FileContents(filename);
        assertTrue(fileContents.inPackageInfo());
    }

    private static void assertEquals(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("Expected: <" + expected + ">, but was: <" + actual + ">");
        }
    }

    private static void assertArrayEquals(Object[] expected, Object[] actual) {
        if (expected.length != actual.length) {
            throw new AssertionError("Expected array of size " + expected.length + " but was of size " + actual.length);
        }
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                throw new AssertionError("Expected array element at index " + i + " to be <" + expected[i] + ">, but was: <" + actual[i] + ">");
            }
        }
    }
}