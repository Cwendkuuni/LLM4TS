import static org.junit.Assert.assertNotNull;
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
import com.google.javascript.rhino.NodeUtil;

public class SourceMapTest {

  private SourceMapGenerator generatorV1;
  private SourceMapGenerator generatorV2;
  private SourceMap sourceMapV1;
  private SourceMap sourceMapV2;

  @Before
  public void setup() {
    generatorV1 = mock(SourceMapGeneratorV1.class);
    generatorV2 = mock(SourceMapGeneratorV2.class);

    when(generatorV1.getInstance()).thenReturn(generatorV1);
    when(generatorV2.getInstance()).thenReturn(generatorV2);

    sourceMapV1 = new SourceMap(generatorV1);
    sourceMapV1.setPrefixMappings(Collections.singletonList(
        new SourceMap.LocationMapping("mockPrefix", "realPath")));

    sourceMapV2 = new SourceMap(generatorV2);
    sourceMapV2.setPrefixMappings(Collections.singletonList(
        new SourceMap.LocationMapping("mockPrefix", "realPath")));
  }

  @Test
  public void testAddMapping() {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(2, 2);

    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition);
    sourceMapV2.addMapping(node, outputStartPosition, outputEndPosition);

    String sourceFile = "mockSourceFile.js";
    String fixedSourceFileV1 = sourceMapV1.fixupSourceLocation(sourceFile);
    String fixedSourceFileV2 = sourceMapV2.fixupSourceLocation(sourceFile);

    assertNotNull(fixedSourceFileV1);
    assertNotNull(fixedSourceFileV2);
    assertEquals("realPath.js", fixedSourceFileV1);
    assertEquals("realPath.js", fixedSourceFileV2);
  }

  @Test
  public void testFixupSourceLocationWithNoPrefixMappings() {
    Node node = mock(Node.class);
    String sourceFile = "mockSourceFile.js";

    SourceMap sourceMap = new SourceMap(generatorV1);
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    assertNotNull(fixedSourceFile);
    assertEquals(sourceFile, fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocationWithMultiplePrefixMappings() {
    Node node = mock(Node.class);
    String sourceFile = "mockSourceFile/path/with/prefix.js";

    SourceMap sourceMap = new SourceMap(generatorV1);
    sourceMap.setPrefixMappings(Collections.singletonList(
        new SourceMap.LocationMapping("mockPrefix1", "realPath1")))
        .andThen(Collections.singletonList(
            new SourceMap.LocationMapping("mockPrefix2", "realPath2")));

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    assertNotNull(fixedSourceFile);
    assertEquals("realPath2.js", fixedSourceFile);
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    sourceMapV1.appendTo(out, "SourceMapName");
    sourceMapV2.appendTo(out, "SourceMapName");

    String sourceMapV1Content = new String(out.toByteArray());
    String sourceMapV2Content = new String(out.toByteArray());

    // TODO: write assertions for the generated source map contents
  }

  @Test
  public void testReset() {
    sourceMapV1.reset();
    sourceMapV2.reset();

    assertNotNull(sourceMapV1.generator);
    assertNotNull(sourceMapV2.generator);
  }

  @Test
  public void testSetStartingPosition() {
    sourceMapV1.setStartingPosition(1, 1);
    sourceMapV2.setStartingPosition(1, 1);

    assertNotNull(sourceMapV1.generator);
    assertNotNull(sourceMapV2.generator);
  }

  @Test
  public void testSetWrapperPrefix() {
    sourceMapV1.setWrapperPrefix("wrapperPrefix");
    sourceMapV2.setWrapperPrefix("wrapperPrefix");

    assertNotNull(sourceMapV1.generator);
    assertNotNull(sourceMapV2.generator);
  }

  @Test
  public void testValidate() {
    sourceMapV1.validate(true);
    sourceMapV2.validate(true);

    assertNotNull(sourceMapV1.generator);
    assertNotNull(sourceMapV2.generator);
  }
}