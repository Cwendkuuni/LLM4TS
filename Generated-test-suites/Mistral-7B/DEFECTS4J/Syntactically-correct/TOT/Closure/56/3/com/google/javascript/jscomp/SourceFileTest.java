import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceFile;

public class SourceFileTest {

  @Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    try {
      sourceFile.setCode("function test() {\n" +
          "  var x = 1;\n" +
          "}\n");
      String line1 = sourceFile.getLine(1);
      String line2 = sourceFile.getLine(2);

      assertEquals("function test() {\n", line1);
      assertEquals("  var x = 1;\n", line2);
    } catch (IOException e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testGetLineInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    try {
      sourceFile.setCode("function test() {\n" +
          "  var x = 1;\n" +
          "}\n");
      String line100 = sourceFile.getLine(100);

      assertNull(line100);
    } catch (IOException e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testGetLineMissingCode() {
    SourceFile sourceFile = new SourceFile("testFile.js");

    String line1 = sourceFile.getLine(1);

    assertNull(line1);
  }

  @Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    try {
      sourceFile.setCode("function test() {\n" +
          "  var x = 1;\n" +
          "}\nfunction test2() {\n" +
          "  var y = 2;\n" +
          "}\n");
      Region region = sourceFile.getRegion(3);

      assertEquals(2, region.getStartLineNumber());
      assertEquals(2, region.getEndLineNumber());
      assertEquals("  var y = 2;\n", region.getCode());
    } catch (IOException e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testGetRegionInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    try {
      sourceFile.setCode("function test() {\n" +
          "  var x = 1;\n" +
          "}\nfunction test2() {\n" +
          "  var y = 2;\n" +
          "}\n");
      Region region = sourceFile.getRegion(10);

      assertNull(region);
    } catch (IOException e) {
      fail("Unexpected exception: " + e.getMessage());
    }
  }

  @Test
  public void testGetRegionMissingCode() {
    SourceFile sourceFile = new SourceFile("testFile.js");

    Region region = sourceFile.getRegion(1);

    assertNull(region);
  }
}