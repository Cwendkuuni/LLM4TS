package com.example.junit.test;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;

public class FileContentsTest {

 private FileContents fileContents;

 @Test
 public void testFileContentsConstructionWithFileNameAndLines() {
     String fileName = "TestFile.java";
     String[] lines = new String[]{"public class TestClass {", " // This is a comment", "}"};
     fileContents = new FileContents(fileName, lines);
     assertEquals(fileName, fileContents.getFileName());
     assertEquals(lines.length, fileContents.getLines().length);
 }

 @Test
 public void testFileContentsConstructionWithFileText() {
     String fileName = "TestFile.java";
     String fileContent = "public class TestClass {\n // This is a comment\n}";
     FileText fileText = new FileText(fileContent);
     fileContents = new FileContents(fileText);
     assertEquals(fileName, fileContents.getFileName());
 }

 @Test
 public void testReportSingleLineComment() {
     fileContents = new FileContents("TestFile.java", new String[]{"// This is a single line comment"});
     fileContents.reportCppComment(1, 1);
     TextBlock comment = fileContents.cppComments.get(1);
     assertEquals("// This is a single line comment", comment.getText());
 }

 @Test
 public void testGetCppComments() {
     fileContents = new FileContents("TestFile.java", new String[]{"// This is a single line comment", "/* This is a block comment */"});
     List<TextBlock> expected = Arrays.asList(
         new TextBlock("// This is a single line comment", 1, 1),
         new TextBlock("/* This is a block comment */", 1, 2, 1, 10)
     );
     ImmutableMap<Integer, List<TextBlock>> actual = fileContents.getCComments();
     assertEquals(expected.size(), actual.size());
     for (Map.Entry<Integer, List<TextBlock>> entry : actual.entrySet()) {
         List<TextBlock> value = entry.getValue();
         assertEquals(expected.get(entry.getKey()).size(), value.size());
         for (int i = 0; i < value.size(); i++) {
             TextBlock expectedValue = expected.get(entry.getKey()).get(i);
             TextBlock actualValue = value.get(i);
             assertEquals(expectedValue.getText(), actualValue.getText());
             assertEquals(expectedValue.getStartLineNo(), actualValue.getStartLineNo());
             assertEquals(expectedValue.getStartColNo(), actualValue.getStartColNo());
             assertEquals(expectedValue.getEndLineNo(), actualValue.getEndLineNo());
             assertEquals(expectedValue.getEndColNo(), actualValue.getEndColNo());
         }
     }
 }

 @Test
 public void testReportBlockComment() {
     fileContents = new FileContents("TestFile.java", new String[]{"/* This is a block comment */"});
     List<TextBlock> expected = Arrays.asList(
         new TextBlock("/* This is a block comment */", 1, 1, 1, 20)
     );
     fileContents.reportCComment(1, 1, 1, 20);
     ImmutableMap<Integer, List<TextBlock>> actual = fileContents.getCComments();
     List<TextBlock> value = actual.get(1);
     assertEquals(expected.get(0).getText(), value.get(0).getText());
     assertEquals(expected.get(0).getStartLineNo(), value.get(0).getStartLineNo());
     assertEquals(expected.get(0).getStartColNo(), value.get(0).getStartColNo());
     assertEquals(expected.get(0).getEndLineNo(), value.get(0).getEndLineNo());
     assertEquals(expected.get(0).getEndColNo(), value.get(0).getEndColNo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testReportBlockCommentInvalidParameters() {
     fileContents = new FileContents("TestFile.java", new String[]{"public class TestClass {", "/** This is a javadoc */"});
     fileContents.reportCComment(1, 1, 2, 2);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetJavadocBeforeInvalidLineNumber() {
     fileContents = new FileContents("TestFile.java", new String[]{"public class TestClass {", "/** This is a javadoc */", "}"});
     fileContents.getJavadocBefore(-1);
 }

 @Test
 public void testGetJavadocBeforeValidLineNumber() {
     fileContents = new FileContents("TestFile.java", new String[]{"public class TestClass {", "/** This is a javadoc */", "}"});
     TextBlock expected = new TextBlock("/** This is a javadoc */", 2, 1);
     TextBlock actual = fileContents.getJavadocBefore(3);
     assertEquals(expected.getText(), actual.getText());
     assertEquals(expected.getStartLineNo(), actual.getStartLineNo());
     assertEquals(expected.getStartColNo(), actual.getStartColNo());
     assertEquals(expected.getEndLineNo(), actual.getEndLineNo());
     assertEquals(expected.getEndColNo(), actual.getEndColNo());
 }

 @Test
 public void testLineIsBlank() {
     fileContents = new FileContents("TestFile.java", new String[]{"public class TestClass {", "", "}"});
     assertTrue(fileContents.lineIsBlank(1));
     assertTrue(fileContents.lineIsBlank(2));
     assertFalse(fileContents.lineIsBlank(3));
 }

 @Test
 public void testLineIsComment() {
     fileContents = new FileContents("TestFile.java", new String[]{"public class TestClass {", "// This is a comment", "}"});
     assertTrue(fileContents.lineIsComment(1));
     assertFalse(fileContents.lineIsComment(2));
     assertFalse(fileContents.lineIsComment(3));
 }

 @Test
 public void testHasIntersectionWithCommentTrue() {
     fileContents = new FileContents("TestFile.java", new String[]{"public class TestClass {", "// This is a comment", "/** This is a javadoc */", "}"});
     assertTrue(fileContents.hasIntersectionWithComment(1, 1, 1, 10));
 }

 @Test
 public void testHasIntersectionWithCommentFalse() {
     fileContents = new FileContents("TestFile.java", new String[]{"public class TestClass {", "// This is a comment", "/** This is a javadoc */", "}"});
     assertFalse(fileContents.hasIntersectionWithComment(1, 1, 3, 3));
 }

 @Test
 public void testInPackageInfo() {
     fileContents = new FileContents("package-info.java", new String[]{});
     assertTrue(fileContents.inPackageInfo());
 }

 @Test
 public void testInPackageInfoFalse() {
     fileContents = new FileContents("TestFile.java", new String[]{});
     assertFalse(fileContents.inPackageInfo());
 }
}