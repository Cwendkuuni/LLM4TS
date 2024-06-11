package com.example.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.CommentType;
import com.puppycrawl.tools.checkstyle.api.FileContents.LineType;

public class FileContentsTest {

 private FileContents fileContents;
 private FileText fileTextMock;
 private String fileName;
 private List<String> lines;

 @Before
 public void setUp() {
 fileTextMock = mock(FileText.class);
 fileName = "testFile.java";
 lines = Arrays.asList("line1", "line2", "line3", "/** This is a javadoc comment */", "line5");
 fileContents = new FileContents(fileName, lines.toArray(new String[0]));
 }

 @Test
 public void testInitializationWithFile() {
 File file = Mockito.mock(File.class);
 when(fileTextMock.getFile()).thenReturn(file);
 fileContents = new FileContents(fileTextMock);
 assertEquals(fileName, fileContents.getFileName());
 }

 @Test
 public void testInitializationWithLines() {
 assertEquals(fileName, fileContents.getFileName());
 assertEquals(lines, fileContents.getLines());
 }

 @Test
 public void testGetText() {
 FileText text = new FileText(lines);
 assertEquals(text, fileContents.getText());
 }

 @Test
 public void testGetLines() {
 assertEquals(lines, fileContents.getLines());
 }

 @Test
 public void testGetLine() {
 assertEquals("line1", fileContents.getLine(0));
 assertEquals("line5", fileContents.getLine(3));
 }

 @Test
 public void testLineIsBlank() {
 assertFalse(fileContents.lineIsBlank(0));
 assertTrue(fileContents.lineIsBlank(1));
 }

 @Test
 public void testLineIsComment() {
 String commentLine = "// this is a single line comment";
 fileContents = new FileContents(fileName, Arrays.asList(commentLine).toArray(new String[0]));
 assertTrue(fileContents.lineIsComment(0));
 }

 @Test
 public void testHasIntersectionWithComment() {
 int startLineNo = 1;
 int startColNo = 3;
 int endLineNo = 1;
 int endColNo = 10;
 assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

 startLineNo = 1;
 startColNo = 3;
 endLineNo = 1;
 endColNo = 10;
 fileContents = new FileContents(fileName, Arrays.asList("// this is a comment", "", "line1").toArray(new String[0]));
 assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

 startLineNo = 1;
 startColNo = 3;
 endLineNo = 2;
 endColNo = 3;
 fileContents = new FileContents(fileName, Arrays.asList("line1", "// this is a comment", "line3").toArray(new String[0]));
 assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testInPackageInfo() {
 assertFalse(fileContents.inPackageInfo());

 lines = Arrays.asList("package com.example.test;");
 fileContents = new FileContents(fileName, lines.toArray(new String[0]));
 assertTrue(fileContents.inPackageInfo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidLineNumber() {
 fileContents.getLine(-1);
 }
}