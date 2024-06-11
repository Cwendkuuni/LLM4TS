import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.source.CodeReader;
import com.google.javascript.jscomp.source.util.AbstractSourceFileTest;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class SourceFileTest extends AbstractSourceFileTest {

  @Override
  protected SourceFile createSourceFileFromCode(String fileName, String code) {
    return new SourceFile(fileName, code);
  }

  @Override
  protected SourceFile createSourceFileFromFile(String fileName) throws IOException {
    return new SourceFile(new File(fileName));
  }

  @Override
  protected SourceFile createSourceFileFromInputStream(String fileName, InputStream inputStream)
      throws IOException {
    return new SourceFile(new File(Files.tempDir().getPath() + "/temp_" + fileName),
        new StringReader(new String(Files.readAllBytes(inputStream))));
  }

  @Override
  protected SourceFile createSourceFileFromReader(Reader reader) throws IOException {
    return new SourceFile(new StringReader(reader));
  }

  @Override
  protected SourceFile createSourceFileFromGenerator(String fileName, Generator generator) {
    return new SourceFile(fileName, new SimpleGenerator(generator));
  }

  @Override
  protected void assertSourceFileEquals(SourceFile source1, SourceFile source2) {
    assertEquals(source1.getName(), source2.getName());
    assertEquals(source1.getCode(), source2.getCode());
    assertEquals(source1.isExtern(), source2.isExtern());
    assertEquals(source1.getNumLines(), source2.getNumLines());
    assertEquals(source1.getOriginalPath(), source2.getOriginalPath());
  }

  @Override
  protected void testSourceFileBasic() throws Exception {
    super.testSourceFileBasic();

    // Test with different charsets
    Charset utf8 = Charsets.UTF_8;
    Charset isoLatin1 = Charsets.ISO_8859_1;
    String code = "test123";
    SourceFile fileUtf8 = createSourceFileFromFile("SourceFileTest_Utf8.js", code);
    SourceFile fileIsoLatin1 = createSourceFileFromFile("SourceFileTest_IsoLatin1.js", code);
    SourceFile codeUtf8 = createSourceFileFromCode("SourceFileTest_Utf8.js", code);
    SourceFile codeIsoLatin1 = createSourceFileFromCode("SourceFileTest_IsoLatin1.js", code, isoLatin1);

    assertEquals(fileUtf8.getCode(), codeUtf8.getCode());
    assertEquals(fileIsoLatin1.getCode(), codeIsoLatin1.getCode());
    assertNotEquals(fileUtf8.getCode(), fileIsoLatin1.getCode());

    Files.delete(new File(Files.tempDir().getPath() + "/temp_SourceFileTest_IsoLatin1.js"));
  }

  @Override
  protected void testSourceFileFromFile() throws Exception {
    super.testSourceFileFromFile();

    // Test from file with different line endings
    String code = "test123\r\n";
    String windowsFile = "SourceFileTest_Windows.js";
    String unixFile = "SourceFileTest_Unix.js";
    SourceFile windows = createSourceFileFromFile(windowsFile, code);
    SourceFile unix = createSourceFileFromFile(unixFile, code);

    assertEquals(windows.getCode(), unix.getCode());

    Files.delete(new File(Files.tempDir().getPath() + "/" + windowsFile));
    Files.delete(new File(Files.tempDir().getPath() + "/" + unixFile));
  }

  @Override
  protected void testSourceFileFromCode() throws Exception {
    super.testSourceFileFromCode();

    // Test from code with different line endings
    String code = "test123\r\n";
    String windowsCode = "test123\r\n";
    String unixCode = "test123\n";
    SourceFile windows = createSourceFileFromCode("SourceFileTest_Windows.js", windowsCode);
    SourceFile unix = createSourceFileFromCode("SourceFileTest_Unix.js", unixCode);

    assertEquals(windows.getCode(), unix.getCode());

    // Test from code with different charsets
    SourceFile utf8 = createSourceFileFromCode("SourceFileTest_Utf8.js", "test123");
    SourceFile isoLatin1 = createSourceFileFromCode("SourceFileTest_IsoLatin1.js", "test123", "ISO-8859-1");

    assertEquals(utf8.getCode(), "test123");
    assertEquals(isoLatin1.getCode(), "test123");
    assertNotEquals(utf8.getCode(), isoLatin1.getCode());

    // Test from code generator
    String fileName = "SourceFileTest_Generator.js";
    String codeGenerator = "test123";
    CodeReader codeReader = new StringCodeReader(codeGenerator);
    Generator generator = new Generator() {
      @Override
      public String getCode() {
        return codeReader.readLine();
      }
    };
    SourceFile source = createSourceFileFromGenerator(fileName, generator);

    assertEquals(source.getCode(), codeGenerator);
  }

  @Override
  protected void testSourceFileFromInputStream() throws Exception {
    super.testSourceFileFromInputStream();

    // Test from InputStream with different line endings
    String code = "test123\r\n";
    String windowsInput = "SourceFileTest_Windows.js";
    String unixInput = "SourceFileTest_Unix.js";
    InputStream windowsStream = Resources.getResource(windowsInput).openStream();
    InputStream unixStream = Resources.getResource(unixInput).openStream();
    SourceFile windows = createSourceFileFromInputStream(windowsInput, windowsStream);
    SourceFile unix = createSourceFileFromInputStream(unixInput, unixStream);

    assertEquals(windows.getCode(), unix.getCode());

    Files.delete(new File(Files.tempDir().getPath() + "/temp_" + windowsInput));
    Files.delete(new File(Files.tempDir().getPath() + "/temp_" + unixInput));
  }

  @Override
  protected void testSourceFileFromReader() throws Exception {
    super.testSourceFileFromReader();

    // Test from Reader with different line endings
    String code = "test123\r\n";
    String windowsReader = "SourceFileTest_Windows.js";
    String unixReader = "SourceFileTest_Unix.js";
    Reader windowsReaderStream = new InputStreamReader(Resources.getResource(windowsReader).openStream(), Charsets.UTF_8);
    Reader unixReaderStream = new InputStreamReader(Resources.getResource(unixReader).openStream(), Charsets.UTF_8);
    SourceFile windows = createSourceFileFromReader(windowsReaderStream);
    SourceFile unix = createSourceFileFromReader(unixReaderStream);

    assertEquals(windows.getCode(), unix.getCode());

    Files.delete(new File(Files.tempDir().getPath() + "/temp_" + windowsReader));
    Files.delete(new File(Files.tempDir().getPath() + "/temp_" + unixReader));
  }

  @Override
  protected void testSourceFileFromGenerator() throws Exception {
    super.testSourceFileFromGenerator();

    // Test from Generator with different line endings
    String fileName = "SourceFileTest_Generator.js";
    String codeGenerator = "test123";
    String windowsCodeGenerator = "test123\r\n";
    String unixCodeGenerator = "test123\n";
    Generator generator = new Generator() {
      @Override
      public String getCode() {
        return codeGenerator;
      }
    };
    Generator generatorWindows = new Generator() {
      @Override
      public String getCode() {
        return windowsCodeGenerator;
      }
    };
    Generator generatorUnix = new Generator() {
      @Override
      public String getCode() {
        return unixCodeGenerator;
      }
    };
    SourceFile sourceWindows = createSourceFileFromGenerator(fileName, generatorWindows);
    SourceFile sourceUnix = createSourceFileFromGenerator(fileName, generatorUnix);

    assertEquals(sourceWindows.getCode(), sourceUnix.getCode());

    // Test from Generator with different charsets
    Generator generatorUtf8 = new Generator() {
      @Override
      public String getCode() {
        return "test123";
      }
    };
    Generator generatorIsoLatin1 = new Generator() {
      @Override
      public String getCode() {
        return "test123";
      }
    };
    SourceFile sourceUtf8 = createSourceFileFromGenerator(fileName, generatorUtf8);
    SourceFile sourceIsoLatin1 = createSourceFileFromGenerator(fileName, generatorIsoLatin1, "ISO-8859-1");

    assertEquals(sourceUtf8.getCode(), "test123");
    assertEquals(sourceIsoLatin1.getCode(), "test123");
    assertNotEquals(sourceUtf8.getCode(), sourceIsoLatin1.getCode());
  }

  @Override
  protected void testSourceFileSetOriginalPath() throws Exception {
    super.testSourceFileSetOriginalPath();

    String fileName = "SourceFileTest_OriginalPath.js";
    String originalPath = "SourceFileTest_OriginalPath.java";
    SourceFile source = createSourceFileFromCode(fileName, "test123");
    source.setOriginalPath(originalPath);

    assertEquals(source.getOriginalPath(), originalPath);
    assertNull(source.getFileName());
  }

  @Override
  protected void testSourceFileIsExtern() throws Exception {
    super.testSourceFileIsExtern();

    String fileName = "SourceFileTest_IsExtern.js";
    SourceFile source = createSourceFileFromCode(fileName, "test123");
    source.setIsExtern(true);

    assertTrue(source.isExtern());
  }

  @Override
  protected void testSourceFileGetCodeNoCache() throws Exception {
    super.testSourceFileGetCodeNoCache();

    String fileName = "SourceFileTest_GetCodeNoCache.js";
    SourceFile source = createSourceFileFromCode(fileName, "test123");
    String code = source.getCodeNoCache();

    assertEquals(code, "test123");
  }

  @Override
  protected void testSourceFileToString() throws Exception {
    super.testSourceFileToString();

    String fileName = "SourceFileTest_ToString.js";
    SourceFile source = createSourceFileFromCode(fileName, "test123");

    assertEquals(source.toString(), fileName);
  }

  @Override
  protected void testSourceFileClearCachedSource() throws Exception {
    super.testSourceFileClearCachedSource();

    String fileName = "SourceFileTest_ClearCachedSource.js";
    SourceFile source = createSourceFileFromCode(fileName, "test123");
    source.clearCachedSource();

    assertNull(source.getCode());
  }

  @Override
  protected void testSourceFileHasSourceInMemory() throws Exception {
    super.testSourceFileHasSourceInMemory();

    String fileName = "SourceFileTest_HasSourceInMemory.js";
    SourceFile source = createSourceFileFromCode(fileName, "test123");

    assertTrue(source.hasSourceInMemory());
  }

  @Override
  protected void testSourceFileGetLine() throws Exception {
    super.testSourceFileGetLine();

    String fileName = "SourceFileTest_GetLine.js";
    SourceFile source = createSourceFileFromCode(fileName, "test1\ntest2\ntest3");

    assertEquals(source.getLine(1), "test1");
    assertEquals(source.getLine(2), "test2");
    assertEquals(source.getLine(3), "test3");
    assertNull(source.getLine(4));
  }

  @Override
  protected void testSourceFileGetRegion() throws Exception {
    super.testSourceFileGetRegion();

    String fileName = "SourceFileTest_GetRegion.js";
    SourceFile source = createSourceFileFromCode(fileName, "test1\ntest2\ntest3");

    Region region1 = source.getRegion(1);
    Region region2 = source.getRegion(2);

    assertEquals(region1.getStartLine(), 1);
    assertEquals(region1.getEndLine(), 1);
    assertEquals(region1.getSourceCode(), "test1");

    assertEquals(region2.getStartLine(), 2);
    assertEquals(region2.getEndLine(), 2);
    assertEquals(region2.getSourceCode(), "test2");

    assertNull(source.getRegion(4));
  }

  public static class SimpleGenerator implements SourceFile.Generator {
    private final Generator generator;

    SimpleGenerator(Generator generator) {
      this.generator = generator;
    }

    @Override
    public String getCode() {
      return generator.getCode();
    }
  }
}