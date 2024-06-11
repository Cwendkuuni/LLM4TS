package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Comment;

public class FileContentsTest {

    private FileContents fileContents;
    private FileText fileText;

    @Before
    public void setup() throws IOException {
        String fileName = "testFile.java";
        String[] lines = { "/** This is a test file */", "public class TestFile {}", "// This is a comment" };
        this.fileText = new FileText(new File(fileName), Arrays.asList(lines));
        this.fileContents = new FileContents(fileName, lines);
    }

    @Test
    public void testGetFileName() {
        assertEquals("testFile.java", fileContents.getFileName());
    }

    @Test
    public void testGetText() {
        FileText expectedText = new FileText(fileText);
        FileText actualText = fileContents.getText();
        assertEquals(expectedText.toString(), actualText.toString());
    }

    @Test
    public void testGetLines() {
        String[] expectedLines = fileText.toLinesArray();
        String[] actualLines = fileContents.getLines();
        assertArrayEquals(expectedLines, actualLines);
    }

    @Test
    public void testGetLine() {
        assertEquals("/** This is a test file */", fileContents.getLine(0));
        assertEquals("public class TestFile {}", fileContents.getLine(1));
        assertNull(fileContents.getLine(2));
    }

    @Test
    public void testReportSingleLineComment() {
        int startLineNo = 1;
        int startColNo = 1;
        fileContents.reportSingleLineComment("cpp", startLineNo, startColNo);
        Comment expectedComment = new Comment(new String[]{"// This is a comment"}, startColNo, startLineNo, 1);
        Comment actualComment = fileContents.cppComments.get(startLineNo);
        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testGetCppComments() {
        ImmutableMap<Integer, Comment> expectedComments = ImmutableMap.of(1, new Comment(new String[]{"// This is a comment"}, 1, 1, 1));
        ImmutableMap<Integer, Comment> actualComments = fileContents.getCppComments();
        assertMapEquals(expectedComments, actualComments);
    }

    @Test
    public void testReportBlockComment() {
        String[] expectedCommentLines = { "/** This is a test file */" };
        int startLineNo = 0;
        int startColNo = 1;
        int endLineNo = 0;
        int endColNo = 12;
        fileContents.reportBlockComment("c", startLineNo, startColNo, endLineNo, endColNo);
        List<Comment> expectedComments = ImmutableList.of(new Comment(expectedCommentLines, 1, endLineNo, endColNo));
        List<Comment> actualComments = fileContents.clangComments.get(startLineNo);
        assertListEquals(expectedComments, actualComments);

        Comment expectedJavadocComment = new Comment(new String[]{"/** This is a test file */"}, 1, endLineNo, endColNo);
        Comment actualJavadocComment = fileContents.javadocComments.get(endLineNo);
        assertEquals(expectedJavadocComment, actualJavadocComment);
    }

    @Test
    public void testGetCComments() {
        ImmutableMap<Integer, List<Comment>> expectedComments = ImmutableMap.of(0, ImmutableList.of(new Comment(new String[]{"/** This is a test file */"}, 1, 0, 12)));
        ImmutableMap<Integer, List<Comment>> actualComments = fileContents.getCComments();
        assertMapEquals(expectedComments, actualComments);
    }

    @Test
    public void testLineIsBlank() {
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertTrue(fileContents.lineIsBlank(2));
    }

    @Test
    public void testLineIsComment() {
        assertTrue(fileContents.lineIsComment(1));
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(2));
    }

    @Test
    public void testHasIntersectionWithComment() {
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 1;
        int endColNo = 12;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testHasIntersectionWithNoComment() {
        int startLineNo = 1;
        int startColNo = 13;
        int endLineNo = 1;
        int endColNo = 12;
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        assertFalse(fileContents.inPackageInfo());
    }

    // Add more test cases for other methods if necessary

    private void assertMapEquals(Map<Integer, ? extends Comment> expected, Map<Integer, ? extends Comment> actual) {
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<Integer, Comment> entry : expected.entrySet()) {
            Map.Entry<Integer, Comment> actualEntry = actual.entrySet().stream().filter(e -> e.getKey().equals(entry.getKey())).findFirst().orElse(null);
            assertNotNull(actualEntry);
            assertEquals(entry.getValue(), actualEntry.getValue());
        }
    }

    private void assertListEquals(List<Comment> expected, List<Comment> actual) {
        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    private static void assertArrayEquals(Object[] expected, Object[] actual) {
        if (expected.length != actual.length) {
            fail("Arrays have different length.");
        }
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].equals(actual[i])) {
                fail("Arrays contain different elements at index " + i + ".");
            }
        }
    }

    private static void assertNull(Object obj) {
        if (obj != null) {
            fail("Expected null but was: " + obj);
        }
    }

}