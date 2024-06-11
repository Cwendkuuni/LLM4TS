import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class SourceMapTest {

  @Test
  public void testDefaultSourceMapGeneration() throws IOException {
    SourceMap sourceMap = Format.DEFAULT.getInstance();
    Node node = Node.newIntLiteral(1);

    String sourceFile = "TestSource.java";

    FilePosition outputStartPosition = new FilePosition(1, 10);
    FilePosition outputEndPosition = new FilePosition(1, 15);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(new ByteArrayOutputStream(), "TestOutput.js");

    String generatedSourceMap = new String(
        ((ByteArrayOutputStream) sourceMap.getOutputStream()).toByteArray());

    Map<String, Object> expectedSourceMap = ImmutableMap.of(
        "sources", ImmutableList.of(sourceFile),
        "file", "TestOutput.js",
        "mappings", ImmutableList.of(
            "AAAA,TestSource.java,TestOutput.js,1,10,1,15"));

    Map<String, Object> actualSourceMap = parseSourceMap(generatedSourceMap);

    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testV1SourceMapGeneration() throws IOException {
    SourceMap sourceMap = Format.V1.getInstance();
    Node node = Node.newIntLiteral(1);

    String sourceFile = "TestSource.java";

    FilePosition outputStartPosition = new FilePosition(1, 10);
    FilePosition outputEndPosition = new FilePosition(1, 15);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(new ByteArrayOutputStream(), "TestOutput.js");

    String generatedSourceMap = new String(
        ((ByteArrayOutputStream) sourceMap.getOutputStream()).toByteArray());

    Map<String, Object> expectedSourceMap = ImmutableMap.of(
        "Version", 1,
        "Sources", ImmutableList.of(sourceFile),
        "File", "TestOutput.js",
        "mappings", ImmutableList.of(
            "0.TestOutput.js:1:10-1:15",
            "TestSource.java:1:0",
            "TestSource.java:1:10-1:15"));

    Map<String, Object> actualSourceMap = parseSourceMap(generatedSourceMap);

    assertEquals(expectedSourceMap, actualSourceMap);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullNode() {
    SourceMap sourceMap = Format.DEFAULT.getInstance();

    Node node = null;

    FilePosition outputStartPosition = new FilePosition(1, 10);
    FilePosition outputEndPosition = new FilePosition(1, 15);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test(expected = IOException.class)
  public void testAppendToWithNullOutputStream() throws IOException {
    SourceMap sourceMap = Format.DEFAULT.getInstance();

    Writer outputStream = null;

    sourceMap.appendTo(outputStream, "TestOutput.js");
  }

  @Test
  public void testReset() throws IOException {
    SourceMap sourceMap = Format.DEFAULT.getInstance();

    Node node = Node.newIntLiteral(1);

    String sourceFile = "TestSource.java";

    FilePosition outputStartPosition = new FilePosition(1, 10);
    FilePosition outputEndPosition = new FilePosition(1, 15);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMap.appendTo(new ByteArrayOutputStream(), "TestOutput.js");

    String generatedSourceMap = new String(
        ((ByteArrayOutputStream) sourceMap.getOutputStream()).toByteArray());

    sourceMap.reset();

    String expectedSourceMap = "";
    Map<String, Object> actualSourceMap = parseSourceMap(generatedSourceMap);

    assertEquals(expectedSourceMap, actualSourceMap);
  }

  private Map<String, Object> parseSourceMap(String sourceMapString) {
    // Implement a source map parser here.
    // For simplicity, this test assumes an empty string as an empty source map.
    return ImmutableMap.of();
  }
}