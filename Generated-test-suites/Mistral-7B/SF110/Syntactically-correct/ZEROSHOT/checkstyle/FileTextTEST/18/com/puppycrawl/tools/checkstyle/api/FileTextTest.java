package com.example.junit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.google.common.io.StringReader;
import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

 private File file;
 private FileText fileText;
 private Charset charset;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 file = new File("test.txt");
 charset = Charset.forName("UTF-8");
 }

 @Test
 public void testFileTextConstructionWithFileAndCharset() throws IOException {
     fileText = new FileText(file, charset.name());
     assertEquals(file, fileText.getFile());
     assertEquals(charset, fileText.getCharset());
     assertNotNull(fileText.getFullText());
     assertNotNull(fileText.lines);
     assertEquals(5, fileText.size()); // Replace with the actual number of lines in the file
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileTextConstructionWithNonExistentFile() throws IOException {
     fileText = new FileText(new File("non-existent-file.txt"), charset.name());
 }

 @Test
 public void testFileTextConstructionWithFileText() {
     String[] lines = {"line1", "line2", "line3"};
     fileText = new FileText(new FileText(lines));
     assertArrayEquals(lines, fileText.lines);
 }

 @Test
 public void testFileTextConstructionWithFileAndLines() {
     List<String> lines = Arrays.asList("line1", "line2", "line3");
     fileText = FileText.fromLines(file, lines);
     assertArrayEquals(lines.toArray(new String[0]), fileText.lines);
 }

 @Test
 public void testGetFile() {
     assertEquals(file, fileText.getFile());
 }

 @Test
 public void testGetCharset() {
     assertEquals(charset, fileText.getCharset());
 }

 @Test
 public void testGetFullText() {
     when(file.length()).thenReturn(5L);
     when(file.getBytes(charset)).thenReturn("test getFullText".getBytes(charset));
     String expectedFullText = new String(file.getBytes(charset), charset);
     assertEquals(expectedFullText, fileText.getFullText());
 }

 @Test
 public void testToLinesArray() {
     assertArrayEquals(new String[]{"line1", "line2", "line3"}, fileText.toLinesArray());
 }

 @Test
 public void testFindLineBreaks() {
     when(file.getBytes(charset)).thenReturn("line1\nline2\nline3".getBytes(charset));
     int[] expectedLineBreaks = {0, 2, 5};
     int[] actualLineBreaks = fileText.findLineBreaks();
     assertArrayEquals(expectedLineBreaks, actualLineBreaks);
 }

 @Test
 public void testLineColumn() {
     when(file.getBytes(charset)).thenReturn("line1\nline2\nline3".getBytes(charset));
     LineColumn expectedLineColumn = new LineColumn(1, 5);
     LineColumn actualLineColumn = fileText.lineColumn(5);
     assertEquals(expectedLineColumn, actualLineColumn);
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testLineColumnNegativeIndex() {
     LineColumn lineColumn = fileText.lineColumn(-1);
 }

 @Test
 public void testLineColumnLastIndex() {
     when(file.getBytes(charset)).thenReturn("line1\nline2\nline3".getBytes(charset));
     LineColumn expectedLineColumn = new LineColumn(3, fileText.size());
     LineColumn actualLineColumn = fileText.lineColumn(fileText.size() - 1);
     assertEquals(expectedLineColumn, actualLineColumn);
 }

 @Test
 public void testGetLine() {
     when(file.getBytes(charset)).thenReturn("line1\nline2\nline3".getBytes(charset));
     String expectedLine = "line2";
     String actualLine = fileText.get(1);
     assertEquals(expectedLine, actualLine);
 }

 @Test
 public void testSize() {
     when(file.getBytes(charset)).thenReturn("line1\nline2\nline3".getBytes(charset));
     assertEquals(3, fileText.size());
 }
}