import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.FileText;
import com.puppycrawl.tools.checkstyle.api.LineColumn;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class FileTextTest {

 private FileText fileText;

 @Before
 public void setUp() throws IOException {
 String filePath = "src/test/resources/FileTextTest.java";
 File file = new File(filePath);
 fileText = new FileText(file, StandardCharsets.UTF_8);
 }

 @Test
 public void testCreateFileText() {
 String expectedText = "package com.example.test;\n" +
         "public class TestClass {\n" +
         "  public void testMethod() {\n" +
         "    int a = 5;\n" +
         "    int b = 10;\n" +
         "    int sum = a + b;\n" +
         "    System.out.println(\"Sum: \" + sum);\n" +
         "  }\n" +
         "}";
 assertEquals(expectedText, fileText.getFullText());
 }

 @Test
 public void testGetLine() {
 String expectedLine = "int a = 5;\n";
 assertEquals(expectedLine, fileText.get(0));
 }

 @Test
 public void testSize() {
 int expectedSize = 4;
 assertEquals(expectedSize, fileText.size());
 }

 @Test
 public void testFindLineBreaks() {
 int[] lineBreaks = fileText.findLineBreaks();
 int expectedLineBreaksSize = 5;
 int[] expectedLineBreakPositions = {0, 11, 29, 43, 53};
 assertArrayEquals(expectedLineBreakPositions, lineBreaks);
 }

 @Test
 public void testLineColumn() {
 int pos = 20;
 LineColumn expectedLineColumn = new LineColumn(2, 5);
 LineColumn actualLineColumn = fileText.lineColumn(pos);
 assertEquals(expectedLineColumn, actualLineColumn);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testLineOutOfBounds() {
 int pos = fileText.size() + 1;
 fileText.lineColumn(pos);
 }

 @Test
 public void testClone() {
 FileText clonedFileText = fileText.clone();
 assertEquals(fileText.getFile(), clonedFileText.getFile());
 assertEquals(fileText.getCharset(), clonedFileText.getCharset());
 assertArrayEquals(fileText.lines, clonedFileText.lines);
 assertNull(clonedFileText.lineBreaks);
 }

 @Test
 public void testCreateFileTextFromLines() {
 List<String> lines = Lists.newArrayList(
         "package com.example.test;\n",
         "public class TestClass {\n",
         "  public void testMethod() {\n",
         "    int a = 5;\n",
         "    int b = 10;\n",
         "    int sum = a + b;\n",
         "    System.out.println(\"Sum: \" + sum);\n",
         "  }\n",
         "}");
 FileText linesFileText = FileText.fromLines(new File("src/test/resources/FileTextTest.java"), lines);
 assertEquals("Sum: 15", linesFileText.get(4).substring(linesFileText.get(4).lastIndexOf(":") + 1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCreateFileTextFromNonList() {
 FileText nonListFileText = FileText.fromLines(new File("src/test/resources/FileTextTest.java"), "Test class content");
 }
}