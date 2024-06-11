import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMapGenerator;
import com.google.javascript.jscomp.SourceMapGeneratorFactory;
import com.google.javascript.rhino.NodeFactory;

public class SourceMapTest {

  private NodeFactory nodeFactory;
  private SourceMapGenerator generatorMock;
  private SourceMap sourceMap;

  @Before
  public void setUp() {
    nodeFactory = NodeFactory.getFactory();
    generatorMock = mock(SourceMapGenerator.class);
    SourceMapGeneratorFactory factoryMock = mock(SourceMapGeneratorFactory.class);
    when(factoryMock.getInstance(SourceMapFormat.V1)).thenReturn(generatorMock);
    sourceMap = new SourceMap(factoryMock);
  }

  @Test
  public void testInstanceWithV1Format() {
    SourceMap sourceMap = SourceMap.Format.V1.getInstance();
    assertNotNull(sourceMap);
  }

  @Test
  public void testAddMapping() {
    Node node = nodeFactory.createCall(null, "print", null, null);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(1, 5);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
    String expectedSourceFile = "TestFile.js";
    String fixedSourceFile = sourceMap.fixupSourceLocation(expectedSourceFile);
    assertNotNull(fixedSourceFile);
    assertEquals(expectedSourceFile, fixedSourceFile);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullNode() {
    sourceMap.addMapping(null, new FilePosition(1, 1), new FilePosition(1, 5));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNullSourceFile() {
    Node node = nodeFactory.createCall(null, "print", null, null);
    sourceMap.addMapping(node, null, new FilePosition(1, 1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddMappingWithNegativeLineno() {
    Node node = nodeFactory.createCall(null, "print", null, null);
    FilePosition outputStartPosition = new FilePosition(1, 1);
    FilePosition outputEndPosition = new FilePosition(-1, 1);
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);
  }

  @Test
  public void testAppendTo() throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);
    sourceMap.appendTo(printStream, "SourceMapTest.java");
    String expectedOutput = "// SourceFile: SourceMapTest.java\n";
    assertEquals(expectedOutput, byteArrayOutputStream.toString());
  }

  @Test
  public void testReset() {
    sourceMap.reset();
    assertNull(sourceMap.generator);
    assertEquals(Collections.emptyList(), sourceMap.prefixMappings);
    assertEquals(Collections.emptyMap(), sourceMap.sourceLocationFixupCache);
  }

  @Test
  public void testSetStartingPosition() {
    sourceMap.setStartingPosition(1, 1);
    FilePosition expectedStartingPosition = new FilePosition(1, 1);
    assertEquals(expectedStartingPosition, sourceMap.generator.getStartingPosition());
  }

  @Test
  public void testSetWrapperPrefix() {
    sourceMap.setWrapperPrefix("wrapper_");
    String expectedWrapperPrefix = "wrapper_";
    assertEquals(expectedWrapperPrefix, sourceMap.generator.getWrapperPrefix());
  }

  @Test
  public void testValidate() {
    sourceMap.validate(true);
  }

  @Test
  public void testValidateWithFalse() {
    sourceMap.validate(false);
  }

  @Test
  public void testSetPrefixMappings() {
    List<LocationMapping> sourceMapLocationMappings = Lists.newArrayList(
        new LocationMapping("test", "test_fixed.js"));
    sourceMap.setPrefixMappings(sourceMapLocationMappings);
    String expectedSourceFile = "test/TestFile.js";
    String fixedSourceFile = sourceMap.fixupSourceLocation(expectedSourceFile);
    assertNotNull(fixedSourceFile);
    assertEquals("test_fixed.js", fixedSourceFile);
  }

  @Test
  public void testFixupSourceLocationWithEmptyPrefixMappings() {
    String expectedSourceFile = "TestFile.js";
    String fixedSourceFile = sourceMap.fixupSourceLocation(expectedSourceFile);
    assertNotNull(fixedSourceFile);
    assertEquals(expectedSourceFile, fixedSourceFile);
  }
}