import static org.junit.Assert.*;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.OnDisk;
import com.google.javascript.rhino.SourceName;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SourceFileTest {

  @org.junit.Test
  public void testSourceFile() throws IOException {
    String fileName = "testFile.js";
    String content = "function test() {\n" +
        "  print('Hello World');\n" +
        "}";

    SourceFile sourceFile = new OnDisk(new File(fileName), StandardCharsets.UTF_8);
    sourceFile.setCode(content);

    assertEquals(SourceName.getSourceName(fileName), sourceFile.getName());
    assertEquals(content, sourceFile.getCode());
    assertEquals(1, sourceFile.getNumLines());
    assertEquals("function test() {\\n  print('Hello World');\\n}", sourceFile.getCode());
    assertEquals(ContentType.JS, SourceFile.getContentType(fileName));

    assertEquals("Hello World", sourceFile.getLine(1).trim());
    assertEquals(content, sourceFile.getRegion(1).getCode());
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testSourceFileNullFileName() {
    new SourceFile(null);
  }

  @org.junit.Test
  public void testSourceFileEmptyFileName() {
    new SourceFile("");
  }

  @org.junit.Test
  public void testSourceFileNoCode() throws IOException {
    String fileName = "testFile.js";
    SourceFile sourceFile = new OnDisk(new File(fileName));
    assertEquals(1, sourceFile.getNumLines());
    assertNull(sourceFile.getCode());
  }

  @org.junit.Test
  public void testSourceFileWithOriginalPath() throws IOException {
    String fileName = "testFile.js";
    String originalPath = "/path/to/testFile.js";
    String content = "function test() {\n" +
        "  print('Hello World');\n" +
        "}";

    SourceFile sourceFile = new OnDisk(new File(fileName), originalPath, content);

    assertEquals(SourceName.getSourceName(originalPath), sourceFile.getOriginalPath());
    assertEquals(SourceName.getSourceName(fileName), sourceFile.getName());
    assertEquals(content, sourceFile.getCode());
    assertEquals("function test() {\\n  print('Hello World');\\n}", sourceFile.getCode());
    assertEquals(1, sourceFile.getNumLines());
    assertEquals("Hello World", sourceFile.getLine(1).trim());
    assertEquals(content, sourceFile.getRegion(1).getCode());
  }
}