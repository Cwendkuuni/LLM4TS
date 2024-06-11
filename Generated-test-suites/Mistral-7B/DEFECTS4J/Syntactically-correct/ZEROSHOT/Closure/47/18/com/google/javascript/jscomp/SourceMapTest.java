import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class SourceMapTest {

  private SourceMapGeneratorV1 sourceMapGeneratorV1;
  private SourceMapGeneratorV2 sourceMapGeneratorV2;
  private SourceMap sourceMap;

  @Before
  public void setup() {
    sourceMapGeneratorV1 = mock(SourceMapGeneratorV1.class);
    sourceMapGeneratorV2 = mock(SourceMapGeneratorV2.class);

    SourceMapGeneratorFactory factory = mock(SourceMapGeneratorFactory.class);
    when(factory.getInstance(SourceMapFormat.V1)).thenReturn(sourceMapGeneratorV1);
    when(factory.getInstance(SourceMapFormat.V2)).thenReturn(sourceMapGeneratorV2);

    sourceMap = new SourceMap(factory);
  }

  @Test
  public void testInstance() {
    SourceMap instance = SourceMap.Format.DEFAULT.getInstance();
    assertNotNull(instance);
  }

  @Test
  public void testAddMapping() throws IOException {
    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);
    String sourceFile = "test.js";

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assert (fixedSourceFile.equals(sourceFile));
  }

  @Test
  public void testFixupSourceLocation() {
    String sourceFile = "test/prefix.js";
    String replacement = "replaced";
    List<LocationMapping> sourceMapLocationMappings = Lists.newArrayList(
        new SourceMap.LocationMapping("test/prefix.", replacement));

    sourceMap.setPrefixMappings(sourceMapLocationMappings);

    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assert (fixedSourceFile.equals(replacement));
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    String name = "test.js.map";

    sourceMap.appendTo(out, name);

    assertNotNull(out.toString());
  }

  @Test
  public void testReset() {
    sourceMap.reset();

    // check if generator is reset
    assertNotNull(sourceMap.generator);
    assert (sourceMap.generator instanceof SourceMapGenerator);
    SourceMapGenerator generator = (SourceMapGenerator) sourceMap.generator;
    generator.reset();
  }

  @Test
  public void testSetStartingPosition() {
    sourceMap.setStartingPosition(1, 1);

    // check if generator is set starting position
    SourceMapGenerator generator = (SourceMapGenerator) sourceMap.generator;
    assertNotNull(generator);
    generator.setStartingPosition(1, 1);
  }

  @Test
  public void testSetWrapperPrefix() {
    String prefix = "wrapper_";
    sourceMap.setWrapperPrefix(prefix);

    // check if generator is set wrapper prefix
    SourceMapGenerator generator = (SourceMapGenerator) sourceMap.generator;
    assertNotNull(generator);
    generator.setWrapperPrefix(prefix);
  }

  @Test
  public void testValidate() {
    sourceMap.validate(true);

    // check if generator is validate
    SourceMapGenerator generator = (SourceMapGenerator) sourceMap.generator;
    assertNotNull(generator);
    generator.validate(true);
  }
}