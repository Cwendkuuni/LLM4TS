
package com.puppycrawl.tools.checkstyle.api;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class FileTextTest {
  @Test
  public void testFileTextConstruction() throws IOException {
    String testFilePath = "test/resources/testFile.txt";
    File file = new File(testFilePath);
    Charset charset = Charset.forName("UTF-8");
    FileText fileText = new FileText(file, charset.getName());

    String expectedFirstLine = "This is the first line.";
    String expectedSecondLine = "This is the second line.";
    String[] expectedLines = {expectedFirstLine, expectedSecondLine};
    String[] actualLines = fileText.toLinesArray();

    Assert.assertArrayEquals(expectedLines, actualLines);
  }

  @Test
  public void testGetFile() throws IOException {
    String testFilePath = "test/resources/testFile.txt";
    File file = new File(testFilePath);
    Charset charset = Charset.forName("UTF-8");
    FileText fileText = new FileText(file, charset.getName());

    File expectedFile = file;
    File actualFile = fileText.getFile();

    Assert.assertEquals(expectedFile, actualFile);
  }

  @Test
  public void testFindLineBreaks() throws IOException {
    String testFilePath1 = "test/resources/testFile1.txt";
    String testFilePath2 = "test/resources/testFile2.txt";
    String testFilePath3 = "test/resources/testFile3.txt";

    File file1 = new File(testFilePath1);
    File file2 = new File(testFilePath2);
    File file3 = new File(testFilePath3);

    Charset charset = StandardCharsets.UTF_8;

    FileText fileText1 = new FileText(file1, charset.getName());
    FileText fileText2 = new FileText(file2, charset.getName());
    FileText fileText3 = new FileText(file3, charset.getName());

    int[] expectedLineBreaks1 = {13, 26, 40};
    int[] expectedLineBreaks2 = {0};
    int[] expectedLineBreaks3 = {-1};

    int[] actualLineBreaks1 = fileText1.findLineBreaks();
    int[] actualLineBreaks2 = fileText2.findLineBreaks();
    int[] actualLineBreaks3 = fileText3.findLineBreaks();

    Assert.assertArrayEquals(expectedLineBreaks1, actualLineBreaks1);
    Assert.assertArrayEquals(expectedLineBreaks2, actualLineBreaks2);
    Assert.assertArrayEquals(expectedLineBreaks3, actualLineBreaks3);
  }
}