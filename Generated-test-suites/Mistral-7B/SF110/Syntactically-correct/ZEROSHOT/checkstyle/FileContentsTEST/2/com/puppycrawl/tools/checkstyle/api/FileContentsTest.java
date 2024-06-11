package com.example.junit;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.grammars.java.*;
import com.google.common.collect.*;

public class FileContentsTest {

 private static final String TEST_FILE = "TestFile.java";

 @Test
 public void testFileContents() {
 FileContents fileContents = new FileContents(TEST_FILE, "package com.example.junit;", "public class FileContentsTest {", "}");
 assertEquals(TEST_FILE, fileContents.getFileName());
 assertTrue(fileContents instanceof FileContents);
 assertNotNull(fileContents.getText());
 assertNotNull(fileContents.getCComments());
 assertNotNull(fileContents.getCppComments());
 assertNotNull(fileContents.getJavadocComments());

 List<TextBlock> cComments = fileContents.getCComments().get(TEST_FILE);
 assertTrue(cComments.isEmpty());

 List<TextBlock> cppComments = fileContents.getCppComments().get(TEST_FILE);
 assertTrue(cppComments.isEmpty());

 TextBlock javadocComment = fileContents.getJavadocBefore(0);
 assertNull(javadocComment);

 String[] lines = fileContents.getLines();
 assertArrayEquals(new String[]{"package com.example.junit;", "public class FileContentsTest {", "}", ""}, lines);

 String line0 = fileContents.getLine(0);
 assertEquals("package com.example.junit;", line0);

 String line1 = fileContents.getLine(1);
 assertEquals("public class FileContentsTest {", line1);

 String line2 = fileContents.getLine(2);
 assertEquals("}", line2);

 String line3 = fileContents.getLine(3);
 assertNull(line3);

 FileText text = fileContents.getText();
 assertEquals(lines.length, text.size());
 for (int i = 0; i < lines.length; i++) {
 assertEquals(lines[i], text.get(i));
 }
 }

 @Test
 public void testFileContentsWithComment() {
 String testFilePath = "TestFileWithComment.java";
 String testContent = "/**\n * Test File with Javadoc comment.\n */\n" +
         "public class TestFileWithComment {\n" +
         "  /**\n * Test method with Javadoc comment.\n */\n" +
         "  public void testMethod() {\n" +
         "  }\n" +
         "}";

 File textFile = new File(testFilePath);
 List<String> lines = Arrays.asList(testContent.split("\\r?\\n"));

 FileContents fileContents = new FileContents(testFilePath, lines.toArray(new String[0]));
 assertEquals(testFilePath, fileContents.getFileName());
 assertTrue(fileContents instanceof FileContents);
 assertNotNull(fileContents.getText());
 assertNotNull(fileContents.getCComments());
 assertNotNull(fileContents.getCppComments());
 assertNotNull(fileContents.getJavadocComments());

 List<TextBlock> cComments = fileContents.getCComments().get(testFilePath);
 assertEquals(1, cComments.size());
 TextBlock cComment = cComments.get(0);
 assertNotNull(cComment);
 assertNotNull(cComment.getText());
 assertEquals("/**\n * Test File with Javadoc comment.\n */", cComment.getText());

 List<TextBlock> cppComments = fileContents.getCppComments().get(testFilePath);
 assertEquals(0, cppComments.size());

 TextBlock javadocComment = fileContents.getJavadocBefore(0);
 assertNotNull(javadocComment);
 assertNotNull(javadocComment.getText());
 assertEquals("/**\n * Test File with Javadoc comment.\n */", javadocComment.getText());

 TextBlock javadocTestMethodComment = fileContents.getJavadocBefore(1);
 assertNotNull(javadocTestMethodComment);
 assertNotNull(javadocTestMethodComment.getText());
 assertEquals("/**\n * Test method with Javadoc comment.\n */", javadocTestMethodComment.getText());
 }

 @Test
 public void testFileContentsWithCComment() {
 String testFilePath = "TestFileWithCComment.java";
 String testContent = "/**\n * Test File with Javadoc comment.\n */\n" +
         "/*\n * Test C comment.\n */\n" +
         "public class TestFileWithCComment {\n" +
         "}";

 File textFile = new File(testFilePath);
 List<String> lines = Arrays.asList(testContent.split("\\r?\\n"));

 FileContents fileContents = new FileContents(testFilePath, lines.toArray(new String[0]));
 assertEquals(testFilePath, fileContents.getFileName());
 assertTrue(fileContents instanceof FileContents);
 assertNotNull(fileContents.getText());
 assertNotNull(fileContents.getCComments());
 assertNotNull(fileContents.getCppComments());
 assertNotNull(fileContents.getJavadocComments());

 List<TextBlock> cComments = fileContents.getCComments().get(testFilePath);
 assertEquals(1, cComments.size());
 TextBlock cComment = cComments.get(0);
 assertNotNull(cComment);
 assertNotNull(cComment.getText());
 assertEquals("/*\n * Test C comment.\n */", cComment.getText());

 List<TextBlock> cppComments = fileContents.getCppComments().get(testFilePath);
 assertEquals(0, cppComments.size());

 TextBlock javadocComment = fileContents.getJavadocBefore(0);
 assertNotNull(javadocComment);
 assertNotNull(javadocComment.getText());
 assertEquals("/**\n * Test File with Javadoc comment.\n */", javadocComment.getText());
 }

 @Test
 public void testFileContentsWithMultipleCComments() {
 String testFilePath = "TestFileWithMultipleCComments.java";
 String testContent = "/**\n * Test File with Javadoc comment.\n */\n" +
         "/*\n * Test C comment 1.\n */\n" +
         "/*\n * Test C comment 2.\n */\n" +
         "public class TestFileWithMultipleCComments {\n" +
         "}";

 File textFile = new File(testFilePath);
 List<String> lines = Arrays.asList(testContent.split("\\r?\\n"));

 FileContents fileContents = new FileContents(testFilePath, lines.toArray(new String[0]));
 assertEquals(testFilePath, fileContents.getFileName());
 assertTrue(fileContents instanceof FileContents);
 assertNotNull(fileContents.getText());
 assertNotNull(fileContents.getCComments());
 assertNotNull(fileContents.getCppComments());
 assertNotNull(fileContents.getJavadocComments());

 List<TextBlock> cComments = fileContents.getCComments().get(testFilePath);
 assertEquals(2, cComments.size());
 TextBlock cComment1 = cComments.get(0);
 TextBlock cComment2 = cComments.get(1);
 assertNotNull(cComment1);
 assertNotNull(cComment1.getText());
 assertEquals("/*\n * Test C comment 1.\n */", cComment1.getText());
 assertNotNull(cComment2);
 assertNotNull(cComment2.getText());
 assertEquals("/*\n * Test C comment 2.\n */", cComment2.getText());

 List<TextBlock> cppComments = fileContents.getCppComments().get(testFilePath);
 assertEquals(0, cppComments.size());

 TextBlock javadocComment = fileContents.getJavadocBefore(0);
 assertNotNull(javadocComment);
 assertNotNull(javadocComment.getText());
 assertEquals("/**\n * Test File with Javadoc comment.\n */", javadocComment.getText());
 }

 @Test
 public void testFileContentsWithMultipleCppComments() {
 String testFilePath = "TestFileWithMultipleCppComments.java";
 String testContent = "/**\n * Test File with Javadoc comment.\n */\n" +
         "// Test single line CPP comment 1.\n" +
         "// Test single line CPP comment 2.\n" +
         "public class TestFileWithMultipleCppComments {\n" +
         "}";

 File textFile = new File(testFilePath);
 List<String> lines = Arrays.asList(testContent.split("\\r?\\n"));

 FileContents fileContents = new FileContents(testFilePath, lines.toArray(new String[0]));
 assertEquals(testFilePath, fileContents.getFileName());
 assertTrue(fileContents instanceof FileContents);
 assertNotNull(fileContents.getText());
 assertNotNull(fileContents.getCComments());
 assertNotNull(fileContents.getCppComments());
 assertNotNull(fileContents.getJavadocComments());

 List<TextBlock> cComments = fileContents.getCComments().get(testFilePath);
 assertEquals(0, cComments.size());

 List<TextBlock> cppComments = fileContents.getCppComments().get(testFilePath);
 assertEquals(2, cppComments.size());
 TextBlock cppComment1 = cppComments.get(0);
 TextBlock cppComment2 = cppComments.get(1);
 assertNotNull(cppComment1);
 assertNotNull(cppComment1.getText());
 assertEquals("// Test single line CPP comment 1.", cppComment1.getText());
 assertNotNull(cppComment2);
 assertNotNull(cppComment2.getText());
 assertEquals("// Test single line CPP comment 2.", cppComment2.getText());

 List<TextBlock> javadocComments = fileContents.getJavadocComments().get(testFilePath);
 assertEquals(0, javadocComments.size());
 }
}