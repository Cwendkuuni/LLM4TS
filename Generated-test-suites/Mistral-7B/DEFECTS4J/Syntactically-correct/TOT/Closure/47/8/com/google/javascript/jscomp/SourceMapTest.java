import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SourceMapTest {

  @Test
  public void testSourceMapGeneration() throws IOException {
    // Given
    SourceMap sourceMap = createSourceMap(Format.V1);
    String inputCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";

    // When
    String output = runCompiler(sourceMap, inputCode);

    // Then
    Assertions.assertNotNull(output);
    Assertions.assertFalse(output.isEmpty());

    // Check if source map is generated correctly
    List<SourceMap.LocationMapping> prefixMappings = Lists.newArrayList(
        new SourceMap.LocationMapping("foo.js", "compiled_foo.js")
    );
    sourceMap.setPrefixMappings(prefixMappings);
    sourceMap.addMapping(new Node("FunctionDeclaration", 1), null, null);
    sourceMap.addMapping(new Node("VariableDeclaration", 2, "a"), null, null);
    sourceMap.addMapping(new Node("VariableDeclaration", 3, "b"), null, null);
    sourceMap.addMapping(new Node("ReturnStatement", 4), null, null);

    ByteArrayOutputStream os = new ByteArrayOutputStream();
    sourceMap.appendTo(os, "sourcemap.js");
    String sourcemap = os.toString();

    Map<String, String> sourceMapData = parseSourceMap(sourcemap);

    Assertions.assertEquals(inputCode, sourceMapData.get("foo.js"));
    Assertions.assertEquals("compiled_foo.js", sourceMapData.get("compiled_foo.js"));
    Assertions.assertEquals("1:2", sourceMapData.get("compiled_foo.js#L1;C2").split(":")[1]);
    Assertions.assertEquals("1:2", sourceMapData.get("foo.js#L1;C2").split(":")[1]);
  }

  @Test
  public void testInvalidSourceMapGeneration() throws IOException {
    // Given
    SourceMap sourceMap = createSourceMap(Format.V1);
    String inputCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";

    // When
    String output = runCompiler(sourceMap, inputCode);

    // Then
    Assertions.assertNotNull(output);
    Assertions.assertFalse(output.isEmpty());

    // Check if source map is generated with invalid format
    SourceMap invalidSourceMap = createSourceMap(Format.V1);
    invalidSourceMap.addMapping(new Node("FunctionDeclaration", 1), null, null);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    invalidSourceMap.appendTo(os, "sourcemap.js");
    String sourcemap = os.toString();

    Map<String, String> sourceMapData = parseSourceMap(sourcemap);

    Assertions.assertNull(sourceMapData.get("foo.js"));
  }

  @Test
  public void testDetailedSourceMapGeneration() throws IOException {
    // Given
    SourceMap sourceMap = createSourceMap(Format.V1);
    String inputCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  return a + b;\n" +
        "}";

    // When
    String output = runCompiler(sourceMap, inputCode);

    // Then
    Assertions.assertNotNull(output);
    Assertions.assertFalse(output.isEmpty());

    // Check if source map is generated with ALL detail level
    SourceMap detailedSourceMap = createSourceMap(Format.V1);
    detailedSourceMap.setDetailLevel(SourceMap.DetailLevel.ALL);
    detailedSourceMap.addMapping(new Node("FunctionDeclaration", 1), null, null);
    detailedSourceMap.addMapping(new Node("VariableDeclaration", 2, "a"), null, null);
    detailedSourceMap.addMapping(new Node("VariableDeclaration", 3, "b"), null, null);
    detailedSourceMap.addMapping(new Node("ReturnStatement", 4), null, null);
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    detailedSourceMap.appendTo(os, "sourcemap.js");
    String sourcemap = os.toString();

    Map<String, String> sourceMapData = parseSourceMap(sourcemap);

    Assertions.assertEquals(inputCode, sourceMapData.get("foo.js"));
    Assertions.assertFalse(sourceMapData.isEmpty());
    Assertions.assertTrue(sourceMapData.containsKey("foo.js#L1;C1"));
    Assertions.assertTrue(sourceMapData.containsKey("foo.js#L1;C2"));
    Assertions.assertTrue(sourceMapData.containsKey("foo.js#L1;C3"));
    Assertions.assertTrue(sourceMapData.containsKey("foo.js#L1;C4"));
    Assertions.assertTrue(sourceMapData.containsKey("foo.js#L1;C5"));
    Assertions.assertTrue(sourceMapData.containsKey("foo.js#L1;C6"));
  }

  private SourceMap createSourceMap(Format format) {
    return new SourceMap(SourceMapGeneratorFactory.getInstance(format));
  }

  private String runCompiler(SourceMap sourceMap, String inputCode) throws IOException {
    ByteArrayOutputStream compilerOutput = new ByteArrayOutputStream();
    ByteArrayOutputStream sourcemapOutput = new ByteArrayOutputStream();
    SourceMapGenerator generator = sourceMap.getInstance();
    generator.setOutputFile(sourcemapOutput.toByteArray());
    compiler(inputCode, sourceMap, generator, compilerOutput, null);
    return compilerOutput.toString();
  }

  private Map<String, String> parseSourceMap(String sourcemap) {
    SourceMapGenerator generator = new SourceMapGeneratorV1();
    return generator.parse(sourcemap.getBytes());
  }

  private void compiler(String code, SourceMap sourceMap,
      SourceMapGenerator generator, ByteArrayOutputStream compilerOutput,
      CodeConsumer consumer) throws IOException {
    // Implementation of the compiler method is omitted for the sake of brevity.
  }

  public void tearDown() throws Exception {
    // Implementation of the tearDown method is omitted for the sake of brevity.
  }
}