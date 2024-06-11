package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileContents.TextBlock;
import com.puppycrawl.tools.checkstyle.api.FileLineOffsetMapping;
import com.puppycrawl.tools.checkstyle.api.LineNumberUtil;
import com.puppycrawl.tools.checkstyle.grammars.JavaTokenTypes;

public class FileContentsTest {

    private FileContents fileContents;
    private FileText fileText;
    private Comment comment;
    private Map<Integer, TextBlock> cppComments;
    private Map<Integer, List<TextBlock>> clangComments;

    @Before
    public void setUp() {
        fileText = mock(FileText.class);
        comment = mock(Comment.class);
        cppComments = new HashMap<>();
        clangComments = new HashMap<>();

        fileContents = new FileContents("testFile.java", fileText);
    }

    @Test
    public void testFileContentsConstructorWithFileName() {
        assertEquals("testFile.java", fileContents.getFileName());
        verify(fileText, times(1)).getFile();
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        when(fileText.getFile()).thenReturn(new File("testFile.java"));
        assertEquals("testFile.java", fileContents.getFileName());
        verify(fileText, times(1)).getFile();
    }

    @Test
    public void testReportSingleLineComment() {
        int startLineNo = 1;
        int startColNo = 1;

        fileContents.reportSingleLineComment("CPP", startLineNo, startColNo);

        assertEquals(ImmutableMap.of(startLineNo, comment), fileContents.getCppComments());
    }

    @Test
    public void testReportBlockComment() {
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 2;
        int endColNo = 2;

        fileContents.reportBlockComment("C", startLineNo, startColNo, endLineNo, endColNo);

        List<TextBlock> clangComment = Lists.newArrayList(comment);
        assertEquals(ImmutableMap.of(startLineNo, clangComment), fileContents.getCComments());
    }

    @Test
    public void testGetJavadocBefore() {
        TextBlock javadocBefore = new TextBlock("/** This is a javadoc comment */");
        int lineNoBefore = 1;

        fileContents.javadocComments.put(lineNoBefore, javadocBefore);

        assertEquals(javadocBefore, fileContents.getJavadocBefore(lineNoBefore));
    }

    @Test
    public void testGetText() {
        String expectedText = "Hello World";
        when(fileText.toLinesArray()).thenReturn(new String[] { expectedText });

        assertEquals(expectedText, fileContents.getText().get(0));
    }

    @Test
    public void testGetLines() {
        String[] expectedLines = new String[] { "Line 1", "Line 2" };
        when(fileText.toLinesArray()).thenReturn(expectedLines);

        assertEquals(Arrays.asList(expectedLines), Arrays.asList(fileContents.getLines()));
    }

    @Test
    public void testGetLine() {
        String expectedLine = "Line 1";
        when(fileText.get(0)).thenReturn(expectedLine);

        assertEquals(expectedLine, fileContents.getLine(0));
    }

    @Test
    public void testHasIntersectionWithComment() {
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 2;
        int endColNo = 2;

        fileContents.reportBlockComment("C", startLineNo, startColNo, endLineNo, endColNo);

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "package-info.java";
        FileContents fileContents = new FileContents(fileName, null);

        assertTrue(fileContents.inPackageInfo());

        fileName = "TestFile.java";
        fileContents = new FileContents(fileName, null);

        assertFalse(fileContents.inPackageInfo());
    }

    @Test
    public void testExtractCComment() {
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 2;
        int endColNo = 2;

        String[] expectedResult = new String[] { "Line 1", "Line 2" };

        when(fileText.get(startLineNo - 1)).thenReturn("Line 1");
        when(fileText.get(startLineNo)).thenReturn("/** This is a comment */ * Line 2");
        when(fileText.get(endLineNo)).thenReturn("*/ Line 3");

        assertArrayEquals(expectedResult, fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testReportCppComment() {
        int startLineNo = 1;
        int startColNo = 1;

        fileContents.reportCppComment(startLineNo, startColNo);

        assertEquals(ImmutableMap.of(startLineNo, comment), fileContents.getCppComments());
    }

    @Test
    public void testReportCComment() {
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 2;
        int endColNo = 2;

        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);

        List<TextBlock> clangComment = Lists.newArrayList(comment);
        assertEquals(ImmutableMap.of(startLineNo, clangComment), fileContents.getCComments());
    }
}