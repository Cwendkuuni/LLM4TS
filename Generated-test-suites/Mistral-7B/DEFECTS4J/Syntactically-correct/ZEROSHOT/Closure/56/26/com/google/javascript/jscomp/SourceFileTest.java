import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.SerializableInputStream;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import com.google.javascript.jscomp.SourceFile.Preloaded;
import com.google.javascript.jscomp.SourceFile.PreloadedWithOriginalPath;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class SourceFileTest {

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @org.junit.Before
  public void setUp() throws Exception {
    File tempFile = File.createTempFile("test", ".js");
    try (OutputStreamWriter writer = new OutputStreamWriter(new FileWriter(tempFile))) {
      writer.write("function foo() {}");
    }

    SourceFile underTest = SourceFile.fromFile(tempFile.getPath());
    assertSame(tempFile.getPath(), underTest.getName());
    assertEquals("function foo() {}\n", underTest.getCode());
  }

  @org.junit.Test
  public void testGetLine() throws IOException {
    SourceFile underTest = SourceFile.fromFile("test.js");
    assertEquals("function foo() {}\n", underTest.getCode());
    assertEquals("function foo() {", underTest.getLine(1));
    assertEquals("  }\n", underTest.getLine(3));
    assertNull(underTest.getLine(0));
    assertNull(underTest.getLine(5));
  }

  @org.junit.Test
  public void testGetRegion() throws IOException {
    SourceFile underTest = SourceFile.fromFile("test.js");
    assertEquals("function foo() {}\n", underTest.getCode());
    String expected = "function foo() {\n  }\n";
    Region region = underTest.getRegion(1);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(1, region.getEndLineNumber());
    assertEquals(expected, region.getSourceCode());
  }

  @org.junit.Test
  public void testGetLineOffset() throws IOException {
    SourceFile underTest = SourceFile.fromFile("test.js");
    assertEquals("function foo() {}\n", underTest.getCode());
    int line = 1;
    int offset = 0;
    int expected = 0;
    int lineLength = "function foo() ".length();

    for (int i = 0; i < line; i++) {
      offset += lineLength;
      line++;
      lineLength += System.lineSeparator().length();
      lineLength += "  ".length();
    }

    int actual = underTest.getLineOffset(line);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFromGenerator() throws IOException {
    String code = "function foo() {}";
    Generator generator = () -> code;
    SourceFile underTest = SourceFile.fromGenerator("test.js", generator);
    assertEquals("function foo() {}\n", underTest.getCode());

    // Clear cached code
    underTest.clearCachedSource();

    // Verify cache is cleared
    assertNull(underTest.getCode());

    // Regenerate code and verify it matches the original
    String regeneratedCode = generator.getCode();
    assertEquals(code, regeneratedCode);
    assertEquals(code, underTest.getCode());
  }

  @org.junit.Test
  public void testFromInputStream() throws IOException {
    String code = "function foo() {}";
    InputSupplier inputSupplier = () -> new SerializableInputStream(
        new InputStreamReader(new StringReader(code), StandardCharsets.UTF_8)) {
          @Override
          public void close() throws IOException {}
        };
    SourceFile underTest = SourceFile.fromInputStream("test.js", inputSupplier);
    assertEquals("function foo() {}\n", underTest.getCode());

    // Clear cached code
    underTest.clearCachedSource();

    // Verify cache is cleared
    assertNull(underTest.getCode());

    // Regenerate code and verify it matches the original
    String regeneratedCode = CharStreams.toString(inputSupplier.getInput());
    assertEquals(code, regeneratedCode);
    assertEquals(code, underTest.getCode());
  }

  @org.junit.Test
  public void testFromFileWithOriginalPath() throws IOException {
    File tempFile = File.createTempFile("test", ".js");
    String originalPath = tempFile.getParent();
    try (OutputStreamWriter writer = new OutputStreamWriter(new FileWriter(tempFile))) {
      writer.write("function foo() {}");
    }

    SourceFile underTest =
        SourceFile.fromFile("test.js", originalPath, "test.js");
    assertEquals("function foo() {}\n", underTest.getCode());
    assertEquals(tempFile.getPath(), underTest.getOriginalPath());
  }

  @org.junit.Test
  public void testPreloaded() throws IOException {
    String code = "function foo() {}";
    Preloaded underTest = new Preloaded("test.js", code);
    assertEquals("function foo() {}\n", underTest.getCode());

    // Clear cached code
    underTest.clearCachedSource();

    // Verify cache is cleared
    assertNull(underTest.getCode());

    // Verify original code is not affected
    assertEquals(code, underTest.getCodeNoCache());
  }

  @org.junit.Test
  public void testPreloadedWithOriginalPath() throws IOException {
    String code = "function foo() {}";
    String originalPath = "src";
    PreloadedWithOriginalPath underTest =
        new PreloadedWithOriginalPath("test.js", originalPath, code);
    assertEquals("function foo() {}\n", underTest.getCode());
    assertEquals(originalPath, underTest.getOriginalPath());
  }

  @org.junit.Test
  public void testOnDisk() throws IOException {
    String code = "function foo() {}";
    File tempFile = File.createTempFile("test", ".js");
    try (OutputStreamWriter writer = new OutputStreamWriter(new FileWriter(tempFile))) {
      writer.write(code);
    }

    SourceFile underTest = SourceFile.fromFile("test.js", StandardCharsets.UTF_8);
    assertEquals("function foo() {}\n", underTest.getCode());

    // Clear cached code
    underTest.clearCachedSource();

    // Verify cache is cleared
    assertNull(underTest.getCode());

    // Verify original code is still present on disk
    assertEquals(code, Files.toString(tempFile, StandardCharsets.UTF_8));
  }

  @org.junit.Test
  public void testOnDiskWithCustomCharset() throws IOException {
    String code = "function foo() {}";
    File tempFile = File.createTempFile("test", ".js");
    try (OutputStreamWriter writer = new OutputStreamWriter(new FileWriter(tempFile))) {
      writer.write(code);
    }

    SourceFile underTest = SourceFile.fromFile("test.js", tempFile, Charsets.ISO_8859_1);
    assertEquals("function foo() {}\n", underTest.getCode());

    // Clear cached code
    underTest.clearCachedSource();

    // Verify cache is cleared
    assertNull(underTest.getCode());

    // Verify original code is still present on disk
    assertEquals(code, Files.toString(tempFile, StandardCharsets.UTF_8));
  }

  @org.junit.Test
  public void testGetCodeWithDifferentCharset() throws IOException {
    String code = "function foo() {}";
    File tempFile = File.createTempFile("test", ".js");
    try (OutputStreamWriter writer = new OutputStreamWriter(new FileWriter(tempFile))) {
      writer.write(code);
    }

    SourceFile underTest = SourceFile.fromFile("test.js", tempFile);
    String expected = "function foo() {}\n";
    String actual = underTest.getCode(StandardCharsets.UTF_8);
    assertEquals(expected, actual);

    // Clear cached code
    underTest.clearCachedSource();

    // Verify cache is cleared
    assertNull(underTest.getCode());

    // Verify original code is still present on disk
    assertEquals(code, Files.toString(tempFile, StandardCharsets.UTF_8));

    // Verify custom charset returns expected result
    actual = underTest.getCode(Charsets.ISO_8859_1);
    assertEquals(expected, actual);
  }

  @org.junit.After
  public void tearDown() throws Exception {
    Files.delete(tempFile.toPath());
  }
}

class Region {
  private final int startLineNumber;
  private final int endLineNumber;
  private final String sourceCode;

  public Region(int startLineNumber, int endLineNumber, String sourceCode) {
    this.startLineNumber = startLineNumber;
    this.endLineNumber = endLineNumber;
    this.sourceCode = sourceCode;
  }

  public int getStartLineNumber() {
    return startLineNumber;
  }

  public int getEndLineNumber() {
    return endLineNumber;
  }

  public String getSourceCode() {
    return sourceCode;
  }
}