package com.example.junit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.grammars.*;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import org.mockito.ArgumentMatchers.*;
import org.mockito.MockitoAnnotations;

public class FileContentsTest {

 private FileContents fileContents;
 private FileText mockFileText;
 private CommentListener mockCommentListener;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockFileText = mock(FileText.class);
 mockCommentListener = mock(CommentListener.class);
 fileContents = new FileContents("testFile.java", mockFileText);
 fileContents.setCommentListener(mockCommentListener);
 }

 @Test
 public void testFileContentsConstructorWithFileNameAndLines() {
 String[] lines = {"public class TestClass {", "// This is a single line comment", "}"};
 FileContents fileContentsWithLines = new FileContents("testFile.java", lines);
 assertEquals("testFile.java", fileContentsWithLines.getFileName());
 assertEquals(lines.length, fileContentsWithLines.getLines().length);
 }

 @Test
 public void testFileContentsConstructorWithFileText() {
 File file = new File("testFile.java");
 String content = "public class TestClass {}\n// This is a single line comment";
 FileText mockFileText = mock(FileText.class);
 when(mockFileText.getFile()).thenReturn(file);
 when(mockFileText.toLinesArray()).thenReturn(new String[]{"public class TestClass {", "// This is a single line comment", "}"});
 FileContents fileContentsWithFileText = new FileContents(mockFileText);
 assertEquals("testFile.java", fileContentsWithFileText.getFileName());
 assertEquals(3, fileContentsWithFileText.getLines().length);
 }

 @Test
 public void testReportSingleLineComment() {
 int startLineNo = 1;
 int startColNo = 10;
 fileContents.reportSingleLineComment("CPP", startLineNo, startColNo);
 verify(mockCommentListener).reportCppComment(eq(startLineNo), eq(startColNo));
 }

 @Test
 public void testReportBlockComment() {
 int startLineNo = 1;
 int startColNo = 10;
 int endLineNo = 3;
 int endColNo = 20;
 fileContents.reportBlockComment("JAVADOC", startLineNo, startColNo, endLineNo, endColNo);
 verify(mockCommentListener).reportCComment(eq(startLineNo), eq(startColNo), eq(endLineNo), eq(endColNo));
 }

 @Test
 public void testGetCppComments() {
 TextBlock comment = mock(TextBlock.class);
 int startLineNo = 1;
 int startColNo = 10;
 int endLineNo = 1;
 int endColNo = 20;
 when(mockCommentListener.getCppComments()).thenReturn(ImmutableMap.of(startLineNo, comment));
 fileContents.setCommentListener(mockCommentListener);
 fileContents.reportCppComment(startLineNo, startColNo);
 ImmutableMap<Integer, TextBlock> expectedCppComments = ImmutableMap.of(startLineNo, comment);
 assertEquals(expectedCppComments, fileContents.getCppComments());
 }

 @Test
 public void testGetCComments() {
 List<TextBlock> comments = Arrays.asList(mock(TextBlock.class), mock(TextBlock.class), mock(TextBlock.class));
 int startLineNo = 1;
 int endLineNo = 3;
 when(mockCommentListener.getCComments()).thenReturn(comments);
 fileContents.setCommentListener(mockCommentListener);
 fileContents.reportCComment(startLineNo, 0, endLineNo, comments.size());
 ImmutableMap<Integer, List<TextBlock>> expectedCComments = ImmutableMap.of(startLineNo, comments);
 assertEquals(expectedCComments, fileContents.getCComments());
 }

 @Test
 public void testGetJavadocBefore() {
 String javadoc = "/** This is a Javadoc comment */";
 String lineBefore = "public class TestClass {";
 int lineNoBefore = 1;
 fileContents.reportBlockComment("JAVADOC", lineNoBefore, 0, 2, javadoc.length());
 TextBlock expectedJavadocBefore = new TextBlock(javadoc);
 TextBlock result = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expectedJavadocBefore, result);
 }

 @Test
 public void testLineIsBlank() {
 String blankLine = " ";
 int lineNo = 1;
 assertTrue(fileContents.lineIsBlank(lineNo));
 fileContents = new FileContents(mockFileText);
 when(mockFileText.get(lineNo)).thenReturn(blankLine);
 assertTrue(fileContents.lineIsBlank(lineNo));
 }

 @Test
 public void testLineIsComment() {
 String singleLineComment = "// This is a single line comment";
 int lineNo = 1;
 assertTrue(fileContents.lineIsComment(lineNo));
 when(mockFileText.get(lineNo)).thenReturn(singleLineComment);
assertTrue(fileContents.lineIsComment(lineNo));
 }

 @Test
 public void testHasIntersectionWithComment() {
 int startLineNo = 1;
 int startColNo = 10;
 int endLineNo = 2;
 int endColNo = 20;
 assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testInPackageInfo() {
 assertTrue(fileContents.inPackageInfo());
 FileContents fileContentsWithPackageInfo = new FileContents("package-info.java", mockFileText);
 assertTrue(fileContentsWithPackageInfo.inPackageInfo());
 }

 @Test
 public void testExtractCComment() {
 int startLineNo = 1;
 int startColNo = 10;
 int endLineNo = 3;
 int endColNo = 20;
 String[] expected = {"public class TestClass {", "// This is a single line comment", "}", "public static void main(String[] args) {"};
 String[] actual = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testReportCComment() {
 int startLineNo = 1;
 int startColNo = 10;
 int endLineNo = 3;
 int endColNo = 20;
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 TextBlock comment = new TextBlock("This is a C comment");
 List<TextBlock> clangComments = Arrays.asList(comment);
 ImmutableMap<Integer, List<TextBlock>> expectedCComments = ImmutableMap.of(startLineNo, clangComments);
 assertEquals(expectedCComments, fileContents.getCComments());
 }
}