package com.example.junit4;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.google.common.collect.Lists;

public class FileTextTest {

 private File file;
 private FileText fileText;

 @Before
 public void setup() throws Exception {
 file = new File("path/to/your/testfile.txt");
 fileText = new FileText(file, StandardCharsets.UTF_8.name());
 }

 @Test
 public void testConstructionWithFileAndCharset() throws Exception {
 assertEquals("Expected file path", file.getAbsolutePath(), fileText.getFile().getAbsolutePath());
 assertEquals("Expected charset", StandardCharsets.UTF_8, fileText.getCharset());
 }

 @Test
 public void testConstructionWithFileText() throws Exception {
 FileText fileTextFromFileText = new FileText(fileText);
 assertEquals("Expected file paths are equal", fileText.getFile().getAbsolutePath(), fileTextFromFileText.getFile().getAbsolutePath());
 assertEquals("Expected charsets are equal", fileText.getCharset(), fileTextFromFileText.getCharset());
 assertEquals("Expected lines are equal", fileText.toLinesArray(), fileTextFromFileText.toLinesArray());
 }

 @Test
 public void testConstructionWithFileAndLines() throws Exception {
 List<String> lines = Lists.newArrayList(fileText.toLinesArray());
 FileText fileTextFromLines = FileText.fromLines(file, lines);
 assertEquals("Expected file paths are equal", fileText.getFile().getAbsolutePath(), fileTextFromLines.getFile().getAbsolutePath());
 assertEquals("Expected charsets are equal", fileText.getCharset(), fileTextFromLines.getCharset());
 assertArrayEquals("Expected lines are equal", fileText.toLinesArray(), fileTextFromLines.toLinesArray());
 }

 @Test
 public void testGetFile() {
 assertEquals("Expected file paths are equal", fileText.getFile().getAbsolutePath(), file.getAbsolutePath());
 }

 @Test
 public void testGetCharset() {
 assertEquals("Expected charset", StandardCharsets.UTF_8, fileText.getCharset());
 }

 @Test
 public void testGetFullText() {
 String fullText = fileText.getFullText();
 assertNotNull("Expected full text not to be null", fullText);
 }

 @Test
 public void testToLinesArray() {
 String[] lines = fileText.toLinesArray();
 assertNotNull("Expected lines array not to be null", lines);
 assertEquals("Expected number of lines", fileText.size(), lines.length);
 }

 @Test
 public void testFindLineBreaks() {
 int[] lineBreakPositions = fileText.findLineBreaks();
 assertNotNull("Expected line break positions array not to be null", lineBreakPositions);
 }

 @Test
 public void testLineColumn() {
 int pos = 10;
 LineColumn lineColumn = fileText.lineColumn(pos);
 assertNotNull("Expected line column not to be null", lineColumn);
 }

 @Test
 public void testGet() {
 int lineNumber = 1;
 String expectedLine = "Expected line at position 1";
 String actualLine = fileText.get(lineNumber);
 assertEquals("Expected line at position 1", expectedLine, actualLine);
 }

 @Test
 public void testSize() {
 int size = fileText.size();
 assertEquals("Expected size", size, fileText.getLines().length);
 }
}