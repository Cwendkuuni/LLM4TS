package com.example.test;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FileContentsTest {

 private static final String TEST_FILE = "TestFile.java";
 private static final String[] TEST_FILE_CONTENTS = new String[]{"public class TestFile {",
         " // This is a single line comment",
         " /**",
         " * This is a Javadoc comment",
         " */",
         " int main(int a, int b) {",
         "}",
         "};"};

 @Test
 public void testFileContents() {
 FileContents fileContents = new FileContents(TEST_FILE, TEST_FILE_CONTENTS);
 assertEquals(TEST_FILE, fileContents.getFileName());
 assertEquals(Arrays.asList(TEST_FILE_CONTENTS), fileContents.getLines());

 TextBlock javadocComment = fileContents.getJavadocBefore(2);
 assertNotNull(javadocComment);
 assertEquals("* This is a Javadoc comment", javadocComment.getText());

 TextBlock cppComment = fileContents.getCppComments().get(3);
 assertNull(cppComment);

 List<TextBlock> clangComments = fileContents.getCComments().get(3);
 assertTrue(clangComments.isEmpty());

 int startLineNo = 1;
 int startColNo = 1;
 int endLineNo = 2;
 int endColNo = 30;
 assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, 1, endColNo));
 assertFalse(fileContents.hasIntersectionWithComment(1, startColNo, endLineNo, endColNo));
 assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo + 1));
 assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo - 1, endColNo));

 File testFile = new File(TEST_FILE);
 FileContents fileContents2 = new FileContents(testFile);
 assertEquals(TEST_FILE, fileContents2.getFileName());
 assertEquals( Arrays.asList(FileIO.readAllLines(testFile, Charset.forName("UTF-8"))), fileContents2.getLines() );
 }
}