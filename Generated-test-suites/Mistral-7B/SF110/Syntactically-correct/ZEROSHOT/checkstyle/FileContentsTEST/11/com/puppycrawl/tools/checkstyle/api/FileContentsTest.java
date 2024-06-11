package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.CommentListener;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileTextType;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.LineNumber;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class FileContentsTest {

    private CommentListener listener;
    private FileContents fileContents;
    private FileText text;

    @Before
    public void setUp() {
        listener = mock(CommentListener.class);
        text = new FileText("test.java", FileTextType.Java, Arrays.asList("public class Test {}", "// this is a comment", "}"));
        fileContents = new FileContents("test.java", text);
        fileContents.addListener(listener);
    }

    @Test
    public void testFileContentsCreationWithFileNameAndLines() {
        String[] lines = {"public class Test {}", "// this is a comment", "}"};
        FileContents fileContents = new FileContents("test.java", lines);
        assertEquals("test.java", fileContents.getFileName());
        assertEquals(3, fileContents.getLines().length);
        assertEquals("public class Test {}", fileContents.getLines()[0]);
        assertEquals("// this is a comment", fileContents.getLines()[1]);
        assertEquals("}", fileContents.getLines()[2]);
    }

    @Test
    public void testFileContentsCreationWithFileText() {
        FileContents fileContents = new FileContents(text);
        assertEquals("test.java", fileContents.getFileName());
        assertEquals(3, fileContents.getLines().length);
        assertEquals("public class Test {}", fileContents.getLines()[0]);
        assertEquals("// this is a comment", fileContents.getLines()[1]);
        assertEquals("}", fileContents.getLines()[2]);
    }

    @Test
    public void testReportSingleLineComment() {
        int startLineNo = 1;
        int startColNo = 10;
        fileContents.reportSingleLineComment("JavaDoc", startLineNo, startColNo);
        TextBlock expectedComment = new TextBlock("// this is a comment", startColNo, startLineNo, 15);
        verify(listener).reportSingleLineComment("JavaDoc", eq(startLineNo), eq(startColNo), eq(expectedComment.getText()));
    }

    @Test
    public void testReportBlockComment() {
        String[] cComment = {"/**", "* This is a comment", "*/"};
        int startLineNo = 1;
        int startColNo = 10;
        int endLineNo = 3;
        int endColNo = 20;
        fileContents.reportBlockComment("Javadoc", startLineNo, startColNo, endLineNo, endColNo);
        TextBlock expectedComment = new TextBlock(cComment, startColNo, endLineNo, endColNo);
        verify(listener).reportBlockComment("Javadoc", eq(startLineNo), eq(startColNo), eq(endLineNo), eq(endColNo), eq(expectedComment.getText()));
    }

    @Test
    public void testGetCppComments() {
        int startLineNo = 1;
        int startColNo = 10;
        fileContents.reportCppComment(startLineNo, startColNo);
        TextBlock expectedComment = new TextBlock("// this is a comment", startColNo, startLineNo, 15);
        List<TextBlock> comments = Arrays.asList(expectedComment);
        when(listener.getCppComments()).thenReturn(ImmutableMap.of(startLineNo, comments));
        ImmutableMap<Integer, TextBlock> actualComments = fileContents.getCppComments();
        assertEquals(1, actualComments.size());
        assertEquals(expectedComment, actualComments.get(startLineNo));
    }

    @Test
    public void testGetCComments() {
        int startLineNo = 1;
        int startColNo = 10;
        int endLineNo = 3;
        int endColNo = 20;
        fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
        TextBlock[] cComment = {new TextBlock("/**", 10, startLineNo, 12), new TextBlock("* This is a comment", 12, endLineNo, 20), new TextBlock("*/", endColNo, endLineNo, endColNo + 1)};
        List<TextBlock> comments = new ArrayList<>();
        comments.add(cComment[0]);
        comments.add(cComment[1]);
        when(listener.getCComments()).thenReturn(ImmutableMap.of(startLineNo, comments));
        ImmutableMap<Integer, List<TextBlock>> actualComments = fileContents.getCComments();
        assertEquals(1, actualComments.size());
        List<TextBlock> expectedComments = actualComments.get(startLineNo);
        assertEquals(2, expectedComments.size());
        assertEquals(cComment[0], expectedComments.get(0));
        assertEquals(cComment[1], expectedComments.get(1));
    }

    @Test
    public void testGetJavadocBefore() {
        int lineNoBefore = 0;
        TextBlock expectedComment = new TextBlock("public class Test {}", 0, 0, 37);
        when(listener.getJavadocBefore(eq(lineNoBefore))).thenReturn(expectedComment);
        TextBlock actualComment = fileContents.getJavadocBefore(lineNoBefore);
        assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testLineIsBlank() {
        int lineNo = 1;
        String blankLine = " ";
        FileText text = new FileText("test.java", FileTextType.Java, Arrays.asList(blankLine, "public class Test {}", ""));
        FileContents fileContents = new FileContents("test.java", text);
        assertTrue(fileContents.lineIsBlank(lineNo));
    }

    @Test
    public void testLineIsComment() {
        int lineNo = 1;
        String commentLine = "// this is a comment";
        FileText text = new FileText("test.java", FileTextType.Java, Arrays.asList("", commentLine, ""));
        FileContents fileContents = new FileContents("test.java", text);
        assertTrue(fileContents.lineIsComment(lineNo));
    }

    @Test
    public void testHasIntersectionWithComment() {
        int startLineNo = 1;
        int startColNo = 10;
        int endLineNo = 3;
        int endColNo = 20;
        TextBlock cComment = new TextBlock("/**", 10, startLineNo, 12);
        TextBlock cppComment = new TextBlock("// this is a comment", 15, startLineNo, 30);
        when(listener.getCppComments()).thenReturn(ImmutableMap.of(startLineNo, Arrays.asList(cppComment)));
        when(listener.getCComments()).thenReturn(ImmutableMap.of(startLineNo, Arrays.asList(cComment)));
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testHasIntersectionWithComment_NoIntersection() {
        int startLineNo = 1;
        int startColNo = 10;
        int endLineNo = 3;
        int endColNo = 20;
        when(listener.getCppComments()).thenReturn(Collections.emptyMap());
        when(listener.getCComments()).thenReturn(Collections.emptyMap());
        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "package-info.java";
        FileContents fileContents = new FileContents(fileName, null);
        assertTrue(fileContents.inPackageInfo());
    }

    @Test
    public void testInPackageInfo_NotPackageInfo() {
        String fileName = "Test.java";
        FileContents fileContents = new FileContents(fileName, null);
        assertFalse(fileContents.inPackageInfo());
    }
}