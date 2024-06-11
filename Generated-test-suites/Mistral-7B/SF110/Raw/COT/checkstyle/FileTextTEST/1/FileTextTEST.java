package com.example.junit4;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.io.CharStreams;

public class FileTextTest {

 private static final String TEST_FILE_PATH = "testFile.txt";

 @Test
 public void testFileTextConstructor() throws IOException {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 assertTrue(fileText instanceof FileText);
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileTextConstructorWithNonExistentFile() throws IOException {
 new FileText(new File("nonExistentFile.txt"), StandardCharsets.UTF_8.name());
 }

 @Test
 public void testFileTextToString() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 assertEquals(FileText.readFile(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.newDecoder()), fileText.getFullText());
 }

 @Test
 public void testFileTextFromLines() {
 List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
 FileText fileText = FileText.fromLines(new File(TEST_FILE_PATH), lines);
 assertEquals(lines.toArray(new String[0]), fileText.toLinesArray());
 }

 @Test
 public void testGetFile() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 assertEquals(new File(TEST_FILE_PATH), fileText.getFile());
 }

 @Test
 public void testGetCharset() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 assertSame(StandardCharsets.UTF_8, fileText.getCharset());
 }

 @Test
 public void testLineColumn() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 LineColumn lineColumn = fileText.lineColumn(5);
 assertEquals(3, lineColumn.lineNumber);
 assertEquals(5, lineColumn.columnNumber);
 }

 @Test
 public void testLineColumnWithInvalidPosition() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 LineColumn lineColumn = fileText.lineColumn(-1);
 assertNull(lineColumn);
 }

 @Test
 public void testFindLineBreaks() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 int[] lineBreaks = fileText.findLineBreaks();
 List<Integer> expectedLineBreakPositions = new ArrayList<>();
 try (Reader reader = new BufferedReader(new FileReader(TEST_FILE_PATH))) {
 List<String> lines = CharStreams.readLines(reader);
 for (int i = 0; i < lines.size(); ++i) {
 expectedLineBreakPositions.add(lines.get(i).length() + System.lineSeparator().length().intValue());
 }
 }
 assertArrayEquals(expectedLineBreakPositions.toArray(), Arrays.copyOf(lineBreaks, lineBreaks.length));
 }

 @Test(expected = NullPointerException.class)
 public void testLineColumnWithNullFileText() {
 FileText fileText = null;
 fileText.lineColumn(1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testLineColumnWithLineNumberBeyondSize() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 fileText.lineColumn(fileText.size() + 1);
 }

 @Test
 public void testSize() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 assertEquals(3, fileText.size());
 }

 @Test
 public void testGet() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 assertEquals("Line 1", fileText.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetWithInvalidIndex() {
 FileText fileText = new FileText(new File(TEST_FILE_PATH), StandardCharsets.UTF_8.name());
 fileText.get(fileText.size());
 }
}