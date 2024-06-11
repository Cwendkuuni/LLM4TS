import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.io.CharStreams;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.FileFixture;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SourceFileTest {

  private static final String TEST_FILE = "test.js";
  private static final String TEST_STRING = "var x = 1;";

  @Parameterized.Parameters(name = "testCase{index}: {0}")
  public static Iterable<Object[]> data() {
    return FileFixture.fileList(TEST_FILE);
  }

  private File testFile;

  public SourceFileTest(File testFile) {
    this.testFile = testFile;
  }

  @Test
  public void testFileName() {
    SourceFile file = new SourceFile(TEST_FILE);
    assertEquals(TEST_FILE, file.getName());
  }

  @Test
  public void testGetCode() throws IOException {
    SourceFile fileFromFile = new SourceFile(TEST_FILE);
    assertEquals(getCodeFromFile(fileFromFile), fileFromFile.getCode());

    SourceFile fileFromCode = new SourceFile(TEST_FILE, TEST_STRING);
    assertEquals(TEST_STRING, fileFromCode.getCode());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    SourceFile fileFromFile = new SourceFile(TEST_FILE);
    SourceFile fileFromCode = new SourceFile(TEST_FILE, TEST_STRING);

    Reader readerFromFile = fileFromFile.getCodeReader();
    String codeFromFile = CharStreams.toString(readerFromFile);
    assertEquals(getCodeFromFile(fileFromFile), codeFromFile);

    Reader readerFromCode = fileFromCode.getCodeReader();
    String codeFromCode = CharStreams.toString(readerFromCode);
    assertEquals(TEST_STRING, codeFromCode);
  }

  @Test
  public void testGetLine() throws IOException {
    SourceFile file = new SourceFile(TEST_FILE);
    String line1 = getLineFromFile(file, 1);
    assertEquals("var", line1);

    String line2 = getLineFromFile(file, 2);
    assertNull(line2);
  }

  @Test
  public void testGetRegion() throws IOException {
    SourceFile file = new SourceFile(TEST_FILE);
    Region region = file.getRegion(1);
    assertEquals(1, region.getBeginLine());
    assertEquals(1, region.getEndLine());
    assertEquals("var x = 1;\n", region.getSource());
  }

  @Test
  public void testSetOriginalPath() {
    SourceFile file = new SourceFile(TEST_FILE);
    file.setOriginalPath("test.jar");
    assertEquals("test.jar", file.getOriginalPath());
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    SourceFile file = new SourceFile(TEST_FILE);
    int numLines = file.getNumLines();
    int[] lineOffsets = file.getLineOffsets();

    for (int i = 0; i < numLines; ++i) {
      int offset = lineOffsets[i];
      int lineNumber = i + 1;
      String line = getLineFromFile(file, lineNumber);
      int length = line.length();
      int expectedOffset = (i == 0) ? 0 : lineOffsets[i - 1] + length + 1;
      assertEquals(expectedOffset, offset);
    }
  }

  @Test
  public void testIsExtern() {
    SourceFile file = new SourceFile(TEST_FILE);
    assertFalse(file.isExtern());

    SourceFile externFile = new SourceFile(TEST_FILE + "_extern.js");
    assertTrue(externFile.isExtern());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    SourceFile file = new SourceFile(TEST_FILE);
    String code = file.getCode();

    file.clearCachedSource();

    String code2 = file.getCode();
    assertNotSame(code, code2);
  }

  @Test
  public void testHasSourceInMemory() throws IOException {
    SourceFile fileFromFile = new SourceFile(TEST_FILE);
    assertFalse(fileFromFile.hasSourceInMemory());

    SourceFile fileFromCode = new SourceFile(TEST_FILE, TEST_STRING);
    assertTrue(fileFromCode.hasSourceInMemory());
  }

  @Test
  public void testToString() {
    SourceFile file = new SourceFile(TEST_FILE);
    assertEquals(TEST_FILE, file.toString());
  }

  private static String getCodeFromFile(SourceFile file) throws IOException {
    return file.getCode();
  }

  private static String getLineFromFile(SourceFile file, int lineNumber)
      throws IOException {
    return file.getLine(lineNumber);
  }

  private static String getCodeFromFile(File file) throws IOException {
    return new String(Files.toByteArray(file), Charsets.UTF_8);
  }
}