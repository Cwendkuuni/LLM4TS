package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testutil.Platform;

import java.io.File;
import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link SourceFile}.
 */
public class SourceFileTest {

  private static final String TEST_FILE = "testFile.js";
  private static final String TEST_FILE_EXISTS = "testFileExists.js";
  private static final String TEST_FILE_MISSING = "testFileMissing.js";
  private static final String TEST_FILE_IN_JAR = "testFileInJar.jar!testFileInJar.js";

  private static final String TEST_CODE = "var x = 1;";

  private File testDir;
  private SourceFile testFile;
  private SourceFile testFileExists;
  private SourceFile testFileMissing;
  private SourceFile testFileInJar;

  @Before
  public void setUp() throws Exception {
    testDir = Platform.testTempDir();

    File testFileFile = new File(testDir, TEST_FILE);
    Files.write(testFileFile, TEST_CODE.getBytes(), Charsets.UTF_8);

    testFile = new SourceFile(TEST_FILE);
    testFileExists = new SourceFile(TEST_FILE_EXISTS);
    testFileMissing = new SourceFile(TEST_FILE_MISSING);
    testFileInJar = new SourceFile(TEST_FILE_IN_JAR);
  }

  @After
  public void tearDown() throws Exception {
    if (testFile != null) {
      testFile.clearCachedSource();
    }
    if (testFileFile != null && testFileFile.exists()) {
      testFileFile.delete();
    }
  }

  @Test
  public void testBasic() throws IOException {
    String code = testFile.getCode();
    assertEquals(TEST_CODE, code);
  }

  @Test(expected = IOException.class)
  public void testMissingFile() throws IOException {
    String code = testFileMissing.getCode();
  }

  @Test
  public void testExtern() throws IOException {
    testFileExists.setIsExtern(true);
    String code = testFileExists.getCode();
    assertNull(code);
  }

  @Test
  public void testFindLineOffset() throws IOException {
    int offset = testFile.getLineOffset(1);
    assertEquals(2, offset);

    String line1 = testFile.getLine(1);
    assertEquals(TEST_CODE.substring(0, 1), line1);
  }

  @Test
  public void testFindLineOffsetWithLongLines() throws IOException {
    String longLineCode = "var x = function() {" +
                          "this.x = 1;" +
                          "this.y = 2;" +
                          "this.z = 3;" +
                          "this.w = 4;" +
                          "this.v = 5;" +
                          "this.u = 6;" +
                          "this.t = 7;" +
                          "this.s = 8;" +
                          "this.r = 9;" +
                          "this.q = 10;" +
                          "this.p = 11;" +
                          "this.o = 12;" +
                          "this.n = 13;" +
                          "this.m = 14;" +
                          "this.l = 15;" +
                          "this.k = 16;" +
                          "this.j = 17;" +
                          "this.i = 18;" +
                          "this.h = 19;" +
                          "this.g = 20;" +
                          "this.f = 21;" +
                          "this.e = 22;" +
                          "this.d = 23;" +
                          "this.c = 24;" +
                          "this.b = 25;" +
                          "this.a = 26;" +
                          "};";

    SourceFile longLineFile = new SourceFile("testLongLine.js");
    longLineFile.setCode(longLineCode);

    int offset = longLineFile.getLineOffset(1);
    assertEquals(1, offset % 2); // odd number because of the newline character
    int length = longLineCode.split("\n").length;
    assertEquals(length, offset / 2 + 1);

    String line1 = longLineFile.getLine(1);
    assertEquals(longLineCode.substring(0, longLineCode.indexOf("\n")), line1);
  }

  @Test
  public void testFindRegion() throws IOException {
    int startLine = 2;
    int endLine = 5;

    Region region = testFile.getRegion(startLine);
    assertEquals(TEST_CODE.substring(0, TEST_CODE.indexOf("\n", TEST_CODE.indexOf("\n") + 1) + 1), region.getCode());
    assertEquals(startLine, region.getStartLineNumber());
    assertEquals(endLine, region.getEndLineNumber());

    region = testFile.getRegion(startLine + 1);
    assertEquals(TEST_CODE.substring(TEST_CODE.indexOf("\n") + 1), region.getCode());
    assertEquals(startLine + 1, region.getStartLineNumber());
    assertEquals(endLine, region.getEndLineNumber());

    region = testFile.getRegion(endLine + 1);
    assertNull(region.getCode());
  }

  @Test
  public void testFindRegionWithLongLines() throws IOException {
    String longLineCode = "var x = function() {" +
                          "this.x = 1;" +
                          "this.y = 2;" +
                          "this.z = 3;" +
                          "this.w = 4;" +
                          "this.v = 5;" +
                          "this.u = 6;" +
                          "this.t = 7;" +
                          "this.s = 8;" +
                          "this.r = 9;" +
                          "this.q = 10;" +
                          "this.p = 11;" +
                          "this.o = 12;" +
                          "this.n = 13;" +
                          "this.m = 14;" +
                          "this.l = 15;" +
                          "this.k = 16;" +
                          "this.j = 17;" +
                          "this.i = 18;" +
                          "this.h = 19;" +
                          "this.g = 20;" +
                          "this.f = 21;" +
                          "this.e = 22;" +
                          "this.d = 23;" +
                          "this.c = 24;" +
                          "this.b = 25;" +
                          "this.a = 26;" +
                          "};";

    SourceFile longLineFile = new SourceFile("testLongLine.js");
    longLineFile.setCode(longLineCode);

    int startLine = 4;
    int endLine = 8;

    Region region = longLineFile.getRegion(startLine);
    assertEquals(longLineCode.substring(0, longLineCode.indexOf("\n", longLineCode.indexOf("\n") + 1) + 1), region.getCode());
    assertEquals(startLine, region.getStartLineNumber());
    assertEquals(endLine, region.getEndLineNumber());

    region = longLineFile.getRegion(startLine + 1);
    assertEquals(longLineCode.substring(longLineCode.indexOf("\n") + 1), region.getCode());
    assertEquals(startLine + 1, region.getStartLineNumber());
    assertEquals(endLine, region.getEndLineNumber());

    region = longLineFile.getRegion(endLine + 1);
    assertNull(region.getCode());
  }

  @Test
  public void testFindLineWithLongLines() throws IOException {
    String longLineCode = "var x = function() {" +
                          "this.x = 1;" +
                          "this.y = 2;" +
                          "this.z = 3;" +
                          "this.w = 4;" +
                          "this.v = 5;" +
                          "this.u = 6;" +
                          "this.t = 7;" +
                          "this.s = 8;" +
                          "this.r = 9;" +
                          "this.q = 10;" +
                          "this.p = 11;" +
                          "this.o = 12;" +
                          "this.n = 13;" +
                          "this.m = 14;" +
                          "this.l = 15;" +
                          "this.k = 16;" +
                          "this.j = 17;" +
                          "this.i = 18;" +
                          "this.h = 19;" +
                          "this.g = 20;" +
                          "this.f = 21;" +
                          "this.e = 22;" +
                          "this.d = 23;" +
                          "this.c = 24;" +
                          "this.b = 25;" +
                          "this.a = 26;" +
                          "};";

    SourceFile longLineFile = new SourceFile("testLongLine.js");
    longLineFile.setCode(longLineCode);

    int lineNumber = 10;
    String expectedLine = "this.q = 10;";

    String line = longLineFile.getLine(lineNumber);
    assertEquals(expectedLine, line);
  }

  @Test
  public void testGetCodeReader() throws IOException {
    Reader reader = testFile.getCodeReader();
    assertEquals(TEST_CODE, CharStreams.toString(reader));
  }

  @Test
  public void testGetCodeReaderWithLongLines() throws IOException {
    String longLineCode = "var x = function() {" +
                          "this.x = 1;" +
                          "this.y = 2;" +
                          "this.z = 3;" +
                          "this.w = 4;" +
                          "this.v = 5;" +
                          "this.u = 6;" +
                          "this.t = 7;" +
                          "this.s = 8;" +
                          "this.r = 9;" +
                          "this.q = 10;" +
                          "this.p = 11;" +
                          "this.o = 12;" +
                          "this.n = 13;" +
                          "this.m = 14;" +
                          "this.l = 15;" +
                          "this.k = 16;" +
                          "this.j = 17;" +
                          "this.i = 18;" +
                          "this.h = 19;" +
                          "this.g = 20;" +
                          "this.f = 21;" +
                          "this.e = 22;" +
                          "this.d = 23;" +
                          "this.c = 24;" +
                          "this.b = 25;" +
                          "this.a = 26;" +
                          "};";

    SourceFile longLineFile = new SourceFile("testLongLine.js");
    longLineFile.setCode(longLineCode);

    Reader reader = longLineFile.getCodeReader();
    assertEquals(longLineCode, CharStreams.toString(reader));
  }

  @Test
  public void testGetOriginalPath() {
    assertEquals(TEST_FILE, testFile.getOriginalPath());
    assertEquals(TEST_FILE_EXISTS, testFileExists.getOriginalPath());
  }

  @Test
  public void testFromFile() throws IOException {
    SourceFile fileSource = SourceFile.fromFile(TEST_FILE);
    assertEquals(TEST_FILE, fileSource.getName());
    assertEquals(TEST_CODE, fileSource.getCode());
  }

  @Test
  public void testFromFileWithCharset() throws IOException {
    SourceFile fileSource = SourceFile.fromFile(TEST_FILE, Charsets.UTF_8);
    assertEquals(TEST_FILE, fileSource.getName());
    assertEquals(TEST_CODE, fileSource.getCode());
  }

  @Test
  public void testFromFileWithCustomCharset() throws IOException {
    SourceFile fileSource = SourceFile.fromFile(TEST_FILE, new CustomCharset());
    assertEquals(TEST_FILE, fileSource.getName());
    assertEquals(TEST_CODE, fileSource.getCode());
  }

  @Test(expected = IOException.class)
  public void testFromFileMissing() throws IOException {
    SourceFile fileSource = SourceFile.fromFile(TEST_FILE_MISSING);
  }

  @Test
  public void testFromInputStream() throws IOException {
    String inputCode = TEST_CODE;

    InputStream inputStream = new ByteArrayInputStream(inputCode.getBytes(Charsets.UTF_8));
    SourceFile inputSource = SourceFile.fromInputStream(TEST_FILE, inputStream);
    assertEquals(TEST_FILE, inputSource.getName());
    assertEquals(inputCode, inputSource.getCode());
  }

  @Test
  public void testFromInputStreamWithCustomCharset() throws IOException {
    String inputCode = TEST_CODE;

    InputStream inputStream = new ByteArrayInputStream(inputCode.getBytes(Charsets.UTF_8));
    SourceFile inputSource = SourceFile.fromInputStream(TEST_FILE, Charsets.UTF_16, inputStream);
    assertEquals(TEST_FILE, inputSource.getName());
    assertEquals(inputCode, inputSource.getCode());
  }

  @Test(expected = IOException.class)
  public void testFromInputStreamMissing() throws IOException {
    InputStream inputStream = new ByteArrayInputStream(new byte[0]);
    SourceFile inputSource = SourceFile.fromInputStream(TEST_FILE, inputStream);
  }

  @Test
  public void testFromReader() throws IOException {
    String inputCode = TEST_CODE;

    Reader reader = new StringReader(inputCode);
    SourceFile inputSource = SourceFile.fromReader(TEST_FILE, reader);
    assertEquals(TEST_FILE, inputSource.getName());
    assertEquals(inputCode, inputSource.getCode());
  }

  @Test
  public void testFromGenerator() throws IOException {
    String generatorCode = "return 'var x = 1;';";
    Generator generator = () -> generatorCode;
    SourceFile generatorSource = SourceFile.fromGenerator(TEST_FILE, generator);
    assertEquals(TEST_FILE, generatorSource.getName());
    assertEquals(generatorCode, generatorSource.getCode());
  }

  @Test
  public void testFromJar() throws IOException {
    SourceFile jarSource = SourceFile.fromFile(new File("testFileInJar.jar"));
    assertEquals(TEST_FILE_IN_JAR, jarSource.getName());
    assertEquals(TEST_CODE, jarSource.getCode());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String cachedCode = testFile.getCode();
    testFile.clearCachedSource();
    String newCode = testFile.getCode();
    assertNotEquals(cachedCode, newCode);
  }
}