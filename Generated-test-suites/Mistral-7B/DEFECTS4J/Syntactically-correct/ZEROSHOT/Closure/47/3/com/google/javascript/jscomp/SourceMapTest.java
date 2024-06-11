import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.rhino.Node;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  @org.junit.Test
  public void testAddMapping() {
    Format format = SourceMap.Format.V1;
    SourceMap sourceMap = format.getInstance();

    Node node = mock(Node.class);
    String sourceFile = "test.js";
    String originalName = "testFunction";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    when(node.getSourceFileName()).thenReturn(sourceFile);
    when(node.getProp(Node.ORIGINALNAME_PROP)).thenReturn(originalName);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    try {
      PrintWriter out = mock(PrintWriter.class);
      sourceMap.appendTo(out, "source.map");
    } catch (IOException e) {
      // ignore
    }

    // Verify that the source map was generated correctly
    SourceMapGenerator generator = sourceMap.generator;
    assertEquals(1, generator.getSources().size());
    assertEquals(sourceFile, generator.getSources().get(0));
    assertEquals(1, generator.getSourcesContent().size());
    assertEquals(1, generator.getSourcesContent().get(0).length());
    assertEquals(1, generator.getMappings().size());
    SourceMapGenerator.SourceMapping mapping = generator.getMappings().get(0);
    assertEquals(sourceFile, mapping.getSource());
    assertEquals(1, mapping.getSourceLines().size());
    assertEquals(1, mapping.getSourceLines().get(0).length());
    assertEquals(originalName, mapping.getName());
    SourceMapGenerator.SourceMapping.SourceLine sourceLine = mapping.getSourceLines().get(0).get(0);
    assertEquals(outputStartPosition.getLine(), sourceLine.getStart().getLine());
    assertEquals(outputStartPosition.getColumn(), sourceLine.getStart().getColumn());
    assertEquals(outputEndPosition.getLine(), sourceLine.getEnd().getLine());
    assertEquals(outputEndPosition.getColumn(), sourceLine.getEnd().getColumn());
  }

  @org.junit.Test
  public void testFixupSourceLocation() {
    String sourceFile = "test.js";
    List<SourceMap.LocationMapping> prefixMappings = Lists.newArrayList(
        new SourceMap.LocationMapping("test/", "test"),
        new SourceMap.LocationMapping("test2/", "test2")
    );

    SourceMap sourceMap = new SourceMap(
        SourceMapGeneratorFactory.getInstance(SourceMapFormat.V1));
    sourceMap.setPrefixMappings(prefixMappings);

    String fixed = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals("test/test.js", fixed);

    sourceMap.reset();
    sourceMap.setPrefixMappings(Collections.emptyList());

    fixed = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals(sourceFile, fixed);
  }

  @org.junit.Test
  public void testAddMappingWithPrefixMappings() {
    Format format = SourceMap.Format.V1;
    SourceMap sourceMap = format.getInstance();

    Node node = mock(Node.class);
    String sourceFile = "test.js";
    String originalName = "testFunction";
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    when(node.getSourceFileName()).thenReturn(sourceFile);
    when(node.getProp(Node.ORIGINALNAME_PROP)).thenReturn(originalName);

    List<SourceMap.LocationMapping> prefixMappings = Lists.newArrayList(
        new SourceMap.LocationMapping("test/", "test"),
        new SourceMap.LocationMapping("test2/", "test2")
    );
    sourceMap.setPrefixMappings(prefixMappings);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    try {
      PrintWriter out = mock(PrintWriter.class);
      sourceMap.appendTo(out, "source.map");
    } catch (IOException e) {
      // ignore
    }

    // Verify that the source map was generated correctly
    SourceMapGenerator generator = sourceMap.generator;
    assertEquals(1, generator.getSources().size());
    assertEquals("test/test.js", generator.getSources().get(0));
    assertEquals(1, generator.getSourcesContent().size());
    assertEquals(1, generator.getSourcesContent().get(0).length());
    assertEquals(1, generator.getMappings().size());
    SourceMapGenerator.SourceMapping mapping = generator.getMappings().get(0);
    assertEquals("test/test.js", mapping.getSource());
    assertEquals(1, mapping.getSourceLines().size());
    assertEquals(1, mapping.getSourceLines().get(0).length());
    assertEquals(originalName, mapping.getName());
    SourceMapGenerator.SourceMapping.SourceLine sourceLine = mapping.getSourceLines().get(0).get(0);
    assertEquals(outputStartPosition.getLine(), sourceLine.getStart().getLine());
    assertEquals(outputStartPosition.getColumn(), sourceLine.getStart().getColumn());
    assertEquals(outputEndPosition.getLine(), sourceLine.getEnd().getLine());
    assertEquals(outputEndPosition.getColumn(), sourceLine.getEnd().getColumn());
  }

  @org.junit.Test
  public void testSetDetailLevel() {
    Format format = SourceMap.Format.V1;
    SourceMap sourceMap = format.getInstance();

    Node node = mock(Node.class);
    DetailLevel detailLevel = SourceMap.DetailLevel.SYMBOLS;

    when(node.isCall()).thenReturn(true);

    sourceMap.setDetailLevel(detailLevel);
    sourceMap.addMapping(node, null, null);

    try {
      PrintWriter out = mock(PrintWriter.class);
      sourceMap.appendTo(out, "source.map");
    } catch (IOException e) {
      // ignore
    }

    // Verify that the source map was generated correctly
    SourceMapGenerator generator = sourceMap.generator;
    assertEquals(1, generator.getSources().size());
    assertEquals(1, generator.getSourcesContent().size());
    assertEquals(1, generator.getMappings().size());

    SourceMapGenerator.SourceMapping mapping = generator.getMappings().get(0);
    assertEquals(1, mapping.getSourceLines().size());
    SourceMapGenerator.SourceMapping.SourceLine sourceLine = mapping.getSourceLines().get(0).get(0);
    assertTrue(sourceLine.contains("MyCallNode"));
  }
}