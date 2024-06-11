package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.FileText;

public class FileContentsTest {

    @Test
    public void testEmptyFile() {
        FileContents fileContents = new FileContents("test.java");
        assertNull(fileContents.getCppComments());
        assertNull(fileContents.getCComments());
        assertTrue(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertNull(fileContents.getJavadocBefore(0));
    }

    @Test
    public void testSingleLineComment() {
        String[] lines = new String[] { "// This is a single line comment", "" };
        FileContents fileContents = new FileContents("test.java", lines);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNull(cppComments);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertNull(cComments);
        assertTrue(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsComment(1));
        Comment comment = fileContents.getJavadocBefore(1);
        assertNull(comment);
    }

    @Test
    public void testBlockComment() {
        String[] lines = new String[] { "/** This is a block comment */", "" };
        FileContents fileContents = new FileContents("test.java", lines);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNull(cppComments);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        List<TextBlock> clangComments = cComments.get(1);
        TextBlock comment = clangComments.get(0);
        assertEquals("This is a block comment", comment.getText());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(1, comment.getStartColNo());
        assertEquals(1, comment.getEndLineNo());
        assertEquals(1, comment.getEndColNo());
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsComment(0));
        assertNull(fileContents.getJavadocBefore(0));
    }

    @Test
    public void testMixedComments() {
        String[] lines = new String[] { "/* This is a block comment */", "// This is a single line comment", "" };
        FileContents fileContents = new FileContents("test.java", lines);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertNull(cppComments);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        List<TextBlock> clangComments = cComments.get(1);
        TextBlock comment1 = clangComments.get(0);
        assertEquals("This is a block comment", comment1.getText());
        assertEquals(1, comment1.getStartLineNo());
        assertEquals(1, comment1.getStartColNo());
        assertEquals(1, comment1.getEndLineNo());
        assertEquals(1, comment1.getEndColNo());
        TextBlock comment2 = clangComments.get(1);
        assertEquals("This is a single line comment", comment2.getText());
        assertEquals(2, comment2.getStartLineNo());
        assertEquals(0, comment2.getStartColNo());
        assertEquals(2, comment2.getEndLineNo());
        assertEquals(1, comment2.getEndColNo());
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsComment(0));
        assertNull(fileContents.getJavadocBefore(0));
    }

    @Test
    public void testExtractCComment() {
        FileContents fileContents = new FileContents("test.java");
        String[] expected = new String[] { "// This is a single line comment before", "/* This is a multi line comment", "*/", "// This is a single line comment after" };
        String[] lines = new String[] { "// This is a single line comment before",
                "/* This is a multi line comment",
                " * This is a javadoc comment",
                " */",
                "// This is a single line comment after" };
        FileContents newFileContents = new FileContents("test.java", lines);
        String[] result = newFileContents.extractCComment(1, 1, 3, 1);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testGetJavadocBefore() {
        String[] lines = new String[] { "/** This is a javadoc comment before */", "public void test() {", "}", "/** This is a javadoc comment after */" };
        FileContents fileContents = new FileContents("test.java", lines);
        Comment comment = fileContents.getJavadocBefore(2);
        assertEquals("This is a javadoc comment before", comment.getText());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(0, comment.getStartColNo());
        assertEquals(1, comment.getEndLineNo());
        assertEquals(1, comment.getEndColNo());
    }

    @Test
    public void testGetLines() {
        String[] lines = new String[] { "public class Test {}", "{}", "public static void main(String[] args) {", "}" };
        FileContents fileContents = new FileContents("Test.java", lines);
        String[] expected = new String[] { "public class Test {", "", "public static void main(String[] args) {", "}" };
        assertArrayEquals(expected, fileContents.getLines());
    }

    @Test
    public void testGetLine() {
        String[] lines = new String[] { "public class Test {", "{}", "public static void main(String[] args) {", "}" };
        FileContents fileContents = new FileContents("Test.java", lines);
        assertEquals("public static void main(String[] args) {", fileContents.getLine(2));
    }

    @Test
    public void testGetFileName() {
        FileContents fileContents = new FileContents("Test.java");
        assertEquals("Test.java", fileContents.getFileName());
    }

    @Test
    public void testLineIsBlank() {
        String[] lines = new String[] { "", "public class Test {", "" };
        FileContents fileContents = new FileContents("Test.java", lines);
        assertTrue(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertTrue(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        String[] lines = new String[] { "// This is a single line comment", "", "public class Test {", "" };
        FileContents fileContents = new FileContents("Test.java", lines);
        assertTrue(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(2));
        assertFalse(fileContents.lineIsComment(3));
    }

    @Test
    public void testHasIntersectionWithComment() {
        String[] lines = new String[] { "// This is a single line comment", "", "public class Test {", "" };
        FileContents fileContents = new FileContents("Test.java", lines);
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 1));
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 1, 1));
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 3, 1));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("test/package-info.java");
        assertTrue(fileContents.inPackageInfo());
        FileContents fileContents2 = new FileContents("Test.java");
        assertFalse(fileContents2.inPackageInfo());
    }
}