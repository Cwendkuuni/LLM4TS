import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.rhino.Node;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class SourceMapTest {

  private static final String TEST_SOURCE_FILE = "TestSourceFile.js";
  private static final String TEST_SOURCE_FILE_REPLACEMENT =
      "TestSourceFileReplacement.js";

  @Test
  public void testSourceMap() throws IOException {
    // Initialize mocks
    SourceMapGeneratorFactory factoryMock = mock(SourceMapGeneratorFactory.class);
    SourceMapGenerator generatorV1Mock = mock(SourceMapGeneratorV1.class);
    SourceMapGenerator generatorV2Mock = mock(SourceMapGeneratorV2.class);
    when(factoryMock.getInstance(SourceMapFormat.V1)).thenReturn(generatorV1Mock);
    when(factoryMock.getInstance(SourceMapFormat.V2)).thenReturn(generatorV2Mock);

    SourceMap sourceMap = new SourceMap(factoryMock);

    // Test instantiation
    assertNotNull(sourceMap);

    // Test addMapping method
    Node node = createNode();
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Test setPrefixMappings method
    List<SourceMap.LocationMapping> prefixMappings = Lists.newArrayList(
        new SourceMap.LocationMapping("TestPrefix", "TestReplacement"));
    sourceMap.setPrefixMappings(prefixMappings);

    // Test fixupSourceLocation method
    String sourceFile = TEST_SOURCE_FILE;
    String fixedSourceFile = TEST_SOURCE_FILE_REPLACEMENT;
    String fixedSourceFileAfterPrefix = "TestSourceFileReplacementTestPrefix.js";
    when(generatorV1Mock.appendTo(anyObject(), anyString())).thenAnswer(
        invocation -> "");
    when(generatorV1Mock.addMapping(
        eq(TEST_SOURCE_FILE_REPLACEMENT), anyString(),
        any(FilePosition.class), any(FilePosition.class))).thenReturn(true);
    when(generatorV1Mock.isValid()).thenReturn(true);

    sourceMap.setPrefixMappings(prefixMappings);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    String fixed = sourceMap.fixupSourceLocation(sourceFile);
    assertNotNull(fixed);
    assertEquals(fixedSourceFileAfterPrefix, fixed);

    // Test validate method
    when(generatorV1Mock.isValid()).thenReturn(false);
    sourceMap.validate(true);
    verify(generatorV1Mock).validate(true);

    // Test reset method
    sourceMap.reset();
    verify(generatorV1Mock).reset();

    // Test appendTo method
    Appendable out = mock(Appendable.class);
    String name = "TestName";
    sourceMap.appendTo(out, name);
    verify(generatorV1Mock).appendTo(out, eq("TestName"));

    // Test getInstance method for different formats
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();
    SourceMap sourceMapDefault = SourceMap.Format.DEFAULT.getInstance();
    SourceMap sourceMapV2 = SourceMap.Format.V2.getInstance();
    assertNotNull(sourceMapV1);
    assertNotNull(sourceMapDefault);
    assertNotNull(sourceMapV2);

    // Test DetailLevel enum values
    DetailLevel detailLevelAll = SourceMap.DetailLevel.ALL;
    DetailLevel detailLevelSymbols = SourceMap.DetailLevel.SYMBOLS;
    assertNotNull(detailLevelAll);
    assertNotNull(detailLevelSymbols);
  }

  private Node createNode() {
    // Create a mock Node for testing purposes
    Node node = mock(Node.class);
    when(node.getSourceFileName()).thenReturn(TEST_SOURCE_FILE);
    when(node.getLineno()).thenReturn(1);
    when(node.getCharno()).thenReturn(1);
    return node;
  }

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }
}