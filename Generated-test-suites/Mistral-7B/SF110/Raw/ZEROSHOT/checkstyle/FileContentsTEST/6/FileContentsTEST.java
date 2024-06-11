package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.grammars.java.*;

public class FileContentsTest {

    private FileContents fileContents;
    private TextBlock javadocComment;
    private TextBlock cComment;
    private TextBlock clangComment;

    @Before
    public void setup() {
        String fileName = "TestFile.java";
        String[] lines = {"package com.example.test;", "/** This is a javadoc comment */", "public class TestClass {", "}", "/** This is another javadoc comment */"};
        fileContents = new FileContents(fileName, lines);

        javadocComment = mock(TextBlock.class);
        when(javadocComment.getText()).thenReturn("This is a javadoc comment");

        cComment = mock(TextBlock.class);
        when(cComment.getText()).thenReturn("/* This is a c comment */");

        clangComment = mock(TextBlock.class);
        when(clangComment.getText()).thenReturn("// This is a clang comment");
    }

    @Test
    public void testConstructorWithFileNameAndLines() {
        assertEquals("TestFile.java", fileContents.getFileName());
        assertEquals(lines.length, fileContents.getLines().length);
    }

    @Test
    public void testConstructorWithFileText() {
        FileText fileText = mock(FileText.class);
        when(fileText.getFile()).thenReturn(new File("TestFile.java"));

        fileContents = new FileContents(fileText);
        assertEquals("TestFile.java", fileContents.getFileName());
    }

    @Test
    public void testReportSingleLineComment() {
        int startLineNo = 1;
        int startColNo = 10;

        fileContents.reportCppComment(startLineNo, startColNo);

        verify(fileContents, times(1)).reportCppComment(eq(startLineNo), eq(startColNo));
    }

    @Test
    public void testGetCppComments() {
        ImmutableMap<Integer, TextBlock> expectedCppComments = ImmutableMap.of(1, javadocComment, 3, cComment);
        ImmutableMap<Integer, TextBlock> actualCppComments = fileContents.getCppComments();

        assertEquals(expectedCppComments, actualCppComments);
    }

    @Test
    public void testReportBlockComment() {
        String type = "Javadoc";
        int startLineNo = 1;
        int startColNo = 1;
        int endLineNo = 2;
        int endColNo = 10;

        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);

        verify(fileContents, times(1)).reportCComment(eq(startLineNo), eq(startColNo), eq(endLineNo), eq(endColNo));
    }

    @Test
    public void testGetCComments() {
        ImmutableMap<Integer, List<TextBlock>> expectedCComments = ImmutableMap.of(1, Collections.singletonList(clangComment));
        ImmutableMap<Integer, List<TextBlock>> actualCComments = fileContents.getCComments();

        assertEquals(expectedCComments, actualCComments);
    }

    @Test
    public void testGetJavadocBefore() {
        int lineNoBefore = 2;
        TextBlock expectedJavadocComment = javadocComment;

        TextBlock actualJavadocComment = fileContents.getJavadocBefore(lineNoBefore);

        assertEquals(expectedJavadocComment, actualJavadocComment);
    }

    @Test
    public void testLineIsBlank() {
        int lineNo = 1;
        String blankLine = "";
        String nonBlankLine = "public class TestClass {";

        assertTrue(fileContents.lineIsBlank(lineNo - 1));
        assertFalse(fileContents.lineIsBlank(lineNo));

        assertEquals(blankLine, fileContents.getLine(lineNo - 1));
        assertEquals(nonBlankLine, fileContents.getLine(lineNo));
    }

    @Test
    public void testHasIntersectionWithComment() {
        int startLineNo = 2;
        int startColNo = 1;
        int endLineNo = 3;
        int endColNo = 10;

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        assertFalse(fileContents.hasIntersectionWithComment(1, 1, 1, 1));
    }

    @Test
    public void testInPackageInfo() {
        assertTrue(fileContents.inPackageInfo());
        assertFalse(fileContents.inPackageInfo());
        FileText fileText = mock(FileText.class);
        when(fileText.getFileName()).thenReturn("TestFile.java");
        fileContents = new FileContents(fileText);
        assertTrue(fileContents.inPackageInfo());
    }
}