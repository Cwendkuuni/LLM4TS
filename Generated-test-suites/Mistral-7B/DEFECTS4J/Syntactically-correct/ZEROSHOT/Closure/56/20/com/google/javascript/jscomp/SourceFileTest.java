import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SourceFileTest {

  @org.junit.BeforeClass
  public static void setup() throws IOException {
    String testFileContent = "println('Hello World');";
    Files.write(new File("testFile.js"), testFileContent.getBytes(StandardCharsets.UTF_8));
  }

  @org.junit.AfterClass
  public static void teardown() throws IOException {
    Files.delete(new File("testFile.js"));
  }

  @org.junit.Test
  public void testFromFile() throws IOException {
    SourceFile sourceFile = SourceFile.fromFile("testFile.js");
    assertEquals("testFile.js", sourceFile.getName());
    assertEquals(testFileContent, sourceFile.getCode());
  }

  @org.junit.Test
  public void testFromCode() throws IOException {
    String testCode = "println('Hello World');";
    SourceFile sourceFile = SourceFile.fromCode("testFile.js", testCode);
    assertEquals("testFile.js", sourceFile.getName());
    assertEquals(testCode, sourceFile.getCode());
  }

  @org.junit.Test
  public void testGetLine() throws IOException {
    SourceFile sourceFile = SourceFile.fromFile("testFile.js");
    assertEquals("println('Hello World');", sourceFile.getCode());
    assertEquals("println('Hello World');", sourceFile.getLine(1));
    assertEquals("", sourceFile.getLine(10));
    assertNull(sourceFile.getLine(0));
  }

  @org.junit.Test
  public void testGetRegion() throws IOException {
    SourceFile sourceFile = SourceFile.fromFile("testFile.js");
    Region region = sourceFile.getRegion(1);
    assertEquals("println('Hello World');\n", region.getCode());
    assertEquals(1, region.getStartLineNumber());
    assertEquals(1, region.getEndLineNumber());

    region = sourceFile.getRegion(2);
    assertNull(region);
  }

  @org.junit.Test
  public void testGetNumLines() throws IOException {
    SourceFile sourceFile = SourceFile.fromFile("testFile.js");
    assertEquals(1, sourceFile.getNumLines());
  }

  @org.junit.Test
  public void testIsExtern() throws IOException {
    SourceFile sourceFile = SourceFile.fromFile("testFile.js");
    assertFalse(sourceFile.isExtern());

    SourceFile externFile = new SourceFile("extern.js") {
      @Override
      public boolean isExtern() {
        return true;
      }
    };
    assertTrue(externFile.isExtern());
  }
}