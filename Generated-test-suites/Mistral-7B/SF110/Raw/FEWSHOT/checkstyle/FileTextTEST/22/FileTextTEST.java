package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import org.junit.Test;

public class FileTextTest {

 private static final String TEST_FILE = "testFile.txt";
 private static final String TEST_CHARSET = "UTF-8";

 @Test
 public void testFileTextConstruction() throws IOException {
 File file = new File(TEST_FILE);
 FileText fileText = new FileText(file, TEST_CHARSET);
 assertEquals(TEST_FILE, fileText.getFile().getPath());
 assertNotNull(fileText.getCharset());
 assertNotNull(fileText.getFullText());
 assertArrayEquals(fileText.toLinesArray(), readFileToArray(file, TEST_CHARSET));
 }

 @Test
 public void testFileTextConstructionFromFileText() throws IOException {
 FileText fileText = new FileText(new FileText(TEST_FILE, TEST_CHARSET));
 assertEquals(TEST_FILE, fileText.getFile().getPath());
 assertNotNull(fileText.getCharset());
 assertNotNull(fileText.getFullText());
 assertArrayEquals(fileText.toLinesArray(), readFileToArray(new File(TEST_FILE), TEST_CHARSET));
 }

 @Test
 public void testFileTextConstructionFromLines() throws IOException {
 File file = new File(TEST_FILE);
 List<String> lines = readFileToLines(file, TEST_CHARSET);
 FileText fileText = new FileText(file, lines);
 assertEquals(TEST_FILE, fileText.getFile().getPath());
 assertNull(fileText.getCharset());
 assertArrayEquals(fileText.toLinesArray(), lines.toArray(new String[lines.size()]));
 }

 @Test
 public void testGetFile() {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 assertEquals("testFile.txt", fileText.getFile().getPath());
 }

 @Test
 public void testGetCharset() {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 assertNotNull(fileText.getCharset());
 }

 @Test
 public void testGetFullText() {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 assertNotNull(fileText.getFullText());
 }

 @Test
 public void testToLinesArray() {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 String[] lines = fileText.toLinesArray();
 assertNotNull(lines);
 assertArrayEquals(readFileToArray(new File("testFile.txt"), "UTF-8"), lines);
 }

 @Test
 public void testFindLineBreaks() {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 int[] lineBreakPositions = fileText.findLineBreaks();
 assertNotNull(lineBreakPositions);
 }

 @Test
 public void testLineColumn() {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 int lineNo = 5;
 int columnNo = 10;
 LineColumn lineColumn = fileText.lineColumn(lineNo * this.linesPerPage + columnNo);
 assertEquals(lineNo + 1, lineColumn.getLineNumber());
 assertEquals(columnNo, lineColumn.getColumnNumber());
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileNotFoundException() throws IOException {
 FileText fileText = new FileText(new File("testFileNotFound.txt"), "UTF-8");
 fail("Expected FileNotFoundException");
 }

 private static String[] readFileToArray(File file, String charset) throws IOException {
 List<String> lines = readFileToLines(file, charset);
 return lines.toArray(new String[lines.size()]);
 }

 private static List<String> readFileToLines(File file, String charset) throws IOException {
 List<String> lines = new ArrayList<>();
 try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName(charset)))) {
 String line;
 while ((line = reader.readLine()) != null) {
 lines.add(line);
 }
 }
 return lines;
}

 private static int linesPerPage = 10;
}