package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class FileTextTest {

 @Test
 public void testFileTextConstructionThrowsIOExceptionWhenFileDoesNotExist() {
 File file = new File("non-existing-file.txt");
 assertThrows("FileNotFoundException", () -> new FileText(file, StandardCharsets.UTF_8.name()));
 }

 @Test
 public void testFileTextConstructionWithFileAndCharset() throws IOException {
 File file = new File("test-file.txt");
 CharSequence expectedContent = "Line 1\nLine 2\nLine 3";
 try (Reader reader = new BufferedReader(new FileReader(file))) {
 StringBuilder contentBuilder = new StringBuilder();
 String line;
 while ((line = reader.readLine()) != null) {
 contentBuilder.append(line).append("\n");
 }
 expectedContent = contentBuilder.toString();
 }
 FileText fileText = new FileText(file, StandardCharsets.UTF_8.name());
 assertEquals(expectedContent, fileText.getFullText());
 }

 @Test
 public void testFileTextConstructionWithFileText() {
 FileText fileText = new FileText(new FileText("test-file.txt"));
 assertEquals(fileText.getFile(), new File("test-file.txt"));
 }

 @Test
 public void testFileTextConstructionWithFileAndLines() {
 File file = new File("test-file.txt");
 List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
 FileText fileText = new FileText(file, lines);
 assertEquals(fileText.getFile(), file);
 assertEquals(fileText.size(), lines.size());
 for (int i = 0; i < lines.size(); i++) {
 assertEquals(lines.get(i), fileText.get(i));
 }
 }

 @Test
 public void testLineColumn() {
 FileText fileText = new FileText(new File("test-file.txt"), Arrays.asList("Line 1", "Line 2", "Line 3"));
 LineColumn lineColumn = fileText.lineColumn(11);
 assertEquals(3, lineColumn.lineNumber);
 assertEquals(8, lineColumn.columnNumber);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testLineColumnWhenPositionIsOutsideTheFileContent() {
 FileText fileText = new FileText(new File("test-file.txt"), Arrays.asList("Line 1", "Line 2", "Line 3"));
 fileText.lineColumn(-1);
 }
}