package com.example.junit; // replace with your test package name

import static org.junit.Assert.*;
import static com.google.common.collect.Maps.newHashMap; // for JDK < 8
import static com.google.common.collect.Lists.newArrayList;
import static com.puppycrawl.tools.checkstyle.api.FileContents.*;

import java.io.*;
import java.util.Map;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FileContentsTest {

    private FileContents fileContents;
    private static final String SAMPLE_FILE_NAME = "Sample.java";
    private static final String[] SAMPLE_LINES = {
            "/**",
            " * This is a sample Java file.",
            " */",
            "public class Sample {}",
            "/**",
            " * This is a comment in a sample Java file.",
            " */",
            "// This is a single-line comment."
    };

    @Before
    public void setup() {
        fileContents = new FileContents(SAMPLE_FILE_NAME, SAMPLE_LINES);
    }

    @Test
    public void testFileContentsConstructorWithFileNameAndLines() {
        assertEquals(SAMPLE_FILE_NAME, fileContents.getFileName());
        assertEquals(SAMPLE_LINES.length, fileContents.getLines().length);
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        File file = new File("src/test/java/com/example/junit/FileContentsTest.java");
        FileText fileText = new FileText(file);
        fileContents = new FileContents(fileText);
        assertEquals(file.toString(), fileContents.getFileName());
    }

    @Test
    public void testReportSingleLineComment() {
        int startLineNo = 4;
        int startColNo = 0;
        fileContents.reportCppComment(startLineNo, startColNo);
        TextBlock comment = fileContents.cppComments.get(startLineNo);
        assertNotNull(comment);
        assertEquals("// This is a single-line comment.", comment.getText());
    }

    @Test
    public void testGetCppComments() {
        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        assertEquals(comment, cppComments.get(startLineNo));
    }

    @Test(expected = IllegalStateException.class)
    public void testReportBlockCommentWithoutEnd() {
        int startLineNo = 1;
        int startColNo = 0;
        int endLineNo = startLineNo;
        int endColNo = 0;
        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
    }

    @Test
    public void testGetCComments() {
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(0, cComments.size());
    }

    @Test
    public void testExtractCComment() {
        int startLineNo = 2;
        int startColNo = 0;
        int endLineNo = 5;
        int endColNo = 10;
        String[] cComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
        assertEquals(5, cComment.length);
        assertEquals("/**\n * This is a comment in a sample Java file.\n */\n", String.join("", cComment));
    }

    @Test
    public void testGetJavadocBefore() {
        int lineNoBefore = 2;
        TextBlock javadocBefore = fileContents.getJavadocBefore(lineNoBefore);
        assertNotNull(javadocBefore);
        assertEquals("/**\n * This is a comment in a sample Java file.\n */\n", javadocBefore.getText());
    }

    @Test
    public void testLineIsBlank() {
        int lineNo = 0;
        assertTrue(fileContents.lineIsBlank(lineNo));
    }

    @Test
    public void testLineIsComment() {
        int lineNo = 3;
        assertTrue(fileContents.lineIsComment(lineNo));
    }

    @Test
    public void testHasIntersectionWithComment() {
        int startLineNo = 3;
        int startColNo = 0;
        int endLineNo = 5;
        int endColNo = 10;
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        assertTrue(fileContents.inPackageInfo());
    }

    @Test(expected = IllegalStateException.class)
    public void testHasIntersectionWithCommentOutOfBounds() {
        int startLineNo = 0;
        int startColNo = 0;
        int endLineNo = 3;
        int endColNo = 0;
        fileContents.hasIntersectionWithCppComment(startLineNo, startColNo, endLineNo, endColNo);
    }
}