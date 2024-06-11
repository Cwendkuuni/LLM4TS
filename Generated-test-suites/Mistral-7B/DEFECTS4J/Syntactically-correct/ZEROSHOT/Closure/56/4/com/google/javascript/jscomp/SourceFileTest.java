import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.source.testutils.SourceFileTestUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testSourceFileBasic() throws IOException {
    String fileName = "TestSourceFile.java";
    SourceFile sourceFile = SourceFileTestUtil.newSourceFile(fileName);

    assertEquals(fileName, sourceFile.getName());
    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals("TestSourceFile.java", sourceFile.getOriginalPath());

    String code = "public class TestSourceFile {\n" +
        "  public void test() {\n" +
        "  }\n" +
        "}";
    sourceFile.setCode(code);
    assertEquals(code, sourceFile.getCode());

    sourceFile.clearCachedSource();
    assertEquals(null, sourceFile.getCode());

    sourceFile.setOriginalPath("path/to/TestSourceFile.java");
    assertEquals("path/to/TestSourceFile.java", sourceFile.getOriginalPath());

    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileNullName() {
    new SourceFile(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileEmptyName() {
    new SourceFile("");
  }

  @Test
  public void testSourceFileFindLineOffsets() throws IOException {
    String fileName = "TestSourceFile.java";
    SourceFile sourceFile = SourceFileTestUtil.newSourceFile(fileName);

    // Set code manually to ensure file is read only once.
    String code = "public class TestSourceFile {\n" +
        "  public void test() {\n" +
        "  }\n" +
        "}";
    sourceFile.setCode(code);

    int[] lineOffsets = sourceFile.getLineOffsets();
    assertEquals(3, lineOffsets.length);
    assertEquals(0, lineOffsets[0]);
    assertEquals(22, lineOffsets[1]);
    assertEquals(44, lineOffsets[2]);
  }

  @Test
  public void testSourceFileGetLine() throws IOException {
    String fileName = "TestSourceFile.java";
    SourceFile sourceFile = SourceFileTestUtil.newSourceFile(fileName);

    // Set code manually to ensure file is read only once.
    String code = "public class TestSourceFile {\n" +
        "  public void test() {\n" +
        "    int a = 1;\n" +
        "    int b = 2;\n" +
        "    int c = a + b;\n" +
        "  }\n" +
        "}";
    sourceFile.setCode(code);

    String line1 = sourceFile.getLine(1);
    assertEquals("public class TestSourceFile {\n", line1);

    String line3 = sourceFile.getLine(3);
    assertEquals("    int a = 1;\n", line3);

    String line6 = sourceFile.getLine(6);
    assertNull(line6);
  }

  @Test
  public void testSourceFileGetRegion() throws IOException {
    String fileName = "TestSourceFile.java";
    SourceFile sourceFile = SourceFileTestUtil.newSourceFile(fileName);

    // Set code manually to ensure file is read only once.
    String code = "public class TestSourceFile {\n" +
        "  public void test() {\n" +
        "    int a = 1;\n" +
        "    int b = 2;\n" +
        "    int c = a + b;\n" +
        "  }\n" +
        "}";
    sourceFile.setCode(code);

    Region region = sourceFile.getRegion(5);
    String expectedRegion = "    int a = 1;\n" +
        "    int b = 2;\n";
    assertEquals(5, region.getStartLineNumber());
    assertEquals(5, region.getEndLineNumber());
    assertEquals(expectedRegion, region.getSource());
  }

  @Test
  public void testSourceFileFromFile() throws IOException {
    String fileName = "TestSourceFile.java";
    Path filePath = Files.createTempFile(null, null);
    Files.write(filePath, "public class TestSourceFile {\n" +
        "  public void test() {\n" +
        "  }\n" +
        "}".getBytes(Charset.forName("UTF-8")));

    SourceFile sourceFile = SourceFile.fromFile(fileName);
    assertEquals("TestSourceFile.java", sourceFile.getName());
    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals("TestSourceFile.java", sourceFile.getOriginalPath());

    String code = sourceFile.getCode();
    assertEquals("public class TestSourceFile {\n" +
        "  public void test() {\n" +
        "  }\n" +
        "}", code);
  }
}