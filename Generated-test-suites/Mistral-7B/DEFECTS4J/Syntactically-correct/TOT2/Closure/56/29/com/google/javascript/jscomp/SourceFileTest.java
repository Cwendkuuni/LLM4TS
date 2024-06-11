import com.google.javascript.jscomp.SourceFile;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceFileTest {

  @Test
  public void testSourceFileCreation() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertEquals("testFile.js", sourceFile.getName());
    assertFalse(sourceFile.isExtern());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileCreationWithEmptyName() {
    new SourceFile("");
  }

  @Test
  public void testGetCode() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js") {
      @Override
      String getCode() throws IOException {
        return "function test() { return 42; }";
      }
    };
    assertEquals("function test() { return 42; }", sourceFile.getCode());
  }

  @Test(expected = IOException.class)
  public void testGetCodeThrowsIOException() throws IOException {
    new SourceFile("testFile.js") {
      @Override
      String getCode() throws IOException {
        throw new IOException();
      }
    };
  }

  @Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("testFile.js") {
      @Override
      String getCode() throws IOException {
        return "function test() {\n" +
               "  var x = 1;\n" +
               "  return x;\n" +
               "}";
      }
    };
    assertEquals("var x = 1;", sourceFile.getLine(2));
    assertEquals(null, sourceFile.getLine(5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineWithInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.getLine(10);
  }

  @Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("testFile.js") {
      @Override
      String getCode() throws IOException {
        return "function test() {\n" +
               "  var x = 1;\n" +
               "  return x;\n" +
               "}";
      }
    };
    Region region = sourceFile.getRegion(3);
    assertEquals("  var x = 1;", region.getCode());
    assertEquals(3, region.getStartLineNumber());
    assertEquals(3, region.getEndLineNumber());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionWithInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.getRegion(10);
  }
}