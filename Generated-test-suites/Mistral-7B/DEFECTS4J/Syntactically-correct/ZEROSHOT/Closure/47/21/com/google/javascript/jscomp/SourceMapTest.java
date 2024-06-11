import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.NodeOperations;

public class SourceMapTest {

  private NodeFactory nodeFactory;
  private NodeOperations nodeOperations;
  private SourceMapGenerator generatorMock;
  private SourceMapGeneratorFactory generatorFactoryMock;
  private SourceMap sourceMap;

  @Before
  public void setup() {
    nodeFactory = new NodeFactory();
    nodeOperations = new NodeOperations();
    generatorMock = mock(SourceMapGenerator.class);
    generatorFactoryMock = mock(SourceMapGeneratorFactory.class);
    when(generatorFactoryMock.getInstance(SourceMapFormat.V1)).thenReturn(generatorMock);
    sourceMap = new SourceMap(generatorFactoryMock);
  }

  @Test
  public void testGetInstance() {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();
    assertNotNull(sourceMapV1);
  }

  @Test
  public void testAddMapping() throws IOException {
    String sourceFile = "TestSourceFile.java";
    String originalName = "testFunction";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    Node node = nodeFactory.createCall(null, null, null, originalName);
    node.setSourceFileName(sourceFile);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // No prefix mapping
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertNull(fixedSourceFile);
    assertNull(sourceMap.sourceLocationFixupCache.get(sourceFile));

    // Prefix mapping
    List<LocationMapping> prefixMappings = Collections.singletonList(
        new LocationMapping("test_prefix", "fixed_prefix")
    );
    sourceMap.setPrefixMappings(prefixMappings);

    String fixedSourceFileWithPrefix = sourceMap.fixupSourceLocation(sourceFile);
    assertNotNull(fixedSourceFileWithPrefix);
    assertNotNull(sourceMap.sourceLocationFixupCache.get(sourceFile));
    assertEquals("fixed_prefixTestSourceFile.java", fixedSourceFileWithPrefix);
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    sourceMap.appendTo(out, "testFile.js");

    assertFalse(out.toString().isEmpty());
  }

  @Test
  public void testReset() {
    sourceMap.reset();

    // Verify cache is empty
    assertNull(sourceMap.sourceLocationFixupCache.get("TestSourceFile.java"));
  }

  @Test
  public void testValidate() {
    sourceMap.validate(true);
  }

  @Test
  public void testSetPrefixMappings() {
    List<LocationMapping> sourceMapLocationMappings = Collections.singletonList(
        new LocationMapping("test_prefix", "fixed_prefix")
    );
    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    assertNotNull(sourceMap.prefixMappings);
  }
}