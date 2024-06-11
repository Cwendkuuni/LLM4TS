import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.InputStreams;
import com.google.common.io.Resources;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import com.google.javascript.jscomp.SourceFile.OnDisk;
import com.google.javascript.jscomp.testing.CompilerTestCase;

public class SourceFileTest extends CompilerTestCase {

  @Test
  public void testSourceFileFromCode() throws Exception {
    String fileName = "TestSourceFile";
    String code = "function test() { return 1; }";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testSourceFileFromFile() throws Exception {
    String fileName = "TestSourceFile.js";
    String filePath = getTestSourceDirectoryPath() + File.separator + fileName;
    SourceFile sourceFile = SourceFile.fromFile(fileName);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(Files.toString(new File(filePath), Charsets.UTF_8), sourceFile.getCode());
  }

  @Test
  public void testSourceFileFromInputStream() throws Exception {
    String fileName = "TestSourceFile.js";
    String code = "function test() { return 1; }";
    InputStream inputStream = new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testSourceFileFromFileWithCharset() throws Exception {
    String fileName = "TestSourceFile.js";
    String filePath = getTestSourceDirectoryPath() + File.separator + fileName;
    SourceFile sourceFile = SourceFile.fromFile(fileName, Charsets.UTF_16);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(Files.toString(new File(filePath), Charsets.UTF_8), sourceFile.getCode());
  }

  @Test
  public void testSourceFileFromReader() throws Exception {
    String fileName = "TestSourceFile.js";
    String code = "function test() { return 1; }";
    String testFilePath = getTestSourceDirectoryPath() + File.separator + fileName;
    File testFile = new File(testFilePath);
    Files.write(testFile, code.getBytes(StandardCharsets.UTF_8));

    String testFilePathWithCharset = testFilePath + "?charset=UTF-16";
    Reader reader = Resources.newReader(testFilePathWithCharset, Charsets.UTF_16);
    SourceFile sourceFile = SourceFile.fromReader(fileName, reader);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());

    Files.delete(testFile);
  }

  @Test
  public void testSourceFileFromGenerator() throws Exception {
    String fileName = "TestSourceFile";
    String originalPath = "TestSourceFileOriginalPath";
    Generator generator = mock(Generator.class);
    when(generator.getCode()).thenReturn("function test() { return 1; }");

    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);
    assertEquals(fileName, sourceFile.getName());
    assertEquals("function test() { return 1; }", sourceFile.getCode());
  }

  @Test
  public void testGetLine() throws Exception {
    String fileName = "TestSourceFile";
    String code = "function test() { return 1; }\nfunction test2() { return 2; }";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    String line1 = sourceFile.getLine(1);
    assertEquals("function test() { return 1; }", line1);

    String line2 = sourceFile.getLine(2);
    assertEquals("function test2() { return 2; }", line2);

    String line3 = sourceFile.getLine(3);
    assertNull(line3);
  }

  @Test
  public void testGetRegion() throws Exception {
    String fileName = "TestSourceFile";
    String code = "function test() { return 1; }\nfunction test2() { return 2; }";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    Region region1 = sourceFile.getRegion(1);
    assertEquals(1, region1.getLineNumber());
    assertEquals(Arrays.asList("function test() { return 1; }"), region1.getLines());

    Region region2 = sourceFile.getRegion(2);
    assertEquals(2, region2.getLineNumber());
    assertEquals(Arrays.asList("function test2() { return 2; }"), region2.getLines());

    Region region3 = sourceFile.getRegion(3);
    assertNull(region3);
  }

  @Test
  public void testOnDiskSourceFile() throws Exception {
    String fileName = "TestOnDiskSourceFile";
    String code = "function test() { return 1; }";
    File testFile = new File(getTestSourceDirectoryPath() + File.separator + fileName);
    Files.write(testFile, code.getBytes(StandardCharsets.UTF_8));

    SourceFile sourceFile = SourceFile.fromFile(fileName);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());

    Files.delete(testFile);
  }

  @Test
  public void testPreloadedSourceFile() throws Exception {
    String fileName = "TestPreloadedSourceFile";
    String code = "function test() { return 1; }";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    sourceFile.getCodeReader().close();
    sourceFile.getCodeReader().transferTo(outputStream);
    byte[] codeBytes = outputStream.toByteArray();
    assertEquals(code.getBytes(StandardCharsets.UTF_8), codeBytes);
  }

  @Test
  public void testGeneratedSourceFile() throws Exception {
    String fileName = "TestGeneratedSourceFile";
    Generator generator = mock(Generator.class);
    when(generator.getCode()).thenReturn("function test() { return 1; }");

    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);
    assertEquals(fileName, sourceFile.getName());
    assertEquals("function test() { return 1; }", sourceFile.getCode());

    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }
}

class SimpleRegion implements Region {
  private final int lineNumber;
  private final List<String> lines;

  public SimpleRegion(int lineNumber, int lineCount, String content) {
    this.lineNumber = lineNumber;
    this.lines = Arrays.asList(content.split("\n"));
  }

  @Override
  public int getLineNumber() {
    return lineNumber;
  }

  @Override
  public List<String> getLines() {
    return lines;
  }
}