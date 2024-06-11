
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {

  @Test
  public void testFileTextConstruction() throws IOException {
    // Test for FileText(File file, String charsetName) constructor
    String testFilePath = "testFile.txt";
    File testFile = new File(testFilePath);
    String charsetName = "UTF-8";
    FileText fileText = new FileText(testFile, charsetName);

    // Check file properties
    Assert.assertEquals(testFile, fileText.getFile());
    Assert.assertEquals(charsetName, fileText.getCharset().name());

    // Check contents of the FileText
    String expectedFirstLine = "This is the first line.";
    String expectedLastLine = "This is the last line.";
    String[] expectedLines = new String[] { expectedFirstLine, expectedLastLine };
    String[] actualLines = fileText.toLinesArray();

    // Compare expected and actual lines
    Assert.assertArrayEquals(expectedLines, actualLines);

    // Test getting a single line
    String actualLine = fileText.get(0);
    Assert.assertEquals(expectedFirstLine, actualLine);
  }

  @Test
  public void testFileTextCopy() {
    // Test for FileText(FileText fileText) constructor
    String testFilePath = "testFile.txt";
    File testFile = new File(testFilePath);
    String charsetName = "UTF-8";

    // Create the original FileText
    FileText originalFileText = new FileText(testFile, charsetName);

    // Copy the FileText
    FileText copyFileText = new FileText(originalFileText);

    // Check that the copies have the same properties
    Assert.assertEquals(originalFileText.getFile(), copyFileText.getFile());
    Assert.assertEquals(originalFileText.getCharset(), copyFileText.getCharset());
    Assert.assertArrayEquals(originalFileText.toLinesArray(), copyFileText.toLinesArray());
  }

  @Test
  public void testFileTextFromLines() {
    // Test for FileText(File file, List<String> lines) private constructor
    String testFilePath = "testFile.txt";
    File testFile = new File(testFilePath);
    String charsetName = "UTF-8";

    // Create a List of lines
    List<String> lines = Arrays.asList(
      "This is the first line.",
      "This is the second line.",
      "This is the last line."
    );

    // Create a FileText from the lines
    FileText fileText = FileText.fromLines(testFile, lines);

    // Check that the FileText has the same properties as the original FileText
    File originalFile = testFile;
    String originalCharsetName = charsetName;
    FileText originalFileText = new FileText(originalFile, originalCharsetName);
    Assert.assertEquals(originalFileText.getFile(), fileText.getFile());
    Assert.assertEquals(originalCharsetName, fileText.getCharset().name());
    String[] expectedLines = originalFileText.toLinesArray();
    Assert.assertArrayEquals(expectedLines, fileText.toLinesArray());
  }
}