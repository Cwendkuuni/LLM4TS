import static com.google.common.truth.Truth.assertThat;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.FileText;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class FileContentsTest {
  @Test
  public void testFileContentsWithValidFileAndLines() {
    String fileName = "SampleFile.java";
    String[] lines = {"package com.example;", "public class SampleClass {}", "// This is a comment"};
    File file = new File(fileName);

    FileContents fileContents = new FileContents(fileName, lines);
    FileText expectedText = FileText.fromLines(file, Arrays.asList(lines));

    FileText actualText = fileContents.getText();
    assertThat(actualText).isEqualTo(expectedText);
    Map<Integer, TextBlock> cppComments = fileContents.getCppComments();
    TextBlock expectedCppComment = new Comment(new String[]{"// This is a comment"}, 0, 0, 0);
    TextBlock actualCppComment = cppComments.get(1);
    assertThat(actualCppComment).isEqualTo(expectedCppComment);
  }
}