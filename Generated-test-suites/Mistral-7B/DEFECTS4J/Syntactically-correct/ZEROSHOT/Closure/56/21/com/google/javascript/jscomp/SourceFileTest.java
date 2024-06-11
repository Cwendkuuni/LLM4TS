import static org.junit.Assert.*;

import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.Charset;

public class SourceFileTest {

  @SuppressWarnings("unchecked")
  private static Generator createMockGenerator(String code) {
    return new Generator() {
      @Override
      public String getCode() {
        return code;
      }
    };
  }

  @org.junit.Test
  public void testNewSourceFile() throws Exception {
    SourceFile sf = new SourceFile("test.js");
    assertEquals("test.js", sf.getName());
    assertFalse(sf.isExtern());
    assertNull(sf.getCode());
    assertNull(sf.getOriginalPath());
    assertEquals(1, sf.getNumLines());
    assertNull(sf.getLine(1));
  }

  @org.junit.Test
  public void testNewSourceFileWithCode() throws Exception {
    String code = "function test() {};";
    SourceFile sf = new SourceFile("test.js", createMockGenerator(code));
    assertEquals("test.js", sf.getName());
    assertFalse(sf.isExtern());
    assertEquals(code, sf.getCode());
    assertNull(sf.getOriginalPath());
    assertEquals(1, sf.getNumLines());
    assertEquals(code.substring(0, code.lastIndexOf(";")), sf.getLine(1));
  }

  @org.junit.Test
  public void testNewSourceFileFromFile() throws Exception {
    SourceFile sf = SourceFile.fromFile("test.txt");
    assertEquals("test.txt", sf.getName());
    assertFalse(sf.isExtern());
    String expectedCode = Files.toString(new File("test.txt"), Charsets.UTF_8);
    assertEquals(expectedCode, sf.getCode());
    assertNull(sf.getOriginalPath());
    assertEquals(1, sf.getNumLines());
    assertEquals(expectedCode.substring(0, expectedCode.lastIndexOf("\n")),
        sf.getLine(1));
  }

  @org.junit.Test
  public void testNewSourceFileFromInputStream() throws Exception {
    String inputCode = "function test() {};";
    String expectedCode = "function test() {};";
    try (InputStream is = new FileInputStream("test.txt")) {
      SourceFile sf = SourceFile.fromInputStream("test.txt", is);
      assertEquals("test.txt", sf.getName());
      assertFalse(sf.isExtern());
      assertEquals(expectedCode, sf.getCode());
      assertNull(sf.getOriginalPath());
      assertEquals(1, sf.getNumLines());
      assertEquals(expectedCode.substring(0, expectedCode.lastIndexOf(";")),
          sf.getLine(1));
    }
  }

  @org.junit.Test
  public void testNewSourceFileFromReader() throws Exception {
    String inputCode = "function test() {};";
    String expectedCode = "function test() {};";
    try (Reader reader = new FileReader("test.txt")) {
      SourceFile sf = SourceFile.fromReader("test.txt", reader);
      assertEquals("test.txt", sf.getName());
      assertFalse(sf.isExtern());
      assertEquals(expectedCode, sf.getCode());
      assertNull(sf.getOriginalPath());
      assertEquals(1, sf.getNumLines());
      assertEquals(expectedCode.substring(0, expectedCode.lastIndexOf(";")),
          sf.getLine(1));
    }
  }

  @org.junit.Test
  public void testNewSourceFileFromGenerator() throws Exception {
    String code = "function test() {};";
    SourceFile sf = new SourceFile("test.js", createMockGenerator(code));
    assertEquals("test.js", sf.getName());
    assertFalse(sf.isExtern());
    assertEquals(code, sf.getCode());
    assertNull(sf.getOriginalPath());
    assertEquals(1, sf.getNumLines());
    assertEquals(code.substring(0, code.lastIndexOf(";")), sf.getLine(1));
  }

  @org.junit.Test
  public void testGetLine() throws Exception {
    String code = "function test() {\n" +
                  "  var x = 1;\n" +
                  "  var y = 2;\n" +
                  "  var z = x + y;\n" +
                  "}";
    SourceFile sf = new SourceFile("test.js", createMockGenerator(code));
    assertEquals(1, sf.getLine(1).indexOf("function test"));
    assertEquals(2, sf.getLine(2).indexOf("  var x = 1;"));
    assertEquals(3, sf.getLine(3).indexOf("  var y = 2;"));
    assertEquals(4, sf.getLine(4).indexOf("  var z = x + y;"));
  }

  @org.junit.Test
  public void testGetRegion() throws Exception {
    String code = "function test() {\n" +
                  "  var x = 1;\n" +
                  "  var y = 2;\n" +
                  "  var z = x + y;\n" +
                  "}";
    SourceFile sf = new SourceFile("test.js", createMockGenerator(code));
    Region region = sf.getRegion(4);
    assertEquals(3, region.getStartLineNumber());
    assertEquals(4, region.getEndLineNumber());
    assertEquals("  var z = x + y;", region.getSource());
  }

  @org.junit.Test
  public void testSetOriginalPath() throws Exception {
    SourceFile sf = new SourceFile("test.js");
    sf.setOriginalPath("/path/to/test.js");
    assertEquals("/path/to/test.js", sf.getOriginalPath());
  }

  @org.junit.Test
  public void testIsExtern() throws Exception {
    SourceFile sf = new SourceFile("test.js");
    assertFalse(sf.isExtern());
    sf.setIsExtern(true);
    assertTrue(sf.isExtern());
  }

  @org.junit.Test
  public void testClearCachedSource() throws Exception {
    String code = "function test() {};";
    SourceFile sf = new SourceFile("test.js", createMockGenerator(code));
    String originalCode = sf.getCode();
    sf.clearCachedSource();
    assertNull(sf.getCode());
    sf.setCode(code); // restore original state
  }
}