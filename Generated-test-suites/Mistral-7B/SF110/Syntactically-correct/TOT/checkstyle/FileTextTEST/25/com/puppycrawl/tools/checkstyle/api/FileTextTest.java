package com.puppycrawl.tools.checkstyle.api;

import static org.junit.Assert.*;
import static com.google.common.io.Files.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FileTextTest {
  private File file;
  private FileText fileText;
  private List<String> lines;

  @Before
  public void setUp() throws Exception {
    file = new File("path/to/testfile.txt");
    lines = Files.readLines(file, Charsets.UTF_8);
  }

  @Test
  public void testFileTextConstruction() throws Exception {
    fileText = new FileText(file, "UTF-8");
    assertEquals(file, fileText.getFile());
    assertEquals("UTF-8", fileText.getCharset().name());
    assertEquals(readLinesFromFile(file), fileText.toLinesArray());
  }

  @Test
  public void testCopyConstructor() {
    fileText = new FileText(file, "UTF-8");
    FileText copy = new FileText(fileText);
    assertEquals(fileText.getFile(), copy.getFile());
    assertEquals(fileText.getCharset(), copy.getCharset());
    assertArrayEquals(fileText.lines, copy.lines);
  }

  @Test
  public void testPrivateConstructor() {
    fileText = new FileText(file, lines);
    assertEquals(file, fileText.getFile());
    assertNull(fileText.getCharset());
    assertArrayEquals(lines.toArray(new String[lines.size()]), fileText.lines);
  }

  private FileText fromLines(File file, List<String> lines) {
    return new FileText(file, lines);
  }

  private List<String> readLinesFromFile(File file) throws IOException {
    return new ArrayList<String>(Files.readLines(file, Charsets.UTF_8));
  }
}