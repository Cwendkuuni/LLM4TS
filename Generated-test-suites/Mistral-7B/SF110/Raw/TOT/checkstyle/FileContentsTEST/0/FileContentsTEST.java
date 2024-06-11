package com.example.junit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableMap;
import com.puppycrawl.tools.checkstyle.api.CommentListener;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileType;
import com.puppycrawl.tools.checkstyle.api.LineNumberUtil;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.utils.FileUtils;

public class FileContentsTest {

 private FileContents fileContents;
 private CommentListener mockCommentListener;

 @Before
 public void setup() {
 mockCommentListener = mock(CommentListener.class);

 String fileName = "test.java";
 String[] lines = new String[] { "@Test", "public void test() {" };
 FileText text = new FileText(FileUtils.toInputStream("test.java"), FileType.JAVA, fileName);

 fileContents = new FileContents(fileName, lines);
 fileContents.addCommentListener(mockCommentListener);
 }

 @Test
 public void testReportSingleLineComment() {
 String comment = "// This is a single line comment";
 int startLineNo = 1;
 int startColNo = 5;

 fileContents.reportSingleLineComment("JAVADOC", startLineNo, startColNo);

 TextBlock expectedComment = new TextBlock("// This is a single line comment", startLineNo, startColNo, startLineNo, startColNo + comment.length());
 assertEquals(expectedComment, fileContents.cppComments.get(startLineNo));
 }

 @Test
 public void testReportBlockComment() {
 String comment = "/* This is a block comment */";
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 3;
 int endColNo = 20;

 fileContents.reportBlockComment("JAVADOC", startLineNo, startColNo, endLineNo, endColNo);

 TextBlock expectedComment = new TextBlock(comment, startColNo, endLineNo, startLineNo, endColNo);
 List<TextBlock> clangComments = fileContents.clangComments.get(startLineNo);
 assertEquals(expectedComment, clangComments.get(0));

 TextBlock javadocComment = fileContents.javadocComments.get(endLineNo - 1);
 assertEquals(expectedComment, javadocComment);
 }

 @Test
 public void testReportCppComment() {
 String comment = "// This is a Cpp comment";
 int startLineNo = 1;
 int startColNo = 5;

 fileContents.reportCppComment(startLineNo, startColNo);

 TextBlock expectedComment = new TextBlock(comment, startColNo, startLineNo, startLineNo, startColNo + comment.length());
 assertEquals(expectedComment, fileContents.cppComments.get(startLineNo));
 }

 @Test
 public void testGetCppComments() {
 ImmutableMap<Integer, TextBlock> expectedComments = ImmutableMap.<Integer, TextBlock>builder()
 .put(1, new TextBlock("// This is a Cpp comment 1", 5, 1, 5, 20))
 .build();

 when(mockCommentListener.getCppComments()).thenReturn(expectedComments);

 fileContents = new FileContents("test.java", mockCommentListener);

 ImmutableMap<Integer, TextBlock> actualComments = fileContents.getCppComments();
 assertEquals(expectedComments, actualComments);
 }

 @Test
 public void testGetCComments() {
 List<TextBlock> expectedComments = Arrays.asList(new TextBlock("/* This is a C comment 1 */", 5, 1, 5, 20), new TextBlock("/* This is a C comment 2 */", 7, 1, 7, 20));

 when(mockCommentListener.getCComments()).thenReturn(expectedComments);

 fileContents = new FileContents("test.java", mockCommentListener);

 List<TextBlock> actualComments = fileContents.getCComments();
 assertEquals(expectedComments, actualComments);
 }

 @Test
 public void testGetJavadocBefore() {
 String comment = "/** This is a Javadoc comment */";
 int lineNoBefore = 1;

 TextBlock expectedComment = new TextBlock(comment, 5, lineNoBefore, 5, lineNoBefore + comment.length());
 TextBlock actualComment = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expectedComment, actualComment);
 }

 @Test
 public void testLineIsBlank() {
 String blankLine = "";
 int lineNo = 1;
 assertTrue(fileContents.lineIsBlank(lineNo));
 assertTrue(fileContents.lineIsBlank(lineNo + 1));
 assertFalse(fileContents.lineIsBlank(lineNo + 2));
 assertFalse(fileContents.lineIsBlank(lineNo + 2, blankLine));
 }

 @Test
 public void testLineIsComment() {
 String singleLineComment = "// This is a single line comment";
 int lineNo = 1;
 assertTrue(fileContents.lineIsComment(lineNo));
 assertFalse(fileContents.lineIsComment(lineNo + 1));
 assertTrue(fileContents.lineIsComment(lineNo, singleLineComment));
 }

 @Test
 public void testHasIntersectionWithComment() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 3;
 int endColNo = 20;

 assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 assertTrue(fileContents.hasIntersectionWithCppComment(startLineNo, startColNo, endLineNo, endColNo));
 assertTrue(fileContents.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testInPackageInfo() {
 String packageInfoFileName = "package-info.java";
 File packageInfoFile = new File(packageInfoFileName);
 FileContents packageInfoFileContents = new FileContents(packageInfoFileName);
 assertTrue(packageInfoFileContents.inPackageInfo());
 }

 @Test
 public void testGetLines() {
 String[] expectedLines = new String[] { "@Test", "public void test() {" };
 String[] actualLines = fileContents.getLines();
 assertArrayEquals(expectedLines, actualLines);
 }

 @Test
 public void testGetFileName() {
 assertEquals("test.java", fileContents.getFileName());
 }

 @Test
 public void testGetFilename() {
 assertEquals("test.java", fileContents.getFilename());
 }

 @Test
 public void testGetText() {
 FileText expectedText = new FileText(new String[] { "@Test", "public void test() {" }, FileType.JAVA, "test.java");
 FileText actualText = fileContents.getText();
 assertEquals(expectedText, actualText);
 }
}