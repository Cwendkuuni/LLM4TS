package com.example.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.*;

public class FileContentsTest {

 private static final String TEST_FILE = "TestFile.java";

 @Test
 public void testFileContentsConstructorWithFilenameAndLines() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[]{"public class TestClass", "{"});
 assertEquals(TEST_FILE, fileContents.getFileName());
 assertNotNull(fileContents.getText());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFileContentsConstructorWithInvalidFilename() {
 FileContents fileContents = new FileContents(null, new String[]{"public class TestClass", "{"});
 fail("Expected IllegalArgumentException, but got none");
 }

 @Test
 public void testFileContentsConstructorWithFileText() {
 FileContents fileContents = new FileContents(new FileText(new File("TestFile.java"), Arrays.asList(new String[]{"public class TestClass", "{"})));
 assertEquals(TEST_FILE, fileContents.getFileName());
 assertNotNull(fileContents.getText());
 }

 @Test(expected = NullPointerException.class)
 public void testFileContentsConstructorWithNullFileText() {
 FileContents fileContents = new FileContents(null);
 fail("Expected NullPointerException, but got none");
 }

 @Test
 public void testReportSingleLineComment() {
 FileContents fileContents = new FileTextTestBuilder()
         .addLine("// This is a single line comment")
         .createFileContents();
 int startLineNo = 1;
 int startColNo = 3;
 fileContents.reportSingleLineComment("", startLineNo, startColNo);
 TextBlock comment = fileContents.cppComments.get(startLineNo);
 assertEquals("// This is a single line comment", comment.getText());
 }

 @Test
 public void testReportBlockComment() {
 FileContents fileContents = new FileTextTestBuilder()
         .addLine("/**")
         .addLine(" * This is a block comment")
         .addLine(" */")
         .createFileContents();
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 1;
 fileContents.reportBlockComment("", startLineNo, startColNo, endLineNo, endColNo);
 List<TextBlock> clangComments = fileContents.clangComments.get(startLineNo);
 assertEquals(1, clangComments.size());
 TextBlock comment = clangComments.get(0);
 assertEquals("* This is a block comment", comment.getText());
 }

 @Test
 public void testGetCppComments() {
 FileContents fileContents = new FileTextTestBuilder()
         .addLine("// This is a single line C comment")
         .addLine("/**")
         .addLine(" * This is a Javadoc comment")
         .addLine(" */")
         .addLine("// This is another single line C comment")
         .createFileContents();
 ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
 assertEquals(3, cppComments.size());
 }

 @Test
 public void testGetCComments() {
 FileContents fileContents = new FileTextTestBuilder()
         .addLine("/**")
         .addLine(" * This is a Javadoc comment")
         .addLine(" */")
         .addLine("/*")
         .addLine(" * This is a C comment")
         .addLine(" */")
         .addLine("/**")
         .addLine(" * This is another Javadoc comment")
         .addLine(" */")
         .createFileContents();
 ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
 assertEquals(3, cComments.size());
 }

 @Test
 public void testExtractCComment() {
 FileContents fileContents = new FileTextTestBuilder()
         .addLine("/*")
         .addLine(" * This is a multi-line C comment")
         .addLine(" * with multiple lines")
         .addLine(" */")
         .addLine("public void testMethod()")
         .createFileContents();
 String[] extractedComment = fileContents.extractCComment(2, 0, 3, 0);
 assertEquals(3, extractedComment.length);
 assertEquals(" * This is a multi-line C comment", extractedComment[0]);
 assertEquals(" * with multiple lines", extractedComment[1]);
 assertEquals(" */", extractedComment[2]);
 }

 @Test
 public void testGetJavadocBefore() {
 FileContents fileContents = new FileTextTestBuilder()
         .addLine("/**")
         .addLine(" * This is a Javadoc comment before the method")
         .addLine(" */")
         .addLine("public void testMethod()")
         .addLine("{")
         .createFileContents();
 TextBlock javadocBefore = fileContents.getJavadocBefore(3);
 assertEquals(" * This is a Javadoc comment before the method", javadocBefore.getText());
 }

 @Test
 public void testLineIsBlank() {
 FileContents fileContents = new FileTextTestBuilder()
         .addLine("")
         .createFileContents();
 assertTrue(fileContents.lineIsBlank(0));
 }

 @Test
 public void testLineIsComment() {
 FileContents fileContents = new FileTextTestBuilder()
         .addLine("// This is a single line comment")
         .createFileContents();
 assertTrue(fileContents.lineIsComment(0));
 }

 @Test
 public void testHasIntersectionWithComment() {
 FileContents fileContents = new FileTextTestBuilder()
         .addLine("// This is a single line C comment")
         .addLine("/**")
         .addLine(" * This is a Javadoc comment")
         .addLine(" * intersects with this C comment")
         .addLine(" */")
         .createFileContents();
 assertTrue(fileContents.hasIntersectionWithComment(2, 0, 3, 0));
 }

 @Test
 public void testInPackageInfo() {
 FileContents fileContents = new FileTextTestBuilder()
         .addLine("/**")
         .addLine(" * package-info.java file")
         .addLine(" */")
         .createFileContents();
 assertTrue(fileContents.inPackageInfo());
 }

 @Test
 public void testHasIntersectionWithComment_noIntersection() {
 FileContents fileContents = new FileTextTestBuilder()
         .addLine("// This is a single line C comment")
         .addLine("/**")
         .addLine(" * This is a Javadoc comment")
         .addLine(" */")
         .createFileContents();
 assertFalse(fileContents.hasIntersectionWithComment(2, 0, 3, 0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetJavadocBefore_invalidLineNo() {
 FileContents fileContents = new FileTextTestBuilder()
         .addLine("/**")
         .addLine(" * This is a Javadoc comment")
         .addLine(" */")
         .addLine("public void testMethod()")
         .createFileContents();
 try {
 fileContents.getJavadocBefore(-1);
 fail("Expected IndexOutOfBoundsException, but got none");
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }
}

class FileTextTestBuilder {
 private FileText text;

 public FileTextTestBuilder() {
 text = new FileText(Collections.emptyList());
 }

 public FileTextTestBuilder addLine(String line) {
 text = text.addLine(line);
 return this;
 }

 public FileContents createFileContents() {
 return new FileContents(text);
 }
}