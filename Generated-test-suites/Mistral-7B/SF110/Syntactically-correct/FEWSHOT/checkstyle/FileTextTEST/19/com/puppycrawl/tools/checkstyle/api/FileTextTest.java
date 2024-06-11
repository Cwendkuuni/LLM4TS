package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;
import java.util.regex.*;
import org.junit.Test;

public class FileTextTest {

 // Test constructor with valid file and charset
 @Test
 public void testFileTextConstructorValid() throws IOException {
 File file = new File("testFilePath");
 FileText fileText = new FileText(file, "UTF-8");
 assertEquals("testFilePath", fileText.getFile().getPath());
 assertNotNull(fileText.getCharset());
 assertNotNull(fileText.getFullText());
 assertNotNull(fileText.lines);
 assertNotNull(fileText.lineBreaks);
 }

 // Test constructor with null file
 @Test(expected = NullPointerException.class)
 public void testFileTextConstructorNullFile() {
 FileText fileText = new FileText(null, "UTF-8");
 }

 // Test constructor with invalid charset
 @Test(expected = UnsupportedCharsetException.class)
 public void testFileTextConstructorInvalidCharset() throws IOException {
 File file = new File("testFilePath");
 FileText fileText = new FileText(file, "InvalidCharset");
 }

 // Test getFile method
 @Test
 public void testGetFile() {
 FileText fileText = new FileText(new File("testFilePath"), "UTF-8");
 assertEquals("testFilePath", fileText.getFile().getPath());
 }

 // Test getCharset method
 @Test
 public void testGetCharset() {
 FileText fileText = new FileText(new File("testFilePath"), "UTF-8");
 assertNotNull(fileText.getCharset());
 }

 // Test getFullText method
 @Test
 public void testGetFullText() throws IOException {
 File file = new File("testFilePath");
 FileText fileText = new FileText(file, "UTF-8");
 String fullText = fileText.getFullText();
 assertNotNull(fullText);
 }

 // Test toLinesArray method
 @Test
 public void testToLinesArray() {
 FileText fileText = new FileText(new File("testFilePath"), "UTF-8");
 String[] lines = fileText.toLinesArray();
 assertNotNull(lines);
 }

 // Test findLineBreaks method
 @Test
 public void testFindLineBreaks() {
 FileText fileText = new FileText(new File("testFilePath"), "UTF-8");
 int[] lineBreaks = fileText.findLineBreaks();
 assertNotNull(lineBreaks);
 }

 // Test lineColumn method
 @Test
 public void testLineColumn() {
 FileText fileText = new FileText(new File("testFilePath"), "UTF-8");
 LineColumn lineColumn = fileText.lineColumn(10);
 assertNotNull(lineColumn);
 }

 // Test get method
 @Test
 public void testGet() {
 FileText fileText = new FileText(new File("testFilePath"), "UTF-8");
 String expectedLine = "Expected line";
 fileText.lines[0] = expectedLine;
 String actualLine = fileText.get(0);
 assertEquals(expectedLine, actualLine);
 }

 // Test size method
 @Test
 public void testSize() throws IOException {
 FileText fileText = new FileText(new File("testFilePath"), "UTF-8");
 List<String> lines = Files.readAllLines(fileText.getFile().toPath(), StandardCharsets.UTF_8);
 fileText = FileText.fromLines(fileText.getFile(), lines);
 assertEquals(lines.size(), fileText.size());
 }

 // Test static fromLines method
 @Test
 public void testFromLines() throws IOException {
 File file = new File("testFilePath");
 List<String> lines = Arrays.asList("Line1", "Line2", "Line3");
 FileText fileText = FileText.fromLines(file, lines);
 assertEquals(file.getPath(), fileText.getFile().getPath());
 assertEquals(lines.toArray(new String[lines.size()]), fileText.toLinesArray());
 }
}