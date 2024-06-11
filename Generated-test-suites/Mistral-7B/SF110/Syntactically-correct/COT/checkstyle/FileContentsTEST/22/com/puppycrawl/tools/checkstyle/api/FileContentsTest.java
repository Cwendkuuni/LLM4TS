package com.example.junit;

import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FileContentsTest {

 private static final String TEST_FILE = "TestFile.java";
 private static final String TEST_COMMENT = "This is a test comment.";

 @Test
 public void testFileContentsConstructorWithFileName() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[0]);
 assertEquals(TEST_FILE, fileContents.getFileName());
 }

 @Test
 public void testFileContentsConstructorWithFileText() {
 FileContents fileContents = new FileContents(new FileText(new String[] { TEST_FILE + ":10:This is a file text." }));
 assertEquals(TEST_FILE, fileContents.getFileName());
 }

 @Test
 public void testReportSingleLineComment() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[0]);
 fileContents.reportCppComment(1, 1);
 assertEquals(1, fileContents.cppComments.size());
 }

 @Test
 public void testGetCppComments() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[] { TEST_COMMENT + "\n" });
 List<TextBlock> cppComments = fileContents.getCppComments().asList();
 assertEquals(1, cppComments.size());
 assertEquals(TEST_COMMENT, cppComments.get(0).getText());
 }

 @Test
 public void testReportCComment() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[] { TEST_COMMENT + "\n" });
 fileContents.reportCComment(1, 1, 1, 10);
 List<TextBlock> cComments = fileContents.getCComments().get(1);
 assertEquals(1, cComments.size());
 assertEquals(TEST_COMMENT, cComments.get(0).getText());
 }

 @Test
 public void testExtractCComment() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[] { "/** This is a multiline comment.\n", TEST_COMMENT + "\n" });
 String[] extractedComment = fileContents.extractCComment(1, 1, 2, 10);
 assertEquals(2, extractedComment.length);
 assertEquals(TEST_COMMENT, extractedComment[0]);
 assertEquals("This is a multiline comment.", extractedComment[1]);
 }

 @Test
 public void testGetJavadocBefore() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[] { "/** This is a javadoc comment.\n", TEST_COMMENT + "\n" });
 TextBlock javadocBefore = fileContents.getJavadocBefore(2);
 assertEquals(TEST_COMMENT, javadocBefore.getText());
 }

 @Test
 public void testHasIntersectionWithComment() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[] { TEST_COMMENT + "\n" });
 assertTrue(fileContents.hasIntersectionWithComment(1, 1, 2, 10));
 assertFalse(fileContents.hasIntersectionWithComment(1, 1, 1, 1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetJavadocBeforeNegativeIndex() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[0]);
 fileContents.getJavadocBefore(-1);
 }

 @Test
 public void testLineIsBlank() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[] { " ", "\n" });
 assertTrue(fileContents.lineIsBlank(1));
 assertFalse(fileContents.lineIsBlank(0));
 }

 @Test
 public void testLineIsComment() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[] { "// This is a comment." });
 assertTrue(fileContents.lineIsComment(1));
 assertFalse(fileContents.lineIsComment(0));
 }

 @Test
 public void testHasIntersectionWithCommentEmptyFile() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[0]);
 assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
 }

 @Test
 public void testHasIntersectionWithCommentEmptyComment() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[] { "/**\n" });
 assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
 }

 @Test
 public void testHasIntersectionWithCommentNonOverlappingComments() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[] { "/** This is a javadoc comment.\n", "// This is a single line comment." });
 assertFalse(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
 }

 @Test
 public void testHasIntersectionWithCommentOverlappingComments() {
 FileContents fileContents = new FileContents(TEST_File, new String[] { "/** This is a javadoc comment.\n", "// This is a single line comment.\n" + " * This is a javadoc comment." });
 assertTrue(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
 }

 @Test
 public void testHasIntersectionWithCommentOverlappingCppComment() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[] { "// This is a single line comment.", "/** This is a javadoc comment.\n" });
 assertTrue(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
 }

 @Test
 public void testHasIntersectionWithCommentOverlappingCComment() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[] { "/** This is a javadoc comment.\n", "/** This is a multiline comment.\n" });
 assertTrue(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
 }

 @Test
 public void testInPackageInfo() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[] { "package com.example.junit;" });
 assertTrue(fileContents.inPackageInfo());
 }

 @Test
 public void testInPackageInfoFalse() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[] { "public class TestFile {" });
 assertFalse(fileContents.inPackageInfo());
 }
}