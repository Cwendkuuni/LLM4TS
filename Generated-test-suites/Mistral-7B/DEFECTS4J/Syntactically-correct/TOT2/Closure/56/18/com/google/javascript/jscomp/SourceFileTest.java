import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

  @Test
  public void testSourceFileWithValidFileName() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("test.js", sourceFile.getName());
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testSourceFileWithExternFlagSet() {
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testGetCode() throws IOException {
    String fileContent = "function test() {\n" +
        "  alert('Hello World!');\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", fileContent);
    assertEquals(fileContent, sourceFile.getCode());
  }

  @Test(expected = IOException.class)
  public void testGetCodeThrowsIOException() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.getCode();
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String fileContent = "function test() {\n" +
        "  alert('Hello World!');\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", fileContent);
    Reader reader = sourceFile.getCodeReader();
    assertEquals(fileContent, CharStreams.toString(reader));
  }

  @Test(expected = IOException.class)
  public void testGetCodeReaderThrowsIOException() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.getCodeReader();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.getLineOffset(-1);
  }

  @Test
  public void testGetLineOffset() {
    String fileContent = "function test() {\n" +
        "  alert('Hello World!');\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", fileContent);
    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals(fileContent.length(), sourceFile.getLineOffset(11));
  }

  @Test
  public void testGetNumLines() {
    String fileContent = "function test1() {\n" +
        "  alert('Hello World 1');\n" +
        "}\nfunction test2() {\n" +
        "  alert('Hello World 2');\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", fileContent);
    assertEquals(2, sourceFile.getNumLines());
  }

  @Test
  public void testGetRegion() {
    String fileContent = "function test() {\n" +
        "  alert('Hello World!');\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", fileContent);
    Region region = sourceFile.getRegion(3);
    assertNotNull(region);
    assertEquals("  alert('Hello World!');\n", region.toString());
  }

  @Test(expected = NullPointerException.class)
  public void testGetRegionWithNullSourceFile() {
    SourceFile sourceFile = null;
    region = sourceFile.getRegion(3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionWithInvalidLineNumber() {
    String fileContent = "function test() {\n" +
        "  alert('Hello World!');\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", fileContent);
    region = sourceFile.getRegion(-1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionWithInvalidLineNumberRange() {
    String fileContent = "function test() {\n" +
        "  alert('Hello World!');\n" +
        "}";
    SourceFile sourceFile = new Preloaded("test.js", fileContent);
    region = sourceFile.getRegion(1, 3);
  }

  @Test
  public void testToString() {
    SourceFile sourceFile = new SourceFile("test.js");
    assertEquals("test.js", sourceFile.toString());
  }

  @Test
  public void testFromFile() throws IOException {
    String fileContent = "function test() {\n" +
        "  alert('Hello World!');\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromFile("test.js");
    assertEquals("test.js", sourceFile.getName());
    assertEquals(fileContent, sourceFile.getCode());
  }

  @Test(expected = IOException.class)
  public void testFromFileThrowsIOException() throws IOException {
    SourceFile sourceFile = SourceFile.fromFile("test.js");
  }

  @Test
  public void testFromCode() {
    String fileName = "test.js";
    String code = "function test() {\n" +
        "  alert('Hello World!');\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  @Test(expected = IOException.class)
  public void testFromCodeThrowsIOException() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n" +
        "  alert('Hello World!');\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
  }
}