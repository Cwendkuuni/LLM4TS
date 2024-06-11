package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.FileTextFactory;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class FileContentsTest {

    private FileContents fileContents;
    private FileText fileText;
    private FileTextFactory fileTextFactory;

    @Before
    public void setup() {
        fileTextFactory = mock(FileTextFactory.class);
        fileText = mock(FileText.class);
        when(fileTextFactory.get(anyString(), any(List.class))).thenReturn(fileText);

        fileContents = new FileContents("filename.java", fileText);
    }

    @Test
    public void testReportSingleLineComment() {
        final int startLineNo = 1;
        final int startColNo = 5;

        fileContents.reportSingleLineComment("cpp", startLineNo, startColNo);
        verify(fileText, times(1)).reportSingleLineComment("cpp", eq(startLineNo - 1), anyInt());
    }

    @Test
    public void testReportBlockComment() {
        final int startLineNo = 1;
        final int startColNo = 5;
        final int endLineNo = 3;
        final int endColNo = 20;

        fileContents.reportBlockComment("cpp", startLineNo, startColNo, endLineNo, endColNo);
        verify(fileText, times(1)).reportBlockComment("cpp", eq(startLineNo - 1), anyInt(), eq(endLineNo), anyInt());
    }

    @Test
    public void testReportCppComment() {
        final int startLineNo = 1;
        final int startColNo = 5;

        fileContents.reportCppComment(startLineNo, startColNo);

        final String[] expectedComment = { "// This is a single line comment" };
        assertEquals(expectedComment[0], fileText.getComment(startLineNo - 1).getText()[0]);
    }

    @Test
    public void testGetCppComments() {
        final int startLineNo1 = 1;
        final int startColNo1 = 5;
        final int startLineNo2 = 3;
        final int startColNo2 = 10;

        fileContents.reportCppComment(startLineNo1, startColNo1);
        fileContents.reportCppComment(startLineNo2, startColNo2);

        ImmutableMap<Integer, TextBlock> actualComments = fileContents.getCppComments();
        TextBlock comment1 = new Comment(new String[] { "// This is a single line comment" }, 5, startLineNo1 - 1, startColNo1);
        TextBlock comment2 = new Comment(new String[] { "// This is another single line comment" }, 10, startLineNo2 - 1, startColNo2);
        ImmutableMap<Integer, TextBlock> expectedComments = ImmutableMap.of(startLineNo1, comment1, startLineNo2, comment2);

        assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testReportCComment() {
        final int startLineNo = 1;
        final int startColNo = 5;
        final int endLineNo = 3;
        final int endColNo = 20;

        final String[] cComment = { "/** This is a multi-line comment */" };
        when(fileText.get(startLineNo - 2)).thenReturn(cComment[0]);

        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);

        verify(fileText, times(1)).reportBlockComment("c", eq(startLineNo - 1), anyInt(), eq(endLineNo), anyInt());
    }

    @Test
    public void testGetCComments() {
        final int startLineNo1 = 1;
        final int startColNo1 = 5;
        final int startLineNo2 = 3;
        final int startColNo2 = 10;
        final int endLineNo1 = 5;
        final int endColNo1 = 20;
        final int endLineNo2 = 7;
        final int endColNo2 = 30;

        final String[] cComment1 = { "/** This is a multi-line comment */" };
        final String[] cComment2 = { "/** This is another multi-line comment */" };

        when(fileText.get(startLineNo1 - 2)).thenReturn(cComment1[0]);
        when(fileText.get(startLineNo2 - 2)).thenReturn(cComment2[0]);

        fileContents.reportCComment(startLineNo1, startColNo1, endLineNo1, endColNo1);
        fileContents.reportCComment(startLineNo2, startColNo2, endLineNo2, endColNo2);

        ImmutableMap<Integer, List<TextBlock>> actualComments = fileContents.getCComments();
        List<TextBlock> commentList1 = Lists.newArrayList(new Comment(cComment1, 5, startLineNo1 - 1, endLineNo1, endColNo1));
        List<TextBlock> commentList2 = Lists.newArrayList(new Comment(cComment2, 10, startLineNo2 - 1, endLineNo2, endColNo2));
        ImmutableMap.Builder<Integer, List<TextBlock>> expectedCommentsBuilder = ImmutableMap.builder();
        expectedCommentsBuilder.put(startLineNo1, commentList1);
        expectedCommentsBuilder.put(startLineNo2, commentList2);
        ImmutableMap<Integer, List<TextBlock>> expectedComments = expectedCommentsBuilder.build();

        assertEquals(expectedComments, actualComments);
    }

    @Test
    public void testExtractCComment() {
        final int startLineNo = 1;
        final int startColNo = 5;
        final int endLineNo = 3;
        final int endColNo = 20;

        final String[] expectedComment = { "/** This is a multi-line comment */" };
        when(fileText.get(startLineNo - 2)).thenReturn("/** This is a multi-line comment */");
        when(fileText.get(startLineNo - 1)).thenReturn("int a = 10;");
        when(fileText.get(startLineNo)).thenReturn("int b = 20;");
        when(fileText.get(endLineNo)).thenReturn("}");

        String[] actualComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);

        assertArrayEquals(expectedComment, actualComment);
    }

    @Test
    public void testGetJavadocBefore() {
        final int lineNoBefore = 3;

        final String expectedComment = "/** This is a javadoc comment */";
        when(fileText.get(lineNoBefore - 2)).thenReturn(expectedComment);

        TextBlock actualComment = fileContents.getJavadocBefore(lineNoBefore);

        assertEquals(expectedComment, actualComment.getText()[0]);
    }

    @Test
    public void testGetFileName() {
        assertEquals("filename.java", fileContents.getFileName());
    }

    @Test
    public void testGetText() {
        FileText expectedText = mock(FileText.class);
        when(expectedText.get(anyInt())).thenAnswer(i -> "Line " + i);

        when(fileTextFactory.get(anyString(), any(List.class))).thenReturn(expectedText);

        FileText actualText = fileContents.getText();

        assertEquals(expectedText, actualText);
    }

    @Test
    public void testGetLines() {
        List<String> expectedLines = Arrays.asList("Line 1", "Line 2", "Line 3");
        when(fileText.toLinesArray()).thenReturn(expectedLines.toArray(new String[expectedLines.size()]));

        String[] actualLines = fileContents.getLines();

        assertArrayEquals(expectedLines.toArray(new String[expectedLines.size()]), actualLines);
    }

    @Test
    public void testGetLine() {
        final String expectedLine = "int a = 10;";

        when(fileText.get(anyInt())).thenAnswer(i -> expectedLine);

        String actualLine = fileContents.getLine(0);

        assertEquals(expectedLine, actualLine);
    }

    @Test
    public void testLineIsBlank() {
        final int lineNo = 1;

        assertTrue(fileContents.lineIsBlank(lineNo));

        String blankLine = " ";
        when(fileText.get(lineNo)).thenReturn(blankLine);

        assertFalse(fileContents.lineIsBlank(lineNo));
    }

    @Test
    public void testLineIsComment() {
        final int lineNo = 1;

        assertTrue(fileContents.lineIsComment(lineNo));

        when(fileText.get(lineNo)).thenReturn("// This is a comment");

        assertTrue(fileContents.lineIsComment(lineNo));
    }

    @Test
    public void testHasIntersectionWithComment() {
        final int startLineNo = 1;
        final int startColNo = 5;
        final int endLineNo = 3;
        final int endColNo = 20;

        Map<Integer, List<TextBlock>> cComments = new HashMap<>();
        List<TextBlock> commentList1 = Lists.newArrayList(new Comment(new String[] { "/** This is a multi-line comment */" }, 5, 0, 5, 30));
        cComments.put(1, commentList1);

        Map<Integer, TextBlock> cppComments = new HashMap<>();
        TextBlock comment2 = new Comment(new String[] { "// This is a single line comment" }, 5, 0, 5, 15);
        cppComments.put(1, comment2);

        when(fileText.getCComments()).thenReturn(cComments);
        when(fileText.getCppComments()).thenReturn(cppComments);

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

        assertFalse(fileContents.hasIntersectionWithComment(startLineNo + 1, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        assertTrue(fileContents.inPackageInfo());

        fileContents = new FileContents("filename.java", fileText);
        assertFalse(fileContents.inPackageInfo());
    }
}