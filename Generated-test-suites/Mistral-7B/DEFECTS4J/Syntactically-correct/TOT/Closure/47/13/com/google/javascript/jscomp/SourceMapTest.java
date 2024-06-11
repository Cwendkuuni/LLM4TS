import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMapGenerator;
import com.google.javascript.jscomp.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.SourceMapGeneratorV1;
import com.google.javascript.jscomp.SourceMapGeneratorV2;
import com.google.javascript.rhino.SourceName;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Test class for SourceMap.
 */
public class SourceMapTest {

  private Node createTestNode() {
    Node node = new Node();
    node.setSourceFileName("testSourceFile.js");
    node.setLineno(10);
    node.setCharno(20);
    node.setProp(Node.ORIGINALNAME_PROP, "testFunction");
    return node;
  }

  private void setupMockGenerator(SourceMapGenerator generator, Format format)
      throws IOException {
    switch (format) {
      case V1:
        generator = mock(SourceMapGeneratorV1.class);
        break;
      case DEFAULT:
      case V2:
        generator = mock(SourceMapGeneratorV2.class);
        break;
      case V3:
        generator = mock(SourceMapGeneratorV3.class);
        break;
    }

    when(generator.getInstance()).thenReturn(generator);
  }

  @org.junit.Test
  public void testAddMapping() throws IOException {
    // Given
    SourceMapGenerator generator = new SourceMapGeneratorFactory().newInstance(
        SourceMapFormat.DEFAULT);
    setupMockGenerator(generator, SourceMap.Format.DEFAULT);

    SourceMap sourceMap = new SourceMap(generator);

    Node node = createTestNode();
    FilePosition outputStartPosition = new FilePosition(5, 5);
    FilePosition outputEndPosition = new FilePosition(10, 10);

    // When
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Then
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "testMap.js.map");
    String result = out.toString();
    assertEquals(
        "{\n"
            + "  \"version\": 3,\n"
            + "  \"file\": \"testSourceFile.js\",\n"
            + "  \"sourcesContent\": {},\n"
            + "  \"sources\": [\"testSourceFile.js\"],\n"
            + "  \"names\": [],\n"
            + "  \"mappings\": \"....\",\n"
            + "}\n",
        result);
  }

  @org.junit.Test
  public void testAddMappingWithPrefixMapping() throws IOException {
    // Given
    String prefix = "/test/";
    List<LocationMapping> locationMappings = Lists.newArrayList(
        new LocationMapping(prefix, "/testMapped/"));

    SourceMapGenerator generator = new SourceMapGeneratorFactory()
        .newInstance(SourceMapFormat.DEFAULT);
    setupMockGenerator(generator, SourceMap.Format.DEFAULT);

    SourceMap sourceMap = new SourceMap(generator);
    sourceMap.setPrefixMappings(locationMappings);

    Node node = createTestNode();
    FilePosition outputStartPosition = new FilePosition(5, 5);
    FilePosition outputEndPosition = new FilePosition(10, 10);

    // When
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Then
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "testMap.js.map");
    String result = out.toString();

    assertEquals(
        "{\n"
            + "  \"version\": 3,\n"
            + "  \"file\": \"/test/testSourceFile.js\",\n"
            + "  \"sourcesContent\": {},\n"
            + "  \"sources\": [\"/testMapped/testSourceFile.js\"],\n"
            + "  \"names\": [],\n"
            + "  \"mappings\": \"....\",\n"
            + "}\n",
        result);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullNode() throws IOException {
    // Given
    SourceMapGenerator generator = new SourceMapGeneratorFactory()
        .newInstance(SourceMapFormat.DEFAULT);
    setupMockGenerator(generator, SourceMap.Format.DEFAULT);

    SourceMap sourceMap = new SourceMap(generator);

    // When
    sourceMap.addMapping(null, new FilePosition(5, 5), new FilePosition(10, 10));

    // Then
    // An exception is expected
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullOutputStartPosition()
      throws IOException {
    // Given
    SourceMapGenerator generator = new SourceMapGeneratorFactory()
        .newInstance(SourceMapFormat.DEFAULT);
    setupMockGenerator(generator, SourceMap.Format.DEFAULT);

    SourceMap sourceMap = new SourceMap(generator);

    Node node = createTestNode();

    // When
    sourceMap.addMapping(node, null, new FilePosition(10, 10));

    // Then
    // An exception is expected
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullOutputEndPosition()
      throws IOException {
    // Given
    SourceMapGenerator generator = new SourceMapGeneratorFactory()
        .newInstance(SourceMapFormat.DEFAULT);
    setupMockGenerator(generator, SourceMap.Format.DEFAULT);

    SourceMap sourceMap = new SourceMap(generator);

    Node node = createTestNode();
    FilePosition outputStartPosition = new FilePosition(5, 5);

    // When
    sourceMap.addMapping(node, outputStartPosition, null);

    // Then
    // An exception is expected
  }

  @org.junit.Test
  public void testReset() throws IOException {
    // Given
    SourceMapGenerator generator = new SourceMapGeneratorFactory()
        .newInstance(SourceMapFormat.DEFAULT);
    setupMockGenerator(generator, SourceMap.Format.DEFAULT);

    SourceMap sourceMap = new SourceMap(generator);

    Node node = createTestNode();
    FilePosition outputStartPosition = new FilePosition(5, 5);
    FilePosition outputEndPosition = new FilePosition(10, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // When
    sourceMap.reset();

    // Then
    assertEquals(Collections.emptyList(), sourceMap.getPrefixMappings());
  }

  @org.junit.Test
  public void testValidateTrue() throws IOException {
    // Given
    SourceMapGenerator generator = new SourceMapGeneratorFactory()
        .newInstance(SourceMapFormat.DEFAULT);
    setupMockGenerator(generator, SourceMap.Format.DEFAULT);

    SourceMap sourceMap = new SourceMap(generator);

    Node node = createTestNode();
    FilePosition outputStartPosition = new FilePosition(5, 5);
    FilePosition outputEndPosition = new FilePosition(10, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // When
    sourceMap.validate(true);

    // Then
    // No exception is expected
  }

  @org.junit.Test(expected = IOException.class)
  public void testValidateFalse() throws IOException {
    // Given
    SourceMapGenerator generator = new SourceMapGeneratorFactory()
        .newInstance(SourceMapFormat.DEFAULT);
    setupMockGenerator(generator, SourceMap.Format.DEFAULT);

    SourceMap sourceMap = new SourceMap(generator);

    Node node = createTestNode();
    FilePosition outputStartPosition = new FilePosition(5, 5);
    FilePosition outputEndPosition = new FilePosition(10, 10);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // When
    sourceMap.validate(false);

    // Then
    // An IOException is expected
  }
}