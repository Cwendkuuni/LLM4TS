package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineNumber;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.util.Checksums;
import com.puppycrawl.tools.checkstyle.api.util.FileTextUtils;
import com.puppycrawl.tools.checkstyle.api.util.StringUtils;

public class FileContentsTest {

    @Test
    public void testEmptyConstructor() {
        FileContents fileContents = new FileContents();
        assertNull(fileContents.getFileName());
        assertNull(fileContents.getText());
        assertTrue(fileContents.getLines().length == 0);
    }

    @Test
    public void testFileTextConstructor() {
        String content = "line1\nline2\nline3";
        FileText fileText = new FileText(content);
        FileContents fileContents = new FileContents(fileText);
        assertEquals(content, fileContents.getText().getContents());
        assertEquals(3, fileContents.getLines().length);
        assertEquals("line1", fileContents.getLines()[0]);
        assertEquals("line2", fileContents.getLines()[1]);
        assertEquals("line3", fileContents.getLines()[2]);
        assertEquals("line1", fileContents.getLine(0));
        assertEquals("line2", fileContents.getLine(1));
        assertEquals("line3", fileContents.getLine(2));
        assertEquals("line1", fileContents.getLine(new LineNumber(0, 1)));
        assertFalse(fileContents.hasIntersectionWithComment(new LineNumber(0, 1), 0, 3, 3));
    }

    @Test
    public void testLinesConstructor() {
        String[] lines = {"line1", "line2", "line3"};
        FileContents fileContents = new FileContents("test.java", lines);
        assertEquals("test.java", fileContents.getFileName());
        assertEquals(lines.length, fileContents.getLines().length);
        for (int i = 0; i < lines.length; i++) {
            assertEquals(lines[i], fileContents.getLines()[i]);
        }
        assertFalse(fileContents.hasIntersectionWithComment(new LineNumber(0, 1), 0, 3, 3));
    }

    @Test
    public void testGetText() {
        String content = "line1\nline2\nline3";
        FileContents fileContents = new FileContents("test.java", content.split("\n"));
        assertEquals(content, fileContents.getText().getContents());
    }

    @Test
    public void testReportSingleLineComment() {
        String testContent = "// This is single line comment";
        FileContents fileContents = new FileContents("test.java", testContent.split("\n"));
        fileContents.reportSingleLineComment("JavaDoc", 1, 1);
        assertEquals(1, fileContents.getCppComments().size());
        TextBlock comment = fileContents.getCppComments().get(1);
        assertEquals("// This is single line comment", comment.getText());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(1, comment.getStartColNo());
        assertEquals(1, comment.getEndLineNo());
        assertEquals(1, comment.getEndColNo());
    }

    @Test
    public void testReportBlockComment() {
        String testContent = "/**\n * This is block comment\n */";
        FileContents fileContents = new FileContents("test.java", testContent.split("\n"));
        fileContents.reportBlockComment("JavaDoc", 1, 1, 1, 37);
        assertEquals(1, fileContents.getCComments().size());
        List<TextBlock> comments = fileContents.getCComments().get(1);
        assertEquals(1, comments.size());
        TextBlock comment = comments.get(0);
        assertEquals("/**\n * This is block comment\n */", comment.getText());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(1, comment.getStartColNo());
        assertEquals(1, comment.getEndLineNo());
        assertEquals(37, comment.getEndColNo());
    }

    @Test
    public void testGetCComments() {
        String testContent = "/* This is a C comment */";
        FileContents fileContents = new FileContents("test.java", testContent.split("\n"));
        fileContents.reportCComment(1, 1, 1, 33);
        List<TextBlock> comments = fileContents.getCComments().get(1);
        assertEquals(1, comments.size());
        TextBlock comment = comments.get(0);
        assertEquals("/* This is a C comment */", comment.getText());
        assertEquals(1, comment.getStartLineNo());
        assertEquals(1, comment.getStartColNo());
        assertEquals(1, comment.getEndLineNo());
        assertEquals(33, comment.getEndColNo());
    }

    @Test
    public void testExtractCComment() {
        String testContent = "/* This is a C comment\n * This is a line in the comment\n * This is another line in the comment\n */";
        FileContents fileContents = new FileContents("test.java", testContent.split("\n"));
        String[] comment = fileContents.extractCComment(1, 1, 1, 42);
        assertEquals(3, comment.length);
        assertEquals("/* This is a C comment\n * This is a line in the comment\n * This is another line in the comment\n */", StringUtils.join(comment, System.lineSeparator()));
        assertEquals("/* This is a C comment\n * This is a line in the comment\n", comment[0]);
        assertEquals(" * This is another line in the comment\n", comment[1]);
        assertEquals(" */", comment[2]);
    }

    @Test
    public void testGetJavadocBefore() {
        String testContent = "@author John Doe\n /**\n * This is a Javadoc comment\n */\n public class Test {}";
        FileContents fileContents = new FileContents("test.java", testContent.split("\n"));
        TextBlock comment = fileContents.getJavadocBefore(1);
        assertSame(comment, fileContents.getCppComments().get(0));
    }

    @Test
    public void testLineIsBlank() {
        String testContent = "\n\nline1\n\n";
        FileContents fileContents = new FileContents("test.java", testContent.split("\n"));
        assertTrue(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(2));
        assertFalse(fileContents.lineIsBlank(1));
    }

    @Test
    public void testLineIsComment() {
        String testContent = "// This is a single line comment\n/**\n * This is a multi-line Javadoc comment\n */";
        FileContents fileContents = new FileContents("test.java", testContent.split("\n"));
        assertTrue(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(3));
        assertFalse(fileContents.lineIsComment(1));
    }

    @Test
    public void testHasIntersectionWithComment() {
        String testContent = "// This is a single line comment\n/**\n * This is a multi-line Javadoc comment\n */\npublic class Test {}";
        FileContents fileContents = new FileContents("test.java", testContent.split("\n"));
        assertTrue(fileContents.hasIntersectionWithComment(new LineNumber(1, 1), 0, 18, 18));
        assertTrue(fileContents.hasIntersectionWithComment(new LineNumber(1, 1), 0, 18, 34));
        assertTrue(fileContents.hasIntersectionWithComment(new LineNumber(2, 1), 0, 17, 34));
        assertFalse(fileContents.hasIntersectionWithComment(new LineNumber(1, 1), 0, 18, 19));
    }

    @Test
    public void testInPackageInfo() {
        String testContent = "package com.example.test;\npublic class Test {}";
        FileContents fileContents = new FileContents("test.java", testContent.split("\n"));
        assertTrue(fileContents.inPackageInfo());
        testContent = "public class Test {}";
        fileContents = new FileContents("test.java", testContent.split("\n"));
        assertFalse(fileContents.inPackageInfo());
    }
}