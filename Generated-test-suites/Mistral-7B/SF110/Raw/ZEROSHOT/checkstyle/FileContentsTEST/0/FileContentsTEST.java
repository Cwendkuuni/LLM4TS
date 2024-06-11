package com.example.junit;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineNumberUtil;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.Util;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public class FileContentsTest {

    @Test
    public void testFileContentsConstructionFromFile() {
        String filename = "FileContentsTest.java";
        String[] lines = {"public class FileContentsTest {", "    // This is a comment", "}", ""};
        FileContents fileContents = new FileContents(filename, lines);

        assertEquals(filename, fileContents.getFileName());
        assertEquals(Arrays.asList(lines), fileContents.getLines());
        assertNull(fileContents.getText());

        TextBlock comment = fileContents.getJavadocBefore(0);
        assertNotNull(comment);
        assertEquals("// This is a comment", comment.getText());
        assertEquals(0, comment.getStartLine());
        assertEquals(0, comment.getStartColumn());
        assertEquals(0, comment.getEndLine());
        assertEquals(comment.getText().length(), comment.getEndColumn());
    }

    @Test
    public void testFileContentsConstructionFromFileText() {
        String filename = "FileContentsTest.java";
        FileText fileText = new FileText(new File(filename), Arrays.asList("public class FileContentsTest {", "    // This is a comment", "}", ""));
        FileContents fileContents = new FileContents(fileText);

        assertEquals(filename, fileContents.getFileName());
        assertEquals(fileText.getLines(), fileContents.getLines());
        assertSame(fileText, fileContents.getText());

        TextBlock comment = fileContents.getJavadocBefore(0);
        assertNotNull(comment);
        assertEquals("// This is a comment", comment.getText());
        assertEquals(0, comment.getStartLine());
        assertEquals(0, comment.getStartColumn());
        assertEquals(0, comment.getEndLine());
        assertEquals(comment.getText().length(), comment.getEndColumn());
    }

    @Test
    public void testReportSingleLineComment() {
        String filename = "FileContentsTest.java";
        String[] lines = {"// This is a single line comment", "public class FileContentsTest {", "    // This is a block comment", "}"};
        FileContents fileContents = new FileContents(filename, lines);

        fileContents.reportSingleLineComment("cpp", 1, 1);

        TextBlock comment = fileContents.getCppComments().get(1);
        assertNotNull(comment);
        assertEquals("// This is a single line comment", comment.getText());
        assertEquals(1, comment.getStartLine());
        assertEquals(1, comment.getStartColumn());
        assertEquals(1, comment.getEndLine());
        assertEquals(comment.getText().length(), comment.getEndColumn());
    }

    @Test
    public void testReportBlockComment() {
        String filename = "FileContentsTest.java";
        String[] lines = {"/* This is a block comment", " * This is a javadoc comment", " */", "public class FileContentsTest {", "    // This is a single line comment", "}"};
        FileContents fileContents = new FileContents(filename, lines);

        fileContents.reportBlockComment("c", 1, 1, 1, lines[0].length());

        List<TextBlock> commentList = fileContents.getCComments().get(1);
        assertNotNull(commentList);
        assertEquals(2, commentList.size());

        TextBlock javadocComment = commentList.get(0);
        assertNotNull(javadocComment);
        assertEquals(" * This is a javadoc comment", javadocComment.getText());
        assertEquals(1, javadocComment.getStartLine());
        assertEquals(lines[0].length(), javadocComment.getStartColumn());
        assertEquals(1, javadocComment.getEndLine());
        assertEquals(lines[0].length() + lines[1].length(), javadocComment.getEndColumn());

        TextBlock cComment = commentList.get(1);
        assertNotNull(cComment);
        assertEquals("/* This is a block comment", cComment.getText());
        assertEquals(1, cComment.getStartLine());
        assertEquals(0, cComment.getStartColumn());
        assertEquals(1, cComment.getEndLine());
        assertEquals(lines[0].length(), cComment.getEndColumn());
    }

    @Test
    public void testGetJavadocBefore() {
        String filename = "FileContentsTest.java";
        String[] lines = {"/** This is a javadoc comment before", " * This is a javadoc comment", " * @param param1", " * @param param2", " */", "public class FileContentsTest {", "    // This is a single line comment", "}"};
        FileContents fileContents = new FileContents(filename, lines);

        TextBlock javadocComment = fileContents.getJavadocBefore(2);
        assertNotNull(javadocComment);
        assertEquals("/** This is a javadoc comment before", javadocComment.getText());
        assertEquals(1, javadocComment.getStartLine());
        assertEquals(lines[0].length(), javadocComment.getStartColumn());
        assertEquals(2, javadocComment.getEndLine());
        assertEquals(lines[0].length() + lines[1].length() + lines[2].length(), javadocComment.getEndColumn());
    }

    @Test
    public void testExtractCComment() {
        String filename = "FileContentsTest.java";
        String[] lines = {"/** This is a javadoc comment before", " * This is a javadoc comment", " * @param param1", " * @param param2", " */",
            "/* This is a c comment before",
            " * This is line 1 of the c comment",
            " * This is line 2 of the c comment",
            " * This is line 3 of the c comment",
            " */", "public class FileContentsTest {", "    // This is a single line comment", "}"};
        FileContents fileContents = new FileContents(filename, lines);

        String[] result = fileContents.extractCComment(3, 0, 5, lines[5].length());
        assertArrayEquals(new String[]{" * This is line 1 of the c comment", " * This is line 2 of the c comment", " * This is line 3 of the c comment"}, result);
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java");
        assertTrue(fileContents.inPackageInfo());

        fileContents = new FileContents("FileContentsTest.java");
        assertFalse(fileContents.inPackageInfo());
    }

    @Test
    public void testHasIntersectionWithComment() {
        String filename = "FileContentsTest.java";
        String[] lines = {"public class FileContentsTest {", "    // This is a single line comment", "    /** This is a javadoc comment before", "     * This is a javadoc comment", "     * @param param1", "     * @param param2", "     */", "    /* This is a c comment before", "     * This is line 1 of the c comment", "     * This is line 2 of the c comment", "     * This is line 3 of the c comment", "     */", "}"};
        FileContents fileContents = new FileContents(filename, lines);

        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 2;
        int endColNo = 1;

        // Test intersection with javadoc comment
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        TextBlock comment = fileContents.getJavadocBefore(startLineNo - 1);
        assertTrue(comment.intersects(startLineNo, startColNo, endLineNo, endColNo));

        // Test intersection with c comment
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        List<TextBlock> cComments = fileContents.getCComments().get(startLineNo);
        TextBlock cComment = cComments.get(0);
        assertTrue(cComment.intersects(startLineNo, startColNo, endLineNo, endColNo));

        // Test intersection with c comment on different line
        startLineNo = 3;
        startColNo = 1;
        endLineNo = 5;
        endColNo = lines[4].length();
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        cComments = fileContents.getCComments().get(startLineNo);
        cComment = cComments.get(2);
        assertTrue(cComment.intersects(startLineNo, startColNo, endLineNo, endColNo));

        // Test no intersection
        startLineNo = 1;
        startColNo = 1;
        endLineNo = 2;
        endColNo = 1;
        FileContents fileContentsNoIntersection = new FileContents("FileContentsTestNoIntersection.java");
        assertFalse(fileContentsNoIntersection.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }
}