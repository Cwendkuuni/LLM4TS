package com.example.test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import com.puppycrawl.tools.checkstyle.api.*;
import java.util.*;
import java.io.*;
import com.google.common.collect.*;

public class FileContentsTest {

 private FileContents fileContents;

 @Before
 public void setUp() throws IOException {
 String fileName = "FileUnderTest.java";
 String[] lines = new String[]{"package com.test;", "import java.util.Arrays;", "public class FileUnderTest {", " // This is a single line comment", " int main(String[] args) {", " // Another single line comment", " int a = 5;", " int b = 10;", " int sum = a + b;", " System.out.println(\"Sum: \" + sum);", "}"};
 fileContents = new FileContents(fileName, lines);
 }

 @Test
 public void testFileContentsWithLines() {
 FileText expectedText = FileText.fromLines(new File("src/test/resources/FileUnderTest.java"), Arrays.asList(lines));
 FileText actualText = fileContents.getText();
 assertEquals(expectedText, actualText);
 }

 @Test
 public void testGetFileName() {
 assertEquals("FileUnderTest.java", fileContents.getFileName());
 }

 @Test
 public void testLineIsBlank() {
 assertTrue(fileContents.lineIsBlank(1));
 assertTrue(fileContents.lineIsBlank(5));
 assertFalse(fileContents.lineIsBlank(2));
 }

 @Test
 public void testLineIsComment() {
 assertTrue(fileContents.lineIsComment(1));
 assertTrue(fileContents.lineIsComment(3));
 assertFalse(fileContents.lineIsComment(2));
 }

 @Test
 public void testHasIntersectionWithComment() {
 int startLineNo = 1;
 int startColNo = 0;
 int endLineNo = 2;
 int endColNo = 10;

 assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testHasIntersectionWithComment_NoIntersection() {
 int startLineNo = 1;
 int startColNo = 15;
 int endLineNo = 2;
 int endColNo = 20;

 assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testReportSingleLineComment() {
 int startLineNo = 1;
 int startColNo = 0;
 fileContents.reportSingleLineComment("test", startLineNo, startColNo);
 TextBlock expectedComment = new TextBlock("// This is a single line comment");
 TextBlock actualComment = fileContents.cppComments.get(startLineNo);
 assertEquals(expectedComment, actualComment);
 }

 @Test
 public void testReportBlockComment() {
 int startLineNo = 2;
 int startColNo = 0;
 int endLineNo = 3;
 int endColNo = 10;
 fileContents.reportBlockComment("test", startLineNo, startColNo, endLineNo, endColNo);
 List<TextBlock> expectedComments = Lists.newArrayList(new TextBlock("// Another single line comment"));
 List<TextBlock> actualComments = fileContents.clangComments.get(startLineNo);
 assertEquals(expectedComments, actualComments);
 }

 @Test
 public void testGetCppComments() {
 ImmutableMap<Integer, TextBlock> expectedCppComments = ImmutableMap.of(1, new TextBlock("// This is a single line comment"));
 ImmutableMap<Integer, TextBlock> actualCppComments = fileContents.getCppComments();
 assertEquals(expectedCppComments, actualCppComments);
 }

 @Test
 public void testGetCComments() {
 ImmutableMap<Integer, List<TextBlock>> expectedCComments = ImmutableMap.of(2, Lists.newArrayList(new TextBlock("// Another single line comment")));
 ImmutableMap<Integer, List<TextBlock>> actualCComments = fileContents.getCComments();
 assertEquals(expectedCComments, actualCComments);
 }

 @Test
 public void testReportCppComment() {
 int startLineNo = 1;
 int startColNo = 0;
 String[] cComment = {"This is a C comment"};
 fileContents.reportCppComment(startLineNo, startColNo);
 TextBlock expectedComment = new TextBlock(cComment);
 TextBlock actualComment = fileContents.cppComments.get(startLineNo);
 assertEquals(expectedComment, actualComment);
 }

 @Test
 public void testReportCComment() {
 int startLineNo = 2;
 int startColNo = 0;
 int endLineNo = 3;
 int endColNo = 10;
 String[] cComment = {"/**", " * This is a Javadoc comment", " */"};
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 List<TextBlock> expectedComments = Lists.newArrayList(new TextBlock(cComment));
 List<TextBlock> actualComments = fileContents.clangComments.get(startLineNo);
 assertEquals(expectedComments, actualComments);
 }

 @Test
 public void testExtractCComment() {
 int startLineNo = 2;
 int startColNo = 0;
 int endLineNo = 3;
 int endColNo = 10;
 String[] expectedCComment = {"/**", " * This is a Javadoc comment", " */"};
 String[] actualCComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
 assertArrayEquals(expectedCComment, actualCComment);
 }

 @Test
 public void testGetJavadocBefore() {
 int lineNoBefore = 0;
 TextBlock expectedComment = new TextBlock("package com.test;");
 TextBlock actualComment = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expectedComment, actualComment);
 }
}