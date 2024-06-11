import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class SourceFileTest {

  @Test
  public void testConstructorAndGetName() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    Assert.assertEquals("testFile.js", sourceFile.getName());
  }

  @Test
  public void testGetLineOffset() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";
    sourceFile.setCode(code);

    Assert.assertEquals(1, sourceFile.getLineOffset(1));
    Assert.assertEquals(5, sourceFile.getLineOffset(6));
    Assert.assertEquals(-1, sourceFile.getLineOffset(10));
  }

  @Test
  public void testGetCode() throws IOException {
    String fileName = "testFile.js";
    String code = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";

    SourceFile sourceFile = new SourceFile(fileName);
    SourceFile sourceFileWithCode = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    Assert.assertNull(sourceFile.getCode());
    Assert.assertEquals(code, sourceFileWithCode.getCode());
  }

  @Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";
    sourceFile.setCode(code);

    Assert.assertEquals("function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}", sourceFile.getLine(1));
    Assert.assertEquals("  var b = 2;\n" +
        "  return a + b;\n", sourceFile.getLine(5));
    Assert.assertNull(sourceFile.getLine(10));
  }

  @Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";
    sourceFile.setCode(code);

    Region region = sourceFile.getRegion(5);
    Assert.assertEquals("  var b = 2;\n" +
        "  return a + b;\n", region.getCode());
    Assert.assertEquals(3, region.getLength());
  }

  @Test
  public void testFromCode() {
    String fileName = "testFile.js";
    String code = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    Assert.assertEquals("testFile.js", sourceFile.getName());
    Assert.assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testFromFile() throws IOException {
    String fileName = "testFile.js";
    String expectedCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";

    File file = new File(fileName);
    Files.write(file, expectedCode.getBytes(StandardCharsets.UTF_8));

    SourceFile sourceFile = SourceFile.fromFile(fileName);
    Assert.assertEquals("testFile.js", sourceFile.getName());
    Assert.assertEquals(expectedCode, sourceFile.getCode());

    Files.delete(file);
  }

  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "testFile.js";
    String code = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";

    InputStream inputStream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);

    Assert.assertEquals("testFile.js", sourceFile.getName());
    Assert.assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testFromGenerator() {
    String fileName = "testFile.js";
    String expectedCode = "function test() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";

    class TestGenerator implements SourceFile.Generator {
      @Override
      public String getCode() {
        return expectedCode;
      }
    }

    SourceFile sourceFile = SourceFile.fromGenerator(fileName, new TestGenerator());

    Assert.assertEquals("testFile.js", sourceFile.getName());
    Assert.assertEquals(expectedCode, sourceFile.getCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorEmptyFileName() {
    new SourceFile("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorNullFileName() {
    new SourceFile(null);
  }

  public static class Region {
    private int startLine;
    private int endLine;
    private String code;

    public Region(int startLine, int endLine, String code) {
      this.startLine = startLine;
      this.endLine = endLine;
      this.code = code;
    }

    public int getStartLine() {
      return startLine;
    }

    public int getEndLine() {
      return endLine;
    }

    public String getCode() {
      return code;
    }

    public int getLength() {
      return endLine - startLine + 1;
    }
  }
}