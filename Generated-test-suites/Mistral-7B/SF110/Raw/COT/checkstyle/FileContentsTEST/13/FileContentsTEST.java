package com.example.test;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.*;

public class FileContentsTest {

 private FileContents fileContents;

 @Before
 public void setUp() {
 String filename = "TestFile.java";
 String[] lines = {"public class TestClass", "{\\n", " // This is a single line comment", " int a = 1; // Another single line comment", "}", ""};
 fileContents = new FileContents(filename, lines);
 }

 @Test
 public void testConstructorWithFileAndLines() {
 assertEquals("TestFile.java", fileContents.getFileName());
 assertEquals(3, fileContents.getText().size());
 }

 @Test
 public void testConstructorWithFileText() {
 File file = new File("TestFile.java");
 FileText fileText = new FileText(file);
 fileContents = new FileContents(fileText);
 assertEquals("TestFile.java", fileContents.getFileName());
 assertEquals(3, fileContents.getText().size());
 }

 @Test
 public void testReportSingleLineComment() {
 int startLineNo = 1;
 int startColNo = 2;
 fileContents.reportCppComment(startLineNo, startColNo);
 TextBlock expectedComment = new TextBlock(" // This is a single line comment", startColNo, startLineNo);
 TextBlock actualComment = fileContents.cppComments.get(startLineNo);
 assertEquals(expectedComment.getText(), actualComment.getText());
 }

 @Test
 public void testGetCppComments() {
 ImmutableMap<Integer, TextBlock> expectedComments = FileContents.newBuilder()
 .put(1, new TextBlock(" // This is a single line comment", 2, 1))
 .put(2, new TextBlock(" int a = 1; // Another single line comment", 0, 2))
 .build();
 ImmutableMap<Integer, TextBlock> actualComments = fileContents.getCppComments();
 assertEquals(expectedComments, actualComments);
 }

 @Test
 public void testExtractCComment() {
 int startLineNo = 1;
 int startColNo = 2;
 int endLineNo = 2;
 int endColNo = 10;
 String[] expectedComment = {" // This is a single line comment", " int a = 1;"};
 String[] actualComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
 assertArrayEquals(expectedComment, actualComment);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetJavadocBeforeWithOutOfBoundsIndex() {
 int lineNoBefore = -1;
 TextBlock expectedComment = fileContents.getJavadocBefore(lineNoBefore);
 assertNull(expectedComment);
 }

 @Test
 public void testGetJavadocBefore() {
 int lineNoBefore = 0;
 TextBlock expectedComment = new TextBlock("public class TestClass", 0, 0);
 TextBlock actualComment = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expectedComment.getText(), actualComment.getText());
 }

 @Test
 public void testHasIntersectionWithCommentWithCComment() {
 int startLineNo = 1;
 int startColNo = 2;
 int endLineNo = 2;
 int endColNo = 10;
 assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testHasIntersectionWithCommentWithCppComment() {
 int startLineNo = 1;
 int startColNo = 2;
 int endLineNo = 2;
 int endColNo = 20;
 assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testHasIntersectionWithCommentWithNoIntersection() {
 int startLineNo = 3;
 int startColNo = 2;
 int endLineNo = 3;
 int endColNo = 5;
 assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testInPackageInfo() {
 String fileName = "package-info.java";
 FileContents fileContents = new FileContents(fileName, new String[0]);
 assertTrue(fileContents.inPackageInfo());
 }

 @Test
 public void testInPackageInfoWithNonPackageFile() {
 String fileName = "TestFile.java";
 FileContents fileContents = new FileContents(fileName, new String[0]);
 assertFalse(fileContents.inPackageInfo());
 }

}