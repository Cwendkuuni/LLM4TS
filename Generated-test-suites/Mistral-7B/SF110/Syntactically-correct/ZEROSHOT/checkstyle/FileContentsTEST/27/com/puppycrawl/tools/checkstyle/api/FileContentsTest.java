package com.example.junit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.grammars.*;

public class FileContentsTest {

    private static final String TEST_FILE = "test.java";
    private static final String[] TEST_LINES = {"package com.example;", "import static org.junit.Assert.*;", "import org.mockito.Mockito;"};

    private FileContents fileContents;
    private CommentListener commentListenerMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        commentListenerMock = mock(CommentListener.class);
    }

    @After
    public void tearDown() {
        fileContents = null;
    }

    @Test
    public void testFileContentsConstructionWithFileNameAndLines() {
        File file = new File(TEST_FILE);
        FileText textMock = mock(FileText.class);
        when(textMock.getFile()).thenReturn(file);
        when(textMock.toLinesArray()).thenReturn(TEST_LINES);

        fileContents = new FileContents(TEST_FILE, TEST_LINES);

        assertEquals(TEST_FILE, fileContents.getFileName());
        assertEquals(TEST_LINES.length, fileContents.getText().size());
        assertEquals(TEST_LINES[0], fileContents.getLines()[0]);
        verify(commentListenerMock, never()).reportSingleLineComment(anyString(), anyInt(), anyInt());
        verify(commentListenerMock, never()).reportBlockComment(anyString(), anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test
    public void testFileContentsConstructionWithFileText() {
        FileText textMock = mock(FileText.class);

        fileContents = new FileContents(textMock);

        assertEquals(TEST_FILE, fileContents.getFileName());
        assertEquals(TEST_LINES.length, fileContents.getText().size());
        assertEquals(TEST_LINES[0], fileContents.getLines()[0]);
        verify(commentListenerMock, never()).reportSingleLineComment(anyString(), anyInt(), anyInt());
        verify(commentListenerMock, never()).reportBlockComment(anyString(), anyInt(), anyInt(), anyInt(), anyInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileContentsConstructionWithNullFileName() {
        fileContents = new FileContents(null, TEST_LINES);
    }

    @Test
    public void testReportSingleLineComment() {
        int startLineNo = 1;
        int startColNo = 5;

        fileContents = new FileContents(TEST_FILE, TEST_LINES);
        doCallRealMethod().when(fileContents).reportSingleLineComment(anyString(), eq(startLineNo), eq(startColNo));

        fileContents.reportSingleLineComment("// Single line comment", startLineNo, startColNo);

        verify(commentListenerMock).reportSingleLineComment("Single line comment", startLineNo, startColNo);
    }

    @Test
    public void testReportBlockComment() {
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 10;

        fileContents = new FileContents(TEST_FILE, TEST_LINES);
        doCallRealMethod().when(fileContents).reportBlockComment(anyString(), eq(startLineNo), eq(startColNo), eq(endLineNo), eq(endColNo));

        fileContents.reportBlockComment("/** Multiline comment */", startLineNo, startColNo, endLineNo, endColNo);

        verify(commentListenerMock).reportBlockComment("Multiline comment", startLineNo, startColNo, endLineNo, endColNo);
    }

    @Test
    public void testGetCppComments() {
        String cCommentLine = "// C comment";
        String cCommentBlock = "/* C comment */";
        int cCommentStartLineNo = 1;
        int cCommentStartColNo = 5;
        int cCommentEndLineNo = 2;
        int cCommentEndColNo = 10;

        FileText textMock = mock(FileText.class);
        when(textMock.get(cCommentStartLineNo - 1)).thenReturn(cCommentLine);
        when(textMock.get(cCommentStartLineNo)).thenReturn(cCommentBlock);
        when(textMock.get(cCommentEndLineNo - 1)).thenReturn(cCommentLine);
        when(textMock.get(cCommentEndLineNo)).thenReturn(cCommentLine);

        FileContents fileContents = new FileContents(textMock);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

        TextBlock cppComment = cppComments.get(cCommentStartLineNo);
        assertEquals(cCommentStartLineNo, cppComment.getStartLineNumber());
        assertEquals(cCommentStartColNo, cppComment.getStartColumnNumber());
        assertEquals(cCommentEndLineNo, cppComment.getEndLineNumber());
        assertEquals(cCommentEndColNo, cppComment.getEndColumnNumber());
        assertEquals(cCommentLine, cppComment.getText());

        TextBlock anotherCppComment = cppComments.get(cCommentEndLineNo);
        assertEquals(cCommentEndLineNo, anotherCppComment.getStartLineNumber());
        assertEquals(cCommentStartColNo, anotherCppComment.getStartColumnNumber());
        assertEquals(cCommentEndLineNo, anotherCppComment.getEndLineNumber());
        assertEquals(cCommentLine, anotherCppComment.getText());
    }

    @Test
    public void testGetCComments() {
        String cCommentLineStart = "/** C comment start */";
        String cCommentLineEnd = "*/ C comment end */";
        String cCommentLineMiddle = " C comment middle";

        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 4;
        int endColNo = 10;

        FileText textMock = mock(FileText.class);
        when(textMock.get(startLineNo - 1)).thenReturn(cCommentLineStart);
        when(textMock.get(endLineNo)).thenReturn(cCommentLineEnd);
        when(textMock.get(eq(startLineNo))).thenReturn(cCommentLineMiddle);
        when(textMock.size()).thenReturn(endLineNo + 1);

        FileContents fileContents = new FileContents(textMock);

        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();

        List<TextBlock> clangComments = cComments.get(startLineNo);
        assertEquals(1, clangComments.size());
        TextBlock cComment = clangComments.get(0);
        assertEquals(cCommentLineStart, cComment.getText());
        assertEquals(startLineNo, cComment.getStartLineNumber());
        assertEquals(startColNo, cComment.getStartColumnNumber());
        assertEquals(endLineNo, cComment.getEndLineNumber());
        assertEquals(endColNo, cComment.getEndColumnNumber());

        List<TextBlock> anotherClangComments = cComments.get(endLineNo);
        assertEquals(0, anotherClangComments.size());
    }

    @Test
    public void testGetJavadocBefore() {
        String javadocLine = "/** Javadoc comment before */";
        int lineNoBefore = 1;

        FileText textMock = mock(FileText.class);
        when(textMock.get(lineNoBefore - 2)).thenReturn(javadocLine);

        FileContents fileContents = new FileContents(textMock);

        TextBlock javadocBefore = fileContents.getJavadocBefore(lineNoBefore);

        assertEquals(javadocLine, javadocBefore.getText());
        assertEquals(lineNoBefore - 2, javadocBefore.getStartLineNumber());
        assertEquals(0, javadocBefore.getStartColumnNumber());
        assertEquals(lineNoBefore - 2, javadocBefore.getEndLineNumber());
        assertEquals(0, javadocBefore.getEndColumnNumber());
    }

    @Test
    public void testHasIntersectionWithComment() {
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 10;

        FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testGetLines() {
        FileText textMock = mock(FileText.class);
        when(textMock.toLinesArray()).thenReturn(TEST_LINES);

        FileContents fileContents = new FileContents(textMock);

        String[] lines = fileContents.getLines();

        assertEquals(TEST_LINES.length, lines.length);
        assertEquals(TEST_LINES[0], lines[0]);
    }

    @Test
    public void testGetLine() {
        FileText textMock = mock(FileText.class);
        when(textMock.get(eq(0))).thenReturn("Line 0");
        when(textMock.get(eq(1))).thenReturn("Line 1");

        FileContents fileContents = new FileContents(textMock);

        assertEquals("Line 0", fileContents.getLine(0));
        assertEquals("Line 1", fileContents.getLine(1));
    }

    @Test
    public void testInPackageInfo() {
        FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);

        assertFalse(fileContents.inPackageInfo());

        TextBlock packageInfo = mock(TextBlock.class);
        when(packageInfo.getText()).thenReturn("package com.example.packageinfo;");

        fileContents.javadocComments.put(0, packageInfo);

        assertTrue(fileContents.inPackageInfo());
    }

    @Test
    public void testHasIntersectionWithComment_cpp() {
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 10;

        FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);

        String cCommentLine = "// C comment";
        String cCommentBlock = "/* C comment */";

        doReturn(Collections.singletonList(new TextBlock(cCommentLine, startLineNo, startColNo, startLineNo, startColNo))).when(fileContents).getCppComments();

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testHasIntersectionWithComment_cpp_multiple() {
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 4;
        int endColNo = 10;

        FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);

        String cCommentLineStart = "/** C comment start */";
        String cCommentLineEnd = "*/ C comment end */";
        String cCommentLineMiddle = " C comment middle";

        doReturn(Collections.singletonList(new TextBlock(cCommentLineStart, startLineNo, 0, startLineNo, 0))).when(fileContents).getCppComments();
        doReturn(Collections.singletonList(new TextBlock(cCommentLineEnd, endLineNo + 1, 0, endLineNo + 1, 0))).when(fileContents).getCppComments();
        doReturn(Collections.singletonList(new TextBlock(cCommentLineMiddle, startLineNo, startColNo, startLineNo, endColNo))).when(fileContents).getCppComments();

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testHasIntersectionWithComment_cpp_noIntersection() {
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 10;

        FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);

        String cCommentLine = "// C comment";
        String cCommentBlock = "/* C comment */";

        doReturn(Collections.singletonList(new TextBlock(cCommentLine, 5, 0, 5, 0))).when(fileContents).getCppComments();

        assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testHasIntersectionWithComment_cpp_intersectsButNotContained() {
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 10;

        FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);

        String cCommentLineStart = "/** C comment start */";
        String cCommentLineEnd = "*/ C comment end */";
        String cCommentLineMiddle = " C comment middle";

        doReturn(Collections.singletonList(new TextBlock(cCommentLineStart, startLineNo, 0, startLineNo, 0))).when(fileContents).getCppComments();
        doReturn(Collections.singletonList(new TextBlock(cCommentLineEnd, endLineNo + 1, 0, endLineNo + 1, 0))).when(fileContents).getCppComments();

        TextBlock cComment = new TextBlock(cCommentLineMiddle, startLineNo, startColNo, startLineNo, endColNo);

        doReturn(Collections.singletonList(cComment)).when(fileContents).getCppComments();

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
    }

    @Test
    public void testHasIntersectionWithComment_cpp_intersectsAndContained() {
        int startLineNo = 1;
        int startColNo = 5;
        int endLineNo = 3;
        int endColNo = 10;

        FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);

        String cCommentLineStart = "/** C comment start */";
        String cCommentLineEnd = "*/ C comment end */";
        String cCommentLineMiddle = " C comment middle";

        doReturn(Collections.singletonList(new TextBlock(cCommentLineStart, startLineNo, 0, startLineNo, 0))).when(fileContents).getCppComments();
        doReturn(Collections.singletonList(new TextBlock(cCommentLineEnd, endLineNo + 1, 0, endLineNo + 1, 0))).when(fileContents).getCppComments();
        doReturn(Collections.singletonList(new TextBlock(cCommentLineMiddle, startLineNo, startColNo, endLineNo, endColNo))).when(fileContents).getCppComments();

        TextBlock cComment = new TextBlock(cCommentLineMiddle, startLineNo, startColNo, endLineNo, endColNo);

        doReturn(Collections.singletonList(cComment)).when(fileContents).getCppComments();

        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
        assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo).isContainedIn(cComment));
    }
}