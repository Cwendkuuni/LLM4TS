import static org.junit.Assert.*;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import com.google.javascript.rhino.Node;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testGetLine() throws IOException {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    SourceFile sourceFile = new SourceFile("Test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertEquals("var x = 1;\n", sourceFile.getLine(1));
    assertEquals("return x + y;\n", sourceFile.getLine(4));
    assertEquals(null, sourceFile.getLine(5));
  }

  @Test
  public void testGetRegion() throws IOException {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    SourceFile sourceFile = new SourceFile("Test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    Region region = sourceFile.getRegion(3);
    assertEquals(3, region.getStartLine());
    assertEquals(3, region.getEndLine());
    assertEquals("  var y = 2;\nreturn x + y;\n", region.getCode());
  }

  @Test
  public void testFromFile() throws IOException {
    String filePath = "Test.js";
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    File file = new File(filePath);
    Files.write(file, code.getBytes(StandardCharsets.UTF_8));

    SourceFile sourceFile = SourceFile.fromFile(filePath);

    assertEquals("function foo() {\n  var x = 1;\n  var y = 2;\n  return x + y;\n}",
        sourceFile.getCode());

    Files.delete(file.toPath());
  }

  @Test
  public void testFromInputStream() throws IOException {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    InputStream inputStream = new ByteArrayInputStream(code.getBytes());
    SourceFile sourceFile = SourceFile.fromInputStream(
        "Test.js", inputStream);

    assertEquals("function foo() {\n  var x = 1;\n  var y = 2;\n  return x + y;\n}",
        sourceFile.getCode());
  }

  @Test
  public void testFromGenerator() {
    String fileName = "Test.js";
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  return x + y;\n" +
        "}";

    Generator generator = () -> code;
    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return null;
      }

      @Override
      public void clearCachedSource() {
        // Do nothing
      }
    };
    sourceFile.setGenerator(generator);

    assertEquals("function foo() {\n  var x = 1;\n  var y = 2;\n  return x + y;\n}",
        sourceFile.getCode());
  }
}

class ByteArrayInputStream extends InputStream {
  private final byte[] buf;
  private int pos = 0;

  public ByteArrayInputStream(byte[] buf) {
    this.buf = buf;
  }

  @Override
  public int read() throws IOException {
    if (pos >= buf.length) {
      return -1;
    }
    return buf[pos++] & 0xff;
  }
}