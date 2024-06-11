package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FileTextTest {

 private static final File TEST_FILE = new File("src/test/resources/FileTextTest.java");

 @Test
 public void testFileTextConstructionFromFile() throws IOException {
 FileText fileText = new FileText(TEST_FILE, "UTF-8");
 assertEquals(TEST_FILE.getAbsolutePath(), fileText.getFile().getAbsolutePath());
 assertEquals("UTF-8", fileText.getCharset().name());
 String expectedText = new String(Files.readAllBytes(TEST_FILE.toPath()), Charsets.UTF_8);
 assertEquals(expectedText, fileText.getFullText());
 }

 @Test
 public void testFileTextConstructionFromFileNullCharset() throws IOException {
 FileText fileText = new FileText(TEST_FILE, null);
 assertNull(fileText.getCharset());
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileTextConstructionFromNonExistentFile() throws IOException {
 new FileText(new File("non-existent-file.txt"), "UTF-8");
 }

 @Test
 public void testFileTextConstructionFromFileWithEmptyContent() throws IOException {
 FileText fileText = new FileText(new File("src/test/resources/empty.txt"), "UTF-8");
 assertEquals("", fileText.getFullText());
 assertEquals(0, fileText.size());
 }

 @Test
 public void testFileTextConstructionFromString() {
 FileText fileText = new FileText(Arrays.asList("line1", "line2"), null);
 assertEquals(2, fileText.size());
 assertEquals("line1\nline2", fileText.getFullText());
 }

 @Test
 public void testFileTextCopyConstructor() {
 FileText original = new FileText(TEST_FILE, "UTF-8");
 FileText copy = new FileText(original);
 assertEquals(original.getFile(), copy.getFile());
 assertEquals(original.getCharset(), copy.getCharset());
 assertEquals(original.getFullText(), copy.getFullText());
 assertEquals(original.lines, copy.lines);
 }

 @Test
 public void testFileTextToStringArray() {
 FileText fileText = new FileText(TEST_FILE, "UTF-8");
 String[] linesArray = fileText.toLinesArray();
 assertArrayEquals(fileText.lines, linesArray);
 }

 @Test
 public void testLineColumn() {
 FileText fileText = new FileText(TEST_FILE, "UTF-8");
 int pos = 15;
 LineColumn lineColumn = fileText.lineColumn(pos);
 assertEquals(1, lineColumn.lineNumber);
 assertEquals(15 - fileText.findLineBreaks()[0], lineColumn.columnNumber);
 }

 @Test
 public void testFindLineBreaks() {
 FileText fileText = new FileText(TEST_FILE, "UTF-8");
 int[] lineBreaks = fileText.findLineBreaks();
 List<Integer> expectedLineBreaks = Arrays.asList(11, 22, 33, 44, 55, 66, 77, 88, 99, 100);
 Arrays.sort(lineBreaks);
 List<Integer> actualLineBreaks = Arrays.asList(lineBreaks);
 assertEquals(expectedLineBreaks, actualLineBreaks);
 }
}