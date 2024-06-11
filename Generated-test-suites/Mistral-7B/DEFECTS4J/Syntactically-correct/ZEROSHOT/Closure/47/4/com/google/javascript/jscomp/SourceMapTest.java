package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.debugging.sourcemap.FilePosition;
import com.google.debugging.sourcemap.SourceMap;
import com.google.debugging.sourcemap.SourceMapFormat;
import com.google.debugging.sourcemap.SourceMapGenerator;
import com.google.debugging.sourcemap.SourceMapGeneratorFactory;
import com.google.javascript.rhino.Node;

public class SourceMapTest {

  @Test
  public void testSourceMap() throws IOException {
    // Create a mock SourceMapGenerator
    SourceMapGeneratorFactory sourceMapGeneratorFactory = mock(SourceMapGeneratorFactory.class);
    SourceMapGenerator generator = mock(SourceMapGenerator.class);
    when(sourceMapGeneratorFactory.getInstance(SourceMapFormat.DEFAULT)).thenReturn(generator);

    SourceMap sourceMap = new SourceMap(sourceMapGeneratorFactory);

    // Test the constructor
    assertNotNull(sourceMap);

    // Test the addMapping method
    Node node = mock(Node.class);
    FilePosition outputStartPosition = mock(FilePosition.class);
    FilePosition outputEndPosition = mock(FilePosition.class);
    String sourceFile = "test.js";
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Test the appendTo method
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    sourceMap.appendTo(out, "test.js");
    String expectedSourceMap = "{\n" +
        "  \"version\": 3,\n" +
        "  \"file\": \"test.js\",\n" +
        "  \"sources\": [\n" +
        "    \"test.js\"\n" +
        "  ],\n" +
        "  \"sourcesContent\": {}\n" +
        "}";
    assertNotNull(out.toString());
    assertNotNull(sourceMap.getGenerator());
    when(generator.getSourceMapFormat()).thenReturn(SourceMapFormat.V3);
    when(generator.toString()).thenReturn(expectedSourceMap);
    assertEquals(expectedSourceMap, out.toString());

    // Test the reset method
    sourceMap.reset();
    assertNotNull(sourceMap.getGenerator());
    when(generator.getSourceMapFormat()).thenReturn(SourceMapFormat.V3);
    when(generator.toString()).thenReturn("");
    assertEquals("", out.toString());

    // Test the setStartingPosition method
    sourceMap.setStartingPosition(1, 1);
    when(generator.getSourceMapFormat()).thenReturn(SourceMapFormat.V3);
    when(generator.toString()).thenReturn(expectedSourceMap);
    expectedSourceMap = expectedSourceMap.replace("\"version\": 3,\n", "\"version\": 3,\n\"sourcesContent\": {\"test.js\": [\n  {\"source\": \"test.js\", \"line\": 1,\"column\": 1}\n ]},\n");
    assertEquals(expectedSourceMap, out.toString());

    // Test the setWrapperPrefix method
    sourceMap.setWrapperPrefix("wrapper");
    when(generator.getSourceMapFormat()).thenReturn(SourceMapFormat.V3);
    when(generator.toString()).thenReturn(expectedSourceMap);
    expectedSourceMap = expectedSourceMap.replace("\"file\": \"test.js\",", "\"file\": \"wrapper/test.js\",");
    assertEquals(expectedSourceMap, out.toString());

    // Test the validate method
    sourceMap.validate(true);
    when(generator.getSourceMapFormat()).thenReturn(SourceMapFormat.V3);
    when(generator.validate(true)).thenReturn(true);
    assertTrue(sourceMap.isValid());

    // Test the setPrefixMappings method
    List<LocationMapping> sourceMapLocationMappings = Collections.singletonList(new LocationMapping("test", "test_fixed.js"));
    sourceMap.setPrefixMappings(sourceMapLocationMappings);
    when(generator.getSourceMapFormat()).thenReturn(SourceMapFormat.V3);
    when(generator.toString()).thenReturn(expectedSourceMap);
    expectedSourceMap = expectedSourceMap.replace("\"file\": \"test.js\",", "\"file\": \"test_fixed.js\",");
    assertEquals(expectedSourceMap, out.toString());

  }

  private boolean isValid() {
    try {
      generator.validate(false);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private static class NodeMock {}

  private static class LocationMappingMock {
    final String prefix;
    final String replacement;

    LocationMappingMock(String prefix, String replacement) {
      this.prefix = prefix;
      this.replacement = replacement;
    }
  }
}