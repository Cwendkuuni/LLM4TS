package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FileTextTest {

 @Test
 public void testFileTextConstructionWithFileAndCharset() throws IOException {
 File file = new File("testFile.txt");
 FileText fileText = new FileText(file, "UTF-8");
 assertEquals("testFile.txt", fileText.getFile().getPath());
 assertEquals("UTF-8", fileText.getCharset().name());
 String fullText = fileText.getFullText();
 assertNotNull(fullText);
 String[] lines = fileText.toLinesArray();
 assertEquals(5, lines.length);
 }

 @Test
 public void testFileTextConstructionWithFileText() {
 FileText fileText1 = new FileText(new File("testFile.txt"), Arrays.asList("Line 1", "Line 2", "Line 3", "Line 4", "Line 5"));
 FileText fileText2 = new FileText(fileText1);
 assertEquals(fileText1.getFile(), fileText2.getFile());
 assertEquals(fileText1.getCharset(), fileText2.getCharset());
 assertEquals(fileText1.fullText, fileText2.fullText);
 assertArrayEquals(fileText1.lines, fileText2.lines);
 }

 @Test
 public void testFileTextConstructionWithFileAndListOfLines() throws IOException {
 File file = new File("testFile.txt");
 List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3", "Line 4", "Line 5");
 FileText fileText = new FileText(file, lines);
 assertEquals(file.getPath(), fileText.getFile().getPath());
 assertArrayEquals(lines.toArray(new String[0]), fileText.lines);
 }

 @Test
 public void testFromLines() throws IOException {
 File file = new File("testFile.txt");
 List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3", "Line 4", "Line 5");
 FileText fileText1 = new FileText(file, lines);
 FileText fileText2 = FileText.fromLines(file, lines);
 assertEquals(fileText1, fileText2);
 }

 @Test(expected = FileNotFoundException.class)
 public void testThrowsFileNotFoundException() throws IOException {
 File file = new File("notExistingFile.txt");
 new FileText(file, "UTF-8");
 }

 @Test(expected = UnsupportedCharsetException.class)
 public void testThrowsUnsupportedCharsetException() throws IOException {
 File file = new File("testFile.txt");
 new FileText(file, "X-123456");
 }

 @Test
 public void testFindLineBreaks() {
 FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line 1", "Line 2", "Line 3", "Line 4", "Line 5"));
 int[] lineBreaks = fileText.findLineBreaks();
 int[] expectedLineBreaks = {0, 7, 17, 27, 42};
 assertArrayEquals(expectedLineBreaks, lineBreaks);
 }

 @Test
 public void testLineColumn() {
 FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line 1\nLine 1.1", "Line 2", "Line 3\nLine 3.1", "Line 4"));
 int pos = 17;
 LineColumn lineColumn = fileText.lineColumn(pos);
 int expectedLineNo = 3;
 int expectedColumnNo = 1;
 assertEquals(expectedLineNo, lineColumn.lineNumber);
 assertEquals(expectedColumnNo, lineColumn.columnNumber);
 }

 @Test
 public void testToString() {
 FileText fileText = new FileText(new File("testFile.txt"), Arrays.asList("Line 1", "Line 2", "Line 3", "Line 4", "Line 5"));
 String expected = "FileText[size=5]\nLine 1\nLine 2\nLine 3\nLine 4\nLine 5\n";
 assertEquals(expected, fileText.toString());
 }
}