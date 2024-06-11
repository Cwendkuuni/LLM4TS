package com.example.junit.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.grammars.JdtJpaGrammar;

public class FileContentsTest {

 @Mock
 private FileText mockFileText;

 @InjectMocks
 private FileContents fileContents;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testFileContentsConstructorWithFileNameAndLines() {
 String fileName = "testFile.java";
 String[] lines = new String[]{"public class TestClass {", "// This is a comment", "}"};
 FileContents fileContents = new FileContents(fileName, lines);
 assertEquals(fileName, fileContents.getFileName());
 assertEquals(Arrays.asList(lines), fileContents.getLines());
 }

 @Test
 public void testFileContentsConstructorWithFileText() {
 FileText fileText = mockFileText;
 when(fileText.getFile()).thenReturn(new File("testFile.java"));
 when(fileText.toLinesArray()).thenReturn(new String[]{"public class TestClass {", "// This is a comment", "}"});
 FileContents fileContents = new FileContents(fileText);
 assertEquals("testFile.java", fileContents.getFileName());
 assertEquals(Arrays.asList(new String[]{"public class TestClass {", "// This is a comment", "}"}), fileContents.getLines());
 }

 @Test
 public void testReportSingleLineComment() {
 String fileContent = "// This is a single line comment";
 fileContents = new FileContents("testFile.java", fileContent.split("\n"));
 int startLineNo = 1;
 int startColNo = 1;
 fileContents.reportSingleLineComment("", startLineNo, startColNo);
 assertEquals(startLineNo, fileContents.cppComments.size());
 TextBlock comment = fileContents.cppComments.get(startLineNo);
 assertEquals("// This is a single line comment", comment.getText());
 }

 @Test
 public void testGetCppComments() {
 String fileContent = "// This is a single line comment\n" +
 "/* This is a multi-line comment\n" +
 " * This is a javadoc comment\n" +
 " */\n" +
 "public class TestClass {}";
 fileContents = new FileContents("testFile.java", fileContent.split("\n"));
 ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
 assertEquals(3, cppComments.size());
 TextBlock singleLineComment = cppComments.get(1);
 assertEquals("// This is a single line comment", singleLineComment.getText());
 TextBlock multiLineComment = cppComments.get(2);
 assertEquals("/* This is a multi-line comment\n" +
 " * This is a javadoc comment\n" +
 " */", multiLineComment.getText());
 }

 @Test
 public void testReportBlockComment() {
 String fileContent = "/* This is a multi-line comment\n" +
 " * This is a javadoc comment\n" +
 " */\n" +
 "public class TestClass {}\n" +
 "// This is a single line comment";
 fileContents = new FileContents("testFile.java", fileContent.split("\n"));
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 3;
 int endColNo = 1;
 fileContents.reportBlockComment("", startLineNo, startColNo, endLineNo, endColNo);
 assertEquals(3, fileContents.clangComments.size());
 List<TextBlock> clangComments = fileContents.clangComments.get(startLineNo);
 assertEquals(1, clangComments.size());
 TextBlock comment = clangComments.get(0);
 assertEquals("This is a multi-line comment\n" +
 " * This is a javadoc comment", comment.getText());
 }

 @Test(expected = IllegalStateException.class)
 public void testReportBlockCommentWithInvalidArguments() {
 String fileContent = "/* This is a multi-line comment\n" +
 " * This is a javadoc comment\n" +
 " */\n" +
 "public class TestClass {}\n" +
 "// This is a single line comment";
 fileContents = new FileContents("testFile.java", fileContent.split("\n"));
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 3;
 int endColNo = 3;
 fileContents.reportBlockComment("", startLineNo, startColNo, endLineNo, endColNo);
 }

 @Test
 public void testExtractCComment() {
 String fileContent = "/*\n" +
 " * This is a javadoc comment\n" +
 "*\n" +
 "* This is a multi-line comment\n" +
 " */\n" +
 "public class TestClass {\n" +
 " // This is a single line comment\n" +
 "}";
 int startLineNo = 1;
 int startColNo = 3;
 int endLineNo = 4;
 int endColNo = 6;
 String[] expected = {"/*", " * This is a javadoc comment", "*",
 " * This is a multi-line comment", " */"};
 String[] actual = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testExtractCCommentWithSingleLineComment() {
 String fileContent = "// This is a single line comment\n" +
 "/*\n" +
 " * This is a javadoc comment\n" +
 " */\n" +
 "public class TestClass {\n" +
 " // This is another single line comment\n" +
 "}";
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 33;
 String[] expected = {"// This is a single line comment", ""};
 String[] actual = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testExtractCCommentWithMissingEndTag() {
 String fileContent = "/*\n" +
 " * This is a javadoc comment\n" +
 " */\n" +
 "public class TestClass {\n" +
 " // This is a single line comment\n" +
 "}";
 int startLineNo = 1;
 int startColNo = 3;
 int endLineNo = 2;
 int endColNo = 33;
 String[] expected = {"/*", " * This is a javadoc comment"};
 String[] actual = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testGetJavadocBefore() {
 String fileContent = "/**\n" +
 " * This is a javadoc comment before line 3\n" +
 " */\n" +
 "public class TestClass {\n" +
 " /**\n" +
 " * This is a javadoc comment before line 4\n" +
 " */\n" +
 " public void testMethod() {\n" +
 " }";
 FileContents fileContents = new FileContents("testFile.java", fileContent.split("\n"));
 int lineNoBefore = 2;
 TextBlock expected = new TextBlock(" * This is a javadoc comment before line 3\n");
 TextBlock actual = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expected.getText(), actual.getText());
 }

 @Test
 public void testGetJavadocBeforeWithNoJavadoc() {
 String fileContent = "public class TestClass {\n" +
 " public void testMethod() {\n" +
 " }\n";
 FileContents fileContents = new FileContents("testFile.java", fileContent.split("\n"));
 int lineNoBefore = 1;
 assertNull(fileContents.getJavadocBefore(lineNoBefore));
 }

 @Test
 public void testGetJavadocBeforeWithBlankLine() {
 String fileContent = "/**\n" +
 " * This is a javadoc comment before line 3\n" +
 "\n" +
 "public class TestClass {\n" +
 " /**\n" +
 " * This is a javadoc comment before line 4\n" +
 " */\n" +
 " public void testMethod() {\n" +
 " }\n";
 FileContents fileContents = new FileContents("testFile.java", fileContent.split("\n"));
 int lineNoBefore = 3;
 TextBlock expected = new TextBlock(" * This is a javadoc comment before line 3\n");
 TextBlock actual = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expected.getText(), actual.getText());
 }

 @Test
 public void testGetJavadocBeforeWithMultilineComment() {
 String fileContent = "/**\n" +
 " * This is a javadoc comment before line 3\n" +
 " * This is a multiline comment before line 3\n" +
 " */\n" +
 "public class TestClass {\n" +
 " /**\n" +
 " * This is a javadoc comment before line 4\n" +
 " */\n" +
 " public void testMethod() {\n" +
 " }\n";
 FileContents fileContents = new FileContents("testFile.java", fileContent.split("\n"));
 int lineNoBefore = 3;
 assertNull(fileContents.getJavadocBefore(lineNoBefore));
 }

 @Test
 public void testLineIsBlank() {
 String fileContent = "public class TestClass {\n" +
 "}\n" +
 "// This is a single line comment";
 FileContents fileContents = new FileContents("testFile.java", fileContent.split("\n"));
 int lineNo = 1;
 assertTrue(fileContents.lineIsBlank(lineNo));
 int lineNo2 = 2;
 assertFalse(fileContents.lineIsBlank(lineNo2));
 }

 @Test
 public void testLineIsComment() {
 String fileContent = "// This is a single line comment\n" +
 "public class TestClass {\n" +
 "}\n" +
 "public void testMethod() {\n" +
 "}";
 FileContents fileContents = new FileContents("testFile.java", fileContent.split("\n"));
 int lineNo = 1;
 assertTrue(fileContents.lineIsComment(lineNo));
 int lineNo2 = 3;
 assertFalse(fileContents.lineIsComment(lineNo2));
 }

 @Test
 public void testHasIntersectionWithComment() {
 String fileContent = "public class TestClass {\n" +
 " // This is a single line comment before line 3\n" +
 " /**\n" +
 " * This is a javadoc comment before line 3\n" +
 " */\n" +
 " int x = 1;\n" +
 "}\n" +
 "// This is a single line comment after line 3";
 FileContents fileContents = new FileContents("testFile.java", fileContent.split("\n"));
 int startLineNo = 2;
 int startColNo = 1;
 int endLineNo = 4;
 int endColNo = 1;
 assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 int startLineNo2 = 2;
 int startColNo2 = 1;
 int endLineNo2 = 3;
 int endColNo2 = 2;
 assertFalse(fileContents.hasIntersectionWithComment(startLineNo2, startColNo2, endLineNo2, endColNo2));
 int startLineNo3 = 4;
 int startColNo3 = 1;
 int endLineNo3 = 4;
 int endColNo3 = 2;
 assertFalse(fileContents.hasIntersectionWithComment(startLineNo3, startColNo3, endLineNo3, endColNo3));
 }

 @Test
 public void testInPackageInfo() {
 FileContents fileContents = new FileContents("package-info.java", new String[]{"Name: TestClass"});
 assertTrue(fileContents.inPackageInfo());
 FileContents fileContents2 = new FileContents("TestClass.java", new String[]{"public class TestClass"});
 assertFalse(fileContents2.inPackageInfo());
 }

}