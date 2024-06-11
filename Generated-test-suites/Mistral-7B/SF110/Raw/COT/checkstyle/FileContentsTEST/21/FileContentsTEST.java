package com.example.junit.test.filecontents;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.grammars.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class FileContentsTest {

 private static final String SAMPLE_FILE_NAME = "SampleFile.java";
 private static final int SAMPLE_START_LINE_NO = 1;
 private static final int SAMPLE_START_COL_NO = 10;
 private static final int SAMPLE_END_LINE_NO = 3;
 private static final int SAMPLE_END_COL_NO = 20;

 private FileContents fileContents;
 private FileText mockFileText;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 mockFileText = mock(FileText.class);
 fileContents = new FileContents(SAMPLE_FILE_NAME, mockFileText);
 }

 @Test
 public void testFileName() {
 assertEquals(SAMPLE_FILE_NAME, fileContents.getFileName());
 }

 @Test
 public void testGetText() {
 FileText expectedText = new FileText(new String[]{"line1", "line2", "line3"});
 when(mockFileText.toLinesArray()).thenReturn(new String[]{"line1", "line2", "line3"});
 assertEquals(expectedText, fileContents.getText());
 }

 @Test
 public void testGetLines() {
 String[] expectedLines = new String[]{"line1", "line2", "line3"};
 when(mockFileText.toLinesArray()).thenReturn(expectedLines);
 assertArrayEquals(expectedLines, fileContents.getLines());
 }

 @Test
 public void testGetLine() {
 String expectedLine = "line1";
 when(mockFileText.get(0)).thenReturn(expectedLine);
 assertEquals(expectedLine, fileContents.getLine(0));
 }

 @Test
 public void testReportSingleLineComment() {
 String expectedComment = "singleline comment";
 int startLineNo = 1;
 int startColNo = 10;
 fileContents.reportCppComment(startLineNo, startColNo);
 TextBlock actualComment = fileContents.cppComments.get(startLineNo);
 assertEquals(expectedComment, actualComment.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportSingleLineCommentNegativeLineNumber() {
 int startLineNo = -1;
 int startColNo = 10;
 fileContents.reportCppComment(startLineNo, startColNo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportSingleLineCommentNegativeColumnNumber() {
 int startLineNo = 1;
 int startColNo = -1;
 fileContents.reportCppComment(startLineNo, startColNo);
 }

 @Test
 public void testReportBlockComment() {
 String[] expectedComment = new String[]{"block comment text", "another line of comment text"};
 int startLineNo = 1;
 int startColNo = 10;
 int endLineNo = 3;
 int endColNo = 20;
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 TextBlock actualComment = fileContents.clangComments.get(startLineNo).get(0);
 assertEquals(expectedComment[0], actualComment.getText().split("\\r\\n")[0]);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportBlockCommentNegativeEndLineNumber() {
 int startLineNo = 1;
 int startColNo = 10;
 int endLineNo = -1;
 int endColNo = 20;
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportBlockCommentNegativeEndColumnNumber() {
 int startLineNo = 1;
 int startColNo = 10;
 int endLineNo = 3;
 int endColNo = -1;
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 }

 @Test
 public void testGetCppComments() {
 ImmutableMap<Integer, TextBlock> expectedMap = ImmutableMap.of(1, new TextBlock("singleline comment"));
 ImmutableMap<Integer, TextBlock> actualMap = fileContents.getCppComments();
 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testGetCComments() {
 ImmutableMap<Integer, List<TextBlock>> expectedMap = ImmutableMap.of(1, Arrays.asList(new TextBlock("block comment text"), new TextBlock("another line of comment text")));
 ImmutableMap<Integer, List<TextBlock>> actualMap = fileContents.getCComments();
 assertEquals(expectedMap, actualMap);
 }

 @Test
 public void testGetJavadocBefore() {
 TextBlock expectedComment = new TextBlock("javadoc comment text");
 int lineNoBefore = 2;
 fileContents = new FileContents(SAMPLE_FILE_NAME, new String[]{"/**", " * javadoc comment text", " */", "line1", "line2"});
 TextBlock actualComment = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expectedComment, actualComment);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetJavadocBeforeNegativeLineNumber() {
 int lineNoBefore = -1;
 fileContents = new FileContents(SAMPLE_FILE_NAME, new String[]{"line1", "line2"});
 fileContents.getJavadocBefore(lineNoBefore);
 }

 @Test
 public void testHasIntersectionWithComment() {
 FileContents fileContentsWithComment = new FileContents(SAMPLE_FILE_NAME, new String[]{"/**", " * comment text", " */", "line1", "line2"});
 assertTrue(fileContentsWithComment.hasIntersectionWithComment(SAMPLE_START_LINE_NO, SAMPLE_START_COL_NO, SAMPLE_END_LINE_NO, SAMPLE_END_COL_NO));
 }

 @Test
 public void testHasIntersectionWithCommentNoIntersection() {
 FileContents fileContentsWithoutComment = new FileContents(SAMPLE_FILE_NAME, new String[]{"line1", "line2"});
 assertFalse(fileContentsWithoutComment.hasIntersectionWithComment(SAMPLE_START_LINE_NO, SAMPLE_START_COL_NO, SAMPLE_END_LINE_NO, SAMPLE_END_COL_NO));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHasIntersectionWithCommentNegativeStartLineNumber() {
 int startLineNo = -1;
 int startColNo = 10;
 int endLineNo = 3;
 int endColNo = 20;
 fileContents = new FileContents(SAMPLE_FILE_NAME, new String[]{"line1", "line2"});
 fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHasIntersectionWithCommentNegativeStartColumnNumber() {
 int startLineNo = 1;
 int startColNo = -1;
 int endLineNo = 3;
 int endColNo = 20;
 fileContents = new FileContents(SAMPLE_FILE_NAME, new String[]{"line1", "line2"});
 fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHasIntersectionWithCommentNegativeEndLineNumber() {
 int startLineNo = 1;
 int startColNo = 10;
 int endLineNo = -1;
 int endColNo = 20;
 fileContents = new FileContents(SAMPLE_FILE_NAME, new String[]{"line1", "line2"});
 fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHasIntersectionWithCommentNegativeEndColumnNumber() {
 int startLineNo = 1;
 int startColNo = 10;
 int endLineNo = 3;
 int endColNo = -1;
 fileContents = new FileContents(SAMPLE_FILE_NAME, new String[]{"line1", "line2"});
 fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 }

 @Test
 public void testInPackageInfo() {
 assertTrue(fileContents.inPackageInfo());
 fileContents = new FileContents(SAMPLE_FILE_NAME + ".java");
 assertTrue(fileContents.inPackageInfo());
 }

 @Test
 public void testInPackageInfoFalse() {
 FileContents fileContentsWithoutPackageInfo = new FileContents(SAMPLE_FILE_NAME + "Test.java");
 assertFalse(fileContentsWithoutPackageInfo.inPackageInfo());
 }

}