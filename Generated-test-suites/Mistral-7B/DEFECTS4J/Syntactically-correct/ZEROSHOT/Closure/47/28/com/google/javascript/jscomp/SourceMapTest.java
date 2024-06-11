import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMapGenerator;
import com.google.javascript.jscomp.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.SourceMapFormat;
import com.google.javascript.rhino.NodeFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {

  private SourceMapGeneratorFactory sourceMapGeneratorFactory;
  private SourceMapGenerator generatorV1;
  private SourceMapGenerator generatorV2;
  private SourceMap sourceMap;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() throws Exception {
    sourceMapGeneratorFactory = mock(SourceMapGeneratorFactory.class);
    generatorV1 = mock(SourceMapGeneratorV1.class);
    generatorV2 = mock(SourceMapGeneratorV2.class);

    when(sourceMapGeneratorFactory.getInstance(SourceMapFormat.V1)).thenReturn(generatorV1);
    when(sourceMapGeneratorFactory.getInstance(SourceMapFormat.V2)).thenReturn(generatorV2);

    sourceMap = new SourceMap(sourceMapGeneratorFactory);
    nodeFactory = new NodeFactory();
  }

  @After
  public void tearDown() throws Exception {
    sourceMap = null;
    nodeFactory = null;
  }

  @Test
  public void testNewSourceMap() {
    assertNotNull(sourceMap);
  }

  @Test
  public void testAddMapping() throws IOException {
    String sourceFile = "test.js";
    Node node = nodeFactory.createCall(null, null, null, null, null, null, null, null, null, 1, null, null, null, null, null, null);
    node.setSourceFileName(sourceFile);
    node.setLineno(1);
    node.setCharno(1);

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 1);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "");

    String actualSourceMap = out.toString();

    String expectedSourceMapV1 = "{\"version\":1,\"file\":\"test.js\",\"sources\":[],\"sourcesContent\":{},\"mappings\":[";
    String expectedSourceMapV2 = "{\"version\":2,\"file\":\"test.js\",\"sourceRoot\":\"\",\"sources\":[],\"names\":[],\"mappings\":[";

    assertSourceMapEquals(expectedSourceMapV1, actualSourceMap);
    assertSourceMapEquals(expectedSourceMapV2, actualSourceMap);
  }

  @Test
  public void testAddMappingWithDetailLevel() throws IOException {
    String sourceFile = "test.js";
    Node node = nodeFactory.createCall(null, null, null, null, null, null, null, null, null, 1, null, null, null, null, null, null);
    node.setSourceFileName(sourceFile);
    node.setLineno(1);
    node.setCharno(1);

    SourceMap.Format format = SourceMap.Format.V1;
    SourceMap sourceMap = SourceMap.Format.FORMAT.getInstance().getInstance();

    sourceMap.setDetailLevel(SourceMap.DetailLevel.ALL);

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 1);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "");

    String actualSourceMap = out.toString();

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sources\":[],\"sourcesContent\":{},\"mappings\":[{\"original\":{\"source\":\"test.js\",\"line\":1,\"column\":1},\"generated\":{\"line\":1,\"column\":1}}]}";

    assertSourceMapEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testAddMappingWithPrefixMappings() throws IOException {
    List<SourceMap.LocationMapping> prefixMappings = Lists.newArrayList(
        new SourceMap.LocationMapping("test/", "src/")
    );

    String sourceFile = "test.js";
    Node node = nodeFactory.createCall(null, null, null, null, null, null, null, null, null, 1, null, null, null, null, null, null);
    node.setSourceFileName(sourceFile);
    node.setLineno(1);
    node.setCharno(1);

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 1);

    SourceMap sourceMap = new SourceMap(sourceMapGeneratorFactory);
    sourceMap.setPrefixMappings(prefixMappings);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "");

    String actualSourceMap = out.toString();

    String expectedSourceMap = "{\"version\":1,\"file\":\"src/test.js\",\"sources\":[],\"sourcesContent\":{},\"mappings\":[{\"original\":{\"source\":\"test.js\",\"line\":1,\"column\":1},\"generated\":{\"line\":1,\"column\":1}}]}";

    assertSourceMapEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testValidate() throws IOException {
    SourceMap sourceMap = new SourceMap(sourceMapGeneratorFactory);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "");

    sourceMap.validate(true);

    String actualSourceMap = out.toString();

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sources\":[],\"sourcesContent\":{},\"mappings\":[]}";

    assertSourceMapEquals(expectedSourceMap, actualSourceMap);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateInvalidSourceMap() throws IOException {
    SourceMap sourceMap = new SourceMap(sourceMapGeneratorFactory);

    String input = "{\"version\":1,\"file\":\"test.js\",\"sources\":[],\"sourcesContent\":{},\"mappings\":[";

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, input);

    sourceMap.validate(true);
  }

  @Test
  public void testReset() throws IOException {
    String sourceFile = "test.js";
    Node node = nodeFactory.createCall(null, null, null, null, null, null, null, null, null, 1, null, null, null, null, null, null);
    node.setSourceFileName(sourceFile);
    node.setLineno(1);
    node.setCharno(1);

    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 1);

    SourceMap sourceMap = new SourceMap(sourceMapGeneratorFactory);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "");

    sourceMap.reset();

    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    sourceMap.appendTo(out2, "");

    String actualSourceMap = out2.toString();

    String expectedSourceMap = "{}";

    assertSourceMapEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testSetStartingPosition() throws IOException {
    SourceMap sourceMap = new SourceMap(sourceMapGeneratorFactory);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "");

    sourceMap.setStartingPosition(1, 1);

    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    sourceMap.appendTo(out2, "");

    String actualSourceMap = out2.toString();

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"start\":{\"line\":1,\"column\":1},\"sources\":[],\"sourcesContent\":{},\"mappings\":[]}";

    assertSourceMapEquals(expectedSourceMap, actualSourceMap);
  }

  @Test
  public void testSetWrapperPrefix() throws IOException {
    String wrapperPrefix = "test_";

    SourceMap sourceMap = new SourceMap(sourceMapGeneratorFactory);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "");

    sourceMap.setWrapperPrefix(wrapperPrefix);

    ByteArrayOutputStream out2 = new ByteArrayOutputStream();
    sourceMap.appendTo(out2, "");

    String actualSourceMap = out2.toString();

    String expectedSourceMap = "{\"version\":1,\"file\":\"test.js\",\"sourceRoot\":\"" + wrapperPrefix + "\",\"sources\":[],\"sourcesContent\":{},\"mappings\":[]}";

    assertSourceMapEquals(expectedSourceMap, actualSourceMap);
  }

  private void assertSourceMapEquals(String expectedSourceMap, String actualSourceMap) {
    assertEquals(expectedSourceMap, actualSourceMap);
  }

  private static void assertEquals(String expectedSourceMap, String actualSourceMap) {
    Map<String, Object> expectedMap = Maps.fromString(expectedSourceMap);
    Map<String, Object> actualMap = Maps.fromString(actualSourceMap);

    assertEquals(expectedMap.toString(), actualMap.toString());
  }
}