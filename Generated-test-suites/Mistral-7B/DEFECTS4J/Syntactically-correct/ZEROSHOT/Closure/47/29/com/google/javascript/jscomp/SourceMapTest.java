package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Tests for {@link SourceMap} class.
 *
 */
public class SourceMapTest {

  @Test
  public void testInstanceCreation() {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();
    assertNotNull(sourceMapV1);

    SourceMap sourceMapDefault = SourceMap.Format.DEFAULT.getInstance();
    assertNotNull(sourceMapDefault);

    SourceMap sourceMapV2 = SourceMap.Format.V2.getInstance();
    assertNotNull(sourceMapV2);

    SourceMap sourceMapV3 = SourceMap.Format.V3.getInstance();
    assertNotNull(sourceMapV3);
  }

  @Test
  public void testAddMapping() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    Node node = Node.newLiteral(5);
    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition);

    Appendable out = Maps.newArrayList().<Appendable>get(0);
    sourceMapV1.appendTo(out, "test.js");

    String sourceMap = out.toString();
    assertNotNull(sourceMap);

    String expected = "{\"version\":1,\"file\":\"test.js\",\"sources\":{},\"mappings\":[\n" +
        "  [\"test.js\",1,2,1,2,0],\n" +
        "  [\"test.js\",1,5,1,2,1]\n" +
        "]}\n";

    assertTrue(sourceMap.equals(expected));
  }

  @Test
  public void testAddMappingWithLocationFixups() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    String originalSourceFile = "src/com/google/Test.java";
    String fixedSourceFile = "test.js";

    List<LocationMapping> locationMappings = Lists.newArrayList(
        new LocationMapping("src/", "test.js/"));

    sourceMapV1.setPrefixMappings(locationMappings);

    Node node = Node.newLiteral(5);
    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition);

    Appendable out = Maps.newArrayList().<Appendable>get(0);
    sourceMapV1.appendTo(out, originalSourceFile);

    sourceMapV1.reset();

    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition);

    out = Maps.newArrayList().<Appendable>get(0);
    sourceMapV1.appendTo(out, originalSourceFile);

    String sourceMap = out.toString();
    assertNotNull(sourceMap);

    String expected = "{\"version\":1,\"file\":\"test.js/src/com/google/Test.java\",\"sources\":{},\"mappings\":[\n" +
        "  [\"test.js/src/com/google/Test.java\",1,2,1,2,0],\n" +
        "  [\"test.js/src/com/google/Test.java\",1,5,1,2,1]\n" +
        "]}\n";

    assertTrue(sourceMap.equals(expected));
  }

  @Test
  public void testSetWrapperPrefix() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    sourceMapV1.setWrapperPrefix("test_");

    Node node = Node.newLiteral(5);
    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition);

    Appendable out = Maps.newArrayList().<Appendable>get(0);
    sourceMapV1.appendTo(out, "test.js");

    String sourceMap = out.toString();
    assertNotNull(sourceMap);

    String expected = "{\"version\":1,\"file\":\"test.js\",\"sources\":{},\"mappings\":[\n" +
        "  [\"test_test.js\",1,2,1,2,0],\n" +
        "  [\"test_test.js\",1,5,1,2,1]\n" +
        "]}\n";

    assertTrue(sourceMap.equals(expected));
  }

  @Test
  public void testValidate() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    Node node = Node.newLiteral(5);
    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition);

    boolean validationResult = sourceMapV1.validate(true);
    assertTrue(validationResult);

    validationResult = sourceMapV1.validate(false);
    assertFalse(validationResult);
  }

  @Test
  public void testReset() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    Node node = Node.newLiteral(5);
    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition);

    Appendable out = Maps.newArrayList().<Appendable>get(0);
    sourceMapV1.appendTo(out, "test.js");

    String sourceMap = out.toString();
    assertNotNull(sourceMap);

    sourceMapV1.reset();

    out = Maps.newArrayList().<Appendable>get(0);
    sourceMapV1.appendTo(out, "test.js");

    assertNull(out.toString());
  }

  @Test
  public void testDetailedMapping() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();
    SourceMap sourceMapDefault = SourceMap.Format.DEFAULT.getInstance();

    Node node = Node.newFunction("Test", NodeUtil.newFunctionBody(
        NodeUtil.newVariable("x"), Node.newAssign(Node.newName("y"), Node.newNumber(5))));

    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(1, 10);

    sourceMapV1.setDetailLevel(DetailLevel.ALL);
    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition);

    Appendable out = Maps.newArrayList().<Appendable>get(0);
    sourceMapV1.appendTo(out, "test.js");

    String sourceMapV1String = out.toString();
    assertNotNull(sourceMapV1String);

    String expectedV1 = "{\"version\":1,\"file\":\"test.js\",\"sources\":{},\"mappings\":[\n" +
        "  [\"test.js\",1,2,1,2,0,\"Test\",\"Test\",\"x\",\"x\",\"Test.js\",1,2],\n" +
        "  [\"test.js\",1,3,1,2,1,\"Test\",\"Test\",\"y\",\"y\",\"Test.js\",1,5]\n" +
        "]}\n";

    assertTrue(sourceMapV1String.equals(expectedV1));

    sourceMapDefault.setDetailLevel(DetailLevel.SYMBOLS);
    sourceMapDefault.addMapping(node, outputStartPosition, outputEndPosition);

    Appendable outDefault = Maps.newArrayList().<Appendable>get(0);
    sourceMapDefault.appendTo(outDefault, "test.js");

    String sourceMapDefaultString = outDefault.toString();
    assertNotNull(sourceMapDefaultString);

    String expectedDefault = "{\"version\":1,\"file\":\"test.js\",\"sources\":{},\"mappings\":[\n" +
        "  [\"test.js\",1,2,1, \"Test\",\"Test\"],\n" +
        "  [\"test.js\",1,5,1, \"Test\",\"Test\"],\n" +
        "  [\"test.js\",1,4,1, \"y\",\"y\"]\n" +
        "]}\n";

    assertTrue(sourceMapDefaultString.equals(expectedDefault));
  }

  @Test
  public void testSetStartingPosition() throws IOException {
    SourceMap sourceMapV1 = SourceMap.Format.V1.getInstance();

    Node node = Node.newLiteral(5);
    FilePosition outputStartPosition = new FilePosition(1, 2);
    FilePosition outputEndPosition = new FilePosition(1, 5);

    sourceMapV1.setStartingPosition(1, 1);

    sourceMapV1.addMapping(node, outputStartPosition, outputEndPosition);

    Appendable out = Maps.newArrayList().<Appendable>get(0);
    sourceMapV1.appendTo(out, "test.js");

    String sourceMap = out.toString();
    assertNotNull(sourceMap);

    String expected = "{\"version\":1,\"file\":\"test.js\",\"sources\":{},\"mappings\":[\n" +
        "  [\"test.js\",1,1,0,1,0],\n" +
        "  [\"test.js\",1,2,1,2,0],\n" +
        "  [\"test.js\",1,5,1,2,1]\n" +
        "]}\n";

    assertTrue(sourceMap.equals(expected));
  }
}