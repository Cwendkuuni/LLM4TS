package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class FileContentsTest {

    @Test
    public void testFileContentsCreationFromStringArr() {
        String filename = "TestFile.java";
        String[] lines = {"package com.test;", "import static org.junit.Assert.assertEquals;", "import org.junit.Test;"};
        FileContents fileContents = new FileContents(filename, lines);
        assertEquals(filename, fileContents.getFileName());
        assertEquals(Arrays.asList(lines), fileContents.getLines());
        assertEquals(lines.length, fileContents.text.size());
    }

    @Test
    public void testFileContentsCreationFromFileText() {
        String filename = "TestFile.java";
        FileText fileText = new FileText("line1\nline2\nline3");
        FileContents fileContents = new FileContents(filename, fileText);
        assertEquals(filename, fileContents.getFileName());
        assertEquals(fileText, fileContents.getText());
    }

    @Test
    public void testGetters() {
        String filename = "TestFile.java";
        String[] lines = {"package com.test;", "import static org.junit.Assert.assertEquals;", "import org.junit.Test;"};
        FileContents fileContents = new FileContents(filename, lines);
        assertEquals(lines.length, fileContents.text.size());
        assertEquals(filename, fileContents.getFileName());
        assertEquals(lines, fileContents.getLines());
        assertEquals(lines[0], fileContents.getLine(0));
        assertEquals(lines[1], fileContents.getLine(1));
        assertEquals(lines[2], fileContents.getLine(2));
    }

    @Test
    public void testReportSingleLineComment() {
        String filename = "TestFile.java";
        String[] lines = {"// Single line comment"};
        FileContents fileContents = new FileContents(filename, lines);
        fileContents.reportSingleLineComment("JavaDoc", 1, 1);
        assertEquals(1, fileContents.cppComments.size());
        Comment comment = fileContents.cppComments.get(1);
        assertEquals("Single line comment", comment.getText());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(1, comment.getStartColNo());
        assertEquals(1, comment.getEndLineNo());
        assertEquals(1, comment.getEndColNo());
    }

    @Test
    public void testReportBlockComment() {
        String filename = "TestFile.java";
        String[] lines = {"/**", " * This is a block comment", " *", " */"};
        FileContents fileContents = new FileContents(filename, lines);
        fileContents.reportBlockComment("JavaDoc", 1, 1, 1, lines.length - 1);
        assertEquals(1, fileContents.clangComments.size());
        List<TextBlock> commentList = fileContents.clangComments.get(1);
        assertEquals(1, commentList.size());
        Comment comment = commentList.get(0);
        assertEquals("This is a block comment", comment.getText());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(1, comment.getStartColNo());
        assertEquals(1, comment.getEndLineNo());
        assertEquals(lines.length - 1, comment.getEndColNo());
    }

    @Test
    public void testGetCppComments() {
        String filename = "TestFile.java";
        String[] lines = {"// Single line comment1", "// Single line comment2", "/**", " * This is a block comment", " *", " */"};
        FileContents fileContents = new FileContents(filename, lines);
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(3, cppComments.size());
        Comment comment1 = cppComments.get(2);
        assertEquals("This is a block comment", comment1.getText());
        assertEquals(1, comment1.getStartLineNo());
        assertEquals(1, comment1.getStartColNo());
        assertEquals(1, comment1.getEndLineNo());
        assertEquals(lines.length - 1, comment1.getEndColNo());

        Comment comment2 = cppComments.get(3);
        assertEquals("", comment2.getText());
        assertEquals(1, comment2.getStartLineNo());
        assertEquals(lines[1].length(), comment2.getEndColNo());
        assertEquals(lines[1].length(), comment2.getLength());

        Comment comment3 = cppComments.get(4);
        assertEquals("", comment3.getText());
        assertEquals(1, comment3.getStartLineNo());
        assertEquals(lines[2].length(), comment3.getEndColNo());
        assertEquals(lines[2].length(), comment3.getLength());
    }

    @Test
    public void testGetCComments() {
        String filename = "TestFile.java";
        String[] lines = {"/**", " * This is a block comment1", " *", " * This is a block comment2", " */"};
        FileContents fileContents = new FileContents(filename, lines);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        List<TextBlock> commentList = cComments.get(1);
        assertEquals(2, commentList.size());
        Comment comment1 = commentList.get(0);
        assertEquals("This is a block comment1", comment1.getText());
        assertEquals(1, comment1.getStartLineNo());
        assertEquals(1, comment1.getStartColNo());
        assertEquals(1, comment1.getEndLineNo());
        assertEquals(lines.length - 1, comment1.getEndColNo());

        Comment comment2 = commentList.get(1);
        assertEquals("This is a block comment2", comment2.getText());
        assertEquals(1, comment2.getStartLineNo());
        assertEquals(lines[1].length(), comment2.getEndColNo());
        assertEquals(lines[1].length(), comment2.getLength());
    }

    @Test
    public void testExtractCComment() {
        String filename = "TestFile.java";
        String[] lines = {"/**", " * This is a multiline C comment", " *", " * This is a multiline C comment", " *", " */"};
        FileContents fileContents = new FileContents(filename, lines);
        String[] comment = fileContents.extractCComment(1, 1, 1, lines.length - 1);
        assertEquals(3, comment.length);
        assertEquals("This is a multiline C comment", comment[0]);
        assertEquals(" * This is a multiline C comment", comment[1]);
        assertEquals(" * This is a multiline C comment", comment[2]);
    }

    @Test
    public void testGetJavadocBefore() {
        String filename = "TestFile.java";
        String[] lines = {"/**", " * This is a Javadoc comment before", " *", " * This is a Javadoc comment after", " */", "public void testMethod();"};
        FileContents fileContents = new FileContents(filename, lines);
        Comment comment = fileContents.getJavadocBefore(3);
        assertEquals("This is a Javadoc comment before", comment.getText());
        assertEquals(2, comment.getStartLineNo());
        assertEquals(lines[1].length(), comment.getEndColNo());
        assertEquals(lines[1].length(), comment.getLength());
    }

    @Test
    public void testIntersects() {
        String filename = "TestFile.java";
        String[] lines = {"// Single line comment1", "// Single line comment2", "/**", " * This is a Javadoc comment", " *", " * This is a multiline C comment", " *", " *", " *", " */"};
        FileContents fileContents = new FileContents(filename, lines);

        boolean intersectionWithCppComment = fileContents.hasIntersectionWithCppComment(1, 1, 1, lines.length - 1);
        assertFalse(intersectionWithCppComment);

        boolean intersectionWithJavadoc = fileContents.hasIntersectionWithJavadoc(1, 1, 1, lines.length - 1);
        assertFalse(intersectionWithJavadoc);

        boolean intersectionWithCComment = fileContents.hasIntersectionWithCComment(1, 1, 1, lines.length - 1);
        assertTrue(intersectionWithCComment);

        boolean intersectionWithCComment2 = fileContents.hasIntersectionWithCComment(1, 1, 2, lines.length - 1);
        assertTrue(intersectionWithCComment2);

        boolean intersectionWithJavadoc2 = fileContents.hasIntersectionWithJavadoc(1, 1, 2, lines.length - 1);
        assertFalse(intersectionWithJavadoc2);

        boolean intersectionWithCComment3 = fileContents.hasIntersectionWithCComment(2, 1, 2, lines.length - 1);
        assertTrue(intersectionWithCComment3);
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents("package-info.java", Arrays.asList("Name: MyProject", "Version: 1.0.0", ""));
        assertTrue(fileContents.inPackageInfo());
    }

    @Test
    public void testEmptyLines() {
        String filename = "TestFile.java";
        String[] lines = {"", "", "/**", " * This is a Javadoc comment", " *", " */", "", ""};
        FileContents fileContents = new FileContents(filename, lines);
        assertEquals(6, fileContents.text.size());
        List<TextBlock> commentList = fileContents.clangComments.get(1);
        assertEquals(1, commentList.size());
        Comment comment = commentList.get(0);
        assertEquals("This is a Javadoc comment", comment.getText());
        assertEquals(3, comment.getStartLineNo());
        assertEquals(1, comment.getStartColNo());
        assertEquals(3, comment.getEndLineNo());
        assertEquals(lines.length - 1, comment.getEndColNo());
    }

    @Test
    public void testCommentIntersection() {
        String filename = "TestFile.java";
        String[] lines = {"// Single line comment1", "// Single line comment2", "/**", " * This is a Javadoc comment", " *", " * This is a multiline C comment", " *", " *", " *", " */"};
        FileContents fileContents = new FileContents(filename, lines);

        Comment javadocComment = fileContents.getJavadocBefore(3);
        Comment cComment = fileContents.clangComments.get(1).get(0);

        // Javadoc comment doesn't intersect with C comment
        assertFalse(javadocComment.intersects(cComment.getStartLineNo(), cComment.getStartColNo(), cComment.getEndLineNo(), cComment.getEndColNo()));

        // C comment doesn't intersect with Javadoc comment
        assertFalse(cComment.intersects(javadocComment.getStartLineNo(), javadocComment.getStartColNo(), javadocComment.getEndLineNo(), javadocComment.getEndColNo()));

        // Javadoc comment intersects with itself
        TextBlock javadocComment2 = fileContents.javadocComments.get(2);
        assertTrue(javadocComment.intersects(javadocComment2.getStartLineNo(), javadocComment2.getStartColNo(), javadocComment2.getEndLineNo(), javadocComment2.getEndColNo()));

        // C comment intersects with itself
        TextBlock cComment2 = fileContents.clangComments.get(1).get(1);
        assertTrue(cComment.intersects(cComment2.getStartLineNo(), cComment2.getStartColNo(), cComment2.getEndLineNo(), cComment2.getEndColNo()));

        // Javadoc comment intersects with multiline C comment
        TextBlock cComment3 = fileContents.clangComments.get(1).get(2);
        assertTrue(javadocComment.intersects(cComment3.getStartLineNo(), cComment3.getStartColNo(), cComment3.getEndLineNo(), cComment3.getEndColNo()));

        // C comment intersects with multiline C comment
        TextBlock cComment4 = fileContents.clangComments.get(1).get(3);
        assertTrue(cComment.intersects(cComment4.getStartLineNo(), cComment4.getStartColNo(), cComment4.getEndLineNo(), cComment4.getEndColNo()));

        // Multiline C comment intersects with itself
        TextBlock cComment5 = fileContents.clangComments.get(1).get(4);
        assertTrue(cComment3.intersects(cComment5.getStartLineNo(), cComment5.getStartColNo(), cComment5.getEndLineNo(), cComment5.getEndColNo()));
    }

    @Test
    public void testEmptyMap() {
        FileContents fileContents = new FileContents("empty.java", Collections.emptyList());
        assertEquals(0, fileContents.cppComments.size());
        assertEquals(0, fileContents.javadocComments.size());
        assertEquals(0, fileContents.clangComments.size());
    }
}