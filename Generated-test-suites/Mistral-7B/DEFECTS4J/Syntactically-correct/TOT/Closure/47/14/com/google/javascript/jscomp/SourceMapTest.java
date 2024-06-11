import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceMap.*;
import static com.google.javascript.jscomp.NodeUtil.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * Tests the {@link SourceMap} class.
 */
public class SourceMapTest {

  @Test
  public void testDefaultSourceMap() throws IOException {
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(Format.DEFAULT));

    Node node = createSimpleNode("example.js", "function foo() {}");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "sourceMap.js");

    String result = out.toString();
    assertTrue(result.contains("{\"version\":1,\"file\":\"example.js\""));
    assertTrue(result.contains("\"sourcesContent\":[],"));
    assertTrue(result.contains("\"sourceRoot\":\""));

    String expected = "{\"version\":1,\"file\":\"example.js\",\"sourcesContent\":[],"
        + "\"sourceRoot\":\"\",\"mappings\":[{\"original\":{\"line\":1,\"column\":0},"
        + "\"generated\":{\"line\":1,\"column\":0}}]}";
    assertEquals(expected, result);
  }

  @Test
  public void testCustomSourceMap() throws IOException {
    SourceMap sourceMap = new SourceMap(new SourceMapGeneratorFactory() {
      @Override public SourceMapGenerator getInstance(SourceMapFormat format) {
        if (format == SourceMapFormat.V2) {
          return new SourceMapGeneratorV2();
        } else if (format == SourceMapFormat.V3) {
          return new SourceMapGeneratorV3();
        } else {
          throw new UnsupportedOperationException("Unsupported format");
        }
      }
    });

    Node node = createSimpleNode("example.js", "function foo() {}");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "sourceMap.js");

    String result = out.toString();
    // For version 2 and 3 source maps, the sourcesContent array should be empty
    String expected = "{\"version\":2,\"file\":\"example.js\",\"sources\":[],\"mappings\":[{\"original\":{\"line\":1,\"column\":0},"
        + "\"generated\":{\"line\":1,\"column\":0}}]}";
    if (sourceMap.getInstance().getFormat() == SourceMapFormat.V3) {
      expected = "{\"version\":3,\"file\":\"example.js\",\"sources\":[],\"sourcesContent\":[],\"mappings\":[{\"original\":{\"line\":1,\"column\":0},"
          + "\"generated\":{\"line\":1,\"column\":0}}]}";
    }
    assertEquals(expected, result);
  }

  @Test
  public void testPrefixMappings() throws IOException {
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(Format.DEFAULT));

    // Set prefix mappings
    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("src/main/js/", ""),
        new LocationMapping("src/test/js/", "test-"));
    sourceMap.setPrefixMappings(prefixMappings);

    Node node = createSimpleNode("src/main/js/example.js", "function foo() {}");
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "sourceMap.js");

    String result = out.toString();
    String expected = "{\"version\":1,\"file\":\"example.js\",\"sourcesContent\":[],"
        + "\"sourceRoot\":\"\",\"mappings\":[{\"original\":{\"line\":1,\"column\":0},"
        + "\"generated\":{\"line\":1,\"column\":0}}],\"sources\":[\"src/main/js/\"]}";
    assertEquals(expected, result);
  }

  @Test(expected = RuntimeException.class)
  public void testNullNode() {
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(Format.DEFAULT));
    sourceMap.addMapping(null, new FilePosition(1, 1), new FilePosition(1, 5));
  }

  @Test(expected = RuntimeException.class)
  public void testNullStartPosition() {
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(Format.DEFAULT));
    sourceMap.addMapping(createSimpleNode("example.js", "function foo() {}"), null, new FilePosition(1, 5));
  }

  @Test(expected = RuntimeException.class)
  public void testNullEndPosition() {
    SourceMap sourceMap = new SourceMap(SourceMapGeneratorFactory.getInstance(Format.DEFAULT));
    sourceMap.addMapping(createSimpleNode("example.js", "function foo() {}"), new FilePosition(1, 1), null);
  }

  private Node createSimpleNode(String sourceFileName, String nodeCode) {
    Node node = new Node();
    node.setSourceFileName(sourceFileName);
    node.setCode(nodeCode);
    return node;
  }
}