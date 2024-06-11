package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

public class FileContentsTest {

    @Test
    public void testFileContents() {
        String fileName = "TestFile.java";
        String[] lines = { "@Test", "public void testSomething() {" };
        FileContents fileContents = new FileContents(fileName, lines);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(Arrays.asList(lines), fileContents.getLines());
        assertNull(fileContents.getText());
        assertNull(fileContents.getJavadocBefore(0));
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsBlank(1));
        assertFalse(fileContents.lineIsComment(0));
        assertFalse(fileContents.lineIsComment(1));
        assertFalse(fileContents.hasIntersectionWithComment(0, 0, 0, 0));
        assertFalse(fileContents.inPackageInfo());
    }

    @Test
    public void testFileContentsFromFileText() {
        String fileName = "TestFile.java";
        String fileContent = "/**\n" +
                " * Test file.\n" +
                " */\n" +
                "@Test\n" +
                "public void testSomething() {\n" +
                "}";
        FileText fileText = new FileText(new String[] { fileContent });
        FileContents fileContents = new FileContents(fileText);

        assertEquals(fileName, fileContents.getFileName());
        assertEquals(Arrays.asList(fileContent.split("\n")), fileContents.getLines());
        assertEquals(fileContent, fileContents.getText().toString());
        assertNull(fileContents.getJavadocBefore(0));
        assertFalse(fileContents.lineIsBlank(0));
        assertFalse(fileContents.lineIsComment(0));
        assertTrue(fileContents.hasIntersectionWithComment(0, 0, 0, fileContent.length()));
        assertFalse(fileContents.inPackageInfo());
    }

    @Test
    public void testReportSingleLineComment() {
        String fileName = "TestFile.java";
        String[] lines = { "// This is a single line comment" };
        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportSingleLineComment("JavaDoc", 1, 0);

        TextBlock comment = fileContents.cppComments.get(1);
        assertEquals("This is a single line comment", comment.getText());
        assertEquals(1, comment.getStartLineNumber());
        assertEquals(0, comment.getStartColumnNumber());
        assertEquals(1, comment.getEndLineNumber());
        assertEquals(comment.getText().length(), comment.getEndColumnNumber());
    }

    @Test
    public void testReportBlockComment() {
        String fileName = "TestFile.java";
        String[] lines = { "/** This is a block comment */", "// This is a single line comment" };
        FileContents fileContents = new FileContents(fileName, lines);

        fileContents.reportBlockComment("JavaDoc", 1, 0, 2, 0);

        TextBlock comment = fileContents.clangComments.get(1).get(0);
        assertEquals("This is a block comment", comment.getText());
        assertEquals(1, comment.getStartLineNumber());
        assertEquals(0, comment.getStartColumnNumber());
        assertEquals(2, comment.getEndLineNumber());
        assertEquals(0, comment.getEndColumnNumber());
    }

    @Test
    public void testGetCppComments() {
        String fileName = "TestFile.java";
        String[] lines = { "// This is a single line JavaDoc comment", "/** This is a block JavaDoc comment */", "// This is a single line C comment", "/** This is a block C comment */" };
        FileContents fileContents = new FileContents(fileName, lines);

        ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

        TextBlock javaDocComment = cppComments.get(1);
        assertEquals("This is a block JavaDoc comment", javaDocComment.getText());
        assertEquals(1, javaDocComment.getStartLineNumber());
        assertEquals(0, javaDocComment.getStartColumnNumber());
        assertEquals(2, javaDocComment.getEndLineNumber());
        assertEquals(0, javaDocComment.getEndColumnNumber());

        TextBlock cComment = cppComments.get(3);
        assertEquals("This is a block C comment", cComment.getText());
        assertEquals(3, cComment.getStartLineNumber());
        assertEquals(0, cComment.getStartColumnNumber());
        assertEquals(4, cComment.getEndLineNumber());
        assertEquals(0, cComment.getEndColumnNumber());

        assertNull(cppComments.get(0));
        assertNull(cppComments.get(2));
    }

    @Test
    public void testGetCComments() {
        String fileName = "TestFile.java";
        String[] lines = { "/** This is a block JavaDoc comment */", "// This is a single line C comment", "/** This is a block C comment */" };
        FileContents fileContents = new FileContents(fileName, lines);

        ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();

        List<TextBlock> cCommentsList = cComments.get(3);
        assertEquals(2, cCommentsList.size());

        TextBlock javaDocComment = cCommentsList.get(0);
        assertEquals("This is a block JavaDoc comment", javaDocComment.getText());
        assertEquals(1, javaDocComment.getStartLineNumber());
        assertEquals(0, javaDocComment.getStartColumnNumber());
        assertEquals(2, javaDocComment.getEndLineNumber());
        assertEquals(0, javaDocComment.getEndColumnNumber());

        TextBlock cComment = cCommentsList.get(1);
        assertEquals("This is a single line C comment", cComment.getText());
        assertEquals(2, cComment.getStartLineNumber());
        assertEquals(0, cComment.getStartColumnNumber());
        assertEquals(2, cComment.getEndLineNumber());
        assertEquals(cComment.getText().length(), cComment.getEndColumnNumber());

        List<TextBlock> emptyList = cComments.get(1);
        assertTrue(emptyList.isEmpty());

        assertNull(cComments.get(0));
    }

}