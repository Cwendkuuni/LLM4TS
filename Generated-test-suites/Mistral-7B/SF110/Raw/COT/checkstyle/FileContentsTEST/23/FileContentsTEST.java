package com.example.junit.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.ArrayList;
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

public class FileContentsTest {

    private FileContents fileContents;
    private FileText fileTextMock;
    private static final String FILENAME = "filename.java";
    private static final String[] LINES = {"line1", "line2", "line3"};

    @Before
    public void setup() {
        fileTextMock = Mockito.mock(FileText.class);
        fileContents = new FileContents(FILENAME, LINES);
    }

    @Test
    public void testFileContentsConstructorWithFileNameAndLines() {
        assertEquals(FILENAME, fileContents.getFileName());
        assertEquals(LINES, fileContents.getLines());
    }

    @Test
    public void testFileContentsConstructorWithFileText() {
        File fileMock = Mockito.mock(File.class);
        when(fileTextMock.getFile()).thenReturn(fileMock);

        fileContents = new FileContents(fileTextMock);
        assertEquals(FILENAME, fileContents.getFileName());
        assertEquals(fileTextMock.toLinesArray(), fileContents.getLines());
    }

    @Test
    public void testReportSingleLineComment() {
        int startLineNo = 1;
        int startColNo = 10;

        fileContents.reportCppComment(startLineNo, startColNo);

        List<TextBlock> cppComments = fileContents.getCComments().get(startLineNo);
        assertEquals(1, cppComments.size());

        TextBlock comment = cppComments.get(0);
        assertEquals(startLineNo, comment.getStartLineNo());
        assertEquals(startColNo, comment.getStartColNo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReportSingleLineCommentWithInvalidLineNumber() {
        int startLineNo = -1;

        fileContents.reportCppComment(startLineNo, 0);
    }

    @Test
    public void testReportBlockComment() {
        int startLineNo = 1;
        int startColNo = 10;
        int endLineNo = 3;
        int endColNo = 20;

        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);

        List<List<TextBlock>> clangComments = fileContents.getCComments().get(startLineNo);
        assertEquals(1, clangComments.size());

        List<TextBlock> commentList = clangComments.get(0);
        assertEquals(2, commentList.size());

        TextBlock startComment = commentList.get(0);
        assertEquals(startLineNo, startComment.getStartLineNo());
        assertEquals(startColNo, startComment.getStartColNo());

        TextBlock endComment = commentList.get(1);
        assertEquals(endLineNo, endComment.getStartLineNo());
        assertEquals(endColNo, endComment.getStartColNo());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReportBlockCommentWithInvalidStartOrEndLineNumber() {
        int startLineNo = 1;
        int startColNo = 10;
        int endLineNo = 0;
        int endColNo = 20;

        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
    }

    @Test
    public void testGetJavadocBefore() {
        int lineNoBefore = 1;

        TextBlock javadocBefore = fileContents.getJavadocBefore(lineNoBefore);
        assertNull(javadocBefore);

        String line1 = "/**";
        String line2 = " * This is a javadoc comment.";
        String line3 = " */";
        String[] linesWithJavadoc = {line1, line2, line3};

        fileContents = new FileContents(FILENAME, linesWithJavadoc);

        TextBlock javadoc = new TextBlock(" * This is a javadoc comment.", 1, 1);
        when(fileTextMock.get(lineNoBefore - 2)).thenReturn(line1);
        when(fileTextMock.get(lineNoBefore - 1)).thenReturn(line2);
        when(fileTextMock.get(lineNoBefore)).thenReturn(line3);
        when(fileTextMock.get(lineNoBefore + 1)).thenReturn(line1);
        when(fileTextMock.get(lineNoBefore + 2)).thenReturn(line2);
        when(fileTextMock.get(lineNoBefore + 3)).thenReturn(line3);

        TextBlock javadocBeforeExpected = javadoc;

        TextBlock javadocBeforeActual = fileContents.getJavadocBefore(lineNoBefore);

        assertEquals(javadocBeforeExpected, javadocBeforeActual);
    }

    @Test
    public void testGetLines() {
        String[] expectedLines = LINES;
        String[] actualLines = fileContents.getLines();

        assertArrayEquals(expectedLines, actualLines);
    }

    @Test
    public void testGetLine() {
        int index = 0;
        String expectedLine = "line1";
        String actualLine = fileContents.getLine(index);

        assertEquals(expectedLine, actualLine);
    }

    @Test
    public void testHasIntersectionWithComment() {
        int startLineNo = 1;
        int startColNo = 10;
        int endLineNo = 3;
        int endColNo = 20;

        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        fileContents = new FileContents(FILENAME, LINES);

        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        assertTrue(fileContents.inPackageInfo());

        FileContents fileContentsWithoutPackageInfo = new FileContents(FILENAME, LINES);
        assertFalse(fileContentsWithoutPackageInfo.inPackageInfo());
    }
}