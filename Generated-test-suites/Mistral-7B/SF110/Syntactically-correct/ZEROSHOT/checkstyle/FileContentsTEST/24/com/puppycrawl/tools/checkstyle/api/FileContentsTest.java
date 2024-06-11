package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.grammars.JavaMultiLineCommentGrammar;
import com.puppycrawl.tools.checkstyle.grammars.JavaSingleLineCommentGrammar;
import com.puppycrawl.tools.checkstyle.utils.FileText;

public class FileContentsTest {

 private FileContents fileContents;
 private FileText mockFileText;
 private String fileName;

 @Before
 public void setup() {
 mockFileText = mock(FileText.class);
 fileName = "testFile.java";
 fileContents = new FileContents(fileName, mockFileText);
 }

 @Test
 public void testFileContentsEmpty() {
 FileContents emptyFileContents = new FileContents(fileName);
 assertEquals(emptyFileContents.getFileName(), fileName);
 assertTrue(emptyFileContents.lineIsBlank(0));
 assertTrue(emptyFileContents.lineIsComment(0));
 assertNull(emptyFileContents.getText());
 assertNull(emptyFileContents.getLines());
 assertNull(emptyFileContents.getLine(0));
 assertNull(emptyFileContents.getFileName());
 assertNull(emptyFileContents.getJavadocBefore(0));
 assertFalse(emptyFileContents.hasIntersectionWithComment(0, 0, 0, 0));
 }

 @Test
 public void testFileContentsSingleLineComment() {
 String line = "// This is a single line comment";
 when(mockFileText.get(0)).thenReturn(line);
 fileContents = new FileContents(fileName, mockFileText);
 assertEquals(fileName, fileContents.getFileName());
 assertEquals("// This is a single line comment", fileContents.getLine(0));
 assertTrue(fileContents.lineIsComment(0));
 assertFalse(fileContents.lineIsBlank(0));
 assertNull(fileContents.getText());
 assertArrayEquals(new String[] { line }, fileContents.getLines());
 assertNull(fileContents.getFileName());
 assertNull(fileContents.getJavadocBefore(0));
 assertFalse(fileContents.hasIntersectionWithComment(0, 0, 0, 0));
 }

 @Test
 public void testFileContentsMultilineComment() {
 String line1 = "/**";
 String line2 = " * This is a multiline comment";
 String line3 = " */";
 when(mockFileText.get(0)).thenReturn(line1);
 when(mockFileText.get(1)).thenReturn(line2);
 when(mockFileText.get(2)).thenReturn(line3);
 fileContents = new FileContents(fileName, mockFileText);
 assertEquals(fileName, fileContents.getFileName());
 assertNull(fileContents.getLine(0));
 assertNull(fileContents.getLine(1));
 assertNull(fileContents.getLine(2));
 assertNull(fileContents.getText());
 assertArrayEquals(new String[] { line1, line2, line3 }, fileContents.getLines());
 assertNull(fileContents.getFileName());
 assertNull(fileContents.getJavadocBefore(0));
 assertFalse(fileContents.hasIntersectionWithComment(0, 0, 0, 0));

 TextBlock javadocComment = new TextBlock(new JavaMultiLineCommentGrammar().getCommentStartTag(),
 new JavaMultiLineCommentGrammar().getCommentEndTag(), new JavaSingleLineCommentGrammar().getCommentText());
 javadocComment.setLineNo(1);
 javadocComment.setColumnNo(0);
 fileContents.reportJavadocComment("", 1, 0, 1, 0, javadocComment);
 assertEquals(javadocComment, fileContents.getJavadocBefore(0));
 }

 @Test
 public void testFileContentsCComment() {
 String line1 = "/*";
 String line2 = " * This is a C style multiline comment";
 String line3 = " */";
 when(mockFileText.get(0)).thenReturn(line1);
 when(mockFileText.get(1)).thenReturn(line2);
 when(mockFileText.get(2)).thenReturn(line3);
 fileContents = new FileContents(fileName, mockFileText);
 assertEquals(fileName, fileContents.getFileName());
 assertNull(fileContents.getLine(0));
 assertNull(fileContents.getLine(1));
 assertNull(fileContents.getLine(2));
 assertNull(fileContents.getText());
 assertArrayEquals(new String[] { line1, line2, line3 }, fileContents.getLines());
 assertNull(fileContents.getFileName());
 assertNull(fileContents.getJavadocBefore(0));
 assertFalse(fileContents.hasIntersectionWithComment(0, 0, 0, 0));

 List<TextBlock> cCommentList = Arrays.asList(new TextBlock("C comment 1"), new TextBlock("C comment 2"));
 fileContents.reportCComment("C comment", 1, 0, 2, 0, cCommentList.toArray(new TextBlock[0]));
 assertEquals(2, fileContents.getCComments().size());
 List<TextBlock> cComments = fileContents.getCComments().get(1);
 assertEquals(2, cComments.size());
 assertEquals("C comment 1", cComments.get(0).getText());
 assertEquals("C comment 2", cComments.get(1).getText());
 }

 @Test
 public void testFileContentsMixedComments() {
 String line1 = "/* This is a C style multiline comment";
 String line2 = " * This is a Javadoc comment";
 String line3 = " */";
 when(mockFileText.get(0)).thenReturn(line1);
 when(mockFileText.get(1)).thenReturn(line2);
 when(mockFileText.get(2)).thenReturn(line3);
 fileContents = new FileContents(fileName, mockFileText);
 assertEquals(fileName, fileContents.getFileName());
 assertNull(fileContents.getLine(0));
 assertNull(fileContents.getLine(1));
 assertNull(fileContents.getLine(2));
 assertNull(fileContents.getText());
 assertArrayEquals(new String[] { line1, line2, line3 }, fileContents.getLines());
 assertNull(fileContents.getFileName());
 assertNull(fileContents.getJavadocBefore(0));
 assertFalse(fileContents.hasIntersectionWithComment(0, 0, 0, 0));

 List<TextBlock> cCommentList = Arrays.asList(new TextBlock("C comment 1"), new TextBlock("C comment 2"));
 fileContents.reportCComment("C comment", 1, 0, 2, 0, cCommentList.toArray(new TextBlock[0]));
 List<TextBlock> javadocCommentList = Arrays.asList(new TextBlock("Javadoc comment 1"), new TextBlock("Javadoc comment 2"));
 fileContents.reportJavadocComment("Javadoc comment", 2, 0, 2, 0, javadocCommentList.toArray(new TextBlock[0]));
 assertEquals(3, fileContents.getCComments().size());
 List<TextBlock> cComments = fileContents.getCComments().get(1);
 assertEquals(2, cComments.size());
 assertEquals("C comment 1", cComments.get(0).getText());
 assertEquals("C comment 2", cComments.get(1).getText());
 List<TextBlock> javadocComments = fileContents.getJavadocComments().get(1);
 assertEquals(2, javadocComments.size());
 assertEquals("Javadoc comment 1", javadocComments.get(0).getText());
 assertEquals("Javadoc comment 2", javadocComments.get(1).getText());
 }
}