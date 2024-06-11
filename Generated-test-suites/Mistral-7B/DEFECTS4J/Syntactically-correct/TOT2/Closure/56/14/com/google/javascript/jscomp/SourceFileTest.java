import com.google.javascript.jscomp.SourceFile;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.Charset;

public class SourceFileTest {

  @Test
  public void testDefaultConstructor() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    Assert.assertEquals("test.js", sourceFile.getName());
    Assert.assertFalse(sourceFile.isExtern());
    Assert.assertNull(sourceFile.getCode());
  }

  @Test
  public void testGetLine() throws IOException {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", jsCode);
    String expectedLine = "  return x;";
    String actualLine = sourceFile.getLine(6);
    Assert.assertEquals(expectedLine, actualLine);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineWithInvalidLineNumber() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "function test() {}");
    sourceFile.getLine(10);
  }

  @Test
  public void testGetRegion() throws IOException {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", jsCode);
    Region expectedRegion = new Region(1, 3, "  return x;");
    Region actualRegion = sourceFile.getRegion(3);
    Assert.assertEquals(expectedRegion.getStartLine(), actualRegion.getStartLine());
    Assert.assertEquals(expectedRegion.getEndLine(), actualRegion.getEndLine());
    Assert.assertEquals(expectedRegion.getCode(), actualRegion.getCode());
  }

  @Test(expected = NullPointerException.class)
  public void testGetRegionWithNull() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "function test() {}");
    sourceFile.getRegion(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionWithInvalidLineNumber2() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "function test() {}");
    sourceFile.getRegion(10);
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", jsCode);
    int[] expectedLineOffsets = {0, 6, 8, 10};
    Assert.assertArrayEquals(expectedLineOffsets, sourceFile.lineOffsets);
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", jsCode);
    String cachedCode = sourceFile.getCode();
    Assert.assertNotNull(cachedCode);
    sourceFile.clearCachedSource();
    Assert.assertNull(sourceFile.getCode());
  }

  @Test
  public void testHasSourceInMemory() throws IOException {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", jsCode);
    Assert.assertTrue(sourceFile.hasSourceInMemory());
    sourceFile.clearCachedSource();
    Assert.assertFalse(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", jsCode);
    Assert.assertEquals(6, sourceFile.getLineOffset(3));
  }

  @Test
  public void testGetNumLines() throws IOException {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", jsCode);
    Assert.assertEquals(3, sourceFile.getNumLines());
  }

  @Test
  public void testFromFile() throws IOException {
    String expectedCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    String fileName = "test.js";
    Charset charset = Charsets.UTF_8;
    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);
    String actualCode = sourceFile.getCode();
    Assert.assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testFromFileWithNullCharset() throws IOException {
    String fileName = "test.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
    String actualCode = sourceFile.getCode();
    Assert.assertNotNull(actualCode);
  }

  @Test(expected = FileNotFoundException.class)
  public void testFromFileWithNonExistentFile() throws IOException {
    String fileName = "non_existent.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
  }

  @Test
  public void testFromCode() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    String actualCode = sourceFile.getCode();
    Assert.assertEquals(code, actualCode);
  }

  @Test
  public void testFromCodeWithNullFileName() throws IOException {
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromCode(null, code);
    String actualCode = sourceFile.getCode();
    Assert.assertEquals(code, actualCode);
  }

  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    InputStream inputStream = new ByteArrayInputStream(code.getBytes());
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);
    String actualCode = sourceFile.getCode();
    Assert.assertEquals(code, actualCode);
  }

  @Test
  public void testFromInputStreamWithNullFileName() throws IOException {
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    InputStream inputStream = new ByteArrayInputStream(code.getBytes());
    SourceFile sourceFile = SourceFile.fromInputStream(null, inputStream);
    String actualCode = sourceFile.getCode();
    Assert.assertEquals(code, actualCode);
  }

  @Test(expected = IOException.class)
  public void testFromInputStreamWithNullInputStream() throws IOException {
    String fileName = "test.js";
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, null);
  }

  @Test
  public void testFromReader() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    Reader reader = new StringReader(code);
    SourceFile sourceFile = SourceFile.fromReader(fileName, reader);
    String actualCode = sourceFile.getCode();
    Assert.assertEquals(code, actualCode);
  }

  @Test
  public void testFromGenerator() throws IOException {
    String fileName = "test.js";
    Generator generator = () -> "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);
    String actualCode = sourceFile.getCode();
    Assert.assertEquals(generator.getCode(), actualCode);
  }

  @Test
  public void testIsExtern() throws IOException {
    String jsCode = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile1 = new Preloaded("test.js", jsCode);
    Assert.assertFalse(sourceFile1.isExtern());
    SourceFile sourceFile2 = new Preloaded("test.js", jsCode, true);
    Assert.assertTrue(sourceFile2.isExtern());
  }

  @Test
  public void testSetIsExtern() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "function test() {}");
    sourceFile.setIsExtern(true);
    Assert.assertTrue(sourceFile.isExtern());
  }
}