import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.SourceMap;
import com.google.javascript.jscomp.SourceMap.DetailLevel;
import com.google.javascript.jscomp.SourceMap.Format;

public class SourceMapTest {

  private SourceMap sourceMap;
  private Node node;
  private FilePosition outputStartPosition;
  private FilePosition outputEndPosition;

  @Before
  public void setUp() {
    // Initialize the source map with a specific format
    sourceMap = Format.V1.getInstance().getInstance();

    // Prepare a test node with a valid source file and line number
    node = new Node();
    node.setSourceFileName("test.js");
    node.setLineno(1);

    // Create test output positions
    outputStartPosition = new FilePosition(1, 0);
    outputEndPosition = new FilePosition(1, 10);
  }

  @Test
  public void testAddMapping() {
    // Add a mapping for the test node to the source map
    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    // Validate that the mapping has been added correctly
    String expectedSourceFile = "test.js";
    String expectedOriginalName = null;
    FilePosition expectedOutputStartPosition = outputStartPosition;
    FilePosition expectedOutputEndPosition = outputEndPosition;

    SourceMap.Mapping mapping = sourceMap.getMappingForSourceNameAndOriginalName(
        expectedSourceFile, expectedOriginalName);

    assertEquals(expectedSourceFile, mapping.getSourceFile());
    assertEquals(expectedOriginalName, mapping.getOriginalName());
    assertEquals(expectedOutputStartPosition, mapping.getSourcePosition());
    assertEquals(expectedOutputEndPosition, mapping.getTargetPosition());
  }

  @Test
  public void testFixupSourceLocation() {
    // Test that the source location is correctly fixed up with prefix mappings
    List<SourceMap.LocationMapping> prefixMappings = Collections.singletonList(
        new SourceMap.LocationMapping("test", "fixedTest"));

    sourceMap.setPrefixMappings(prefixMappings);

    String sourceFile = "test/test.js";
    String fixedSourceFile = sourceMap.fixupSourceLocation(sourceFile);

    assertEquals("fixedTest/test.js", fixedSourceFile);
  }

  @Test
  public void testAddMappingWithInvalidNode() {
    // Test that an invalid node does not result in a mapping being added
    node.setSourceFileName(null);

    sourceMap.addMapping(node, outputStartPosition, outputEndPosition);

    assertNull(sourceMap.getMappingForSourceNameAndOriginalName(
        node.getSourceFileName(), null));
  }
}