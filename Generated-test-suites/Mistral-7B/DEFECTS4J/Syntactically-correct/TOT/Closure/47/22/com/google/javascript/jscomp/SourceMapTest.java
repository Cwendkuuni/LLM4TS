import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.Format;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import org.junit.Test;
import static org.junit.Assert.*;

public class SourceMapTest {

  @Test
  public void testSourceMapGeneration() {
    // Given a new SourceMap instance with the V1 format and ALL detail level
    SourceMap sourceMap = SourceMap.Format.V1.getInstance().newInstance();

    // When adding mappings for a Node with valid source file and line number
    Node node = new Node();
    node.setSourceFileName("test.js");
    node.setLineno(1);
    sourceMap.addMapping(node, new FilePosition(1, 0), new FilePosition(1, 0));

    // Then the generated source map should contain the mapping
    assertTrue(sourceMap.getInstance().containsMapping("test.js", "test.js", 1, 0));

    // When adding mappings for a Node with an invalid source file or line number
    node.setSourceFileName(null);
    node.setLineno(-1);
    sourceMap.addMapping(node, new FilePosition(1, 0), new FilePosition(1, 0));

    // Then the generated source map should not contain the mapping
    assertFalse(sourceMap.getInstance().containsMapping(null, "test.js", 1, 0));
    assertFalse(sourceMap.getInstance().containsMapping("test.js", null, 1, 0));
    assertFalse(sourceMap.getInstance().containsMapping("test.js", "test.js", -1, 0));

    // When appending the generated SourceMap to an output stream
    Appendable out = new StringBuilder();
    try {
      sourceMap.appendTo(out, "sourceMap.js");
    } catch (IOException e) {
      fail("Unexpected exception: " + e.getMessage());
    }

    // Then the output stream should contain the generated source map
    String sourceMapString = out.toString();
    assertTrue(sourceMapString.contains("{\"version\":1,"));

    // When resetting the SourceMap instance
    sourceMap.reset();

    // Then the generated source map should be empty
    assertFalse(sourceMap.getInstance().containsMapping("test.js", "test.js", 1, 0));

    // When setting a starting position and adding mappings
    sourceMap = SourceMap.Format.V1.getInstance().newInstance();
    sourceMap.setStartingPosition(1, 0);
    sourceMap.addMapping(node, new FilePosition(1, 0), new FilePosition(1, 0));

    // Then the generated source map should have the correct starting position
    FilePosition position = sourceMap.getInstance().getSourceMappingForPosition(
        new FilePosition(1, 0));
    assertEquals(1, position.getLine());
    assertEquals(0, position.getColumn());

    // When setting wrapper prefix and adding mappings
    sourceMap = SourceMap.Format.V1.getInstance().newInstance();
    sourceMap.setWrapperPrefix("test_prefix_");
    sourceMap.addMapping(node, new FilePosition(1, 0), new FilePosition(1, 0));

    // Then the generated source map should have the correct wrapper prefix
    String mapping = sourceMap.getInstance().getSourceContentForPosition(
        new FilePosition(1, 0));
    assertEquals("test_prefix_test.js", mapping);

    // When validating the generated SourceMap
    sourceMap.validate(true);

    // Then no validation errors should be thrown

    // When setting prefix mappings and adding mappings
    List<LocationMapping> sourceMapLocationMappings = Collections.singletonList(
        new SourceMap.LocationMapping("test_prefix_", "fixed_prefix_"));
    sourceMap = SourceMap.Format.V1.getInstance().newInstance();
    sourceMap.setPrefixMappings(sourceMapLocationMappings);
    sourceMap.addMapping(node, new FilePosition(1, 0), new FilePosition(1, 0));

    // Then the generated source map should have the correct prefix mapping
    String fixedMapping = sourceMap.getInstance().getSourceContentForPosition(
        new FilePosition(1, 0));
    assertEquals("fixed_prefix_test.js", fixedMapping);
  }
}