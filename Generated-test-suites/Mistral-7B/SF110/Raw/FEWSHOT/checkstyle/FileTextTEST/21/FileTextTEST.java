package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FileTextTest {

 @Test
 public void testFileTextConstructionWithFileAndCharset() throws IOException {
 File file = new File("testFile.txt");
 FileText fileText = new FileText(file, "UTF-8");
 assertEquals("testFile.txt", fileText.getFile().getAbsolutePath());
 assertEquals("UTF-8", fileText.getCharset().name());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnsupportedCharset() throws IOException {
 File file = new File("testFile.txt");
 FileText fileText = new FileText(file, "XYZ");
 fail("Should have thrown an IllegalStateException with the message: Unsupported charset: XYZ");
 }

 @Test
 public void testFileTextConstructionWithFileText() {
 FileText originalFileText = new FileText(new File("testFile.txt"), Arrays.asList("Line 1.", "Line 2."));
 FileText copyFileText = new FileText(originalFileText);
 assertEquals(originalFileText.getFile(), copyFileText.getFile());
 assertEquals(originalFileText.getCharset(), copyFileText.getCharset());
 assertArrayEquals(originalFileText.toLinesArray(), copyFileText.toLinesArray());
 }

 @Test
 public void testFileTextConstructionWithFileAndList() throws IOException {
 File file = new File("testFile.txt");
 List<String> lines = Arrays.asList("Line 1.", "Line 2.");
 FileText fileText = FileText.fromLines(file, lines);
 assertEquals(lines.toString(), fileText.toString());
 }

 @Test
 public void testGetFile() {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 assertEquals(new File("testFile.txt"), fileText.getFile());
 }

 @Test
 public void testGetCharset() {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 assertEquals(Charset.forName("UTF-8"), fileText.getCharset());
 }

 @Test
 public void testGetFullText() throws IOException {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 String expectedFullText = Files.toString(new File("testFile.txt"), Charsets.UTF_8);
 assertEquals(expectedFullText, fileText.getFullText());
 }

 @Test
 public void testToLinesArray() {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 String[] lines = fileText.toLinesArray();
 assertEquals(2, lines.length);
 assertEquals("Line 1.", lines[0]);
 assertEquals("Line 2.", lines[1]);
 }

 @Test
 public void testFindLineBreaks() throws IOException {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 int[] lineBreakPositions = fileText.findLineBreaks();
 assertEquals(3, lineBreakPositions.length);
 assertEquals(5, lineBreakPositions[0]); // First line Break
 assertEquals(22, lineBreakPositions[1]); // Second line Break
 assertEquals(fileText.getFullText().length(), lineBreakPositions[2]); // Last line Break
 }

 @Test
 public void testLineColumn() throws IOException {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 int lineNo = 1;
 int columnNo = 5;
 LineColumn expectedLineColumn = new LineColumn(lineNo, columnNo);
 LineColumn actualLineColumn = fileText.lineColumn(5);
 assertEquals(expectedLineColumn, actualLineColumn);
 }

 @Test
 public void testLineColumnOutOfBounds() throws IOException {
 FileText fileText = new FileText(new File("testFile.txt"), "UTF-8");
 LineColumn expectedLineColumn = null;
 LineColumn actualLineColumn = fileText.lineColumn(-1);
 assertEquals(expectedLineColumn, actualLineColumn);
 }
}