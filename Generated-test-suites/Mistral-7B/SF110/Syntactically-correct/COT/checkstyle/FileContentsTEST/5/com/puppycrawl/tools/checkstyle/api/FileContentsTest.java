package com.example.junit;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;

public class FileContentsTest {

 private static final String TEST_FILE = "testFile.java";
 private static final String[] TEST_LINES = {"package com.example.junit;", "import static org.junit.Assert.*;", "public class FileContentsTest {", "// This is a single line comment", "// Testing getFileName()", "// Testing getText()", "// Testing getLines()", "// Testing getLine()", "// Testing hasIntersectionWithComment()", "// Testing inPackageInfo()", "}", ""};

 @Test
 public void testFileContents() {
 FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
 assertEquals(TEST_FILE, fileContents.getFileName());
 assertNotNull(fileContents.getText());
 assertArrayEquals(TEST_LINES, fileContents.getLines());
 assertNotNull(fileContents.getLine(0));
 assertFalse(fileContents.lineIsBlank(0));
 assertFalse(fileContents.lineIsComment(0));
 assertTrue(fileContents.lineIsComment(1));
 assertTrue(fileContents.hasIntersectionWithComment(1, 0, 1, TEST_LINES[1].length()));
 assertFalse(fileContents.inPackageInfo());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFileContentsInvalidLines() {
 FileContents fileContents = new FileContents(TEST_FILE, new String[0]);
 }

 @Test
 public void testFileContentsEmptyFile() {
 FileContents fileContents = new FileContents(TEST_FILE);
 assertEquals(TEST_FILE, fileContents.getFileName());
 assertNotNull(fileContents.getText());
 assertEquals(0, fileContents.getLines().length);
 }

 @Test
 public void testGetCppComments() {
 FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
 ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
 assertEquals(1, cppComments.size());
 TextBlock expectedCppComment = new Comment(new String[]{"// Testing getCppComments()"}, 0, 1, TEST_LINES[1].length());
 assertEquals(expectedCppComment, cppComments.get(1));
 }

 @Test
 public void testGetCComments() {
 FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
 ImmutableMap<Integer, List<TextBlock>> cComments = fileContents.getCComments();
 assertEquals(1, cComments.size());
 List<TextBlock> expectedCComments = Arrays.asList(new Comment(new String[]{"/**"}, 0, 0, TEST_LINES[0].length() - 10));
 assertEquals(expectedCComments, cComments.get(0));
 }

 @Test
 public void testExtractCComment() {
 FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
 String[] expectedExtractedCComment = new String[]{"package com.example.junit;", "import static org.junit.Assert.*;", "public class FileContentsTest {", "/**", " * Testing getCppComments()", " */", "// This is a single line comment", "// Testing getJavadocBefore()", "// Testing getText()", "// Testing getLines()", "// Testing getLine()", "// Testing hasIntersectionWithComment()", "// Testing inPackageInfo()", "}", "/**", " * @author Your Name", " */"};
 String[] actualExtractedCComment = fileContents.extractCComment(1, 0, 2, TEST_LINES[1].length());
 assertArrayEquals(expectedExtractedCComment, actualExtractedCComment);
 }

 @Test
 public void testGetJavadocBefore() {
 FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
 TextBlock expectedJavadocBefore = new Comment(new String[]{"/**"}, 0, 0, TEST_LINES[0].length() - 10);
 TextBlock actualJavadocBefore = fileContents.getJavadocBefore(1);
 assertEquals(expectedJavadocBefore, actualJavadocBefore);
 }
}