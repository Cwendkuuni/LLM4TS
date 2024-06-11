package com.example.test;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.collect.Maps;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FileContentsTest {

 private FileContents fileContents;
 private final String fileName = "testFile.java";
 private final String[] lines = {"package test;", "public class TestClass {", " // This is a comment", " int main(String[] args) {", "}"};

 @Before
 public void setUp() {
 fileContents = new FileContents(fileName, lines);
 }

 @Test
 public void testFileContentsConstructorWithFile() throws IOException {
 File file = new File("testFile.java");
 FileText text = new FileText(file);
 fileContents = new FileContents(text);
 assertEquals(fileName, fileContents.getFileName());
 }

 @Test
 public void testGetCppComments() {
 List<TextBlock> expectedCppComments = Arrays.asList(
 new TextBlock(" This is a comment")
 );
 ImmutableMap<Integer, TextBlock> actualCppComments = fileContents.getCppComments();
 assertEquals(expectedCppComments.size(), actualCppComments.size());
 for (Map.Entry<Integer, TextBlock> entry : actualCppComments.entrySet()) {
 int lineNumber = entry.getKey();
 TextBlock comment = entry.getValue();
 assertEquals(expectedCppComments.get(expectedCppComments.size() - 1), comment);
 }
 }

 @Test
 public void testGetCComments() {
 List<List<TextBlock>> expectedCComments = Arrays.asList(
 Collections.singletonList(
 Collections.singletonList(new TextBlock(" This is a comment")))
 );
 ImmutableMap<Integer, List<TextBlock>> actualCComments = fileContents.getCComments();
 assertEquals(expectedCComments.size(), actualCComments.size());
 for (Map.Entry<Integer, List<TextBlock>> entry : actualCComments.entrySet()) {
 int lineNumber = entry.getKey();
 List<TextBlock> comments = entry.getValue();
 assertEquals(expectedCComments.get(expectedCComments.size() - 1).get(0), comments.get(0));
 }
 }

 @Test
 public void testGetJavadocBefore() {
 TextBlock expectedJavadoc = new TextBlock("package test;");
 TextBlock actualJavadoc = fileContents.getJavadocBefore(1);
 assertEquals(expectedJavadoc, actualJavadoc);
 }

 @Test
 public void testHasIntersectionWithComment() {
 int startLineNo = 3;
 int startColNo = 5;
 int endLineNo = 4;
 int endColNo = 10;
 assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testHasIntersectionWithComment_NoIntersection() {
 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 1;
 assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testInPackageInfo() {
 assertFalse(fileContents.inPackageInfo());
 }
}