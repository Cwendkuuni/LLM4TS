import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SourceFileTest {

  @Test
  public void testSourceFileCreation() throws IOException {
    String fileName = "testFile.js";
    SourceFile sourceFile = new SourceFile(fileName);
    Assert.assertEquals(fileName, sourceFile.getName());
    Assert.assertFalse(sourceFile.isExtern());

    String code = "function testFunction() {\n" +
            "  return 42;\n" +
            "}";
    sourceFile.setCode(code);
    Assert.assertEquals(code, sourceFile.getCode());

    String expectedRegion = "function testFunction() {\n" +
            "  return 42;\n" +
            "}";
    Region region = sourceFile.getRegion(3);
    Assert.assertNotNull(region);
    Assert.assertEquals(expectedRegion, region.getCode());
  }

  @Test
  public void testSourceFileFromFile() throws IOException {
    String fileName = "testFile.js";
    File file = new File(fileName);
    SourceFile sourceFile = new SourceFile(file, StandardCharsets.UTF_8);
    Assert.assertEquals(fileName, sourceFile.getName());
    Assert.assertFalse(sourceFile.isExtern());

    String expectedCode = Files.toString(file, StandardCharsets.UTF_8);
    String actualCode = sourceFile.getCode();
    Assert.assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testSourceFileFromGenerator() {
    String fileName = "testFile.js";
    SourceFile sourceFile = new SourceFile(fileName, new MockGenerator());
    Assert.assertEquals(fileName, sourceFile.getName());
    Assert.assertFalse(sourceFile.isExtern());

    String expectedCode = "function testFunction() {\n" +
            "  return 42;\n" +
            "}";
    String actualCode = sourceFile.getCode();
    Assert.assertEquals(expectedCode, actualCode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileCreationWithEmptyName() {
    String fileName = "";
    new SourceFile(fileName);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileCreationWithNullName() {
    String fileName = null;
    new SourceFile(fileName);
  }

  @Test
  public void testSourceFileFindLineOffsets() throws IOException {
    String code = "function testFunction(a) {\n" +
            "  if (a > 10) {\n" +
            "    return 42;\n" +
            "  } else {\n" +
            "    throw new Error(\"Invalid argument: \" + a);\n" +
            "  }\n" +
            "}";
    SourceFile sourceFile = new SourceFile("testFile.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    Assert.assertArrayEquals(new int[]{0, 5, 13, 23, 33, 37, 38, 42},
        sourceFile.getLineOffsets());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileGetLineWithInvalidLineNumber() throws IOException {
    String code = "function testFunction() {\n" +
            "  return 42;\n" +
            "}";
    SourceFile sourceFile = new SourceFile("testFile.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    sourceFile.getLine(100);
  }

  @Test
  public void testSourceFileGetRegionWithValidLineNumber() throws IOException {
    String code = "function testFunction() {\n" +
            "  return 42;\n" +
            "}";
    SourceFile sourceFile = new SourceFile("testFile.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    Region region = sourceFile.getRegion(3);
    Assert.assertNotNull(region);
    Assert.assertEquals("return 42;\n", region.getCode());
  }

  @Test(expected = NullPointerException.class)
  public void testSourceFileGetRegionWithNullCode() throws IOException {
    String fileName = "testFile.js";
    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return null;
      }
    };
    region = sourceFile.getRegion(3);
  }

  @Test(expected = IOException.class)
  public void testSourceFileGetRegionWithInvalidCode() throws IOException {
    String fileName = "testFile.js";
    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        throw new IOException();
      }
    };
    region = sourceFile.getRegion(3);
  }

  static class MockGenerator implements SourceFile.Generator {
    @Override
    public String getCode() {
      return "function testFunction() {\n" +
              "  return 42;\n" +
              "}";
    }
  }
}