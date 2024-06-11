package com.example.junit;

import static org.junit.Assert.*;
import static com.google.common.io.Files.*;

import org.junit.Before;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;

public class FileTextTest {

 private File file;
 private FileText fileText;

 @Before
 public void setUp() throws Exception {
 String fileContent = "line1\nline2\nline3\nline4";
 file = createTempFile("testFile", fileContent);
 fileText = new FileText(file, "UTF-8");
 }

 @Test
 public void testConstructor() {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 assertEquals(4, fileText.size());
 }

 @Test
 public void testGetFile() {
 assertEquals(file, fileText.getFile());
 }

 @Test
 public void testGetCharset() {
 assertEquals("UTF-8", fileText.getCharset().name());
 }

 @Test
 public void testGetFullText() {
 assertEquals("line1\nline2\nline3\nline4", fileText.getFullText());
 }

 @Test
 public void testToLinesArray() {
 String[] expected = new String[]{"line1", "line2", "line3", "line4"};
 assertArrayEquals(expected, fileText.toLinesArray());
 }

 @Test
 public void testFindLineBreaks() {
 int[] lineBreaks = fileText.findLineBreaks();
 int[] expected = new int[]{0, 5, 12, 22, 23, 24, 25, 26, 30};
 assertArrayEquals(expected, lineBreaks);
 }

 @Test
 public void testLineColumn() {
 LineColumn lineColumn = fileText.lineColumn(5);
 LineColumn expected = new LineColumn(1, 5);
 assertEquals(expected, lineColumn);
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileNotFoundException() throws Exception {
 FileText fileText = new FileText(new File("nonExistentFile.txt"), "UTF-8");
 fail("FileNotFoundException expected");
 }

 @Test(expected = UnsupportedCharsetException.class)
 public void testUnsupportedCharsetException() throws Exception {
 FileText fileText = new FileText(file, "nonExistentCharset");
 fail("UnsupportedCharsetException expected");
 }

 @Test
 public void testFromLines() {
 List<String> lines = Arrays.asList("line5", "line6", "line7");
 FileText fileText = FileText.fromLines(file, lines);
 assertEquals(3, fileText.size());
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 FileText original = new FileText(file, "UTF-8");
 FileText cloned = (FileText) original.clone();
 assertEquals(original, cloned);
 }

 @After
 public void tearDown() throws Exception {
 deleteQuietly(file);
 }

}