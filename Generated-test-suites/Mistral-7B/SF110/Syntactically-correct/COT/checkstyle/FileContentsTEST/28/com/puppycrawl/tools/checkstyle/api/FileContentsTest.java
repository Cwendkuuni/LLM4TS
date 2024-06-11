package com.example.junit.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.*;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.*;

public class FileContentsTest {

 private static final String TEST_FILE_PATH = "testFile.java";
 private static final String TEST_FILE_CONTENTS = "test contents";

 @Test
 public void testFileContentsConstructorWithFileNameAndLines() {
 FileContents fileContents = new FileContents(TEST_FILE_PATH, TEST_FILE_CONTENTS.split("\\r?\\n"));
 assertEquals(TEST_FILE_PATH, fileContents.getFileName());
 }

 @Test
 public void testFileContentsConstructorWithFileText() {
 File file = new File(TEST_FILE_PATH);
 FileText fileText = new FileText(file);
 FileContents fileContents = new FileContents(fileText);
 assertEquals(TEST_FILE_PATH, fileContents.getFileName());
 }

 @Test
 public void testReportSingleLineComment() {
 FileContents fileContents = createFileContents();
 int startLineNo = 1;
 int startColNo = 1;
 fileContents.reportSingleLineComment("JAVADOC", startLineNo, startColNo);
 TextBlock expectedComment = new TextBlock("// JAVADOC");
 assertEquals(expectedComment, fileContents.cppComments.get(startLineNo));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportSingleLineCommentWithInvalidStartLineNo() {
 FileContents fileContents = createFileContents();
 int startLineNo = -1;
 int startColNo = 1;
 fileContents.reportSingleLineComment("JAVADOC", startLineNo, startColNo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportSingleLineCommentWithInvalidStartColNo() {
 FileContents fileContents = createFileContents();
 int startLineNo = 1;
 int startColNo = -1;
 fileContents.reportSingleLineComment("JAVADOC", startLineNo, startColNo);
 }

 @Test
 public void testGetCppComments() {
 FileContents fileContents = createFileContents();
 int startLineNo = 1;
 int startColNo = 1;
 fileContents.reportCppComment(startLineNo, startColNo);
 ImmutableMap<Integer, TextBlock> expectedMap = ImmutableMap.of(startLineNo, new TextBlock("// JAVADOC"));
 assertEquals(expectedMap, fileContents.getCppComments());
 }

 @Test
 public void testReportCComment() {
 FileContents fileContents = createFileContents();
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 2;
 String[] cComment = new String[] {"/**", " * JAVADOC", " */"};
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 List<TextBlock> entries = fileContents.clangComments.get(startLineNo);
 TextBlock expectedComment = new TextBlock(cComment);
 assertEquals(expectedComment, entries.get(0));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportCCommentWithInvalidStartLineNo() {
 FileContents fileContents = createFileContents();
 int startLineNo = -1;
 int startColNo = 1;
 int endLineNo = 1;
 int endColNo = 1;
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportCCommentWithInvalidEndLineNo() {
 FileContents fileContents = createFileContents();
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = -1;
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 }

 @Test
 public void testExtractCComment() {
 FileContents fileContents = createFileContents();
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 2;
 String[] expectedComment = new String[] {"// JAVADOC", " * JAVADOC", ""};
 String[] actualComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
 assertArrayEquals(expectedComment, actualComment);
 }

 @Test
 public void testGetJavadocBefore() {
 FileContents fileContents = createFileContents();
 int lineNoBefore = 1;
 TextBlock expectedComment = new TextBlock("/**");
 TextBlock actualComment = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expectedComment, actualComment);
 }

 @Test
 public void testGetText() {
 FileContents fileContents = createFileContents();
 FileText expectedText = new FileText(new String[] {"test", "contents"});
 FileText actualText = fileContents.getText();
 assertEquals(expectedText, actualText);
 }

 @Test
 public void testGetLines() {
 FileContents fileContents = createFileContents();
 String[] expectedLines = new String[] {"test", "contents"};
 String[] actualLines = fileContents.getLines();
 assertArrayEquals(expectedLines, actualLines);
 }

 @Test
 public void testGetLine() {
 FileContents fileContents = createFileContents();
 String expectedLine = "test";
 String actualLine = fileContents.getLine(0);
 assertEquals(expectedLine, actualLine);
 }

 @Test
 public void testHasIntersectionWithComment() {
 FileContents fileContents = createFileContents();
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 1;
 int endColNo = 2;
 assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testHasIntersectionWithComment_NoIntersection() {
 FileContents fileContents = createFileContents();
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 1;
 assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testInPackageInfo() {
 FileContents fileContents = new FileContents(new File("package-info.java"));
 assertTrue(fileContents.inPackageInfo());
 }

 @Test
 public void testInPackageInfo_False() {
 FileContents fileContents = new FileContents(new File("testFile.java"));
 assertFalse(fileContents.inPackageInfo());
 }

 private FileContents createFileContents() {
 File file = new File(TEST_FILE_PATH);
 FileText fileText = new FileText(file);
 return new FileContents(fileText);
 }

}