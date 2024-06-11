package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.OnDisk;

public class SourceFileTest {

  @Test
  public void testFromFile() throws IOException {
    String fileName = "SourceFileTest.java";
    SourceFile sourceFile = SourceFile.fromFile(fileName);

    assertEquals("SourceFileTest.java", sourceFile.getName());
    assertNull(sourceFile.getCode());

    String code = sourceFile.getCode();
    assertEquals("SourceFileTest.java", code);
  }

  @Test
  public void testFromCode() {
    String fileName = "SourceFileTest.java";
    String code = "package com.google.javascript.jscomp.test;\n" +
        "public class SourceFileTest {}\n";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    assertEquals("SourceFileTest.java", sourceFile.getName());
    assertSame(code, sourceFile.getCode());
  }

  @Test
  public void testFromFileWithCharset() throws IOException {
    String fileName = "SourceFileTest.java";
    SourceFile sourceFile = SourceFile.fromFile(new File(fileName));

    assertEquals("SourceFileTest.java", sourceFile.getName());
    assertNull(sourceFile.getCode());

    code = Files.toString(new File(fileName), Charsets.UTF_8);
    sourceFile = SourceFile.fromCode(fileName, code.getBytes(Charsets.UTF_8));

    assertEquals("SourceFileTest.java", sourceFile.getName());
    assertSame(code, sourceFile.getCode());
  }

  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "SourceFileTest.java";
    File file = new File(fileName);
    InputStream is = new FileInputStream(file);

    SourceFile sourceFile = SourceFile.fromInputStream(fileName, is);

    assertEquals("SourceFileTest.java", sourceFile.getName());
    assertSame(Files.toString(file, Charsets.UTF_8), sourceFile.getCode());
  }

  @Test
  public void testFromGenerator() {
    String fileName = "SourceFileTest.java";
    String code = "package com.google.javascript.jscomp.test;\n" +
        "public class SourceFileTest {}\n";

    SourceFile sourceFile = SourceFile.fromGenerator(fileName, () -> code);

    assertEquals("SourceFileTest.java", sourceFile.getName());
    assertSame(code, sourceFile.getCode());
  }

  @Test
  public void testFromFileWithOriginalPath() throws IOException {
    String fileName = "SourceFileTest.java";
    String originalPath = ".";

    SourceFile sourceFile = SourceFile.fromFile(fileName, originalPath);

    assertEquals("SourceFileTest.java", sourceFile.getName());
    assertSame(originalPath, sourceFile.getOriginalPath());
    assertNull(sourceFile.getCode());

    code = Files.toString(new File(fileName), Charsets.UTF_8);
    sourceFile = SourceFile.fromCode(fileName, code.getBytes(Charsets.UTF_8));

    assertEquals("SourceFileTest.java", sourceFile.getName());
    assertEquals(originalPath, sourceFile.getOriginalPath());
    assertSame(code, sourceFile.getCode());
  }

  @Test
  public void testOnDisk() throws IOException {
    String fileName = "SourceFileTest.java";
    File file = new File(fileName);

    SourceFile sourceFile = new OnDisk(file);

    assertEquals("SourceFileTest.java", sourceFile.getName());
    assertNull(sourceFile.getCode());

    Files.write(file.toPath(), "package com.google.javascript.jscomp.test;\n" +
        "public class SourceFileTest {}\n".getBytes(Charsets.UTF_8));

    sourceFile = SourceFile.fromFile(fileName);

    assertEquals("SourceFileTest.java", sourceFile.getName());
    assertSame("package com.google.javascript.jscomp.test;\n" +
        "public class SourceFileTest {}\n", sourceFile.getCode());
  }

  @Test
  public void testGenerated() {
    String fileName = "SourceFileTest.java";
    String code = "package com.google.javascript.jscomp.test;\n" +
        "public class SourceFileTest {}\n";

    Generator generator = () -> code;
    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);

    assertEquals("SourceFileTest.java", sourceFile.getName());
    assertSame(code, sourceFile.getCode());
  }

  @Test
  public void testGetLine() throws IOException {
    String fileName = "SourceFileTest.java";
    String code = "package com.google.javascript.jscomp.test;\n" +
        "public class SourceFileTest {\n" +
        "  public static void main(String[] args) {\n" +
        "    System.out.println(\"Hello World\");\n" +
        "  }\n" +
        "}\n";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    assertEquals("public class SourceFileTest {\n" +
        "  public static void main(String[] args) {\n" +
        "    System.out.println(\"Hello World\");\n" +
        "  }\n}", sourceFile.getCode());

    assertEquals("public class SourceFileTest {", sourceFile.getLine(1));
    assertEquals("  public static void main(String[] args) {", sourceFile.getLine(3));
    assertEquals("    System.out.println(\"Hello World\");", sourceFile.getLine(5));
    assertEquals(null, sourceFile.getLine(10));
  }

  @Test
  public void testGetRegion() throws IOException {
    String fileName = "SourceFileTest.java";
    String code = "package com.google.javascript.jscomp.test;\n" +
        "public class SourceFileTest {\n" +
        "  public static void main(String[] args) {\n" +
        "    System.out.println(\"Hello World\");\n" +
        "  }\n" +
        "}\n";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    assertEquals("public class SourceFileTest {\n" +
        "  public static void main(String[] args) {\n" +
        "    System.out.println(\"Hello World\");\n" +
        "  }\n}", sourceFile.getCode());

    Region region = sourceFile.getRegion(1);
    assertEquals("public class SourceFileTest {", region.getCode());
    assertEquals(1, region.getStartLine());
    assertEquals(1, region.getEndLine());

    region = sourceFile.getRegion(3);
    assertEquals("  public static void main(String[] args) {", region.getCode());
    assertEquals(3, region.getStartLine());
    assertEquals(5, region.getEndLine());

    region = sourceFile.getRegion(6);
    assertEquals(null, region.getCode());
  }
}