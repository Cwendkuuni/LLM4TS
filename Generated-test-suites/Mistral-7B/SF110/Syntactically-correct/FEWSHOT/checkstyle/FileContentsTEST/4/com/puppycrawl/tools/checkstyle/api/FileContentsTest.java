package com.puppycrawl.tools.checkstyle.api.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

public class FileContentsTest {

    @Test
    public void testFileContents_constructor_lines() {
        String fileName = "testFile.java";
        FileContents fileContents = new FileContents(fileName, "line 1".split("\\s+"), "line 2".split("\\s+"), "line 3".split("\\s+"));
        assertEquals(fileName, fileContents.getFileName());
        assertEquals(Arrays.asList("line 1", "line 2", "line 3"), fileContents.getLines());
    }

    @Test
    public void testFileContents_constructor_FileText() {
        String fileName = "testFile.java";
        FileText fileText = new FileText(new ByteArrayInputStream("line 1\nline 2\nline 3".getBytes()));
        FileContents fileContents = new FileContents(fileName, fileText);
        assertEquals(fileName, fileContents.getFileName());
        assertEquals(Arrays.asList("line 1", "line 2", "line 3"), fileContents.getLines());
    }

    @Test
    public void testGetCPpComments() {
        String fileName = "testFile.java";
        String cppComment = "/* test comment */";
        String[] lines = {"line 1", "/*" + cppComment + "*/line 2", "line 3"};
        FileContents fileContents = new FileContents(fileName, lines);
        ImmutableMap<Integer, Comment> cppComments = fileContents.getCppComments();
        assertEquals(1, cppComments.size());
        Comment comment = cppComments.get(2);
        assertNotNull(comment);
        assertEquals(cppComment, comment.getText());
        assertEquals(2, comment.getStartLineNo());
        assertEquals(2, comment.getStartColNo());
        assertEquals(-1, comment.getEndLineNo());
        assertEquals(-1, comment.getEndColNo());
    }

    @Test
    public void testGetCComments() {
        String fileName = "testFile.java";
        String cComment = "/** test comment */";
        String[] lines = {"line 1", "line 2", cComment + "\nline 4"};
        FileContents fileContents = new FileContents(fileName, lines);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        assertEquals(1, cComments.size());
        List<TextBlock> commentList = cComments.get(3);
        assertEquals(1, commentList.size());
        TextBlock comment = commentList.get(0);
        assertNotNull(comment);
        assertEquals(cComment, comment.getText());
        assertEquals(3, comment.getStartLineNo());
        assertEquals(0, comment.getStartColNo());
        assertEquals(3, comment.getEndLineNo());
        assertEquals(-1, comment.getEndColNo());
    }

    @Test
    public void testGetJavadocBefore() {
        String fileName = "testFile.java";
        String javadocComment = "/** test comment */";
        String[] lines = {"line 1", "", javadocComment};
        FileContents fileContents = new FileContents(fileName, lines);
        TextBlock javadocBefore = fileContents.getJavadocBefore(1);
        assertNotNull(javadocBefore);
        assertEquals(javadocComment, javadocBefore.getText());
        assertEquals(1, javadocBefore.getStartLineNo());
        assertEquals(0, javadocBefore.getStartColNo());
        assertEquals(1, javadocBefore.getEndLineNo());
        assertEquals(-1, javadocBefore.getEndColNo());
    }

    @Test
    public void testHasIntersectionWithComment() {
        String fileName = "testFile.java";
        String cppComment = "/* test comment */";
        String cComment = "/** test comment */";
        String[] lines = {"line 1", "/*" + cppComment + "*/line 2", cComment + "\nline 4", "line 5"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 1, 1));
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 2, 1));
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 2, cppComment.length()));
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 3, 1));
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 3, cComment.length()));
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 4, 1));
        assertTrue(fileContents.hasIntersectionWithComment(1, 1, 4, cComment.length()));
        assertTrue(fileContents.hasIntersectionWithComment(2, 1, 2, cppComment.length()));
        assertTrue(fileContents.hasIntersectionWithComment(3, 1, 3, 1));
        assertTrue(fileContents.hasIntersectionWithComment(3, 1, 3, cComment.length()));
        assertFalse(fileContents.hasIntersectionWithComment(4, 1, 4, 1));
        assertFalse(fileContents.hasIntersectionWithComment(5, 1, 5, 1));
    }

    @Test
    public void testLineIsBlank() {
        String fileName = "testFile.java";
        String[] lines = {"", "line 1", "", "line 2", "", "line 3"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.lineIsBlank(0));
        assertTrue(fileContents.lineIsBlank(2));
        assertTrue(fileContents.lineIsBlank(4));
        assertFalse(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsBlank(3));
    }

    @Test
    public void testLineIsComment() {
        String fileName = "testFile.java";
        String cppComment = "/* test comment */";
        String cComment = "/** test comment */";
        String[] lines = {"line 1", "/*" + cppComment + "*/line 2", cComment + "\nline 4", "line 5"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertTrue(fileContents.lineIsComment(0));
        assertTrue(fileContents.lineIsComment(1));
        assertTrue(fileContents.lineIsComment(2));
        assertFalse(fileContents.lineIsComment(3));
        assertFalse(fileContents.lineIsComment(4));
    }

    @Test
    public void testGetText() {
        String fileName = "testFile.java";
        String[] lines = {"line 1", "line 2", "line 3"};
        FileContents fileContents = new FileContents(fileName, lines);
        FileText text = fileContents.getText();
        assertEquals(Arrays.asList("line 1", "line 2", "line 3"), text.toLinesArray());
    }

    @Test
    public void testGetLine() {
        String fileName = "testFile.java";
        String[] lines = {"line 1", "line 2", "line 3"};
        FileContents fileContents = new FileContents(fileName, lines);
        assertEquals("line 2", fileContents.getLine(1));
    }

    @Test
    public void testInPackageInfo() {
        String fileName = "testFile/package-info.java";
        FileContents fileContents = new FileContents(fileName, new String[0]);
        assertTrue(fileContents.inPackageInfo());
    }

    @Test
    public void testGetFileName() {
        String fileName = "testFile.java";
        FileContents fileContents = new FileContents(fileName, new String[0]);
        assertEquals(fileName, fileContents.getFileName());
    }

    @Test
    public void testGetFilename() {
        String fileName = "testFile.java";
        FileContents fileContents = new FileContents(fileName, new String[0]);
        assertEquals(fileName, fileContents.getFilename());
    }

    @Test
    public void testReportBlockComment() {
        String fileName = "testFile.java";
        String[] lines = {"line 1", "/** test comment */line 2", "line 3"};
        FileContents fileContents = new FileContents(fileName, lines);
        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
        List<TextBlock> commentList = cComments.get(2);
        assertEquals(1, commentList.size());
        TextBlock comment = commentList.get(0);
        assertNotNull(comment);
        assertEquals("test comment", comment.getText());
        assertEquals(2, comment.getStartLineNo());
        assertEquals(0, comment.getStartColNo());
        assertEquals(2, comment.getEndLineNo());
        assertEquals(-1, comment.getEndColNo());
    }

    @Test
    public void testReportSingleLineComment() {
        String fileName = "testFile.java";
        String[] lines = {"line 1", "// test comment line 1", "// test comment line 2", "line 3"};
        FileContents fileContents = new FileContents(fileName, lines);
        ImmutableMap<Integer, Comment> cppComments = fileContents.getCppComments();
        Comment comment = cppComments.get(2);
        assertNotNull(comment);
        assertEquals("test comment line 1", comment.getText());
        assertEquals(2, comment.getStartLineNo());
        assertEquals(0, comment.getStartColNo());
        assertEquals(-1, comment.getEndLineNo());
        assertEquals(-1, comment.getEndColNo());
    }

    @Test
    public void testReportCppComment() {
        String fileName = "testFile.java";
        String[] lines = {"line 1", "// test comment line 1", "/* test comment */line 2", "line 3"};
        FileContents fileContents = new FileContents(fileName, lines);
        ImmutableMap<Integer, Comment> cppComments = fileContents.getCppComments();
        Comment comment = cppComments.get(2);
        assertNotNull(comment);
        assertEquals("test comment", comment.getText());
        assertEquals(2, comment.getStartLineNo());
        assertEquals(0, comment.getStartColNo());
        assertEquals(3, comment.getEndLineNo());
        assertEquals(-1, comment.getEndColNo());
    }

    @Test
    public void testGetEmptyMap() {
        String fileName = "testFile.java";
        FileContents fileContents = new FileContents(fileName, new String[0]);
        ImmutableMap<Integer, TextBlock> emptyCppComments = fileContents.getCppComments();
        assertEquals(0, emptyCppComments.size());
    }

    @Test
    public void testGetEmptyList() {
        String fileName = "testFile.java";
        FileContents fileContents = new FileContents(fileName, new String[0]);
        ImmutableMap<Integer, List<TextBlock>> emptyCComments = fileContents.getCComments();
        assertEquals(0, emptyCComments.size());
    }

    @Test
    public void testGetNull() {
        assertNull(new FileContents(null, new String[0]).getText());
    }
}