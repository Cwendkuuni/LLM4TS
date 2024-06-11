import org.junit.Test;
import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.FileContents;

public class FileContentsTest {
  @Test
  public void testFileContents() {
    String filename = "TestFile.java";
    String[] lines = {"package com.test;", "public class TestClass {", "  // This is a comment", "  int x = 5;", "}"};
    FileContents fileContents = new FileContents(filename, lines);
    List<TextBlock> cComments = fileContents.getCComments().asList();
    TextBlock expectedCppComment = new TextBlock("// This is a comment", 0, 14, 1, 14);
    TextBlock expectedCComment = new TextBlock(
        "/**\n" +
        " * This is a Javadoc comment\n" +
        " */",
        0, 5, 3, 23
    );
    TextBlock actualCppComment = fileContents.getCppComments().asList().get(0);
    TextBlock actualCComment = fileContents.getJavadocBefore(2).get(0);
    assertEquals("Unexpected C comment", expectedCComment, actualCComment);
    assertEquals("Unexpected CPP comment", expectedCppComment, actualCppComment);
  }
}