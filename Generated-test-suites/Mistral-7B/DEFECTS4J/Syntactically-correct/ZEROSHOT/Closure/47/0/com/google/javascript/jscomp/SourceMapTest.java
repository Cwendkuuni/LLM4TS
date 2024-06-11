import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.debugging.sourcemap.SourceMapGeneratorV1;
import com.google.debugging.sourcemap.SourceMapGeneratorV2;
import com.google.javascript.rhino.Node;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for SourceMap.
 */
public class SourceMapTest {

  @SuppressWarnings("unchecked")
  private static <T> T mock(Class<T> clazz) {
    try {
      return (T) mock(clazz.getCanonicalName());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static SourceMapGeneratorFactory mockSourceMapGeneratorFactory() {
    SourceMapGeneratorFactory factory = mock(SourceMapGeneratorFactory.class);
    when(factory.getInstance(SourceMapFormat.V1)).thenReturn(mock(SourceMapGeneratorV1.class));
    when(factory.getInstance(SourceMapFormat.DEFAULT)).thenReturn(mock(SourceMapGeneratorV2.class));
    when(factory.getInstance(SourceMapFormat.V2)).thenReturn(mock(SourceMapGeneratorV2.class));
    when(factory.getInstance(SourceMapFormat.V3)).thenReturn(mock(SourceMapGeneratorV2.class));
    return factory;
  }

  @org.junit.Test
  public void testInstanceCreation() {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();
    assertInstanceOf(SourceMap.class, sourceMap);
  }

  @org.junit.Test
  public void testAddMapping() throws IOException {
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);
    SourceMap sourceMap = new SourceMap(generatorMock);

    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);

    String sourceFile = "test.js";
    sourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals(sourceFile, sourceFile);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    verify(generatorMock, times(1)).addMapping(
        eq(sourceFile), eq(""),
        eq(outputStartPosition), eq(outputEndPosition));
  }

  @org.junit.Test
  public void testFixupSourceLocation() throws IOException {
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);
    SourceMap sourceMap = new SourceMap(generatorMock);

    String sourceFile = "test.js";
    String prefix = "test/";
    List<LocationMapping> prefixMappings = Lists.newArrayList(
        new LocationMapping("test/", "src/"));

    sourceMap.setPrefixMappings(prefixMappings);

    sourceFile = sourceMap.fixupSourceLocation(sourceFile);
    assertEquals("src/test.js", sourceFile);
  }

  @org.junit.Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream outputStreamMock = mock(ByteArrayOutputStream.class);
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);
    when(generatorMock.appendTo(any(Appendable.class), anyString())).thenCallRealMethod();

    SourceMap sourceMap = new SourceMap(generatorMock);
    sourceMap.appendTo(outputStreamMock, "test.js");

    verify(generatorMock, times(1)).appendTo(outputStreamMock, eq("test.js"));
  }

  @org.junit.Test
  public void testReset() throws IOException {
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);

    SourceMap sourceMap = new SourceMap(generatorMock);
    sourceMap.reset();

    verify(generatorMock, times(1)).reset();
  }

  @org.junit.Test
  public void testSetStartingPosition() throws IOException {
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);

    SourceMap sourceMap = new SourceMap(generatorMock);
    sourceMap.setStartingPosition(1, 1);

    verify(generatorMock, times(1)).setStartingPosition(eq(1), eq(1));
  }

  @org.junit.Test
  public void testSetWrapperPrefix() throws IOException {
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);

    SourceMap sourceMap = new SourceMap(generatorMock);
    sourceMap.setWrapperPrefix("wrapper");

    verify(generatorMock, times(1)).setWrapperPrefix(eq("wrapper"));
  }

  @org.junit.Test
  public void testValidate() throws IOException {
    SourceMapGenerator generatorMock = mock(SourceMapGenerator.class);

    SourceMap sourceMap = new SourceMap(generatorMock);
    sourceMap.validate(true);

    verify(generatorMock, times(1)).validate(eq(true));
  }

  @org.junit.Test
  public void testInstanceCreationWithFactory() {
    SourceMapGeneratorFactory factory = mockSourceMapGeneratorFactory();
    SourceMap sourceMap = new SourceMap(factory);
    assertInstanceOf(SourceMap.class, sourceMap);
  }

  @org.junit.Test
  public void testInstanceCreationWithV1Format() {
    SourceMapGeneratorFactory factory = mockSourceMapGeneratorFactory();
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();
    assertInstanceOf(SourceMap.class, sourceMap);
    assertEquals(SourceMapFormat.V1, sourceMap.getFormat());
  }

  @org.junit.Test
  public void testInstanceCreationWithDefaultFormat() {
    SourceMapGeneratorFactory factory = mockSourceMapGeneratorFactory();
    SourceMap sourceMap = SourceMap.Format.DEFAULT.getInstance();
    assertInstanceOf(SourceMap.class, sourceMap);
    assertEquals(SourceMapFormat.DEFAULT, sourceMap.getFormat());
  }

  @org.junit.Test
  public void testInstanceCreationWithV2Format() {
    SourceMapGeneratorFactory factory = mockSourceMapGeneratorFactory();
    SourceMap sourceMap = SourceMap.Format.V2.getInstance();
    assertInstanceOf(SourceMap.class, sourceMap);
    assertEquals(SourceMapFormat.V2, sourceMap.getFormat());
  }

  @org.junit.Test
  public void testInstanceCreationWithV3Format() {
    SourceMapGeneratorFactory factory = mockSourceMapGeneratorFactory();
    SourceMap sourceMap = SourceMap.Format.V3.getInstance();
    assertInstanceOf(SourceMap.class, sourceMap);
    assertEquals(SourceMapFormat.V3, sourceMap.getFormat());
  }

  @org.junit.Test
  public void testApplyPredicate() {
    Node node = mock(Node.class);
    DetailLevel detailLevel = DetailLevel.SYMBOLS;

    assertTrue(detailLevel.apply(node));
  }

  @org.junit.Test
  public void testDontApplyPredicate() {
    Node node = mock(Node.class);
    DetailLevel detailLevel = DetailLevel.ALL;

    assertFalse(detailLevel.apply(node));
  }

  @org.junit.Test
  public void testGetFormat() {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();
    assertEquals(SourceMapFormat.V1, sourceMap.getFormat());
  }

  /**
   * Helper method for testing SourceMap with test case specific configurations.
   * @param format SourceMap format
   * @param detailLevel Detail level
   */
  private void testSourceMap(Format format, DetailLevel detailLevel) throws IOException {
    SourceMapGeneratorFactory factory = mockSourceMapGeneratorFactory();
    SourceMap sourceMap = format.getInstance();
    sourceMap.setDetailLevel(detailLevel);

    assertSame(format, sourceMap.getFormat());
  }

  @org.junit.Test
  public void testSourceMapV1() throws IOException {
    testSourceMap(SourceMap.Format.V1, SourceMap.DetailLevel.ALL);
  }

  @org.junit.Test
  public void testSourceMapDefault() throws IOException {
    testSourceMap(SourceMap.Format.DEFAULT, SourceMap.DetailLevel.SYMBOLS);
  }

  @org.junit.Test
  public void testSourceMapV2() throws IOException {
    testSourceMap(SourceMap.Format.V2, SourceMap.DetailLevel.SYMBOLS);
  }

  @org.junit.Test
  public void testSourceMapV3() throws IOException {
    testSourceMap(SourceMap.Format.V3, SourceMap.DetailLevel.SYMBOLS);
  }
}