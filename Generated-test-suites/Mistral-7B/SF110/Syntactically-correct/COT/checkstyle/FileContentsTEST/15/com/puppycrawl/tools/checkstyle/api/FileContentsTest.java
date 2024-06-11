package com.example.junit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.*;

public class FileContentsTest {

 private FileContents fileContents;
 private FileText fileTextMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 fileTextMock = mock(FileText.class);

 String fileName = "testFile.java";
 fileContents = new FileContents(fileName, fileTextMock);
 }

 @Test
 public void testFileContentsConstructorWithFilenameAndLines() {
 String[] lines = {"public class TestClass {"};
 FileContents fileContentsWithLines = new FileContents(fileName, lines);
 assertEquals(fileName, fileContentsWithLines.getFileName());
 }

 @Test
 public void testFileContentsConstructorWithFileText() {
 File file = new File("testFile.java");
 fileTextMock.setFile(file);
 FileContents fileContentsWithFileText = new FileContents(fileTextMock);
 assertEquals(fileName, fileContentsWithFileText.getFileName());
 }

 @Test
 public void testReportSingleLineComment() {
 int startLineNo = 1;
 int startColNo = 1;
 fileContents.reportCppComment(startLineNo, startColNo);
 TextBlock expectedComment = new TextBlock("// comment");
 assertEquals(expectedComment, fileContents.cppComments.get(startLineNo));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportSingleLineCommentInvalidLineNumber() {
 int startLineNo = -1;
 int startColNo = 1;
 fileContents.reportCppComment(startLineNo, startColNo);
 }

 @Test
 public void testGetCppComments() {
 TextBlock comment1 = new TextBlock("/* this is a c comment 1 */");
 TextBlock comment2 = new TextBlock("/* this is a c comment 2 */");
 fileContents.reportCComment(1, 1, 2, 1);
 fileContents.reportCComment(2, 1, 2, 2);
 ImmutableMap<Integer, TextBlock> expectedCppComments = ImmutableMap.of(1, comment1, 2, comment2);
 ImmutableMap<Integer, TextBlock> actualCppComments = fileContents.getCppComments();
 assertEquals(expectedCppComments, actualCppComments);
 }

 @Test
 public void testGetCComments() {
 List<TextBlock> commentList1 = Lists.newArrayList(new TextBlock("/* this is a c comment 1 */"), new TextBlock("/* this is a c comment 2 */"));
 List<TextBlock> commentList2 = Lists.newArrayList(new TextBlock("/* this is a c comment 3 */"), new TextBlock("/* this is a c comment 4 */"));
 fileContents.reportCComment(1, 1, 1, 1, commentList1);
 fileContents.reportCComment(2, 1, 2, 2, commentList2);
 ImmutableMap<Integer, List<TextBlock>> expectedCComments = ImmutableMap.of(1, commentList1, 2, commentList2);
 ImmutableMap<Integer, List<TextBlock>> actualCComments = fileContents.getCComments();
 assertEquals(expectedCComments, actualCComments);
 }

 @Test
 public void testExtractCComment() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 2;
 String[] expectedCComment = new String[]{"/* this is a c comment */"};
 String[] actualCComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
 assertArrayEquals(expectedCComment, actualCComment);
 }

 @Test
 public void testExtractCCommentWithSingleLine() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 1;
 int endColNo = 2;
 String[] expectedCComment = new String[]{"// this is a single line c comment"};
 String[] actualCComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
 assertArrayEquals(expectedCComment, actualCComment);
 }

 @Test
 public void testGetJavadocBefore() {
 int lineNoBefore = 1;
 TextBlock expectedJavadoc = new TextBlock("** this is a javadoc comment */");
 TextBlock actualJavadoc = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expectedJavadoc, actualJavadoc);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetJavadocBeforeInvalidLineNumber() {
 int lineNoBefore = -1;
 fileContents.getJavadocBefore(lineNoBefore);
 }

 @Test
 public void testLineIsBlank() {
 int lineNo = 1;
 String blankLine = "";
 boolean expectedResult = true;
 boolean actualResult = fileContents.lineIsBlank(lineNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testLineIsComment() {
 int lineNo = 1;
 String singleLineComment = "// this is a single line comment";
 boolean expectedResult = true;
 boolean actualResult = fileContents.lineIsComment(lineNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasIntersectionWithCommentFalse() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 2;
 boolean expectedResult = false;
 boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasIntersectionWithCommentTrue() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 2;
 int intersectingLineNo = 1;
 int intersectingColNo = 1;
 fileContents.reportCComment(intersectingLineNo, intersectingColNo, startLineNo, endLineNo);
 boolean expectedResult = true;
 boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testInPackageInfoFalse() {
 String fileName = "testFile.java";
 boolean expectedResult = false;
 boolean actualResult = fileContents.inPackageInfo();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testInPackageInfoTrue() {
 String fileName = "package-info.java";
 boolean expectedResult = true;
 boolean actualResult = fileContents.inPackageInfo();
 assertEquals(expectedResult, actualResult);
 }
}