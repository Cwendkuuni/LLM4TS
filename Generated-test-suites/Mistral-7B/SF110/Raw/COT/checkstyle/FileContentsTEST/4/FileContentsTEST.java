package com.example.test;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileContentsTest {

 private static final String TEST_FILE = "TestFile.java";
 private static final String TEST_MULTILINE_COMMENT = "/**\n * Test Multiline Comment\n */";
 private static final String TEST_SINGLELINE_COMMMENT = "// Test Singleline Comment";

 @Test
 public void testFileContentsConstructorWithFileNameAndLines() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[]{"public class TestFile {", "  ", "}"});
 assertEquals(TEST_FILE, fileContents.getFileName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFileContentsConstructorWithNullFileName() {
 FileContents fileContents = new FileContents(null, new String[]{"public class TestFile {", "  ", "}"});
 }

 @Test
 public void testFileContentsConstructorWithFileText() {
 File file = new File(TEST_FILE);
 FileText fileText = new FileText(file);
 FileContents fileContents = new FileContents(fileText);
 assertEquals(TEST_FILE, fileContents.getFileName());
 }

 @Test
 public void testGetCppComments() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[]{"// Test Cpp Comment"});
 List<TextBlock> cppComments = fileContents.getCppComments().asList();
 assertEquals(1, cppComments.size());
 assertEquals("Test Cpp Comment", cppComments.get(0).getText());
 }

 @Test
 public void testGetCComments() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[]{"/**", " * Test Clang Comment\n */"});
 List<List<TextBlock>> cComments = fileContents.getCComments().asList();
 assertEquals(1, cComments.size());
 List<TextBlock> clangComment = cComments.get(0);
 assertEquals(1, clangComment.size());
 assertEquals("Test Clang Comment", clangComment.get(0).getText());
 }

 @Test
 public void testReportCppComment() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[]{"// Test Cpp Comment"});
 fileContents.reportCppComment(1, 1);
 List<TextBlock> cppComments = fileContents.getCppComments().asList();
 assertEquals(1, cppComments.size());
 assertEquals("Test Cpp Comment", cppComments.get(0).getText());
 }

 @Test
 public void testReportCCommentWithSingleLineCommentBefore() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[]{"// Test Singleline Comment", "/**", " * Test Clang Comment\n */"});
 List<List<TextBlock>> cComments = fileContents.getCComments().asList();
 List<TextBlock> clangComment = cComments.get(0);
 assertEquals(1, clangComment.size());
 assertEquals("Test Clang Comment", clangComment.get(0).getText());
 List<TextBlock> javadocComment = fileContents.javadocComments.values().toList().get(0);
 assertEquals("Test Singleline Comment", javadocComment.getText());
 }

 @Test
 public void testReportCCommentWithSingleLineCommentAfter() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[]{"/**", " * Test Clang Comment\n */", "// Test Singleline Comment"});
 List<List<TextBlock>> cComments = fileContents.getCComments().asList();
 List<TextBlock> clangComment = cComments.get(0);
 assertEquals(1, clangComment.size());
 assertEquals("Test Clang Comment", clangComment.get(0).getText());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetJavadocBeforeWithNegativeLineNumber() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[]{"/**", " * Test Javadoc\n */"});
 fileContents.getJavadocBefore(-1);
 }

 @Test
 public void testGetJavadocBefore() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[]{"// Test Singleline Comment", "/**", " * Test Javadoc\n */", "public static void main(String[] args) {", "  ", "}"});
 TextBlock javadoc = fileContents.getJavadocBefore(3);
 assertEquals("Test Javadoc", javadoc.getText());
 }

 @Test
 public void testLineIsBlank() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[]{"public class TestFile {", "  ", "}"});
 assertTrue(fileContents.lineIsBlank(1));
 assertTrue(fileContents.lineIsBlank(3));
 }

 @Test
 public void testLineIsComment() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[]{"// Test Singleline Comment", "/**", " * Test Javadoc\n */"});
 assertTrue(fileContents.lineIsComment(1));
 }

 @Test
 public void testHasIntersectionWithComment() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[]{"// Test Singleline Comment", "/**", " * Test Clang Comment\n */", "public class TestFile {", "  ", "}"});
 assertTrue(fileContents.hasIntersectionWithComment(2, 0, 3, 0));
 assertTrue(fileContents.hasIntersectionWithComment(2, 0, 2, 0));
 assertTrue(fileContents.hasIntersectionWithComment(2, 0, 4, 0));
 assertFalse(fileContents.hasIntersectionWithComment(2, 0, 2, 1));
 }

 @Test
 public void testInPackageInfo() {
 FileContents fileContents = new FileContents(TEST_FILE + ".java");
 assertTrue(fileContents.inPackageInfo());
 FileContents fileContents2 = new FileContents(TEST_FILE + "_Test.java");
 assertFalse(fileContents2.inPackageInfo());
 }
}