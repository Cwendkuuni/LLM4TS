import static org.junit.Assert.*;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JsdtTestLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SourceFileTest {

  @ org.junit.BeforeClass
  public static void setupClass() {
    JsdtTestLoader.setupJar();
  }

  @ org.junit.Test
  public void testGetLineOffset() throws IOException {
    String sourceCode = "class Test {\n" +
        "  static void main(String[] args) {\n" +
        "    System.out.println('Hello, World!');\n" +
        "  }\n" +
        "}";

    SourceFile sourceFile = new SourceFile("Test.java") {
      @Override
      public String getCode() throws IOException {
        return sourceCode;
      }
    };

    for (int i = 1; i <= sourceFile.getNumLines(); i++) {
      int lineOffset = sourceFile.getLineOffset(i);
      assertEquals("Line " + i + " should start at offset " + (i * 11) + ".",
          (i * 11), lineOffset);
    }
  }

  @ org.junit.Test
  public void testGetRegion() throws IOException {
    String sourceCode = "class Test {\n" +
        "  static void main(String[] args) {\n" +
        "    System.out.println('Hello, World!');\n" +
        "  }\n" +
        "}";

    SourceFile sourceFile = new SourceFile("Test.java") {
      @Override
      public String getCode() throws IOException {
        return sourceCode;
      }
    };

    Region region = sourceFile.getRegion(5);

    String expectedRegion = "static void main(String[] args) {\n" +
        "  System.out.println('Hello, World!');\n";

    assertEquals("Unexpected region.", expectedRegion, region.getCode());
  }

  @ org.junit.Test
  public void testFromFile() throws IOException {
    String sourceCode = "class Test {\n" +
        "  static void main(String[] args) {\n" +
        "    System.out.println('Hello, World!');\n" +
        "  }\n" +
        "}";

    String fileName = "Test.java";
    File file = new File(System.getProperty("user.dir"), fileName);
    Files.write(file, sourceCode.getBytes(StandardCharsets.UTF_8));

    SourceFile sourceFile = SourceFile.fromFile(fileName);
    String code = sourceFile.getCode();

    assertEquals("Unexpected source code.", sourceCode, code);
    Files.delete(file);
  }
}