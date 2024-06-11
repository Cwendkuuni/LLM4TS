package com.example.junit4;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

  @Test
  public void testFileTextConstructorValidArgs() {
    File textFile = new File("testFile.txt");
    Charsequence expectedText = "this is a test file";
    FileText fileText = new FileText(textFile, StandardCharsets.UTF_8.name());
    assertEquals(expectedText, fileText.getFullText());
  }

  @Test(expected = IOException.class)
  public void testFileTextConstructorNullFile() {
    FileText fileText = new FileText(null, StandardCharsets.UTF_8.name());
  }

  @Test(expected = IllegalStateException.class)
  public void testFileTextConstructorUnsupportedCharset() {
    File textFile = new File("testFile.txt");
    FileText fileText = new FileText(textFile, "UnsupportedCharset");
  }

  @Test
  public void testFileTextFromLinesValidArgs() {
    List<String> lines = Arrays.asList("line1", "line2", "line3");
    FileText fileText = FileText.fromLines(new File("testFile.txt"), lines);
    assertEquals("line1\nline2\nline3", fileText.getFullText());
  }

  @Test(expected = FileNotFoundException.class)
  public void testReadFileNonExistentFile() {
    FileText fileText = new FileText(new File("nonExistentFile.txt"), StandardCharsets.UTF_8.name());
  }

  @Test(expected = UnsupportedCharsetException.class)
  public void testReadFileUnsupportedCharset() {
    FileText fileText = new FileText(new File("testFile.txt"), "UnsupportedCharset");
  }

  @Test
  public void testFileTextFromInvalidLines() {
    List<String> lines = Arrays.asList("line1", "line2", "line3", null);
    assertThrows(NullPointerException.class, () -> FileText.fromLines(new File("testFile.txt"), lines));
  }

  // Test other methods and edge cases here

  @Test
  public void testLineColumnValidPosition() {
    FileText fileText = new FileText(new File("testFile.txt"), StandardCharsets.UTF_8.name());
    LineColumn expectedLineColumn = new LineColumn(1, 5);
    LineColumn actualLineColumn = fileText.lineColumn(5);
    assertEquals(expectedLineColumn, actualLineColumn);
  }

  @Test
  public void testLineColumnInvalidPositionBeginningOfFile() {
    FileText fileText = new FileText(new File("testFile.txt"), StandardCharsets.UTF_8.name());
    LineColumn expectedLineColumn = new LineColumn(1, 0);
    LineColumn actualLineColumn = fileText.lineColumn(0);
    assertEquals(expectedLineColumn, actualLineColumn);
  }

  @Test
  public void testLineColumnInvalidPositionEndOfFile() {
    FileText fileText = new FileText(new File("testFile.txt"), StandardCharsets.UTF_8.name());
    LineColumn expectedLineColumn = new LineColumn(1, fileText.size());
    LineColumn actualLineColumn = fileText.lineColumn(fileText.size());
    assertEquals(expectedLineColumn, actualLineColumn);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLineColumnNegativePosition() {
    FileText fileText = new FileText(new File("testFile.txt"), StandardCharsets.UTF_8.name());
    fileText.lineColumn(-1);
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testLineColumnInvalidPositionOutOfBounds() {
    FileText fileText = new FileText(new File("testFile.txt"), StandardCharsets.UTF_8.name());
    fileText.lineColumn(100);
  }

  @Test
  public void testFindLineBreaksValidFile() {
    FileText fileText = new FileText(new File("testFile.txt"), StandardCharsets.UTF_8.name());
    int[] expectedLineBreakPositions = new int[] { 0, 7, 13, 19, 25, 31, 37, 43, 48, 53, 58, 63 };
    int[] actualLineBreakPositions = fileText.findLineBreaks();
    assertArrayEquals(expectedLineBreakPositions, actualLineBreakPositions);
  }

  @Test
  public void testFindLineBreaksEmptyFile() {
    FileText fileText = new FileText(new File("emptyFile.txt"), StandardCharsets.UTF_8.name());
    int[] expectedLineBreakPositions = new int[] { 0 };
    int[] actualLineBreakPositions = fileText.findLineBreaks();
    assertArrayEquals(expectedLineBreakPositions, actualLineBreakPositions);
  }

  @Test(expected = UnsupportedEncodingException.class)
  public void testFindLineBreaksWithInvalidCharset() {
    FileText fileText = new FileText(new File("testFile.txt"), "InvalidCharset");
    fileText.findLineBreaks();
  }
}