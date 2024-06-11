package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineNumber;
import com.puppycrawl.tools.checkstyle.api.SourceFile;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import com.puppycrawl.tools.checkstyle.api.Util;
import com.puppycrawl.tools.checkstyle.api.Util.Pair;

public class FileContentsTest {

 private FileContents fileContents;
 private SourceFile sourceFile;
 private FileText fileText;
 private CommentListener commentListenerMock;

 @Before
 public void setUp() {
 commentListenerMock = mock(CommentListener.class);
 sourceFile = mock(SourceFile.class);
 fileText = Mockito.spy(new FileText(Collections.emptyList()));
 fileContents = new FileContents("testFile.java", fileText);
 }

 @Test
 public void testFileContentsConstructionWithFile() {
 FileContents fileContentsWithFile = new FileContents(sourceFile);
 assertEquals("testFile.java", fileContentsWithFile.getFileName());
 }

 @Test
 public void testFileContentsConstructionWithLines() {
 String[] lines = {"public class TestClass {", " // This is a comment", " int main(int arg) {", "}"};
 FileContents fileContentsWithLines = new FileContents("testFile.java", lines);
 assertEquals("testFile.java", fileContentsWithLines.getFileName());
 assertEquals(lines, fileContentsWithLines.getLines());
 }

 @Test
 public void testGetText() {
 FileText expectedText = Mockito.spy(new FileText(Arrays.asList(
 "public class TestClass {", " // This is a comment", " int main(int arg) {", "}"
 )));
 when(fileText.get(eq(0))).thenReturn("public class TestClass {");
 when(fileText.get(eq(1))).thenReturn(" // This is a comment");
 when(fileText.get(eq(2))).thenReturn(" int main(int arg) {");
 when(fileText.get(eq(3))).thenReturn("}");

 FileContents fileContents = new FileContents("testFile.java", fileText);
 FileText actualText = fileContents.getText();
 assertEquals(expectedText, actualText);
 }

 @Test
 public void testGetLines() {
 String[] expectedLines = {"public class TestClass {", " // This is a comment", " int main(int arg) {", "}"};
 String[] actualLines = fileContents.getLines();
 assertArrayEquals(expectedLines, actualLines);
 }

 @Test
 public void testGetLine() {
 String expectedLine = "int main(int arg) {";
 String actualLine = fileContents.getLine(1);
 assertEquals(expectedLine, actualLine);
 }

 @Test
 public void testGetFileName() {
 assertEquals("testFile.java", fileContents.getFileName());
 }

 @Test
 public void testLineIsBlank() {
 String blankLine = " ";
 String notBlankLine = "public class TestClass {";
 boolean resultBlank = fileContents.lineIsBlank(0);
 boolean resultNotBlank = fileContents.lineIsBlank(1);
 assertFalse(resultBlank);
 assertTrue(resultNotBlank);
 }

 @Test
 public void testLineIsComment() {
 String singleLineComment = "// This is a comment";
 String notCommentLine = "public class TestClass {";
 boolean resultSingleLineComment = fileContents.lineIsComment(1);
 boolean resultNotComment = fileContents.lineIsComment(0);
 assertFalse(resultNotComment);
 assertTrue(resultSingleLineComment);
 }

 @Test
 public void testHasIntersectionWithCommentNoIntersection() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 2;
 int endColNo = 5;
 boolean result = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertFalse(result);
 }

 @Test
 public void testHasIntersectionWithCommentSingleLineComment() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 1;
 int endColNo = 20;
 boolean result = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertTrue(result);
 }

 @Test
 public void testHasIntersectionWithCommentBlockComment() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 2;
 int endColNo = 20;
 String line1 = "/* This is a block comment";
 String line2 = " */";
 when(fileText.get(eq(0))).thenReturn(line1);
 when(fileText.get(eq(1))).thenReturn(line2);
 boolean result = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertTrue(result);
 }

 @Test
 public void testHasIntersectionWithCommentMultipleComments() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 3;
 int endColNo = 20;
 String line1 = "// This is a single line comment";
 String line2 = "/* This is a block comment */";
 String line3 = "// Another single line comment";
 when(fileText.get(eq(0))).thenReturn(line1);
 when(fileText.get(eq(1))).thenReturn(line2);
 when(fileText.get(eq(2))).thenReturn(line3);
 boolean result = fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo);
 assertTrue(result);
 }

 @Test
 public void testGetJavadocBefore() {
 int lineNoBefore = 1;
 TextBlock expectedJavadoc = Mockito.mock(TextBlock.class);
 when(expectedJavadoc.getText()).thenReturn("/** This is a Javadoc comment */");
 FileText expectedFileText = Mockito.spy(new FileText(Arrays.asList(
 "/** This is a Javadoc comment */",
 "public class TestClass {",
 " // This is a comment",
 " int main(int arg) {",
 "}"
 )));
 when(fileText.get(eq(0))).thenReturn("/** This is a Javadoc comment */");
 when(fileText.get(eq(1))).thenReturn("public class TestClass {");
 when(fileText.get(eq(2))).thenReturn(" // This is a comment");
 when(fileText.get(eq(3))).thenReturn(" int main(int arg) {");
 when(fileText.get(eq(4))).thenReturn("}");
 FileContents fileContents = new FileContents("testFile.java", fileText);
 TextBlock actualJavadoc = fileContents.getJavadocBefore(lineNoBefore);
 assertEquals(expectedJavadoc, actualJavadoc);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetJavadocBeforeNegativeLineNumber() {
 int lineNoBefore = -1;
 FileContents fileContents = new FileContents("testFile.java", fileText);
 fileContents.getJavadocBefore(lineNoBefore);
 }

 @Test
 public void testGetJavadocBeforeNoJavadocBefore() {
 int lineNoBefore = 4;
 FileContents fileContents = new FileContents("testFile.java", fileText);
 TextBlock actualJavadoc = fileContents.getJavadocBefore(lineNoBefore);
 assertNull(actualJavadoc);
 }

 @Test
 public void testReportBlockComment() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 3;
 int endColNo = 20;
 String commentText = "/** This is a Javadoc comment */";
 String line1 = "/* This is a block comment */";
 String line2 = " */";
 String line3 = "// This is a single line comment";
 when(fileText.get(eq(0))).thenReturn(line1);
 when(fileText.get(eq(1))).thenReturn(line2);
 when(fileText.get(eq(2))).thenReturn(line3);
 FileContents fileContents = new FileContents("testFile.java", fileText);
 fileContents.reportCComment(startLineNo, startColNo, endLineNo, endColNo);
 verify(commentListenerMock, times(1)).reportBlockComment("JAVADOC", startLineNo, startColNo, endLineNo, endColNo, Arrays.asList(new Pair<>(Comment.Kind.JAVADOC, commentText)));
 }

 @Test
 public void testGetCppComments() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 3;
 int endColNo = 20;
 String commentText = "// This is a C comment";
 String line1 = "// This is a C comment";
 String line2 = "// Another C comment";
 when(fileText.get(eq(0))).thenReturn(line1);
 when(fileText.get(eq(1))).thenReturn(line2);
 FileContents fileContents = new FileContents("testFile.java", fileText);
 ImmutableMap<Integer, TextBlock> actualCppComments = fileContents.getCComments();
 ImmutableMap<Integer, TextBlock> expectedCppComments = ImmutableMap.of(
 startLineNo, Mockito.mock(TextBlock.class),
 endLineNo, Mockito.mock(TextBlock.class)
 );
 assertEquals(expectedCppComments, actualCppComments);
 }

 @Test
 public void testGetCComments() {
 int startLineNo = 1;
 int startColNo = 5;
 int endLineNo = 3;
 int endColNo = 20;
 String commentText1 = "/* This is a clang comment 1 */";
 String commentText2 = "/* This is a clang comment 2 */";
 String line1 = "/* This is a clang comment 1 */";
 String line2 = "/* This is a clang comment 2 */";
 String line3 = "// This is a single line comment";
 when(fileText.get(eq(0))).thenReturn(line1);
 when(fileText.get(eq(1))).thenReturn(line2);
 when(fileText.get(eq(2))).thenReturn(line3);
 FileContents fileContents = new FileContents("testFile.java", fileText);
 List<TextBlock> commentList = Arrays.asList(
 Mockito.mock(TextBlock.class),
 Mockito.mock(TextBlock.class)
 );
 ImmutableMap<Integer, List<TextBlock>> actualCComments = fileContents.getCComments();
 ImmutableMap<Integer, List<TextBlock>> expectedCComments = ImmutableMap.of(
 startLineNo, commentList
 );
 assertEquals(expectedCComments, actualCComments);
 }
}