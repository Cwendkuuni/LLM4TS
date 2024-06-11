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

import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

public class FileContentsTest {

 private FileContents fileContents;
 private FileText fileTextMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 fileTextMock = mock(FileText.class);
 fileContents = new FileContents("testFile.java", fileTextMock);
 }

 @Test
 public void testFileContentsConstructorWithFile() {
 File file = new File("testFile.java");
 String[] lines = {" package test;", " public class TestClass {", " // this is a comment", " }"};
 fileContents = new FileContents("testFile.java", lines);
 assertEquals("testFile.java", fileContents.getFileName());
 }

 @Test
 public void testFileContentsConstructorWithFileText() {
 String[] lines = {" package test;", " public class TestClass {", " // this is a comment", " }"};
 fileContents = new FileContents(fileTextMock);
 assertEquals("testFile.java", fileContents.getFileName());
 }

 @Test
 public void testReportSingleLineComment() {
 int startLineNo = 1;
 int startColNo = 10;
 fileContents.reportCppComment(startLineNo, startColNo);
 TextBlock expectedComment = new TextBlock("// this is a comment", startColNo, startLineNo);
 assertEquals(expectedComment, fileContents.cppComments.get(startLineNo));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportSingleLineCommentWithNegativeStartLineNumber() {
 int startLineNo = -1;
 int startColNo = 1;
 fileContents.reportCppComment(startLineNo, startColNo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportSingleLineCommentWithNegativeStartColumnNumber() {
 int startLineNo = 1;
 int startColNo = -1;
 fileContents.reportCppComment(startLineNo, startColNo);
 }

 @Test
 public void testReportBlockComment() {
 String[] cComment = new String[]{"/**", " * This is a Javadoc comment", " */"};
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 3;
 int endColNo = 11;
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 TextBlock expectedComment = new TextBlock(cComment, startColNo, endLineNo, endColNo);
 assertEquals(expectedComment, fileContents.clangComments.get(startLineNo).get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportBlockCommentWithNegativeStartLineNumber() {
 int startLineNo = -1;
 int startColNo = 1;
 int endLineNo = 1;
 int endColNo = 1;
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportBlockCommentWithNegativeEndLineNumber() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = -1;
 int endColNo = 1;
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportBlockCommentWithNegativeStartColumnNumber() {
 int startLineNo = 1;
 int startColNo = -1;
 int endLineNo = 1;
 int endColNo = 1;
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportBlockCommentWithEndColumnNumberGreaterThanLineLength() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 1;
 int endColNo = fileTextMock.size();
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 }

 @Test
 public void testGetCppComments() {
 String[] lines = {" package test;", " /**", " * This is a Javadoc comment", " */", " public class TestClass {", " // this is a C comment", " }"};
 fileContents = new FileContents("testFile.java", lines);
 ImmutableMap<Integer, TextBlock> expectedCppComments = getCppComments();
 assertEquals(expectedCppComments, fileContents.getCppComments());
 }

 @Test
 public void testGetCComments() {
 String[] lines = {" package test;", " /**", " * This is a Javadoc comment", " */", " public class TestClass {", " // this is a C comment", " }"};
 fileContents = new FileContents("testFile.java", lines);
 ImmutableMap<Integer, List<TextBlock>> expectedCComments = getCComments();
 assertEquals(expectedCComments, fileContents.getCComments());
 }

 @Test
 public void testExtractCComment() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 3;
 int endColNo = 11;
 String[] expectedCComment = new String[]{"/**", " * This is a Javadoc comment", " */"};
 String[] actualCComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
 assertArrayEquals(expectedCComment, actualCComment);
 }

 @Test
 public void testGetJavadocBefore() {
 int lineNoBefore = 2;
 TextBlock expectedJavadocBefore = new TextBlock("/**", 1, 1);
 TextBlock actualJavadocBefore = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expectedJavadocBefore, actualJavadocBefore);
 }

 @Test
 public void testGetJavadocBeforeWithInvalidLineNumberBefore() {
 int lineNoBefore = -1;
 TextBlock expectedJavadocBefore = null;
 TextBlock actualJavadocBefore = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expectedJavadocBefore, actualJavadocBefore);
 }

 @Test
 public void testGetJavadocBeforeWithInvalidLineNumberBeforeAndNoJavadocBefore() {
 int lineNoBefore = 1;
 TextBlock expectedJavadocBefore = null;
 TextBlock actualJavadocBefore = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expectedJavadocBefore, actualJavadocBefore);
 }

 @Test
 public void testHasIntersectionWithComment() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 1;
 boolean expectedResult = false;
 boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasIntersectionWithCommentWithOverlappingArea() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 2;
 boolean expectedResult = true;
 boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasIntersectionWithCommentWithOutsideArea() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 3;
 int endColNo = 1;
 boolean expectedResult = false;
 boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasIntersectionWithCommentWithSameStartLineSameStartColumn() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 1;
 int endColNo = 1;
 boolean expectedResult = false;
 boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasIntersectionWithCommentWithSameStartLineDifferentStartColumn() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 1;
 int endColNo = 2;
 boolean expectedResult = false;
 boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasIntersectionWithCommentWithSameStartColumnDifferentStartLine() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 1;
 boolean expectedResult = false;
 boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasIntersectionWithCommentWithSameStartLineSameEndLine() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 1;
 int endColNo = 1;
 boolean expectedResult = false;
 boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasIntersectionWithCommentWithSameStartLineDifferentEndLine() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 1;
 boolean expectedResult = false;
 boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasIntersectionWithCommentWithDifferentStartLineDifferentStartColumn() {
 int startLineNo1 = 1;
 int startColNo1 = 1;
 int endLineNo1 = 1;
 int endColNo1 = 1;
 int startLineNo2 = 2;
 int startColNo2 = 1;
 int endLineNo2 = 2;
 int endColNo2 = 1;
 boolean expectedResult = false;
 boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo1, startColNo1, endLineNo1, endColNo1);
 boolean expectedResult2 = false;
 boolean actualResult2 = fileContents.hasIntersectionWithComment(startLineNo2, startColNo2, endLineNo2, endColNo2);
 assertEquals(expectedResult, actualResult);
 assertEquals(expectedResult2, actualResult2);
 }

 @Test
 public void testInPackageInfo() {
 String fileName = "package-info.java";
 boolean expectedResult = true;
 boolean actualResult = fileContents.inPackageInfo();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testInPackageInfoWithNonPackageInfoFileName() {
 String fileName = "TestClass.java";
 boolean expectedResult = false;
 boolean actualResult = fileContents.inPackageInfo();
 assertEquals(expectedResult, actualResult);
 }

 private ImmutableMap<Integer, TextBlock> getCppComments() {
 List<TextBlock> cppCommentsList = Arrays.asList(
 new TextBlock("// this is a C comment", 1, 1),
 new TextBlock("// another C comment", 2, 1)
 );
 return Maps.immutableEnumMap(Maps.newHashMap(Collections.singletonMap(1, cppCommentsList.get(0))));
 }

 private ImmutableMap<Integer, List<TextBlock>> getCComments() {
 List<List<TextBlock>> cCommentsList = Arrays.asList(
 Arrays.asList(
 new TextBlock("/**", 1, 1),
 new TextBlock(" * This is a Javadoc comment", 1, 1),
 new TextBlock(" */", 3, 1)
 ),
 Arrays.asList(
 new TextBlock("/**", 4, 1),
 new TextBlock(" * Another Javadoc comment", 4, 1),
 new TextBlock(" */", 5, 1)
 )
 );
 return Maps.immutableEnumMap(Maps.newHashMap(Collections.singletonMap(4, cCommentsList.get(1))));
 }
}