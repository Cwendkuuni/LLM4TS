import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

public class SourceFileTest {

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @org.junit.BeforeClass
  public static void setup() throws IOException {
    // Set up some test files.
    Files.write(new File("test1.js"), Resources.asByteSource(
        Resources.getResource("test1.js")).read(), Charsets.UTF_8);
    Files.write(new File("test2.js"), Resources.asByteSource(
        Resources.getResource("test2.js")).read(), Charsets.UTF_8);
    Files.write(new File("test3.js"), Resources.asByteSource(
        Resources.getResource("test3.js")).read(), Charsets.UTF_8);
  }

  @org.junit.AfterClass
  public static void teardown() throws IOException {
    // Clean up test files.
    Files.delete(new File("test1.js"));
    Files.delete(new File("test2.js"));
    Files.delete(new File("test3.js"));
  }

  @org.junit.Test
  public void testFromFile() throws IOException {
    SourceFile file1 = SourceFile.fromFile("test1.js");
    assertEquals("test1.js", file1.getName());
    assertEquals(1, file1.getNumLines());

    String code1 = file1.getCode();
    String expected1 = Resources.toString(
        Resources.getResource("test1.js"), Charsets.UTF_8);
    assertEquals(expected1, code1);

    String line1 = file1.getLine(1);
    assertEquals("function foo() {\n", line1);

    String region1 = file1.getRegion(1);
    String expectedRegion1 = "function foo() {\n";
    assertEquals(expectedRegion1, region1.getCode());
    assertEquals(1, region1.getBeginLineNumber());
    assertEquals(1, region1.getEndLineNumber());

    SourceFile file2 = SourceFile.fromFile("test2.js");
    assertEquals("test2.js", file2.getName());
    assertEquals(2, file2.getNumLines());

    String code2 = file2.getCode();
    String expected2 = Resources.toString(
        Resources.getResource("test2.js"), Charsets.UTF_8);
    assertEquals(expected2, code2);

    String line2 = file2.getLine(2);
    assertEquals("function bar() {\n", line2);

    String region2 = file2.getRegion(2);
    String expectedRegion2 = "function bar() {\n";
    assertEquals(expectedRegion2, region2.getCode());
    assertEquals(2, region2.getBeginLineNumber());
    assertEquals(2, region2.getEndLineNumber());
  }

  @org.junit.Test
  public void testFromCode() throws IOException {
    String code1 = "function foo() {\n";
    SourceFile file1 = SourceFile.fromCode("test1.js", code1);
    assertEquals("test1.js", file1.getName());
    assertEquals(1, file1.getNumLines());

    String line1 = file1.getLine(1);
    assertEquals("function foo() {\n", line1);

    String region1 = file1.getRegion(1);
    String expectedRegion1 = "function foo() {\n";
    assertEquals(expectedRegion1, region1.getCode());
    assertEquals(1, region1.getBeginLineNumber());
    assertEquals(1, region1.getEndLineNumber());

    String code2 = "function bar() {\n";
    SourceFile file2 = SourceFile.fromCode("test2.js", code2);
    assertEquals("test2.js", file2.getName());
    assertEquals(1, file2.getNumLines());

    String line2 = file2.getLine(1);
    assertEquals("function bar() {\n", line2);

    String region2 = file2.getRegion(1);
    String expectedRegion2 = "function bar() {\n";
    assertEquals(expectedRegion2, region2.getCode());
    assertEquals(1, region2.getBeginLineNumber());
    assertEquals(1, region2.getEndLineNumber());
  }

  @org.junit.Test
  public void testFromInputStream() throws IOException {
    InputStream in1 = Resources.getResource("test1.js").openStream();
    SourceFile file1 = SourceFile.fromInputStream("test1.js", in1);
    assertEquals("test1.js", file1.getName());
    assertEquals(1, file1.getNumLines());

    String code1 = file1.getCode();
    String expected1 = Resources.toString(
        Resources.getResource("test1.js"), Charsets.UTF_8);
    assertEquals(expected1, code1);

    String line1 = file1.getLine(1);
    assertEquals("function foo() {\n", line1);

    String region1 = file1.getRegion(1);
    String expectedRegion1 = "function foo() {\n";
    assertEquals(expectedRegion1, region1.getCode());
    assertEquals(1, region1.getBeginLineNumber());
    assertEquals(1, region1.getEndLineNumber());

    InputStream in2 = Resources.getResource("test2.js").openStream();
    SourceFile file2 = SourceFile.fromInputStream("test2.js", in2);
    assertEquals("test2.js", file2.getName());
    assertEquals(2, file2.getNumLines());

    String code2 = file2.getCode();
    String expected2 = Resources.toString(
        Resources.getResource("test2.js"), Charsets.UTF_8);
    assertEquals(expected2, code2);

    String line2 = file2.getLine(2);
    assertEquals("function bar() {\n", line2);

    String region2 = file2.getRegion(2);
    String expectedRegion2 = "function bar() {\n";
    assertEquals(expectedRegion2, region2.getCode());
    assertEquals(2, region2.getBeginLineNumber());
    assertEquals(2, region2.getEndLineNumber());

    in1.close();
    in2.close();
  }

  @org.junit.Test
  public void testFromReader() throws IOException {
    Reader reader1 = new InputStreamReader(
        Resources.getResource("test1.js").openStream(), Charsets.UTF_8);
    SourceFile file1 = SourceFile.fromReader("test1.js", reader1);
    assertEquals("test1.js", file1.getName());
    assertEquals(1, file1.getNumLines());

    String code1 = file1.getCode();
    String expected1 = Resources.toString(
        Resources.getResource("test1.js"), Charsets.UTF_8);
    assertEquals(expected1, code1);

    String line1 = file1.getLine(1);
    assertEquals("function foo() {\n", line1);

    String region1 = file1.getRegion(1);
    String expectedRegion1 = "function foo() {\n";
    assertEquals(expectedRegion1, region1.getCode());
    assertEquals(1, region1.getBeginLineNumber());
    assertEquals(1, region1.getEndLineNumber());

    reader1.close();
  }

  @org.junit.Test
  public void testFromGenerator() {
    Generator generator = new Generator() {
      @Override
      public String getCode() {
        return "function foo() {\n";
      }
    };
    SourceFile file1 = SourceFile.fromGenerator("test1.js", generator);
    assertEquals("test1.js", file1.getName());
    assertEquals(1, file1.getNumLines());

    String code1 = file1.getCode();
    assertEquals("function foo() {\n", code1);

    String line1 = file1.getLine(1);
    assertEquals("function foo() {\n", line1);

    String region1 = file1.getRegion(1);
    String expectedRegion1 = "function foo() {\n";
    assertEquals(expectedRegion1, region1.getCode());
    assertEquals(1, region1.getBeginLineNumber());
    assertEquals(1, region1.getEndLineNumber());
  }
}

class SimpleRegion implements StaticSourceFile {
  private final int startLine;
  private final int endLine;
  private final String code;

  SimpleRegion(int startLine, int endLine, String code) {
    this.startLine = startLine;
    this.endLine = endLine;
    this.code = code;
  }

  @Override
  public int getLineOffset(int line) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getNumLines() {
    return endLine - startLine + 1;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public int getLength() {
    return code.length();
  }

  @Override
  public Reader getCodeReader() throws IOException {
    return new StringReader(code);
  }

  @Override
  public boolean isExtern() {
    return false;
  }

  @Override
  public String getName() {
    return "simple region";
  }
}