package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FileTextTest {

 private static final String TEST_FILE_PATH = "src/test/resources/testFile.txt";

 @Test
 public void testFileTextConstructionFromFile() throws IOException {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 assertEquals(TEST_FILE_PATH, fileText.getFile().getPath());
 assertEquals(StandardCharsets.UTF_8, fileText.getCharset());
 String expectedContent = new String(Files.readAllBytes(new File(TEST_FILE_PATH).toPath()), StandardCharsets.UTF_8);
 assertEquals(expectedContent, fileText.getFullText());
 }

 @Test
 public void testFileTextConstructionFromLines() {
 List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
 FileText fileText = FileText.fromLines(new File("testFile.txt"), lines);
 assertEquals(new File("testFile.txt"), fileText.getFile());
 assertNull(fileText.getCharset());
 assertEquals(lines.toArray(new String[0]), fileText.toLinesArray());
 }

 @Test
 public void testLineColumn() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 LineColumn lineColumn = fileText.lineColumn(10);
 assertEquals(2, lineColumn.getLineNumber());
 assertEquals(7, lineColumn.getColumnNumber());
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileTextConstructionFromNonExistentFile() throws IOException {
 new FileText(new File("nonExistentFile.txt"), StandardCharsets.UTF_8.name());
 }

 @Test
 public void testFindLineBreaks() throws IOException {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 int[] lineBreaks = fileText.findLineBreaks();
 assertNotNull(lineBreaks);
 assertEquals(4, lineBreaks.length);
 }
}