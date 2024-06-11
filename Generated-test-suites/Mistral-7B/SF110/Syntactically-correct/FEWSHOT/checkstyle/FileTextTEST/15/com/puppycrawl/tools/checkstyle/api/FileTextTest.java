package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FileTextTest {

 private static final String TEST_FILE_PATH = "testFile.txt";
 private static final String TEST_CHARSET = "UTF-8";

 @Test
 public void testFileTextConstructionFromFile() throws IOException {
 File file = new File(TEST_FILE_PATH);
 List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
 FileText fileText = new FileText(file, TEST_CHARSET);

 assertEquals(file, fileText.getFile());
 assertEquals(TEST_CHARSET, fileText.getCharset().name());
 assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
 }

 @Test
 public void testFileTextConstructionFromLines() {
 File file = new File(TEST_FILE_PATH);
 List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
 FileText fileText = FileText.fromLines(file, lines);

 assertEquals(file, fileText.getFile());
 assertArrayEquals(lines.toArray(new String[0]), fileText.toLinesArray());
 }

 @Test
 public void testFileTextConstructionFromFileWithInvalidCharset() throws IOException {
 File file = new File(TEST_FILE_PATH);
 String charsetName = "invalidCharset";

 assertThrows(IllegalStateException.class, () -> new FileText(file, charsetName));
 }

 @Test
 public void testGetFullText() throws IOException {
 File file = new File(TEST_FILE_PATH);
 String charsetName = TEST_CHARSET;

 String expectedContent = "Line 1\nLine 2\nLine 3";
 FileText fileText = new FileText(file, charsetName);

 assertEquals(expectedContent, fileText.getFullText());
 }

 @Test
 public void testLineColumn() throws IOException {
 File file = new File(TEST_FILE_PATH);
 String charsetName = TEST_CHARSET;
 String content = "Line 1\nLine 2\nLine 3";

 FileText fileText = new FileText(file, charsetName, Arrays.asList(content.split("\n")));
 LineColumn lineColumn = fileText.lineColumn(12);

 assertEquals(2, lineColumn.getLineNumber());
 assertEquals(7, lineColumn.getColumnNumber());
 }

 @Test
 public void testFindLineBreaks() throws IOException {
 File file = new File(TEST_FILE_PATH);
 String charsetName = TEST_CHARSET;
 String content = "Line 1\nLine 2\nLine 3";

 FileText fileText = new FileText(file, charsetName, Arrays.asList(content.split("\n")));
 int[] lineBreaks = fileText.findLineBreaks();

 assertArrayEquals(new int[] {0, 2, 5}, lineBreaks);
 }

 @Test
 public void testClone() throws IOException {
 File file = new File(TEST_FILE_PATH);
 String charsetName = TEST_CHARSET;
 String content = "Line 1\nLine 2\nLine 3";

 FileText originalFileText = new FileText(file, charsetName, Arrays.asList(content.split("\n")));
 FileText clonedFileText = (FileText) originalFileText.clone();

 assertEquals(originalFileText.getFile(), clonedFileText.getFile());
 assertEquals(originalFileText.getCharset(), clonedFileText.getCharset());
 assertArrayEquals(originalFileText.toLinesArray(), clonedFileText.toLinesArray());
 }
}