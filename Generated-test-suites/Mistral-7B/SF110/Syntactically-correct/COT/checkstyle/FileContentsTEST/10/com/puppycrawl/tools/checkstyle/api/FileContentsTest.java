package com.example.junit.test.filecontents;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.Comment;
import com.puppycrawl.tools.checkstyle.api.TextBlock;

public class FileContentsTest {

  private FileContents fileContents;
  private FileText mockFileText;

  @Before
  public void setup() {
    mockFileText = Mockito.mock(FileText.class);
    fileContents = new FileContents("fileName.java", mockFileText);
  }

  // Test case for constructor with String and lines
  @Test
  public void testFileContentsWithStringAndLines() {
    String[] lines = new String[]{"public class TestClass {", "  // This is a comment", "}", ""};
    when(mockFileText.get(0)).thenReturn(lines[0]);
    when(mockFileText.get(1)).thenReturn(lines[1]);
    when(mockFileText.get(2)).thenReturn(lines[2]);
    when(mockFileText.get(3)).thenReturn(lines[3]);

    fileContents = new FileContents("fileName.java", lines);

    assertEquals("fileName.java", fileContents.getFileName());
    verify(mockFileText, times(1)).get(0);
    verify(mockFileText, times(1)).get(1);
    verify(mockFileText, times(1)).get(2);
    verify(mockFileText, times(1)).get(3);
  }

  // Test case for constructor with FileText
  @Test
  public void testFileContentsWithFileText() {
    String[] lines = new String[]{"public class TestClass {", "  // This is a comment", "}", ""};
    FileText fileText = Mockito.mock(FileText.class);
    when(fileText.getFile()).thenReturn(new File("fileName.java"));
    when(fileText.toLinesArray()).thenReturn(lines);

    fileContents = new FileContents(fileText);

    assertEquals("fileName.java", fileContents.getFileName());
    verify(fileText, times(1)).getFile();
    verify(fileText, times(1)).toLinesArray();
  }

  // Test case for reportSingleLineComment
  @Test(expected = IllegalArgumentException.class)
  public void testReportSingleLineCommentWithInvalidLineNumber() {
    fileContents.reportSingleLineComment("type", -1, 1);
  }

  @Test
  public void testReportSingleLineComment() {
    String line = "// This is a single line comment";
    int startLineNo = 1;
    int startColNo = 5;

    fileContents.reportSingleLineComment("type", startLineNo, startColNo);

    TextBlock comment = fileContents.cppComments.get(startLineNo);
    assertEquals(comment, fileContents.getJavadocBefore(startLineNo - 1));
    assertEquals("type", comment.getType());
    assertEquals(startColNo, comment.getStartColNo());
    assertEquals(startLineNo, comment.getStartLineNo());
  }

  // Test case for getCppComments
  @Test
  public void testGetCppComments() {
    String line1 = "// This is a C++ comment";
    String line2 = "/* This is a multi-line C++ comment */";
    String line3 = "public class TestClass {";

    when(mockFileText.get(0)).thenReturn(line1);
    when(mockFileText.get(1)).thenReturn(line2);
    when(mockFileText.get(2)).thenReturn(line3);

    fileContents = new FileContents("fileName.java", mockFileText);

    ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();

    TextBlock comment1 = cppComments.get(1);
    TextBlock comment2 = cppComments.get(2);

    assertEquals(2, cppComments.size());
    assertEquals(line1, comment1.getText());
    assertEquals(line2, comment2.getText());
  }

  // Test case for reportCComment
  @Test(expected = IllegalArgumentException.class)
  public void testReportCCommentWithInvalidStartLineNumber() {
    fileContents.reportCComment("type", -1, 1, 1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReportCCommentWithInvalidStartColumnNumber() {
    fileContents.reportCComment("type", 1, -1, 1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReportCCommentWithInvalidEndLineNumber() {
    fileContents.reportCComment("type", 1, 1, -1, 1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testReportCCommentWithInvalidEndColumnNumber() {
    fileContents.reportCComment("type", 1, 1, 1, -1);
  }

  @Test
  public void testReportCComment() {
    String line1 = "/** This is a Javadoc comment */";
    String line2 = "/* This is a multi-line C comment */";
    String line3 = "// This is a single line C comment";

    when(mockFileText.get(0)).thenReturn(line1);
    when(mockFileText.get(1)).thenReturn(line2);
    when(mockFileText.get(2)).thenReturn(line3);

    fileContents = new FileContents("fileName.java", mockFileText);

    int startLineNo = 1;
    int startColNo = 5;
    int endLineNo = 3;
    int endColNo = 10;

    fileContents.reportCComment("type", startLineNo, startColNo, endLineNo, endColNo);

    List<TextBlock> clangComments = fileContents.clangComments.get(startLineNo);
    TextBlock comment = clangComments.get(0);

    assertEquals(1, clangComments.size());
    assertEquals("type", comment.getType());
    assertEquals(startColNo, comment.getStartColNo());
    assertEquals(startLineNo, comment.getStartLineNo());
    assertEquals(endLineNo, comment.getEndLineNo());
    assertEquals(endColNo, comment.getEndColNo());
  }

  // Test case for getCComments
  @Test
  public void testGetCComments() {
    String line1 = "/* This is a multi-line C comment */";
    String line2 = "// This is a single line C comment";

    when(mockFileText.get(0)).thenReturn(line1);
    when(mockFileText.get(1)).thenReturn(line2);

    fileContents = new FileContents("fileName.java", mockFileText);

    ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();

    List<TextBlock> comments = cComments.get(1);

    assertEquals(1, cComments.size());
    assertEquals(1, comments.size());
    assertEquals(line1, comments.get(0).getText());
  }

  // Test case for getJavadocBefore
  @Test(expected = IllegalArgumentException.class)
  public void testGetJavadocBeforeWithInvalidLineNumber() {
    fileContents.getJavadocBefore(-1);
  }

  @Test
  public void testGetJavadocBefore() {
    String line1 = "/** This is a Javadoc comment */";
    String line2 = "public class TestClass {";

    when(mockFileText.get(0)).thenReturn(line1);
    when(mockFileText.get(1)).thenReturn(line2);

    fileContents = new FileContents("fileName.java", mockFileText);

    int lineNoBefore = 1;

    TextBlock javadoc = fileContents.getJavadocBefore(lineNoBefore);

    assertEquals(line1, javadoc.getText());
  }

  // Test case for lineIsBlank
  @Test
  public void testLineIsBlank() {
    String line = " ";

    assertTrue(fileContents.lineIsBlank(0));
    assertTrue(fileContents.lineIsBlank(1));
    assertFalse(fileContents.lineIsBlank(2));

    assertTrue(fileContents.lineIsBlank(0));
    assertFalse(fileContents.lineIsBlank(1));
    assertFalse(fileContents.lineIsBlank(2));

    assertTrue(fileContents.lineIsBlank(0));
    assertFalse(fileContents.lineIsBlank(1));
    assertTrue(fileContents.lineIsBlank(2));

    assertFalse(fileContents.lineIsBlank(0));
    assertFalse(fileContents.lineIsBlank(1));
    assertFalse(fileContents.lineIsBlank(2));

    String blankLine = " ";
    String nonBlankLine = "public class TestClass {";

    assertTrue(fileContents.lineIsBlank(0));
    assertFalse(fileContents.lineIsBlank(1));

    assertTrue(fileContents.lineIsBlank(Arrays.asList(blankLine).indexOf(fileContents.getLine(0))));
    assertFalse(fileContents.lineIsBlank(Arrays.asList(blankLine, nonBlankLine).indexOf(fileContents.getLine(1))));
  }

  // Test case for lineIsComment
  @Test
  public void testLineIsComment() {
    String line1 = "// This is a single line comment";
    String line2 = "public class TestClass {";

    when(mockFileText.get(0)).thenReturn(line1);
    when(mockFileText.get(1)).thenReturn(line2);

    fileContents = new FileContents("fileName.java", mockFileText);

    assertFalse(fileContents.lineIsComment(0));
    assertTrue(fileContents.lineIsComment(1));
  }

  // Test case for hasIntersectionWithComment
  @Test
  public void testHasIntersectionWithComment() {
    String line1 = "// This is a single line comment";
    String line2 = "/* This is a multi-line comment */";
    String line3 = "public class TestClass {";

    when(mockFileText.get(0)).thenReturn(line1);
    when(mockFileText.get(1)).thenReturn(line2);
    when(mockFileText.get(2)).thenReturn(line3);

    fileContents = new FileContents("fileName.java", mockFileText);

    int startLineNo = 1;
    int startColNo = 5;
    int endLineNo = 3;
    int endColNo = 10;

    assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

    startColNo += 10;
    endColNo -= 10;

    assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));

    startColNo -= 10;
    endColNo += 10;

    assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
  }

  // Test case for inPackageInfo
  @Test
  public void testInPackageInfo() {
    String line1 = "package com.example.package;";
    String line2 = "public class TestClass {";

    when(mockFileText.get(0)).thenReturn(line1);
    when(mockFileText.get(1)).thenReturn(line2);

    fileContents = new FileContents("fileName.java", mockFileText);

    assertTrue(fileContents.inPackageInfo());
  }

  // Test case for hasIntersectionWithCComment
  @Test
  public void testHasIntersectionWithCComment() {
    String line1 = "/* This is a multi-line C comment */";
    String line2 = "public class TestClass {";

    when(mockFileText.get(0)).thenReturn(line1);
    when(mockFileText.get(1)).thenReturn(line2);

    fileContents = new FileContents("fileName.java", mockFileText);

    int startLineNo = 1;
    int startColNo = 5;
    int endLineNo = 3;
    int endColNo = 10;

    assertFalse(fileContents.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo));

    startColNo += 5;
    endColNo -= 5;

    assertTrue(fileContents.hasIntersectionWithCComment(startLineNo, startColNo, endLineNo, endColNo));
  }

  // Test case for hasIntersectionWithCppComment
  @Test
  public void testHasIntersectionWithCppComment() {
    String line1 = "// This is a single line C++ comment";
    String line2 = "/* This is a multi-line comment */";
    String line3 = "public class TestClass {";

    when(mockFileText.get(0)).thenReturn(line1);
    when(mockFileText.get(1)).thenReturn(line2);
    when(mockFileText.get(2)).thenReturn(line3);

    fileContents = new FileContents("fileName.java", mockFileText);

    int startLineNo = 1;
    int startColNo = 5;
    int endLineNo = 3;
    int endColNo = 10;

    assertFalse(fileContents.hasIntersectionWithCppComment(startLineNo, startColNo, endLineNo, endColNo));

    startLineNo += 1;

    assertTrue(fileContents.hasIntersectionWithCppComment(startLineNo, startColNo, endLineNo, endColNo));
  }
}