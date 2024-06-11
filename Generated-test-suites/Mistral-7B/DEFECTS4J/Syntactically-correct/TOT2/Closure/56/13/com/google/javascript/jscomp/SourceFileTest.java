import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class SourceFileTest {

  @Test
  public void testGetLine() throws IOException {
    // Test for a regular file
    SourceFile sourceFile = new SourceFile("testFile.js");
    Generator generator = () -> "test Function() {}";
    sourceFile.setCode(generator.getCode());
    String expectedLine = "Function() {";
    String actualLine = sourceFile.getLine(1);
    Assert.assertEquals(expectedLine, actualLine);

    // Test for an empty file
    sourceFile = new SourceFile("emptyFile.js");
    generator = () -> "";
    sourceFile.setCode(generator.getCode());
    String emptyLine = "";
    actualLine = sourceFile.getLine(1);
    Assert.assertEquals(emptyLine, actualLine);

    // Test for a file with an error
    generator = () -> "function test() { throw new Error('Test error'); }";
    sourceFile.setCode(generator.getCode());
    String errorLine = "function test() { throw new Error('Test error'); }";
    try {
      actualLine = sourceFile.getLine(1);
    } catch (IOException e) {
      actualLine = e.getMessage();
    }
    Assert.assertEquals(errorLine, actualLine);
  }

  @Test
  public void testGetRegion() throws IOException {
    // Test for a regular file
    SourceFile sourceFile = new SourceFile("testFile.js");
    Generator generator = () -> "function test1() { }\n" +
            "function test2() { }\n" +
            "function test3() { }";
    sourceFile.setCode(generator.getCode());
    Region expectedRegion = new Region(1, 3, "function test1() { }\n" +
            "function test2() { }\n");
    Region actualRegion = sourceFile.getRegion(3);
    Assert.assertEquals(expectedRegion, actualRegion);

    // Test for a file with an error
    generator = () -> "function test() { throw new Error('Test error'); }";
    sourceFile.setCode(generator.getCode());
    try {
      actualRegion = sourceFile.getRegion(3);
    } catch (IOException e) {
      actualRegion = null;
    }
    Assert.assertNull(actualRegion);
  }

  @Test
  public void testGetLineOffset() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test1() { }\n" +
            "function test2() { }\n";
    sourceFile.setCode(code);
    int[] expectedLineOffsets = {1, 1 + code.split("\n")[0].length() + 1,
            1 + code.split("\n")[0].length() + 1 + code.split("\n")[1].length() + 1};
    int[] actualLineOffsets = sourceFile.lineOffsets;
    Assert.assertArrayEquals(expectedLineOffsets, actualLineOffsets);
  }
}