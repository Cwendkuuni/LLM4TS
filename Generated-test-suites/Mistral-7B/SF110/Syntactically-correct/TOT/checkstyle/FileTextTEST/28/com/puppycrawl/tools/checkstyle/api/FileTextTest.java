package com.example.test;

import static org.junit.Assert.*;
import static com.google.common.io.Files.*;
import static com.puppycrawl.tools.checkstyle.api.FileText.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FileTextTest {

 private File file;
 private FileText fileText;

 @Before
 public void setUp() throws IOException {
 file = new File("src/test/resources/testFile.txt");
 fileText = new FileText(file, StandardCharsets.UTF_8);
 }

 @Test
 public void testConstructorWithValidFile() {
 // Check if the file text is correctly read
 assertEquals(readLinesFromFile(file, StandardCharsets.UTF_8), fileText.toArray());
 // Test size method
 assertEquals(8, fileText.size());
 // Test get method
 assertEquals("This is the first line.\n", fileText.get(0));
 // Test lineColumn method
 assertEquals(new LineColumn(1, 15), fileText.lineColumn(14));
 }

 @Test(expected = FileNotFoundException.class)
 public void testConstructorWithNonExistingFile() {
 new FileText(new File("doesnotexist.txt"), StandardCharsets.UTF_8);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithInvalidCharset() {
 try {
 new FileText(new File("testFile.txt"), "ABCDEFGH");
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 @Test
 public void testCopyConstructor() {
 FileText copyFileText = new FileText(fileText);
 assertEquals(fileText.toArray(), copyFileText.toArray());
 }

 @Test
 public void testOtherConstructor() {
 List<String> lines = Arrays.asList("Line 1.", "Line 2.", "Line 3.");
 FileText fileTextFromLines = new FileText(file, lines);
 assertEquals(lines.toArray(new String[0]), fileTextFromLines.toArray());
 }

 @Test
 public void testFindLineBreaks() {
 int[] lineBreaks = fileText.findLineBreaks();
 assertArrayEquals(new int[]{0, 13, 28, 44, 56, 69, 77, fileText.size()}, lineBreaks);
 }

 @Test
 public void testLineColumnWithValidIndex() {
 LineColumn lineColumn = fileText.lineColumn(14);
 assertEquals(2, lineColumn.lineNo);
 assertEquals(12, lineColumn.columnNo);
 }

 @Test
 public void testLineColumnWithInvalidIndex() {
 try {
 fileText.lineColumn(-1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testLineColumnWithIndexBiggerThanSize() {
 try {
 fileText.lineColumn(fileText.size());
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testGetWithInvalidIndex() {
 try {
 fileText.get(-1);
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testGetWithIndexBiggerThanSize() {
 try {
 fileText.get(fileText.size());
 fail();
 } catch (IndexOutOfBoundsException e) {
 // Expected
 }
 }

 @Test
 public void testSizeWithEmptyFile() throws IOException {
 File emptyFile = new File("src/test/resources/emptyFile.txt");
 FileText emptyFileText = new FileText(emptyFile, StandardCharsets.UTF_8);
 assertEquals(0, emptyFileText.size());
 }

 @Test
 public void testSizeWithLargeFile() throws IOException {
 File largeFile = new File("src/test/resources/largeFile.txt");
 FileText largeFileText = new FileText(largeFile, StandardCharsets.UTF_8);
 // Assuming the large file has more than 5000 lines
 assertEquals(5001, largeFileText.size());
 }

 @Test
 public void testGetWithMiddleLine() {
 assertEquals("Line 4.", fileText.get(3));
 }

 @Test
 public void testGetWithLastLine() {
 assertEquals("Line 7.\n", fileText.get(6));
 }

 @Test
 public void testGetWithFirstLine() {
 assertEquals("This is the first line.\n", fileText.get(0));
 }

 @Test
 public void testGetWithEmptyString() {
 List<String> emptyLines = Collections.emptyList();
 FileText emptyFileText = new FileText(file, emptyLines);
 assertEquals(0, emptyFileText.size());
 }
}