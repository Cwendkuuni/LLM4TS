package com.example.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.grammars.JJTPackageInfoGrammar;
import com.puppycrawl.tools.checkstyle.grammars.JJTSimpleCommentGrammar;

public class FileContentsTest {

 private final String testFile = "TestFile.java";

 @Test
 public void testFileContentsConstructorWithFilenameAndLines() {
 FileContents fileContents = new FileContents(testFile, new String[] {"package", "public", "class", "TestFile", "{", "}", "// this is a comment"});
 assertEquals(testFile, fileContents.getFileName());
 assertNotNull(fileContents.getText());
 }

 @Test
 public void testFileContentsConstructorWithFileText() {
 File file = new File(testFile);
 FileText fileText = new FileText(file);
 FileContents fileContents = new FileContents(fileText);
 assertEquals(testFile, fileContents.getFileName());
 assertNotNull(fileContents.getText());
 }

 @Test
 public void testReportSingleLineComment() {
 FileContents fileContents = new FileContents(testFile);
 fileContents.reportCppComment(1, 1);
 assertEquals(1, fileContents.getCppComments().size());
 }

 @Test
 public void testGetCppComments() {
 FileContents fileContents = new FileContents(testFile);
 fileContents.reportCppComment(1, 1);
 fileContents.reportCppComment(2, 1);
 List<Comment> cppComments = fileContents.getCppComments().asList();
 assertEquals(2, cppComments.size());
 assertEquals(1, cppComments.get(0).getStartLineNo());
 assertEquals(1, cppComments.get(0).getStartColNo());
 assertEquals(1, cppComments.get(1).getStartLineNo());
 assertEquals(1, cppComments.get(1).getStartColNo());
 }

 @Test
 public void testReportCComment() {
 FileContents fileContents = new FileContents(testFile);
 String[] cComment = fileContents.extractCComment(1, 1, 2, 2);
 Comment comment = new Comment(cComment, 1, 2, 2);
 fileContents.reportCComment(1, 1, 2, 2);
 List<Comment> cComments = fileContents.getCComments().get(1);
 assertEquals(1, cComments.size());
 assertEquals(comment, cComments.get(0));
 }

 @Test
 public void testExtractCComment() {
 FileContents fileContents = new FileContents(testFile);
 String[] cComment = fileContents.extractCComment(1, 1, 2, 2);
 assertEquals(3, cComment.length);
 String expectedComment = "/**\n *\n * this is a comment\n */";
 for (int i = 0; i < cComment.length; i++) {
 assertEquals(expectedComment.substring(i, Math.min(expectedComment.length(), i + cComment[i].length())), cComment[i]);
 }
 }

 @Test
 public void testGetJavadocBefore() {
 FileContents fileContents = new FileContents(testFile);
 fileContents.reportCComment(2, 1, 3, 1);
 fileContents.reportCComment(1, 1, 1, 1);
 Comment javadocBefore = fileContents.getJavadocBefore(1);
 assertNull(javadocBefore);
 fileContents.reportJavadocComment(1, 2, "javadoc comment");
 javadocBefore = fileContents.getJavadocBefore(1);
 assertNotNull(javadocBefore);
 assertEquals("*/\n * javadoc comment\n */", javadocBefore.getText());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetJavadocBeforeWithEmptyJavadocComments() {
 FileContents fileContents = new FileContents(testFile);
 assertEquals(Collections.emptyMap(), fileContents.getJavadocComments());
 try {
 fileContents.getJavadocBefore(1);
 } catch (NullPointerException e) {
 fail("Expected UnsupportedOperationException, got NullPointerException");
 }
 }

 @Test
 public void testGetCComments() {
 FileContents fileContents = new FileContents(testFile);
 fileContents.reportCComment(1, 1, 2, 2);
 fileContents.reportCComment(1, 1, 1, 1);
 List<List<Comment>> cComments = fileContents.getCComments().asList();
 assertEquals(1, cComments.size());
 assertEquals(2, cComments.get(0).size());
 Comment firstComment = cComments.get(0).get(0);
 Comment secondComment = cComments.get(0).get(1);
 assertEquals(1, firstComment.getStartLineNo());
 assertEquals(1, firstComment.getStartColNo());
 assertEquals(2, firstComment.getEndLineNo());
 assertEquals(2, firstComment.getEndColNo());
 assertEquals(1, secondComment.getStartLineNo());
 assertEquals(1, secondComment.getStartColNo());
 assertEquals(1, secondComment.getEndLineNo());
 assertEquals(1, secondComment.getEndColNo());
 }

 @Test
 public void testHasIntersectionWithComment() {
 FileContents fileContents = new FileContents(testFile);
 fileContents.reportCComment(1, 1, 2, 2);
 fileContents.reportCComment(2, 1, 2, 2);
 fileContents.reportCppComment(1, 1);
 fileContents.reportCppComment(2, 1);
 assertTrue(fileContents.hasIntersectionWithComment(1, 1, 3, 3));
 assertTrue(fileContents.hasIntersectionWithComment(1, 1, 2, 2));
 assertTrue(fileContents.hasIntersectionWithComment(1, 1, 1, 2));
 assertTrue(fileContents.hasIntersectionWithComment(1, 1, 1, 1));
 assertFalse(fileContents.hasIntersectionWithComment(3, 3, 1, 1));
 }

 @Test
 public void testInPackageInfo() {
 FileContents fileContents = new FileContents("package-info.java");
 assertTrue(fileContents.inPackageInfo());
 FileContents fileContents2 = new FileContents("TestFile.java");
 assertFalse(fileContents2.inPackageInfo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetJavadocBeforeWithNegativeLineNumber() {
 FileContents fileContents = new FileContents(testFile);
 try {
 fileContents.getJavadocBefore(-1);
 } catch (IllegalArgumentException e) {
 assertEquals("Line number cannot be negative", e.getMessage());
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetJavadocBeforeWithNoJavadocBefore() {
 FileContents fileContents = new FileContents(testFile);
 try {
 fileContents.getJavadocBefore(1);
 } catch (NullPointerException e) {
 fail("Expected IndexOutOfBoundsException, got NullPointerException");
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetJavadocBeforeWithNoJavadoc() {
 FileContents fileContents = new FileContents(testFile);
 try {
 fileContents.getJavadocBefore(1);
 } catch (NullPointerException e) {
 fail("Expected IndexOutOfBoundsException, got NullPointerException");
 }
 }
}