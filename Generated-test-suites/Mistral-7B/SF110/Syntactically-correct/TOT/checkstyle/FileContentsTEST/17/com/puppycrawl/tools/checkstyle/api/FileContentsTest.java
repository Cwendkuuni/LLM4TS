package com.example.junit4;

import static org.junit.Assert.*;
import static com.puppycrawl.tools.checkstyle.api.FileContents.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Test;

public class FileContentsTest {

 @Test
 public void testFileContentsCreationFromFile() {
 String filename = "TestFile.java";
 String[] lines = new String[]{"public class TestFile {", "/**", " * This is a Javadoc comment", " */", "}"};
 FileContents fileContents = new FileContents(filename, lines);
 assertEquals(filename, fileContents.getFileName());
 assertEquals(lines.length, fileContents.getLines().length);
 }

 @Test
 public void testFileContentsCreationFromFileText() {
 String filename = "TestFile.java";
 String fileContent = "public class TestFile {\n" +
 "/**\n" +
 " * This is a Javadoc comment\n" +
 " */\n}";
 FileText fileText = new FileText(new StringReader(fileContent));
 FileContents fileContents = new FileContents(fileText);
 assertEquals(filename, fileContents.getFileName());
 }

 @Test
 public void testReportSingleLineComment() {
 FileContents fileContents = createFileContents("TestFile.java", "/** This is a Javadoc comment */");
 int startLineNo = 1;
 int startColNo = 1;
 fileContents.reportSingleLineComment("Javadoc", startLineNo, startColNo);
 TextBlock expectedComment = new TextBlock(" This is a Javadoc comment ");
 TextBlock actualComment = fileContents.cppComments.get(startLineNo);
 assertEquals(expectedComment.getText(), actualComment.getText());
 }

 @Test
 public void testReportBlockComment() {
 FileContents fileContents = createFileContents("TestFile.java", "/** This is a Javadoc comment */\n/* This is a C comment */");
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 10;
 fileContents.reportBlockComment("Javadoc", startLineNo, startColNo, endLineNo, endColNo);
 List<TextBlock> expectedComments = Arrays.asList(
 new TextBlock(" This is a Javadoc comment "),
 new TextBlock(" */\n/* This is a C comment */")
 );
 List<TextBlock> actualComments = fileContents.clangComments.get(startLineNo);
 assertEquals(expectedComments, actualComments);
 }

 @Test
 public void testExtractCComment() {
 FileContents fileContents = createFileContents("TestFile.java", "/** This is a Javadoc comment\n*/\n/* This is a C comment */\n");
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 10;
 String[] expectedComment = new String[]{"/** This is a Javadoc comment\n*/", "/* This is a C comment */"};
 String[] actualComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
 assertArrayEquals(expectedComment, actualComment);
 }

 @Test
 public void testGetJavadocBefore() {
 FileContents fileContents = createFileContents("TestFile.java", "/** This is a Javadoc comment */\npublic class TestFile {}\n");
 int lineNoBefore = 1;
 TextBlock expectedComment = new TextBlock(" This is a Javadoc comment ");
 TextBlock actualComment = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expectedComment.getText(), actualComment.getText());
 }

 @Test
 public void testLineIsBlank() {
 FileContents fileContents = createFileContents("TestFile.java", "");
 int lineNo = 1;
 boolean expectedResult = true;
 boolean actualResult = fileContents.lineIsBlank(lineNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testLineIsComment() {
 FileContents fileContents = createFileContents("TestFile.java", "// This is a single line comment");
 int lineNo = 1;
 boolean expectedResult = true;
 boolean actualResult = fileContents.lineIsComment(lineNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasIntersectionWithComment() {
 FileContents fileContents = createFileContents("TestFile.java", "/** This is a Javadoc comment\n*/\n// This is a single line comment");
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 10;
 boolean expectedResult = true;
 boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testInPackageInfo() {
 FileContents fileContents = createFileContents("package-info.java", "Name: Test\nVersion: 1.0.0\n");
 boolean expectedResult = true;
 boolean actualResult = fileContents.inPackageInfo();
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testHasIntersectionWithNoComment() {
 FileContents fileContents = createFileContents("TestFile.java", "public class TestFile {}\n");
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 10;
 boolean expectedResult = false;
 boolean actualResult = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertEquals(expectedResult, actualResult);
 }

 private FileContents createFileContents(String filename, String fileContent) {
 StringReader stringReader = new StringReader(fileContent);
 FileText fileText = new FileText(stringReader);
 FileContents fileContents = new FileContents(fileText);
 return fileContents;
 }

}